/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.primare.PrimareBindingProvider;
import org.openhab.binding.primare.internal.protocol.PrimareConnector;
import org.openhab.binding.primare.internal.protocol.PrimareSerialConnector;
import org.openhab.binding.primare.internal.protocol.PrimareTCPConnector;
import org.openhab.binding.primare.internal.protocol.PrimareUtils;
import org.openhab.binding.primare.internal.protocol.PrimareResponse;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.binding.BindingChangeListener;
import org.openhab.core.binding.BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;

import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Binding listening to openHAB event bus and send commands to Primare devices when certain
 * commands are received.
 * 
 * @author Pauli Anttila, Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareBinding extends AbstractBinding<PrimareBindingProvider> 
	implements ManagedService, BindingChangeListener, PrimareEventListener {

	private static final Logger logger = LoggerFactory.getLogger(PrimareBinding.class);

	protected static final String WILDCARD_COMMAND_KEY = "*";

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port|serial|model)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port|serial|model)$");
	
	/** Map table to store all available receivers configured by the user */
	protected Map<String, DeviceConfig> deviceConfigCache = null;
	
	public PrimareBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		closeAllConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("bindingChanged for {}", itemName);
		initializeItem(itemName);
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
	
		if (itemName == null) {
			logger.warn("ignore command {} - no item name", command);
			return;
		}

		logger.debug("Received command [item:{} command:{}, class:{}]",
			     itemName, command.toString(), command.getClass().toString());
	    
		
		PrimareBindingProvider provider = 
			findFirstMatchingBindingProvider(itemName, command.toString());
	
		if (provider == null) {
			logger.warn("Ignore item:{} command:{} - no matching binding provider",
				    itemName, command);
			return;
		}
		
		String tmp = provider.getDeviceCommand(itemName, command.toString());

		if (tmp == null) {
			tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);	
		}

		if (tmp == null) {
			logger.warn("Ignore item:{} command {} - no matching command",
				    itemName, command);
			return;
		}

		String[] commandParts = tmp.split(":");
		String deviceId = commandParts[0];
		String deviceCmd = commandParts[1];

		if (deviceId == null || deviceCmd == null) {
			logger.warn("Ignore item:{} command:{} - failed to find both device id and command in {}",
				    itemName, command, tmp);
			return;
		}

		DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);
		if (deviceConfig == null) {
			logger.warn("Ignore item:{} command:{} - no configuration found for device {}",
				    itemName, command, deviceId);
			return;
		}

		PrimareConnector deviceConnector = deviceConfig.getInitializedConnector();

		if (deviceConnector == null) {
			logger.warn("Ignore {} item:{} command:{} - no connector found",
				    deviceConfig.toString(), itemName, command);
			return;
		}

		if (deviceConnector.isConnected()) {
			try {
				deviceConnector.sendCommand(command, deviceCmd);
			} catch (Exception e) {
				logger.warn("Ignore {} item:{} command:{} - message send error {}",
					    deviceConfig.toString(), itemName, command, e.getMessage());
			}
		} else {
			logger.warn("Ignore {} item:{} command:{} - device disconnected",
				    deviceConfig.toString(), itemName, command);
		}
	}
    

	/**
	 * Find the first matching {@link PrimareBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>.
	 * 
	 * @param itemName
	 *            the item for which to find a provider
	 * @param command
	 *            the openHAB command for which to find a provider
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private PrimareBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {

		PrimareBindingProvider firstMatchingProvider = null;

		for (PrimareBindingProvider provider : this.providers) {
			String tmp = provider.getDeviceCommand(itemName, command.toString());
			if (tmp != null) {
				firstMatchingProvider = provider;
				break;
			}
		}
		
		if (firstMatchingProvider == null) {
			for (PrimareBindingProvider provider : this.providers) {
				String tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);
				if (tmp != null) {
					firstMatchingProvider = provider;
					break;
				}
			}
			
		}

		return firstMatchingProvider;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, valid configuration {}", (config != null ? "exists" : "does not exist"));

		if (config != null) {
			Enumeration<String> keys = config.keys();
			
			if ( deviceConfigCache == null ) {
				deviceConfigCache = new HashMap<String, DeviceConfig>();
			}
			
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '" + key
						     + "' does not follow the expected pattern '<id>.<host|port|model>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);
				
				if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.valueOf(value);
				} else if ("serial".equals(configKey)) {
					deviceConfig.serialport = value;
				} else if ("model".equals(configKey)) {
					String model = value.toUpperCase();
					
					deviceConfig.model = value.toUpperCase();					

				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}


			// open connection to all receivers
			for (String device : deviceConfigCache.keySet()) {
				PrimareConnector connector = deviceConfigCache.get(device).getInitializedConnector();
				if (connector != null) {
					try {
						connector.connect();
						connector.addEventListener(this);
					} catch (Exception e) {
						logger.warn("failed to connect to {} after configuration update", device.toString());
					}


					
				}
			}

			for (PrimareBindingProvider provider : this.providers) {
				for (String itemName : provider.getItemNames()) {
					initializeItem(itemName);
				}
			}

		}
	}

	private void closeAllConnections() {
		if (deviceConfigCache != null) {
			for (String device : deviceConfigCache.keySet()) {
				PrimareConnector connector = deviceConfigCache.get(device).getConnector();
				if (connector != null) {
					connector.disconnect();
					connector.removeEventListener(this);
				}
			}
			deviceConfigCache = null;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */	
	@Override
	public void statusUpdateReceived(EventObject event, String deviceId, byte[] data) {

		logger.trace("statusUpdateReceived for device {}: {}", 
			     deviceId, PrimareUtils.byteArrayToHex(data));

		DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

		if (deviceConfig != null) {
			logger.trace("Received status update '{}' from device {}", 
				     PrimareUtils.byteArrayToHex(data), 
				     deviceConfig.toString());

			PrimareResponse deviceResponse = null;
			
			deviceResponse = deviceConfig.connector.getResponseFactory().getResponse(data);


			for (PrimareBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Update all items which refer to command
					HashMap<String, String> values = provider.getDeviceCommands(itemName);

					for (String cmd : values.keySet()) {
						String[] commandParts = values.get(cmd).split(":");
						String deviceCmd = commandParts[1];

						boolean match = deviceResponse.isRelevantFor(deviceCmd);

						if (match) {
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State v = deviceResponse.openHabState(itemType);
							logger.trace("Updating itemName {} to {} based on device command {}",
								     itemName, v, deviceCmd);
							eventPublisher.postUpdate(itemName, v);
							break;
						}

					}
				}
			}
		}
	}


	/**
	 * Initialize item value. Method sends a query to receiver if init query is
	 * configured to binding item configuration
	 * 
	 * @param itemType
	 * 
	 */
	private void initializeItem(String itemName) {

		logger.debug("Called initializeItem for {}, ignored", itemName);
		
		for (PrimareBindingProvider provider : providers) {
			
			String initCmd = provider.getItemInitCommand(itemName);
			
			if (initCmd == null) {
				logger.debug("No init command found for item {}", itemName);
				continue;
			}
			
			logger.debug("Initialize item {} with {}",
				     itemName, initCmd);
			
			String[] commandParts = initCmd.split(":");
			String deviceId = commandParts[0];
			String deviceCmd = commandParts[1];
	    
			if (deviceId == null || deviceCmd == null) {
				logger.warn("Initializing - ignore item:{} initCmd:{} - failed to find both device id and command in {}",
					    itemName, initCmd, initCmd);
				continue;
			}

			DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);
			if (deviceConfig == null) {
				logger.warn("Ignore item:{} initCmd:{} - no configuration found for device {}",
					    itemName, initCmd, deviceId);
				continue;
			}

			PrimareConnector connector = deviceConfig.getInitializedConnector();

			if (connector == null) {
				logger.warn("Ignore device:{} item:{} initCmd:{} - no connector (IP or serial) found for device {}",
					    deviceId, itemName, initCmd, deviceId);
				continue;
			}

			if (!connector.isConnected()) {
				logger.warn("Ignore device:{} item:{} initCmd:{} - {} not connected",
					    deviceId, itemName, initCmd, deviceConfig.toString());
				continue;
			}

			
			try {
				// There are no arguments for a device init command
				connector.sendCommand(null, deviceCmd);
			} catch (Exception e) {
				logger.warn("Ignore device:{} item:{} initCmd:{} Message send error {}",
					    deviceId, itemName, initCmd, e.getMessage());
			}
		}

		return;
	}
	

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {
		
		// TCP-to-serial converter connected to the Primare device
		private String host;     // hostname or IP address
		private int port = 0;    // port

		// Direct serial connection to Primare device
		private String serialport = null;   // Serial port device or PTY, e.g. /dev/ttyS0, /dev/pts/5
	
		// Primare model in uppercase, examples: "SP31.7", "SP31", "SPA20", "SPA21"
		private String model = null;

		// Primare model -dependent PrimareConnector subclass instance
		private PrimareConnector connector = null;

		// Device id used in config files
		private String deviceId;


		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}
		
		@Override
		public String toString() {
			if (deviceId == null && model == null && serialport == null && host == null && port == 0)
				return "Primare device [uninitialized]";
			else
				return (serialport == null ? 
					String.format("Primare device [id:%s model:%s host:%s port:%d]", deviceId, model, host, port) :
					String.format("Primare device [id:%s model:%s serial:%s]", deviceId, model, serialport));
		}


		// Initializes a connector based on openhab.cfg configuration. 
		// See PrimareTCPConnector, PrimareSerialConnector if implementing
		// support for new Primare models
		public PrimareConnector getInitializedConnector() {
			if (connector == null) {
				connector = (serialport == null ? 
					     PrimareTCPConnector.newForModel(model, deviceId, host, port) :
					     PrimareSerialConnector.newForModel(model, deviceId, serialport));
			}
			return connector;
		}
	
		public PrimareConnector getConnector() {
			return connector;
		}

	}

}
