/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2011, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.fritzbox.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashSet;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.fritzbox.FritzboxBindingProvider;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.items.ItemNotFoundException;
import org.openhab.core.items.ItemNotUniqueException;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The FritzBox binding connects to a AVM FritzBox on the monitor port 1012 and listens to event notifications
 * from this box. There are event for incoming and outgoing calls, as well as for connections and disconnections. 
 * 
 * @author Kai Kreuzer
 * @since 0.7.0
 */
public class FritzboxBinding implements ManagedService {

	private static final Logger logger = LoggerFactory.getLogger(FritzboxBinding.class);
	
	protected static final int MONITOR_PORT = 1012;
	
	/** to keep track of all binding providers */
	final static protected Collection<FritzboxBindingProvider> providers = new HashSet<FritzboxBindingProvider>();

	protected static ItemRegistry itemRegistry;
	
	protected static EventPublisher eventPublisher;

	/** the current thread instance that is listening to the FritzBox */
	protected static MonitorThread monitorThread = null;
	
	/* The IP address to connect to */
	protected static String ip;
	
	public FritzboxBinding() {}
		
	public void activate() {
	}
	
	public void deactivate() {
		if(monitorThread!=null) {
			monitorThread.interrupt();
		}
		monitorThread = null;
	}
	
	public void setItemRegistry(ItemRegistry itemRegistry) {
		FritzboxBinding.itemRegistry = itemRegistry;
	}
	
	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		FritzboxBinding.itemRegistry = null;
	}
	
	public void setEventPublisher(EventPublisher eventPublisher) {
		FritzboxBinding.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		FritzboxBinding.eventPublisher = null;
	}

	public void addBindingProvider(FritzboxBindingProvider provider) {
		FritzboxBinding.providers.add(provider);
	}

	public void removeBindingProvider(FritzboxBindingProvider provider) {
		FritzboxBinding.providers.remove(provider);		
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
					if(monitorThread!=null) {
						// let's end the old thread
						monitorThread.interrupt();
						monitorThread = null;
					}
					// create a new thread for listening to the FritzBox
					monitorThread = new MonitorThread();
					monitorThread.start();
				}
			}
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
						while(!interrupted) {
							try {
								String line = reader.readLine();
								MonitorEvent event = parseMonitorEvent(line);
								processMonitorEvent(event);
							} catch (IOException e) {
								  logger.warn("Lost connection to FritzBox on {}: {}", ip + ":" + MONITOR_PORT, e.toString());
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
			if(itemRegistry!=null) {
				for(FritzboxBindingProvider provider : providers) {
					for(String itemName : provider.getItemNamesForType(bindingType)) {
						try {
							Item item = itemRegistry.getItem(itemName);
							org.openhab.core.types.State state = null;
							if(event.eventType.equals("DISCONNECT")) {
								state = item instanceof SwitchItem ? OnOffType.OFF : StringType.EMPTY;
							} else if(event.eventType.equals("CONNECT")) {
								if(bindingType==FritzboxBindingProvider.TYPE_ACTIVE) {
									state = item instanceof SwitchItem ? OnOffType.ON : new StringType(event.externalNo);
								} else {
									state = item instanceof SwitchItem ? OnOffType.OFF : StringType.EMPTY;
								}
							} else if(event.eventType.equals("RING") && bindingType==FritzboxBindingProvider.TYPE_INBOUND) {
								state = item instanceof SwitchItem ? OnOffType.ON : new StringType(event.externalNo);
							} else if(event.eventType.equals("CALL") && bindingType==FritzboxBindingProvider.TYPE_OUTBOUND) {
								state = item instanceof SwitchItem ? OnOffType.ON : new StringType(event.externalNo);
							}
							if(state!=null) {
								eventPublisher.postUpdate(itemName, state);
							}
						} catch (ItemNotFoundException e) {
						} catch (ItemNotUniqueException e) {
						}
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
}
