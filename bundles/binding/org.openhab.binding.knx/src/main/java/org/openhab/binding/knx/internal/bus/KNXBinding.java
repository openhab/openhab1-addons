/**
OO * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.knx.internal.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.IllegalClassException;
import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.binding.knx.config.KNXTypeMapper;
import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.events.AbstractEventSubscriber;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tuwien.auto.calimero.DetachEvent;
import tuwien.auto.calimero.GroupAddress;
import tuwien.auto.calimero.datapoint.Datapoint;
import tuwien.auto.calimero.exception.KNXException;
import tuwien.auto.calimero.exception.KNXIllegalArgumentException;
import tuwien.auto.calimero.process.ProcessCommunicator;
import tuwien.auto.calimero.process.ProcessEvent;
import tuwien.auto.calimero.process.ProcessListener;

/**
 * This is the central class that takes care of the event exchange between openHAB and KNX.
 * It is fully connected (read and write) to the openHAB event bus and also has write access
 * to KNX while as well listening for incoming KNX messages.
 * 
 * The received messages are converted into the right format for the other bus and published 
 * to it.
 * 
 * @author Kai Kreuzer
 * @since 0.3.0
 *
 */
public class KNXBinding extends AbstractEventSubscriber implements ProcessListener, BindingChangeListener {

	private static final Logger logger = LoggerFactory.getLogger(KNXBinding.class);

	/** to keep track of all KNX binding providers */
	protected Set<KNXBindingProvider> providers = new HashSet<KNXBindingProvider>();

	/** to keep track of all KNX type mappers */
	protected Collection<KNXTypeMapper> typeMappers = new HashSet<KNXTypeMapper>();

	private EventPublisher eventPublisher;

	/**
	 * used to store events that we have sent ourselves; we need to remember them for not reacting to them
	 */
	private List<String> ignoreEventList = new ArrayList<String>();

	/**
	 * to keep track of all datapoints for which we should send a read request to the KNX bus
	 */
	private Set<Datapoint> datapointsToInitialize = Collections.synchronizedSet(new HashSet<Datapoint>());

	/** the datapoint initializer, which runs in a separate thread */
	private DatapointInitializer initializer = new DatapointInitializer();

	public void activate(ComponentContext componentContext) {
		initializer.start();
	}

	public void deactivate(ComponentContext componentContext) {
		for(KNXBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();
		initializer.setInterrupted(true);
	}

	public void setEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	public void unsetEventPublisher(EventPublisher eventPublisher) {
		this.eventPublisher = null;
	}

	public void addKNXBindingProvider(KNXBindingProvider provider) {
		this.providers.add(provider);
		provider.addBindingChangeListener(this);
		allBindingsChanged(provider);
	}

	public void removeKNXBindingProvider(KNXBindingProvider provider) {
		this.providers.remove(provider);
		provider.removeBindingChangeListener(this);
	}

	public void addKNXTypeMapper(KNXTypeMapper typeMapper) {
		this.typeMappers.add(typeMapper);
	}

	public void removeKNXTypeMapper(KNXTypeMapper typeMapper) {
		this.typeMappers.remove(typeMapper);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveCommand(String itemName, Command command) {
		handleEventReceived(itemName, command);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void receiveUpdate(String itemName, State newState) {
		handleEventReceived(itemName, newState);
	}

	private void handleEventReceived(String itemName, Type type) {
		String ignoreEventListKey = itemName + type.toString();
		if (ignoreEventList.contains(ignoreEventListKey)) {
			// if we have received this event from knx, don't send it back to
			// the knx bus
			ignoreEventList.remove(ignoreEventListKey);
		} else {
			Iterable<Datapoint> datapoints = getDatapoints(itemName, type.getClass());
			if (datapoints != null) {
				ProcessCommunicator pc = KNXConnection.getCommunicator();
				if (pc != null) {
					for (Datapoint datapoint : datapoints) {
						try {
							pc.write(datapoint, toDPTValue(type, datapoint.getDPT()));
							// after sending this out to KNX, we need to make sure that we do not
							// react on our own update
							ignoreEventList.add(ignoreEventListKey);
							
							if (logger.isDebugEnabled()) {
								logger.debug("wrote value '{}' to datapoint '{}'", type, datapoint);
							}
						} catch (KNXException e) {
							logger.warn("Type could not be sent to the KNX bus - retrying one time: {}", e.getMessage());
							try {
								// do a second try, maybe the reconnection was successful
								pc = KNXConnection.getCommunicator();
								pc.write(datapoint, toDPTValue(type, datapoint.getDPT()));
							} catch (KNXException e1) {
								logger.error("Type could not be sent to the KNX bus - giving up: {}", e.getMessage());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void groupWrite(ProcessEvent e) {
		try {
			GroupAddress destination = e.getDestination();
			byte[] asdu = e.getASDU();
			if(asdu.length==0) {
				return;
			}
			for (String itemName : getItemNames(destination)) {
				Iterable<Datapoint> datapoints = getDatapoints(itemName, destination);
				if (datapoints != null) {
					for (Datapoint datapoint : datapoints) {
						Type type = getType(datapoint, asdu);					
						if(type!=null) {
							if (ignoreEventList.contains(itemName + type.toString())) {
								// if we have send this event ourselves to KNX, 
								// ignore the echo now
								ignoreEventList.remove(itemName + type.toString());
							} else {
								// we need to make sure that we won't send out this event to
								// the knx bus again, when receiving it on the openHAB bus
								ignoreEventList.add(itemName + type.toString());
					
								if (type instanceof Command && isCommandGA(destination)) {
									eventPublisher.postCommand(itemName, (Command) type);
								} else if (type instanceof State) {
									eventPublisher.postUpdate(itemName, (State) type);
								} else {
									throw new IllegalClassException("cannot process datapoint of type " + type.toString());
								}
								
								if(logger.isTraceEnabled()) {
									logger.trace("Processed event: " + destination.toString() + ":" + type.toString() + " -> " + itemName);
								}
							}
							return;
						}
					}
				}
			}
			if(logger.isDebugEnabled()) {
				logger.debug("Received telegram for unknown group address " + destination.toString());
			}
		} catch(RuntimeException re) {
			logger.error("Error while receiving event from KNX bus: " + re.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void detached(DetachEvent e) {
		logger.error("Received detachEvent.");
	}

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof KNXBindingProvider) {
			KNXBindingProvider knxProvider = (KNXBindingProvider) provider;
			for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
				if(datapoint.getName().equals(itemName)) {
					datapointsToInitialize.add(datapoint);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void allBindingsChanged(BindingProvider provider) {
		if (provider instanceof KNXBindingProvider) {
			KNXBindingProvider knxProvider = (KNXBindingProvider) provider;
			for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
				datapointsToInitialize.add(datapoint);
			}
		}
	}

	/**
	 * Determines whether the given <code>groupAddress</code> is the address which
	 * will be interpreted as the command type. This method iterates over all 
	 * registered KNX binding providers to find the result.

	 * @param groupAddress the group address to check
	 * @return the datapoints which corresponds to the given item and group address
	 */
	private boolean isCommandGA(GroupAddress groupAddress) {
		boolean result = true;
		for (KNXBindingProvider provider : providers) {
			result &= provider.isCommandGA(groupAddress);
		}
		return result;
	}
	
	/**
	 * Returns all listening item names. This method iterates over all registered KNX binding providers and aggregates
	 * the result.
	 * 
	 * @param groupAddress
	 *            the group address that the items are listening to
	 * @return an array of all listening items
	 */
	private String[] getItemNames(GroupAddress groupAddress) {
		List<String> itemNames = new ArrayList<String>();
		for (KNXBindingProvider provider : providers) {
			for (String itemName : provider.getListeningItemNames(groupAddress)) {
				itemNames.add(itemName);
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}

	/**
	 * Returns the datapoints for a given item and group address. This method iterates over all registered KNX binding
	 * providers to find the result.
	 * 
	 * @param itemName
	 *            the item name for the datapoint
	 * @param groupAddress
	 *            the group address associated to the datapoint
	 * @return the datapoints which corresponds to the given item and group address
	 */
	private Iterable<Datapoint> getDatapoints(String itemName, GroupAddress groupAddress) {
		for (KNXBindingProvider provider : providers) {
			Iterable<Datapoint> datapoints = provider.getDatapoints(itemName, groupAddress);
			if (datapoints != null)
				return datapoints;
		}
		return null;
	}

	/**
	 * Transforms the raw KNX bus data of a given datapoint into an openHAB type (command or state)
	 * 
	 * @param datapoint
	 *            the datapoint to which the data belongs
	 * @param asdu
	 *            the byte array of the raw data from the KNX bus
	 * @return the openHAB command or state that corresponds to the data
	 */
	private Type getType(Datapoint datapoint, byte[] asdu) {
		for (KNXTypeMapper typeMapper : typeMappers) {
			Type type = typeMapper.toType(datapoint, asdu);
			if (type != null)
				return type;
		}
		return null;
	}

	/**
	 * Returns the datapoints for a given item and type class. This method iterates over all registered KNX binding
	 * providers to find the result.
	 * 
	 * @param itemName
	 *            the item name for the datapoints
	 * @param typeClass
	 *            the type class associated to the datapoints
	 * @return the datapoints which corresponds to the given item and type class
	 */
	private Iterable<Datapoint> getDatapoints(final String itemName, final Class<? extends Type> typeClass) {
		Set<Datapoint> datapoints = new HashSet<Datapoint>();
		for (KNXBindingProvider provider : providers) {
			for (Datapoint datapoint : provider.getDatapoints(itemName, typeClass)) {
				datapoints.add(datapoint);
			}
		}
		return datapoints;
	}

	/**
	 * Transforms an openHAB type (command or state) into a datapoint type value for the KNX bus.
	 * 
	 * @param type
	 *            the openHAB command or state to transform
	 * @param dpt 
	 * 			  the datapoint type to which should be converted
	 * 
	 * @return the corresponding KNX datapoint type value as a string
	 */
	private String toDPTValue(Type type, String dpt) {
		for (KNXTypeMapper typeMapper : typeMappers) {
			String value = typeMapper.toDPTValue(type, dpt);
			if (value != null)
				return value;
		}
		return null;
	}

	/**
	 * The DatapointInitializer runs as a separate thread. Whenever new KNX bindings are added, it takes care that read
	 * requests are sent to all new datapoints, which support this request. By this, the initial status can be
	 * determined and one does not have to stay in an "undefined" state until the first telegram is sent on the NX bus
	 * for this datapoint. As there might be hundreds of datapoints added at the same time and we do not want to flood
	 * the KNX bus with read requests, we wait a configurable period of milliseconds between two requests. As a result,
	 * this might be quite long running and thus is executed in its own thread.
	 * 
	 * @author Kai Kreuzer
	 * @since 0.3.0
	 * 
	 */
	private class DatapointInitializer extends Thread {

		private boolean interrupted = false;
		
		public DatapointInitializer() {
			super("KNX datapoint initializer");
		}

		public void setInterrupted(boolean interrupted) {
			this.interrupted = interrupted;
		}

		@Override
		public void run() {
			// as long as no interrupt is requested, continue running
			while (!interrupted) {
				if(datapointsToInitialize.size() > 0) {
					// we first clone the list, so that it stays unmodified
					Collection<Datapoint> clonedList = new HashSet<Datapoint>(datapointsToInitialize);
					initializeDatapoints(clonedList);
				}
				// just wait before looping again
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}

		private void initializeDatapoints(Collection<Datapoint> readableDatapoints) {
			for (Datapoint datapoint : readableDatapoints) {
				try {
					ProcessCommunicator pc = KNXConnection.getCommunicator();
					if (pc != null) {
						logger.debug("Sending read request to KNX for item {}", datapoint.getName());
						pc.read(datapoint);
					}
				} catch (KNXException e) {
					logger.warn("Cannot read value for item '{}' from KNX bus: {}", new String[] { datapoint.getName(), e.getMessage() });
				} catch (KNXIllegalArgumentException e) {
					logger.warn("Error sending KNX read request for '{}': {}", new String[] { datapoint.getName(), e.getMessage() });
				}
				datapointsToInitialize.remove(datapoint);
				long pause = KNXConnection.getReadingPause();
				if (pause > 0) {
					try {
						sleep(pause);
					} catch (InterruptedException e) {
						logger.debug("KNX reading pause has been interrupted: {}", e.getMessage());
					}
				}
			}
		}
	}
	

}
