/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.knx.internal.bus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.IllegalClassException;
import org.openhab.binding.knx.config.KNXBindingProvider;
import org.openhab.binding.knx.config.KNXTypeMapper;
import org.openhab.binding.knx.internal.connection.KNXConnectionListener;
import org.openhab.binding.knx.internal.connection.KNXConnection;
import org.openhab.core.autoupdate.AutoUpdateBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingProvider;
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
public class KNXBinding extends AbstractBinding<KNXBindingProvider> 
	implements ProcessListener, KNXConnectionListener {

	private static final Logger logger = LoggerFactory.getLogger(KNXBinding.class);

	/** to keep track of all KNX type mappers */
	protected Collection<KNXTypeMapper> typeMappers = new HashSet<KNXTypeMapper>();
	
	/**
	 * used to store events that we have sent ourselves; we need to remember them for not reacting to them
	 */
	private List<String> ignoreEventList = new ArrayList<String>();

	/**
	 * keeps track of all datapoints for which we should send a read request to the KNX bus
	 */
	private Map<Datapoint, Integer> datapointsToInitialize = Collections.synchronizedMap(new HashMap<Datapoint, Integer>());
	

	/** the datapoint initializer, which runs in a separate thread */
	private DatapointInitializer initializer = new DatapointInitializer();
	

	public void activate(ComponentContext componentContext) {
		KNXConnection.addConnectionEstablishedListener(this);
		initializer = new DatapointInitializer();
		initializer.start();
	}

	public void deactivate(ComponentContext componentContext) {
		KNXConnection.removeConnectionEstablishedListener(this);
		for (KNXBindingProvider provider : providers) {
			provider.removeBindingChangeListener(this);
		}
		providers.clear();
		initializer.setInterrupted(true);
		KNXConnection.disconnect();
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
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.trace("Received command (item='{}', command='{}')", itemName, command.toString());
		if (!isEcho(itemName, command)) {
			writeToKNX(itemName, command);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		logger.trace("Received update (item='{}', state='{}')", itemName, newState.toString());
		if (!isEcho(itemName, newState)) {
			writeToKNX(itemName, newState);
		}
	}
	
	private boolean isEcho(String itemName, Type type) {
		String ignoreEventListKey = itemName + type.toString();
		if (ignoreEventList.contains(ignoreEventListKey)) {
			ignoreEventList.remove(ignoreEventListKey);
			logger.trace("We received this event (item='{}', state='{}') from KNX, so we don't send it back again -> ignore!", itemName, type.toString());
			return true;
		}
		else {
			return false;
		}
	}

	private void writeToKNX(String itemName, Type value) {
		Iterable<Datapoint> datapoints = getDatapoints(itemName, value.getClass());
		if (datapoints != null) {
			ProcessCommunicator pc = KNXConnection.getCommunicator();
			if (pc != null) {
				for (Datapoint datapoint : datapoints) {
					try {
						pc.write(datapoint, toDPTValue(value, datapoint.getDPT()));
						logger.debug("Wrote value '{}' to datapoint '{}'", value, datapoint);
					} catch (KNXException e) {
						logger.warn("Value '{}' could not be sent to the KNX bus using datapoint '{}' - retrying one time: {}",
								new Object[]{value, datapoint, e.getMessage()});
						try {
							// do a second try, maybe the reconnection was successful
							pc = KNXConnection.getCommunicator();
							pc.write(datapoint, toDPTValue(value, datapoint.getDPT()));
							logger.debug("Wrote value '{}' to datapoint '{}' on second try", value, datapoint);
						} catch (KNXException e1) {
							logger.error("Value '{}' could not be sent to the KNX bus using datapoint '{}' - giving up after second try: {}",
								new Object[]{value, datapoint, e1.getMessage()});
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
		readFromKNX(e);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void detached(DetachEvent e) {
		logger.error("Received detach Event.");
	}
	
	
	/**
	 * Handles the given {@link ProcessEvent}. After finding the corresponding
	 * Item (by iterating through all known group addresses) this Item is updated.
	 * Each item is added to a special list to identify and avoid echo's in
	 * the <code>receiveUpdate</code> and <code>receiveCommand</code> methods.  
	 *  
	 * @param e the {@link ProcessEvent} to handle.
	 */
	private void readFromKNX(ProcessEvent e) {
		try {
			GroupAddress destination = e.getDestination();
			byte[] asdu = e.getASDU();
			if (asdu.length==0) {
				return;
			}
			for (String itemName : getItemNames(destination)) {
				Iterable<Datapoint> datapoints = getDatapoints(itemName, destination);
				if (datapoints != null) {
					for (Datapoint datapoint : datapoints) {
						Type type = getType(datapoint, asdu);					
						if (type!=null) {
							// we need to make sure that we won't send out this event to
							// the knx bus again, when receiving it on the openHAB bus
							ignoreEventList.add(itemName + type.toString());
							logger.trace("Added event (item='{}', type='{}') to the ignore event list", itemName, type.toString());
							
							if (type instanceof Command && isCommandGA(destination)) {
								eventPublisher.postCommand(itemName, (Command) type);
							} else if (type instanceof State) {
								eventPublisher.postUpdate(itemName, (State) type);
							} else {
								throw new IllegalClassException("Cannot process datapoint of type " + type.toString());
							}								
								
							logger.trace("Processed event (item='{}', type='{}', destination='{}')", new String[] {itemName, type.toString(), destination.toString()});
							return;
						}
					}
				}
			}
			logger.debug("Received telegram for unknown group address {}", destination.toString());
		} catch(RuntimeException re) {
			logger.error("Error while receiving event from KNX bus: " + re.toString());
		}
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void bindingChanged(BindingProvider provider, String itemName) {
		if (provider instanceof KNXBindingProvider) {
			KNXBindingProvider knxProvider = (KNXBindingProvider) provider;
			for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
				if(datapoint.getName().equals(itemName)) {
					datapointsToInitialize.put(datapoint, 0);
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
				datapointsToInitialize.put(datapoint, 0);
			}
		}
	}
	

	/**
	 * When a connection is (re-)established all readable datapoints are refreshed. 
	 */
	@Override
	public void connectionEstablished() {
		for (KNXBindingProvider knxProvider : providers) {
			for (Datapoint datapoint : knxProvider.getReadableDatapoints()) {
				datapointsToInitialize.put(datapoint, 0);
			}
		}
	}
	
	/**
	 * Determines whether the given <code>groupAddress</code> is the address which
	 * will be interpreted as the command type. This method iterates over all 
	 * registered KNX binding providers to find the result.

	 * @param groupAddress the group address to check
	 * @return true, if it is a command GA
	 */
	private boolean isCommandGA(GroupAddress groupAddress) {
		for (KNXBindingProvider provider : providers) {
			if(!provider.isCommandGA(groupAddress)) {
				return false;
			}
		}
		return true;
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
			while (!interrupted && !KNXConnection.shutdown) {
				if (datapointsToInitialize.size() > 0) {
					// we first clone the map, so that it stays unmodified
					HashMap<Datapoint,Integer> clonedMap =
						new HashMap<Datapoint, Integer>(datapointsToInitialize);
					initializeDatapoints(clonedMap);
				}
				// just wait before looping again
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					interrupted = true;
				}
			}
		}

		private void initializeDatapoints(HashMap<Datapoint,Integer> clonedMap) {
			for (Datapoint datapoint : clonedMap.keySet()) {
				try {
					ProcessCommunicator pc = KNXConnection.getCommunicator();
					if (pc != null) {
						logger.debug("Sending read request to KNX for item {}", datapoint.getName());
						pc.read(datapoint);
					}
					datapointsToInitialize.remove(datapoint);
				} catch (KNXException e) {
					logger.warn("Cannot read value for item '{}' from KNX bus: {}", new String[] { datapoint.getName(), e.getMessage() });
					increaseReadLimitCounter(datapoint);
				} catch (KNXIllegalArgumentException e) {
					logger.warn("Error sending KNX read request for '{}': {}", new String[] { datapoint.getName(), e.getMessage() });
					increaseReadLimitCounter(datapoint);
				} catch (InterruptedException e) {
					logger.warn("Cannot read value for item '{}' from KNX bus: {}", new String[] { datapoint.getName(), e.getMessage() });
					increaseReadLimitCounter(datapoint);
				}

				int retriesLimit = KNXConnection.getReadRetriesLimit();
				// it is possible that a key contained in the clonedMap disappeared
				// from the original datapointMap!
				int retriesCounter = datapointsToInitialize.get(datapoint) != null ? 
									 datapointsToInitialize.get(datapoint) : Integer.MIN_VALUE;
				if (retriesLimit < retriesCounter) {
					datapointsToInitialize.remove(datapoint);
					logger.debug("Giving up initialization of item {} - retries ({}) exeeded.", datapoint.getName(), retriesLimit);
				}

				long readingPause = KNXConnection.getReadingPause();
				if (readingPause > 0) {
					try {
						sleep(readingPause);
					} catch (InterruptedException e) {
						logger.debug("KNX reading pause has been interrupted: {}", e.getMessage());
					}
				}
				if(KNXConnection.shutdown) {
					return;
				}
			}
		}

		private synchronized void increaseReadLimitCounter(Datapoint datapoint) {
			if (datapointsToInitialize.containsKey(datapoint)) {
				Integer counter = datapointsToInitialize.get(datapoint);
				datapointsToInitialize.put(datapoint, counter + 1);
			}
		}		
		
	}
	

}
