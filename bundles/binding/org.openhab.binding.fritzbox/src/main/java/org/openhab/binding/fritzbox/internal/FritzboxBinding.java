/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzbox.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collection;
import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.fritzbox.FritzboxBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The FritzBox binding connects to a AVM FritzBox on the monitor port 1012 and listens to event notifications
 * from this box. There are event for incoming and outgoing calls, as well as for connections and disconnections. 
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public class FritzboxBinding extends AbstractBinding<FritzboxBindingProvider> implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(FritzboxBinding.class);
	
	protected static final int MONITOR_PORT = 1012;

	/** the current thread instance that is listening to the FritzBox */
	protected static MonitorThread monitorThread = null;
	
	/* The IP address to connect to */
	protected static String ip;
	
	/** 
	 * Reference to this instance to be used with the reconnection job which
	 * is static.
	 */
	private static FritzboxBinding INSTANCE;
	
	
	public FritzboxBinding() {
		INSTANCE = this;
	}
	
	
	public void activate() {
		// if bundle is already configured, launch the monitor thread right away
		if(ip!=null) {
			reconnect();
		}
	}
	
	public void deactivate() {
		if(monitorThread!=null) {
			monitorThread.interrupt();
		}
		monitorThread = null;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {
		
		if (config != null) {
			String ip = (String) config.get("ip");
			if (StringUtils.isNotBlank(ip)) {
				if(!ip.equals(FritzboxBinding.ip)) {
					// only do something if the ip has changed
					FritzboxBinding.ip = ip;
					reconnect();

					// schedule a daily reconnection as sometimes the FritzBox stops sending data
					// and thus blocks the monitor thread
					try {
						Scheduler sched = StdSchedulerFactory.getDefaultScheduler();
						JobDetail job = newJob(ReconnectJob.class)
						    .withIdentity("Reconnect", "FritzBox")
						    .build();

						CronTrigger trigger = newTrigger()
						    .withIdentity("Reconnect", "FritzBox")
						    .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 * * ?"))
						    .build();

						sched.scheduleJob(job, trigger);
						logger.debug("Scheduled a daily reconnection to FritzBox on {}", ip + ":" + MONITOR_PORT);
					} catch (SchedulerException e) {
						logger.warn("Could not create daily reconnection job: {}", e.getMessage());
					}
				}
			}
		}
	}

	protected void reconnect() {
		if(monitorThread!=null) {
			// let's end the old thread
			monitorThread.interrupt();
			monitorThread = null;
		}
		// create a new thread for listening to the FritzBox
		monitorThread = new MonitorThread(this.eventPublisher, this.providers);
		monitorThread.start();		
	}
	
	
	/** 
	 * This is the thread that does the real work 
	 * 
	 * @author Kai Kreuzer
	 *
	 */
	private static class MonitorThread extends Thread {

		/** the active TCP connection */
		private Socket connection;
		
		/** flag to notify the thread to terminate */
		private boolean interrupted = false;
		
		/** retry interval in ms, if connection fails */
		private long waitBeforeRetry = 60000L;

		private EventPublisher eventPublisher;
		private Collection<FritzboxBindingProvider> providers;
		
		
		public MonitorThread(EventPublisher eventPublisher, Collection<FritzboxBindingProvider> providers) {
			this.eventPublisher = eventPublisher;
			this.providers = providers;
		}

		/**
		 * Notifies the thread to terminate itself.
		 * The current connection will be closed.
		 */
		public void interrupt() {
			this.interrupted = true;
			if(connection!=null) {
				try {
					connection.close();
				} catch (IOException e) {
					logger.warn("Existing connection to FritzBox on {} cannot be closed: {}", ip + ":" + MONITOR_PORT, e.toString());
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {
		
			while(!interrupted) {
				while(ip==null) {
					// if we don't have an IP, let's wait
					try {
						sleep(1000L);
					} catch (InterruptedException e) {
						interrupted = true;
						break;
					}
				}
				if(ip!=null) {
					BufferedReader reader = null;
					try { 
						connection = new Socket(ip, MONITOR_PORT);
						reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						// reset the retry interval
						waitBeforeRetry = 60000L;
					} catch(Exception e) { 
						logger.error("Could not connect to FritzBox on {}: {}", ip + ":" + MONITOR_PORT, e.toString());
						logger.info("Retrying connection to FritzBox in {} s.", waitBeforeRetry / 1000L);
						try {
							Thread.sleep(waitBeforeRetry);
						} catch (InterruptedException ex) {
							interrupted = true;
						}
						// wait another more minute the next time
						waitBeforeRetry += 60000L;
					}
					if(reader!=null) {
						logger.info("Connected to FritzBox on {}", ip + ":" + MONITOR_PORT);
						while(!interrupted) {
							try {
								String line = reader.readLine();
								if(line!=null) {
									MonitorEvent event = parseMonitorEvent(line);
									processMonitorEvent(event);
									try {
										// wait a moment, so that rules can be processed
										// see http://knx-user-forum.de/openhab/25024-bug-im-fritzbox-binding.html
										sleep(100L);
									} catch (InterruptedException e) {}
								}
							} catch (IOException e) {
								  logger.warn("Lost connection to FritzBox on {}: {}", ip + ":" + MONITOR_PORT, e.getMessage());
								  break;
							}
						}
					}
				}
			}
		}

		/**
		 * Parses the string that was received from the FritzBox
		 * @param line the received string
		 * @return the parse result
		 */
		private MonitorEvent parseMonitorEvent(String line) {
			String[] sections = line.split(";");
			MonitorEvent event = new MonitorEvent();
			event.timestamp = sections[0];
			event.eventType = sections[1];
			event.connectionId = sections[2];
			
			if(event.eventType.equals("RING")) {
				event.externalNo = sections[3];
				event.internalNo = sections[4];
				event.connectionType = sections[5];
			} else if(event.eventType.equals("CONNECT")) {
				event.line = sections[3];
				event.externalNo = sections[4];
			} else if(event.eventType.equals("CALL")) {
				event.line = sections[3];
				event.internalNo = sections[4];
				event.externalNo = sections[5];
				event.connectionType = sections[6];
			}			
			return event;
		}

		/**
		 * Processes a monitor event.
		 * 
		 * @param event the event to process
		 */
		private void processMonitorEvent(MonitorEvent event) {
			if(event.eventType.equals("RING")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_INBOUND);
			}
			if(event.eventType.equals("CALL")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_OUTBOUND);
			}
			if(event.eventType.equals("CONNECT") || event.eventType.equals("DISCONNECT")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_INBOUND);
				handleEventType(event, FritzboxBindingProvider.TYPE_ACTIVE);
				handleEventType(event, FritzboxBindingProvider.TYPE_OUTBOUND);
			}
		}

		/**
		 * Processes a monitor event for a given binding type
		 * 
		 * @param event the monitor event to process
		 * @param bindingType the binding type of the items to process
		 */
		private void handleEventType(MonitorEvent event, String bindingType) {
			for(FritzboxBindingProvider provider : providers) {
				for(String itemName : provider.getItemNamesForType(bindingType)) {
					Class<? extends Item> itemType = provider.getItemType(itemName);
					
					org.openhab.core.types.State state = null;
					if(event.eventType.equals("DISCONNECT")) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.OFF : CallType.EMPTY;
					} else if(event.eventType.equals("CONNECT")) {
						if(bindingType.equals(FritzboxBindingProvider.TYPE_ACTIVE)) {
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(event.externalNo, event.line);
						} else {
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.OFF : CallType.EMPTY;
						}
					} else if(event.eventType.equals("RING") && bindingType.equals(FritzboxBindingProvider.TYPE_INBOUND)) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(event.externalNo, event.internalNo);
					} else if(event.eventType.equals("CALL") && bindingType.equals(FritzboxBindingProvider.TYPE_OUTBOUND)) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON : new CallType(event.internalNo, event.externalNo);
					}
					if(state!=null) {
						eventPublisher.postUpdate(itemName, state);
					}
				}
			}
		}

		/**
		 * Class representing a monitor event received from the FritzBox.
		 * Not all attributes are used for the moment, but might be useful for future extensions.
		 * 
		 * @author Kai Kreuzer
		 *
		 */
		@SuppressWarnings("unused")
		private static class MonitorEvent {
			public String timestamp;
			public String eventType;
			public String connectionId;
			public String externalNo;
			public String internalNo;
			public String connectionType;
			public String line;
		}
	}
	
	
	/**
	 * A quartz scheduler job to simply do a reconnection to the FritzBox.
	 */
	public static class ReconnectJob implements Job {
		
		public void execute(JobExecutionContext arg0) throws JobExecutionException {
			INSTANCE.reconnect();
		}
		
	}
	
	
}
