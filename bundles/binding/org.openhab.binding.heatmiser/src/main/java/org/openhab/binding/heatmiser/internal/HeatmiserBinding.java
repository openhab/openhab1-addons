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
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openhab.binding.heatmiser.HeatmiserBindingProvider;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserNetworkThermostat;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserWifiThermostat;
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
	
	// Polling and receiving are separated so that we can automatically detect the type of thermostat via the receive packet.
	private Iterator<HeatmiserThermostat> pollIterator = null;
	private List<HeatmiserThermostat> thermostatTable = new ArrayList<HeatmiserThermostat>();
	
	private MessageListener eventListener = new MessageListener();

	private Map<String, ConnectorConfig> connectorList = new HashMap<String, ConnectorConfig>();


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

		// Loop through all the connectors and initialise the connections
		Iterator<Entry<String, ConnectorConfig>> it = connectorList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ConnectorConfig> pairs = (Map.Entry<String, ConnectorConfig>) it.next();
			ConnectorConfig cfgSettings = pairs.getValue();

			// Initialise the IP connection
			try {
				HeatmiserConnector connector = null;

				connector = new HeatmiserConnector();
				connector.addEventListener(eventListener);
				connector.connect(cfgSettings.getAddress(), cfgSettings.getPort());
				cfgSettings.setConnector(connector);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void stopListening() {
		// Loop through all the connectors and close the connections
		Iterator<Entry<String, ConnectorConfig>> it = connectorList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ConnectorConfig> pairs = (Map.Entry<String, ConnectorConfig>) it.next();
			ConnectorConfig cfgSetting = pairs.getValue();

			HeatmiserConnector connector = cfgSetting.getConnector();

			// Disconnect this connector
			if(connector != null) {
				connector.disconnect();
				connector.removeEventListener(eventListener);
				cfgSetting.setConnector(null);
			}
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
	 * The 'polling table' gets rebuilt after each cycle. This is in case the binding configuration
	 * gets updated
	 */
	private void buildPollingTable() {
		thermostatTable.clear();
		// Loop through all the connectors and initialise the polling table
		Iterator<Entry<String, ConnectorConfig>> it = connectorList.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, ConnectorConfig> pairs = (Map.Entry<String, ConnectorConfig>) it.next();
			ConnectorConfig cfgSettings = pairs.getValue();

			if(cfgSettings.getType() == ConnectorConfig.Type.NETWORK) {
				// Detect all thermostats from the items and add them to the polling table
				for(int address = 0; address < 16; address++) {
					for (HeatmiserBindingProvider provider : providers) {
						if(provider.getBindingItemsAtAddress(cfgSettings.getName(), address).size() != 0) {
							HeatmiserNetworkThermostat thermostat = new HeatmiserNetworkThermostat();
							thermostat.setConnector(cfgSettings.getName());
							thermostat.setAddress((byte)address);
							thermostatTable.add(thermostat);
						}
					}
				}
			}

			// WIFI thermostats get polled immediately
			if(cfgSettings.getType() == ConnectorConfig.Type.WIFI) {
				HeatmiserWifiThermostat thermostat = new HeatmiserWifiThermostat();
				thermostat.setConnector(cfgSettings.getName());
				thermostat.setPIN(cfgSettings.getPIN());
				thermostatTable.add(thermostat);
			}
		}
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void execute() {
		// the frequently executed code (polling) goes here ...
		logger.debug("HEATMISER execute() method is called!");
		
		if(thermostatTable == null)
			return;
		
		// If we've finished the current poll cycle, rebuild the polling table
		if(pollIterator == null) {
			buildPollingTable();
			pollIterator = thermostatTable.iterator();
		}

		// Is there anything to poll?
		if(pollIterator.hasNext() == false) {
			pollIterator = null;
			return;
		}

		HeatmiserThermostat pollThermostat = pollIterator.next();
		if(pollIterator.hasNext() == false)
			pollIterator = null;

		connectorList.get(pollThermostat.getConnector()).getConnector().sendMessage(pollThermostat.pollThermostat());
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
			logger.error("Heatmiser command provider not found!!");
			return;
		}
		logger.debug("Heatmiser command provider is: {}", providerCmd);

		// Get the thermostat connector type (WiFi or Network) and address for this item
		String connector = providerCmd.getConnector(itemName);
		int address = providerCmd.getAddress(itemName);
		Functions function = providerCmd.getFunction(itemName);

		// Find the thermostat
		for (HeatmiserThermostat thermostat: thermostatTable) {
			if(thermostat.getConnector().equalsIgnoreCase(connector) && thermostat.getAddress() == address) {
				logger.debug("Heatmiser command found thermostat: {}", thermostat);
				// Found the thermostat
				byte[] commandPacket = thermostat.formatCommand(function, command);
				if(commandPacket == null)
					logger.error("Heatmiser command packet null");
				else
					connectorList.get(thermostat.getConnector()).getConnector().sendMessage(commandPacket);

				return;
			}	
		}
		logger.error("Heatmiser command did not find thermostat");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("HEATMISER updated() method is called!");

		if (config == null) {
			return;
		}

		Enumeration<String> keys = config.keys();

		if ( connectorList == null ) {
			connectorList = new HashMap<String, ConnectorConfig>();
		}
		
		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();

			// the config-key enumeration contains additional keys that we
			// don't want to process here ...
			if ("service.pid".equals(key)) {
				continue;
			}

			String[] split = key.split("\\.");
			if(split == null)
				continue;
			
			// Global configuration?
			if(split.length == 1) {
				// To override the default refresh interval one has to add a 
				// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
				if(split[0].equalsIgnoreCase("refresh")) {
					refreshInterval = Long.parseLong((String)config.get(key));
				}

				continue;
			}
			
			// Interface configuration
			if(split.length != 2)
				continue;
			
			ConnectorConfig connectorConfig = connectorList.get(split[0]);

			if (connectorConfig == null) {
				connectorConfig = new ConnectorConfig();
				connectorConfig.setName(split[0]);
				connectorList.put(split[0], connectorConfig);
			}

			if(split[1].equalsIgnoreCase("address")) {
				connectorConfig.setHost((String)config.get(key));
			}

			if(split[1].equalsIgnoreCase("type")) {
				connectorConfig.setType((String)config.get(key));
			}

			if(split[1].equalsIgnoreCase("pin")) {
				connectorConfig.setPIN(Integer.parseInt((String)config.get(key)));
			}
		}

		// start the listener
		listen();

		// Tell the system we're good to go!
		setProperlyConfigured(true);
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the HTTP binding
	 * provider.
	 */
	static private class ConnectorConfig {
		private final int DEFAULT_PORT_NETWORK = 1024;
		private final int DEFAULT_PORT_WIFI = 8068;
		private String name;
		private String address;
		private Integer port = 0;
		private Type type = Type.NETWORK;
		private HeatmiserConnector connector;
		private Integer pin;
		
		public void setHost(String host) {
			String split[] = host.split(":");
			if(split.length > 0)
				address = split[0];
			
			if(split.length == 1)
				return;
			
			port = Integer.parseInt(split[1]);
		}
		
		public void setPIN(Integer newPin) {
			pin = newPin;
		}
		
		public Integer getPIN() {
			return pin;
		}
		
		public void setName(String newName) {
			name = newName;
		}
		
		public String getName() {
			return name;
		}

		public void setType(String newType) {
			type = Type.valueOf(newType.toUpperCase());
		}
		
		public Type getType() {
			return type;
		}

		public String getAddress() {
			return address;
		}
		
		public Integer getPort() {
			if(port != 0)
				return port;

			if(type == Type.NETWORK)
				return DEFAULT_PORT_NETWORK;
			return DEFAULT_PORT_WIFI;
		}
		
		public void setConnector(HeatmiserConnector newConnector) {
			connector = newConnector;
		}

		public HeatmiserConnector getConnector() {
			return connector;
		}
		
		enum Type {
			NETWORK, WIFI
		}
	}

	/**
	 * Receives incoming packets
	 */
	private class MessageListener implements HeatmiserEventListener {
		HeatmiserThermostat thermostatPacket = null;

		@Override
		public void packetReceived(EventObject event, byte[] packet) {
			// Get the connector that sent this event
			HeatmiserConnector connectorObj = (HeatmiserConnector) event.getSource();
			
			ConnectorConfig sourceConnector = null;
			Iterator<Entry<String, ConnectorConfig>> it = connectorList.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry<String, ConnectorConfig> pairs = (Map.Entry<String, ConnectorConfig>) it.next();
				ConnectorConfig cfgSettings = pairs.getValue();
				if(cfgSettings.getConnector() == connectorObj) {
					sourceConnector = cfgSettings;
					break;
				}
			}
			
			// Did we find the connector?
			if(sourceConnector == null) {
				logger.error("Unable to correlate event source.");
				return;
			}

			logger.debug("Packet received from {} connector '{}'.", sourceConnector.getType(), sourceConnector.getName());

			switch(sourceConnector.getType()) {
			case NETWORK:
				thermostatPacket = new HeatmiserNetworkThermostat();
				break;
			case WIFI:
				thermostatPacket = new HeatmiserWifiThermostat();
				break;
			default:
				logger.error("Unknown connector type!");
				return;
			}

			if(thermostatPacket.setData(packet) == false)
				return;

			// Thermostat not found in the list of known devices
			// Create a new thermostat and add it to the array
			HeatmiserThermostat newThermostat = null;
			switch(thermostatPacket.getModel()) {
			case PRT:
			case PRTE:
			case PRTHW:
				newThermostat = new HeatmiserNetworkThermostat();
				break;
			case PRT_WIFI:
			case PRTE_WIFI:
			case PRTHW_WIFI:
				newThermostat = new HeatmiserWifiThermostat();
				break;
			default:
				logger.error("Unknown heatmiser thermostat type {} at address {}", thermostatPacket.getModel(), thermostatPacket.getAddress());
				break;
			}

			// Process the thermostat items
			if(newThermostat != null) {
				newThermostat.setData(packet);
				processItems(sourceConnector, newThermostat);
			}
		}

		private void processItems(ConnectorConfig connector, HeatmiserThermostat thermostat) {
			for (HeatmiserBindingProvider provider : providers) {
				for (String itemName : provider.getBindingItemsAtAddress(connector.getName(), thermostat.getAddress())) {
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
