/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.onkyo.OnkyoBindingProvider;
import org.openhab.binding.onkyo.internal.eiscp.Eiscp;
import org.openhab.binding.onkyo.internal.eiscp.EiscpCommand;
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
 * Binding listening to openHAB event bus and send commands to Onkyo devices when certain
 * commands are received.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class OnkyoBinding extends AbstractBinding<OnkyoBindingProvider> implements ManagedService, BindingChangeListener, OnkyoEventListener {

	private static final Logger logger = LoggerFactory.getLogger(OnkyoBinding.class);

	protected static final String ADVANCED_COMMAND_KEY = "#";
	protected static final String WILDCARD_COMMAND_KEY = "*";

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern.compile("^(.*?)\\.(host|port)$");
	
	/** Onkyo receiver default tcp port */
	private final static int DEFAULT_PORT = Eiscp.DEFAULT_EISCP_PORT;

	/** Map table to store all available receivers configured by the user */
	protected Map<String, DeviceConfig> deviceConfigCache = null;
	
	public OnkyoBinding() {
	}

	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		closeAllConnections();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void bindingChanged(BindingProvider provider, String itemName) {
		logger.debug("bindingChanged {}", itemName);
		initializeItem(itemName);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null) {
			OnkyoBindingProvider provider = 
				findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug(
				"Received command (item='{}', state='{}', class='{}')",
				new Object[] { itemName, command.toString(), command.getClass().toString() });

			String tmp = provider.getDeviceCommand(itemName, command.toString());

			if (tmp == null) {
				tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);	
			}
			
			String[] commandParts = tmp.split(":");
			String deviceId = commandParts[0];
			String deviceCmd = commandParts[1];
			
			DeviceConfig device = deviceConfigCache.get(deviceId);
			OnkyoConnection remoteController = device.getConnection();

			if (device != null && remoteController != null) {
		
				if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
					
					// advanced command
					
					deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");
			
					if (deviceCmd.contains("%")) {

						// eISCP command is a template where value should be updated 
						deviceCmd = convertOpenHabCommandToDeviceCommand( command, deviceCmd);
					}
					
				} else {
					
					// normal command
					
					EiscpCommand cmd = EiscpCommand.valueOf(deviceCmd);
					deviceCmd = cmd.getCommand();
					
					if (deviceCmd.contains("%")) {

						// eISCP command is a template where value should be updated 
						deviceCmd = convertOpenHabCommandToDeviceCommand( command, deviceCmd);
					} 
				}
				
				if (deviceCmd != null) {
					remoteController.send(deviceCmd);
				} else {
					logger.warn("Cannot convert value '{}' to eISCP format", command);
				}

			} else {
				logger.warn("Cannot find connection details for device id '{}'", deviceId);
			}
		}
	}
	
	/**
	 * Convert OpenHAB commmand to onkyo receiver command.
	 * 
	 * @param command
	 * @param cmdTemplate
	 * 
	 * @return
	 */
	private String convertOpenHabCommandToDeviceCommand( Command command, String cmdTemplate ) {
		String deviceCmd = null;
		
		if (command instanceof OnOffType) {
			deviceCmd = String.format(cmdTemplate, command == OnOffType.ON ? 1: 0);	 
		
		} else if (command instanceof StringType) {
			deviceCmd = String.format(cmdTemplate, command);	 
			
		} else if (command instanceof DecimalType) {
			deviceCmd = String.format(cmdTemplate, ((DecimalType) command).intValue());	 
				
		} else if (command instanceof PercentType) {
			deviceCmd = String.format(cmdTemplate, ((DecimalType) command).intValue());	 
		} 

		return deviceCmd;
	}

	/**
	 * Find the first matching {@link OnkyoBindingProvider} according to
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private OnkyoBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		OnkyoBindingProvider firstMatchingProvider = null;

		for (OnkyoBindingProvider provider : this.providers) {
			String tmp = provider.getDeviceCommand(itemName, command.toString());
			if (tmp != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		if (firstMatchingProvider == null) {
			for (OnkyoBindingProvider provider : this.providers) {
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

		logger.debug("Configuration updated, config {}", config != null ? true
				: false);

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
						+ "' does not follow the expected pattern '<id>.<host|port>'");
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
				} else {
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");
				}
			}
			
			// open connection to all receivers
			for (String device : deviceConfigCache.keySet()) {
				OnkyoConnection connection = deviceConfigCache.get(device).getConnection();
				if (connection != null) {
					connection.openConnection();
					connection.addEventListener(this);
				}
			}
			
			for (OnkyoBindingProvider provider : this.providers) {
				for (String itemName : provider.getItemNames()) {
					initializeItem(itemName);
				}
			}

		}
	}

	private void closeAllConnections() {
		if (deviceConfigCache != null) {
			for (String device : deviceConfigCache.keySet()) {
				OnkyoConnection connection = deviceConfigCache.get(device).getConnection();
				if (connection != null) {
					connection.closeConnection();
					connection.removeEventListener(this);
				}
			}
			deviceConfigCache = null;
		}
	}

	/**
	 * Find receiver from device caache by ip address.
	 * 
	 * @param ip
	 * @return
	 */
	private DeviceConfig findDevice(String ip) {
		for (String device : deviceConfigCache.keySet()) {
			DeviceConfig deviceConfig = deviceConfigCache.get(device);
			if (deviceConfig != null) {
				if (deviceConfig.getHost().equals(ip)) {
					return deviceConfig;
				}
			}
		}
		
		return null;
	}
	
	@Override
	public void statusUpdateReceived(EventObject event, String ip, String data) {
		// find correct device from device cache
		DeviceConfig deviceConfig = findDevice(ip);

		if (deviceConfig != null) {
			logger.debug("Received status update '{}' from device {}", data, deviceConfig.host);

			for (OnkyoBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Update all items which refer to command
					HashMap<String, String> values = provider.getDeviceCommands(itemName);

					for (String cmd : values.keySet()) {
						String[] commandParts = values.get(cmd).split(":");
						String deviceCmd = commandParts[1];

						boolean match = false;
						if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
							// skip advanced command key and compare 3 first character
							if (data.startsWith(deviceCmd.substring(1, 4))) {
								match = true;
							}
						} else {
							try {
								String eiscpCmd = EiscpCommand.valueOf(deviceCmd).getCommand();
								
								// compare 3 first character
								if (data.startsWith(eiscpCmd.substring(0, 3))) {
									match = true;
								}
								
							} catch (Exception e) {
								logger.error("Unregonized command '" + deviceCmd + "'", e);
							}
						}

						if (match) {
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State v = convertDeviceValueToOpenHabState(itemType, data);
							eventPublisher.postUpdate(itemName, v);
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Convert receiver value to OpenHAB state.
	 * 
	 * @param itemType
	 * @param data
	 * 
	 * @return
	 */
	private State convertDeviceValueToOpenHabState(Class<? extends Item> itemType, String data) {
		State state = UnDefType.UNDEF;

		try {
			int index;
			String s;
		
			if (itemType == SwitchItem.class) {
				index = Integer.parseInt(data.substring(3, 5), 16);
				state = index == 0 ? OnOffType.OFF : OnOffType.ON;
				
			} else if (itemType == NumberItem.class) {
				index = Integer.parseInt(data.substring(3, 5), 16);
				state = new DecimalType(index);
				
			} else if (itemType == DimmerItem.class) {
				index = Integer.parseInt(data.substring(3, 5), 16);
				state = new PercentType(index);
				
			} else if (itemType == RollershutterItem.class) {
				index = Integer.parseInt(data.substring(3, 5), 16);
				state = new PercentType(index);
				
			} else if (itemType == StringItem.class) {
				s = data.substring(3, data.length());
				state = new StringType(s);
			}
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", data, itemType);
		}
		
		return state;
	}
	
	/**
	 * Initialize item value. Method send query to receiver if init query is configured to binding item configuration
	 * 
	 * @param itemType
	 * 
	 */
	private void initializeItem(String itemName) {
		for (OnkyoBindingProvider provider : providers) {
			String initCmd = provider.getItemInitCommand(itemName);
			if (initCmd != null) {
				logger.debug("Initialize item {}", itemName);

				String[] commandParts = initCmd.split(":");
				String deviceId = commandParts[0];
				String deviceCmd = commandParts[1];

				DeviceConfig device = deviceConfigCache.get(deviceId);
				OnkyoConnection remoteController = device.getConnection();

				if (device != null && remoteController != null) {
					if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
						deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");
					} else {
						EiscpCommand cmd = EiscpCommand.valueOf(deviceCmd);
						deviceCmd = cmd.getCommand();
					}

					remoteController.send(deviceCmd);
				} else {
					logger.warn(
							"Cannot find connection details for device id '{}'",
							deviceId);
				}
			}
		}
	}
	

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		String host;
		int port = DEFAULT_PORT;

		OnkyoConnection connection = null;
		String deviceId;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getHost(){
			return host;
		}
		
		public int getPort(){
			return port;
		}
		
		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + "]";
		}

		OnkyoConnection getConnection() {
			if (connection == null) {
				connection = new OnkyoConnection(host, port);
			}
			return connection;
		}

	}

}
