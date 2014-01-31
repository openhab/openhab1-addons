/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.openhab.binding.heatmiser.HeatmiserBindingProvider;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserPRT;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserPRTHW;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	

/**
 * This class implements the Heatmiser binding. It actively polls all thermostats and sets the item values.
 * 
 * The pollingTable is created from the item bindings, and a separate thermostat array is maintained from
 * the responses. The two are separated to allow the system to determine the thermostat type based on the response
 * rather than requiring this additional information in the binding string.
 * 
 * The pollingTable is recreated after each complete poll cycle to allow for new bindings
 * 
 * @author Chris Jackson
 * @since 1.4.0
 */
public class HeatmiserBinding extends AbstractActiveBinding<HeatmiserBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(HeatmiserBinding.class);

	private String ipAddress;
	private int ipPort;
	private final int DefaultPort = 1024;
	
	// Polling and receiving are separated so that we can automatically detect the type of thermostat via the receive packet.
	private Iterator<Integer> pollIterator = null;
	private List<Integer> pollingTable = new ArrayList<Integer>();
	private List<HeatmiserThermostat> thermostatTable = new ArrayList<HeatmiserThermostat>();
	
	private MessageListener eventListener = new MessageListener();
	private HeatmiserConnector connector = null;


	/** 
	 * the refresh interval which is used to poll values from the Heatmiser
	 * system (optional, defaults to 2000ms)
	 */
	private long refreshInterval = 2000;

	public HeatmiserBinding() {
	}
	
	public void activate() {
		logger.debug("Heatmiser binding activated");
		super.activate();
	}
	
	public void deactivate() {
		logger.debug("Heatmiser binding deactivated");

		stopListening();
	}

	private void listen() {
		stopListening();

		connector = new HeatmiserConnector();
		if (connector != null) {
			// Initialise the IP connection
			connector.addEventListener(eventListener);
			try {
				connector.connect(ipAddress, ipPort);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopListening() {
		if(connector != null) {
			connector.disconnect();
			connector.removeEventListener(eventListener);
			connector = null;
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected String getName() {
		return "Heatmiser Refresh Service";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("HEATMISER execute() method is called!");
		
		if(pollingTable == null)
			return;
		
		if(pollIterator == null) {			
			// Rebuild the polling table
			pollingTable = new ArrayList<Integer>();
			if(pollingTable == null) {
				logger.error("HEATMISER error creating pollingTable");
				return;
			}

			// Detect all thermostats from the items and add them to the polling table
			for(int address = 0; address < 16; address++) {
				for (HeatmiserBindingProvider provider : providers) {
					if(provider.getBindingItemsAtAddress(address).size() != 0)
						pollingTable.add(address);
				}
			}

			pollIterator = pollingTable.iterator();
		}

		if(pollIterator.hasNext() == false) {
			pollIterator = null;
			return;
		}

		int pollAddress = (int) pollIterator.next();
		HeatmiserThermostat pollThermostat = new HeatmiserThermostat();
		logger.debug("HEATMISER: polling {}", pollAddress);
		pollThermostat.setAddress((byte)pollAddress);

		if(pollIterator.hasNext() == false)
			pollIterator = null;

		connector.sendMessage(pollThermostat.pollThermostat());
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		logger.debug("Heatmiser Command: {} to {}", itemName, command);

		HeatmiserBindingProvider providerCmd = null;
		for (HeatmiserBindingProvider provider : this.providers) {
			int address = provider.getAddress(itemName);
			if (address != -1) {
				providerCmd = provider;
				break;
			}
		}

		if(providerCmd == null) {
			logger.debug("Heatmiser command provider not found!!");
			return;
		}
		logger.debug("Heatmiser command provider is: {}", providerCmd);
		
		int address = providerCmd.getAddress(itemName);
		Functions function = providerCmd.getFunction(itemName);
		
		for (HeatmiserThermostat thermostat: thermostatTable) {
			if(thermostat.getAddress() == address) {
				logger.debug("Heatmiser command found thermostat: {}", thermostat);
				// Found the thermostat
				byte[] commandPacket = thermostat.formatCommand(function, command);
				if(commandPacket == null)
					logger.debug("Heatmiser command packet null");
				else
					connector.sendMessage(commandPacket);
				return;
			}	
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("HEATMISER updated() method is called!");

		if (config != null) {
			// to override the default refresh interval one has to add a 
			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String localAddress = (String) config.get("address");
			if (StringUtils.isNotBlank(localAddress)) {
				ipAddress = localAddress;
			}
			
			String portConfig = (String) config.get("port");
			if (StringUtils.isNotBlank(portConfig)) {
				ipPort = Integer.parseInt(portConfig);
			} else {
				ipPort = DefaultPort;
			}

			// start the listener
			listen();

			// Tell the system we're good!
			setProperlyConfigured(true);
		}
	}


	/**
	 * Receives incoming packets
	 */
	private class MessageListener implements HeatmiserEventListener {
		HeatmiserThermostat thermostatPacket = null;

		@Override
		public void packetReceived(EventObject event, byte[] packet) {
			thermostatPacket = new HeatmiserThermostat();
			if(thermostatPacket.setData(packet) == false)
				return;

			for (HeatmiserThermostat thermostat: thermostatTable) {
				if(thermostat.getAddress() == thermostatPacket.getAddress()) {
					// Found the thermostat
					thermostat.setData(packet);
					processItems(thermostat);
					return;
				}	
			}
			
			// Thermostat not found in the list of known devices
			// Create a new thermostat and add it to the array
			HeatmiserThermostat newThermostat = null;
			switch(thermostatPacket.getModel()) {
				case PRT:
				case PRTE:
					newThermostat = new HeatmiserPRT();
					break;
				case PRTHW:
					newThermostat = new HeatmiserPRTHW();
					break;
				default:
					logger.error("Unknown heatmiser thermostat type {} at address {}", thermostatPacket.getModel(), thermostatPacket.getAddress());
					break;
			}
			
			// Add the new thermostat to the list
			if(newThermostat != null) {
				newThermostat.setData(packet);
				thermostatTable.add(newThermostat);
				processItems(newThermostat);
			}
		}

		private void processItems(HeatmiserThermostat thermostat) {
			for (HeatmiserBindingProvider provider : providers) {
				for (String itemName : provider.getBindingItemsAtAddress(thermostat.getAddress())) {
					State state = null;
					switch(provider.getFunction(itemName)) {
					case FROSTTEMP:
						state = thermostat.getFrostTemperature(provider.getItemType(itemName));
						break;
					case FLOORTEMP:
						state = thermostat.getFloorTemperature(provider.getItemType(itemName));
						break;
					case ONOFF:
						state = thermostat.getOnOffState(provider.getItemType(itemName));
						break;
					case HEATSTATE:
						state = thermostat.getHeatState(provider.getItemType(itemName));
						break;
					case ROOMTEMP:
						state = thermostat.getTemperature(provider.getItemType(itemName));
						break;
					case SETTEMP:
						state = thermostat.getSetTemperature(provider.getItemType(itemName));
						break;
					case WATERSTATE:
						state = thermostat.getWaterState(provider.getItemType(itemName));
						break;
					case HOLIDAYTIME:
						state = thermostat.getHolidayTime(provider.getItemType(itemName));
						break;
					case HOLIDAYSET:
						state = thermostat.getHolidaySet(provider.getItemType(itemName));
						break;
					case HOLIDAYMODE:
						state = thermostat.getHolidayMode(provider.getItemType(itemName));
						break;
					case HOLDTIME:
						state = thermostat.getHoldTime(provider.getItemType(itemName));
						break;
					case HOLDMODE:
						state = thermostat.getHoldTime(provider.getItemType(itemName));
						break;
					case STATE:
						state = thermostat.getState(provider.getItemType(itemName));
						break;
					default:
						break;
					}

					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					} else {
						logger.error("'{}' couldn't be parsed to a State.", itemName);
					}
				}
			}
		}
	}
}
