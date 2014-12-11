/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzbox.internal;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.openhab.binding.fritzbox.FritzboxBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.library.tel.types.CallType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The FritzBox binding connects to a AVM FritzBox on the monitor port 1012 and
 * listens to event notifications from this box. There are event for incoming
 * and outgoing calls, as well as for connections and disconnections.
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public class FritzboxBinding extends
		AbstractActiveBinding<FritzboxBindingProvider> implements
		ManagedService {

	private static HashMap<String, String> commandMap = new HashMap<String, String>();
	private static HashMap<String, String> queryMap = new HashMap<String, String>();
	
	// TODO: configurable?
	// daily cron schedule
	private final String cronSchedule = "0 0 0 * * ?";

	static {
		commandMap.put(FritzboxBindingProvider.TYPE_DECT,
				"ctlmgr_ctl w dect settings/enabled");
		commandMap.put(FritzboxBindingProvider.TYPE_WLAN,
				"ctlmgr_ctl w wlan settings/ap_enabled");

		queryMap.put(FritzboxBindingProvider.TYPE_DECT,
				"ctlmgr_ctl r dect settings/enabled");
		queryMap.put(FritzboxBindingProvider.TYPE_WLAN,
				"ctlmgr_ctl r wlan settings/ap_enabled");
	}

	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		super.bindingChanged(provider, itemName);

		conditionalDeActivate();

	}

	private void conditionalDeActivate() {
		logger.info("Fritzbox conditional deActivate: {}", bindingsExist());

		if (bindingsExist()) {
			activate();
		} else {
			deactivate();
		}
	}

	private static final Logger logger = LoggerFactory
			.getLogger(FritzboxBinding.class);

	protected static final int MONITOR_PORT = 1012;

	/** the current thread instance that is listening to the FritzBox */
	protected static MonitorThread monitorThread = null;

	/* The IP address to connect to */
	protected static String ip;

	/* The password of the FritzBox to access via Telnet */
	protected static String password;

	/**
	 * Reference to this instance to be used with the reconnection job which is
	 * static.
	 */
	private static FritzboxBinding INSTANCE;

	public FritzboxBinding() {
		INSTANCE = this;
	}

	public void activate() {
		super.activate();
		setProperlyConfigured(true);
		// if bundle is already configured, launch the monitor thread right away
		if (ip != null) {
			reconnect();
		}
	}

	public void deactivate() {
		if (monitorThread != null) {
			monitorThread.interrupt();
		}
		monitorThread = null;
	}

	@Override
	public void internalReceiveCommand(String itemName, Command command) {

		if (password != null && !password.isEmpty()) {
			String type = null;
			for (FritzboxBindingProvider provider : providers) {
				type = provider.getType(itemName);
				if (type != null) {
					break;
				}
			}

			logger.info("Fritzbox type: {}", type);

			if (type == null)
				return;

			TelnetCommandThread thread = new TelnetCommandThread(type, command);
			thread.start();
			
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("rawtypes")
	public void updated(Dictionary config) throws ConfigurationException {

		if (config != null) {
			String ip = (String) config.get("ip");
			if (StringUtils.isNotBlank(ip)) {
				if (!ip.equals(FritzboxBinding.ip)) {
					// only do something if the ip has changed
					FritzboxBinding.ip = ip;
					conditionalDeActivate();
                    
					// schedule a daily reconnection as sometimes the FritzBox
					// stops sending data
					// and thus blocks the monitor thread
					try {
						Scheduler sched = StdSchedulerFactory
								.getDefaultScheduler();
                                
                        JobKey jobKey = jobKey("Reconnect", "FritzBox");
                        TriggerKey triggerKey = triggerKey("Reconnect", "FritzBox");
                        
                        if (sched.checkExists(jobKey)) {
                            logger.debug("Daily reconnection job already exists");
                        } else {
                            CronScheduleBuilder scheduleBuilder = 
                            		CronScheduleBuilder.cronSchedule(cronSchedule);
                            
                            JobDetail job = newJob(ReconnectJob.class)
                                    .withIdentity(jobKey)
                                    .build();

                            CronTrigger trigger = newTrigger()
                                    .withIdentity(triggerKey)
                                    .withSchedule(scheduleBuilder)
                                    .build();

                            sched.scheduleJob(job, trigger);
                            logger.debug("Scheduled a daily reconnection to FritzBox on {}:{}", ip, MONITOR_PORT);
                        }
					} catch (SchedulerException e) {
						logger.warn("Could not create daily reconnection job", e);
					}
				}
			}
			String password = (String) config.get("password");
			if (StringUtils.isNotBlank(password)) {
				FritzboxBinding.password = password;
			}
		}
	}

	protected void reconnect() {
		if (monitorThread != null) {
			// let's end the old thread
			monitorThread.interrupt();
			monitorThread = null;
		}
		// create a new thread for listening to the FritzBox
		monitorThread = new MonitorThread(this.eventPublisher, this.providers);
		monitorThread.start();
	}

	private static class TelnetCommandThread extends Thread {

		private static HashMap<String, String> commandMap = new HashMap<String, String>();

		static {
			commandMap.put(FritzboxBindingProvider.TYPE_DECT,
					"ctlmgr_ctl w dect settings/enabled");
			commandMap.put(FritzboxBindingProvider.TYPE_WLAN,
					"ctlmgr_ctl w wlan settings/ap_enabled");
		}

		public TelnetCommandThread(String type, Command command) {
			super();
			this.type = type;
			this.command = command;
		}

		private String type;

		private Command command;

		@Override
		public void run() {
			try {
				TelnetClient client = new TelnetClient();
				client.connect(ip);

				int state = 0;
				if (command == OnOffType.ON)
					state = 1;

				String cmdString = null;
				if (commandMap.containsKey(type)) {
					cmdString = commandMap.get(type) + " " + state;
				} else if (type.startsWith("tam")) {
					cmdString = "ctlmgr_ctl w tam settings/"
							+ type.toUpperCase() + "/Active " + state;
				} else if (type.startsWith("cmd")) {
					int on = type.indexOf("ON=");
					int off = type.indexOf("OFF=");
					if (state == 0) {
						cmdString = type.substring(off + 4,
								on < off ? type.length() : on);
					} else {
						cmdString = type.substring(on + 3,
								off < on ? type.length() : off);
					}
					cmdString = cmdString.trim();
				}

				/*
				 * This is a approach with receive/send in serial way. This
				 * could be done via a sperate thread but for just sending one
				 * command it is not necessary
				 */
				receive(client); // password:
				send(client, password);
				receive(client); // welcome text
				send(client, cmdString);
				Thread.sleep(1000L); // response not needed - may be interesting
										// for reading status
				client.disconnect();

			} catch (Exception e) {
				logger.warn("Error processing command", e);
			}
		}

		private void send(TelnetClient client, String data) {
			logger.trace("Sending data ({})...", data);
			try {
				data += "\r\n";
				client.getOutputStream().write(data.getBytes());
				client.getOutputStream().flush();
			} catch (IOException e) {
				logger.warn("Error sending data", e);
			}
		}

		private String receive(TelnetClient client) {
			StringBuffer strBuffer;
			try {
				strBuffer = new StringBuffer();

				byte[] buf = new byte[4096];
				int len = 0;

				Thread.sleep(750L);

				while ((len = client.getInputStream().read(buf)) != 0) {
					strBuffer.append(new String(buf, 0, len));

					Thread.sleep(750L);

					if (client.getInputStream().available() == 0)
						break;
				}

				return strBuffer.toString();
				
			} catch (Exception e) {
				logger.warn("Error receiving data", e);
			}

			return null;
		}
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

		public MonitorThread(EventPublisher eventPublisher,
				Collection<FritzboxBindingProvider> providers) {
			this.eventPublisher = eventPublisher;
			this.providers = providers;
		}

		/**
		 * Notifies the thread to terminate itself. The current connection will
		 * be closed.
		 */
		public void interrupt() {
			this.interrupted = true;
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					logger.warn("Existing connection to FritzBox cannot be closed", e);
				}
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void run() {

			while (!interrupted) {
				while (ip == null) {
					// if we don't have an IP, let's wait
					try {
						sleep(1000L);
					} catch (InterruptedException e) {
						interrupted = true;
						break;
					}
				}
				if (ip != null) {
					BufferedReader reader = null;
					try {
						logger.info("Attempting connection to FritzBox on {}:{}...", ip, MONITOR_PORT);
						connection = new Socket(ip, MONITOR_PORT);
						reader = new BufferedReader(new InputStreamReader(
								connection.getInputStream()));
						// reset the retry interval
						waitBeforeRetry = 60000L;
					} catch (Exception e) {
						logger.warn("Error attempting to connect to FritzBox. Retrying in " + waitBeforeRetry / 1000L + "s.", e);
						try {
							Thread.sleep(waitBeforeRetry);
						} catch (InterruptedException ex) {
							interrupted = true;
						}
						// wait another more minute the next time
						waitBeforeRetry += 60000L;
					}
					if (reader != null) {
						logger.info("Connected to FritzBox on {}:{}", ip, MONITOR_PORT);
						while (!interrupted) {
							try {
								String line = reader.readLine();
								if (line != null) {
									MonitorEvent event = parseMonitorEvent(line);
									processMonitorEvent(event);
									try {
										// wait a moment, so that rules can be
										// processed
										// see
										// http://knx-user-forum.de/openhab/25024-bug-im-fritzbox-binding.html
										sleep(100L);
									} catch (InterruptedException e) {
									}
								}
							} catch (IOException e) {
								logger.error("Lost connection to FritzBox", e);
								break;
							}
						}
					}
				}
			}
		}

		/**
		 * Parses the string that was received from the FritzBox
		 * 
		 * @param line
		 *            the received string
		 * @return the parse result
		 */
		private MonitorEvent parseMonitorEvent(String line) {
			String[] sections = line.split(";");
			MonitorEvent event = new MonitorEvent();
			event.timestamp = sections[0];
			event.eventType = sections[1];
			event.connectionId = sections[2];

			if (event.eventType.equals("RING")) {
				event.externalNo = sections[3];
				event.internalNo = sections[4];
				event.connectionType = sections[5];
			} else if (event.eventType.equals("CONNECT")) {
				event.line = sections[3];
				event.externalNo = sections[4];
			} else if (event.eventType.equals("CALL")) {
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
		 * @param event
		 *            the event to process
		 */
		private void processMonitorEvent(MonitorEvent event) {
			if (event.eventType.equals("RING")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_INBOUND);
			}
			if (event.eventType.equals("CALL")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_OUTBOUND);
			}
			if (event.eventType.equals("CONNECT")
					|| event.eventType.equals("DISCONNECT")) {
				handleEventType(event, FritzboxBindingProvider.TYPE_INBOUND);
				handleEventType(event, FritzboxBindingProvider.TYPE_ACTIVE);
				handleEventType(event, FritzboxBindingProvider.TYPE_OUTBOUND);
			}
		}

		/**
		 * Processes a monitor event for a given binding type
		 * 
		 * @param event
		 *            the monitor event to process
		 * @param bindingType
		 *            the binding type of the items to process
		 */
		private void handleEventType(MonitorEvent event, String bindingType) {
			for (FritzboxBindingProvider provider : providers) {
				for (String itemName : provider
						.getItemNamesForType(bindingType)) {
					Class<? extends Item> itemType = provider
							.getItemType(itemName);

					org.openhab.core.types.State state = null;
					if (event.eventType.equals("DISCONNECT")) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.OFF
								: CallType.EMPTY;
					} else if (event.eventType.equals("CONNECT")) {
						if (bindingType
								.equals(FritzboxBindingProvider.TYPE_ACTIVE)) {
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON
									: new CallType(event.externalNo, event.line);
						} else {
							state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.OFF
									: CallType.EMPTY;
						}
					} else if (event.eventType.equals("RING")
							&& bindingType
									.equals(FritzboxBindingProvider.TYPE_INBOUND)) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON
								: new CallType(event.externalNo,
										event.internalNo);
					} else if (event.eventType.equals("CALL")
							&& bindingType
									.equals(FritzboxBindingProvider.TYPE_OUTBOUND)) {
						state = itemType.isAssignableFrom(SwitchItem.class) ? OnOffType.ON
								: new CallType(event.internalNo,
										event.externalNo);
					}
					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					}
				}
			}
		}

		/**
		 * Class representing a monitor event received from the FritzBox. Not
		 * all attributes are used for the moment, but might be useful for
		 * future extensions.
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

		public void execute(JobExecutionContext arg0)
				throws JobExecutionException {
			INSTANCE.conditionalDeActivate();
		}

	}

	@Override
	protected void execute() {

		if (password == null)
			return;
		else if (password.trim().isEmpty())
			return;
		
		try {
			TelnetClient client = null ;
			

			for (FritzboxBindingProvider provider : providers) {
				for (String item : provider.getItemNames()) {
					String query = null;

					String type = provider.getType(item);
					if (queryMap.containsKey(type)) {
						query = queryMap.get(type);
					} else if (type.startsWith("tam")) {
						query = "ctlmgr_ctl r tam settings/"
								+ type.toUpperCase() + "/Active";
					} else if (type.startsWith("query")) {
						query = type.substring(type.indexOf(":") + 1).trim();
					}else
						continue;

					if (client == null){
						client = new TelnetClient();
						client.connect(ip);
						receive(client);
						send(client, password);
						receive(client);
					}
					
					send(client, query);

					String answer = receive(client);
					String[] lines = answer.split("\r\n");

					if (lines.length >= 2) {
						answer = lines[1].trim();
					}

					Class<? extends Item> itemType = provider.getItemType(item);

					org.openhab.core.types.State state = null;

					if (itemType.isAssignableFrom(SwitchItem.class)) {
						if (answer.equals("1"))
							state = OnOffType.ON;
						else
							state = OnOffType.OFF;
					} else if (itemType.isAssignableFrom(NumberItem.class)) {
						state = new DecimalType(answer);
					} else if (itemType.isAssignableFrom(StringItem.class)) {
						state = new StringType(answer);
					}

					if (state != null)
						eventPublisher.postUpdate(item, state);

				}
			}
			if (client != null)
				client.disconnect();
		} catch (Exception e) {
			logger.warn("Could not get item state ", e);
		}

	}

	@Override
	protected long getRefreshInterval() {
		return 60000L;
	}

	@Override
	protected String getName() {
		return "FritzBox refresh Service";
	}

	/**
	 * Send line via Telnet to FritzBox
	 * 
	 * @param client
	 *            the telnet client
	 * @param data
	 *            the data to send
	 */
	private static void send(TelnetClient client, String data) {
		try {
			data += "\r\n";
			client.getOutputStream().write(data.getBytes());
			client.getOutputStream().flush();
		} catch (IOException e) {
			logger.warn("Error sending data", e);
		}
	}

	/**
	 * Receive answer from FritzBox - careful! This blocks if there is no answer
	 * from FritzBox
	 * 
	 * @param client
	 *            the telnet client
	 * @return
	 */
	private static String receive(TelnetClient client) {

		StringBuffer strBuffer;
		try {
			strBuffer = new StringBuffer();

			byte[] buf = new byte[4096];
			int len = 0;

			Thread.sleep(750L);

			while ((len = client.getInputStream().read(buf)) != 0) {
				strBuffer.append(new String(buf, 0, len));

				Thread.sleep(750L);

				if (client.getInputStream().available() == 0)
					break;

			}

			return strBuffer.toString();

		} catch (Exception e) {
			logger.warn("Error receiving data", e);
		}

		return null;
	}

}
