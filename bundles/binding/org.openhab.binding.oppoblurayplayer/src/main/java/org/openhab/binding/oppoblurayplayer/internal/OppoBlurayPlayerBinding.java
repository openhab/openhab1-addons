/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.oppoblurayplayer.OppoBlurayPlayerBindingProvider;
import org.openhab.binding.oppoblurayplayer.internal.core.OppoBlurayPlayerNoMatchingItemException;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.events.EventPublisher;
import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
	

/**
 * Implement this class if you are going create an actively polling service
 * like querying a Website/Device.
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerBinding extends AbstractBinding<OppoBlurayPlayerBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(OppoBlurayPlayerBinding.class);

	
	/** 
	 * the refresh interval which is used to poll values from the OppoBlurayPlayer
	 * server (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;
	
	
	/**
	 * the interval to find new refresh candidates (defaults to 1000
	 * milliseconds)
	 */
	private int granularity = 1000;

	private Map<String, Long> lastUpdateMap = new HashMap<String, Long>();

	protected Map<String, DeviceConfig> deviceConfigCache = null;

	/**
	 * RegEx to validate a config
	 * <code>'^(.*?)\\.(host|port|serialPort)$'</code>
	 */
	private static final Pattern EXTRACT_CONFIG_PATTERN = 
		Pattern.compile("^(.*?)\\.(host|port|serialPort)$");

	
	@Override
	public void activate() {
		logger.debug("Activate");
		openConnection();
	}

	public void deactivate() {
		logger.debug("Deactivate");
		closeConnection();
	}

	private void openConnection() {
		if (deviceConfigCache != null) {
			// close all connections
			for (Entry<String, DeviceConfig> entry : deviceConfigCache.entrySet()) {
				DeviceConfig deviceCfg = entry.getValue();
				if (deviceCfg != null) {
					OppoBlurayPlayerDevice remoteController = deviceCfg.getConnection();
					if (remoteController.isConnected() == false){
						try {
							remoteController.connect();
						} catch (OppoBlurayPlayerException e) {
							logger.warn(
									"Error occured when open connection to device '{}'",
									deviceCfg.deviceId);;
						}
					}
				}
			}
		}
	}
	
	private void closeConnection() {
		if (deviceConfigCache != null) {
			// close all connections
			for (Entry<String, DeviceConfig> entry : deviceConfigCache.entrySet()) {
				DeviceConfig deviceCfg = entry.getValue();
				if (deviceCfg != null) {
					OppoBlurayPlayerDevice device = deviceCfg.getConnection();

					if (device != null) {
						try {
							logger.debug("Closing connection to device '{}' ", deviceCfg.deviceId);
							device.disconnect();
						} catch (OppoBlurayPlayerException e) {
							logger.error(
									"Error occured when closing connection to device '{}'",
									deviceCfg.deviceId);
						}
					}
				}
			}

			deviceConfigCache = null;
		}
	}
	


	private State queryDataFromDevice(String deviceId,
			OppoBlurayPlayerCommandType commmandType, Class<? extends Item> itemType) {

			DeviceConfig device = deviceConfigCache.get(deviceId);

			if (device == null) {
				logger.error("Could not find device '{}'", deviceId);
				return null;
			}

			OppoBlurayPlayerDevice remoteController = device.getConnection();

			if (remoteController == null) {
				logger.error("Could not find device '{}'", deviceId);
				return null;
			}

			try {
				if (remoteController.isConnected() == false)
					remoteController.connect();

				switch (commmandType) {
					default:
						logger.warn("Unknown '{}' command!", commmandType);
						return null;
				}

			} catch (OppoBlurayPlayerException e) {
				logger.warn("Couldn't execute command '{}', {}",
						commmandType.toString(), e);

			} catch (Exception e) {
				logger.warn("Couldn't create state of type '{}'", itemType);
				return null;
			}

			return null;
		}

//	/**
//	 * @{inheritDoc}
//	 */
//	@Override
//	protected void internalReceiveCommand(String itemName, Command command) {
//		// the code being executed when a command was sent on the openHAB
//		// event bus goes here. This method is only called if one of the 
//		// BindingProviders provide a binding for the given 'itemName'.
//		logger.debug("internalReceiveCommand() is called!");
//	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		OppoBlurayPlayerBindingProvider provider = findFirstMatchingBindingProvider( itemName, command);

		if (provider == null) {
			logger.warn(
					"doesn't find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}

		if (provider.isOutBinding(itemName)) {
			OppoBlurayPlayerCommandType commmandType = provider.getCommandType(itemName);
			String deviceId = provider.getDeviceId(itemName);
			if (commmandType != null) {
				sendCommandToDevice(deviceId, commmandType, command);
			}
		} else {
			logger.warn("itemName={} is not out binding", itemName);
		}
	}

	private void sendCommandToDevice(String deviceId, OppoBlurayPlayerCommandType commmandType, Command command) {
		DeviceConfig device = deviceConfigCache.get(deviceId);

		if (device == null) {
			logger.error("Could not find device '{}'", deviceId);
			return;
		}

		OppoBlurayPlayerDevice remoteController = device.getConnection();

		if (remoteController == null) {
			logger.error("Could not find device '{}'", deviceId);
			return;
		}

		try {
			
			OppoBlurayPlayerCommand commandToSend = OppoBlurayPlayerCommand.findMessageForCommand(commmandType, command);

			if (remoteController.isConnected() == false){
				remoteController.connect();
			}
			logger.debug("Sending command {} to '{}'", commandToSend, deviceId);
			remoteController.sendCommand(commandToSend);
			
			
//			switch (commmandType) {
//
//				default:
//					logger.warn("Unknown '{}' command!", commmandType);
//					break;
//			}

		} catch (OppoBlurayPlayerException e) {
			logger.error("Couldn't execute command '{}', {}", commmandType, e);

		}
	}

	/**
	 * Find the first matching {@link OppoBlurayPlayerBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>. If no direct match is
	 * found, a second match is issued with wilcard-command '*'.
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private OppoBlurayPlayerBindingProvider findFirstMatchingBindingProvider(
			String itemName, Command command) {

		OppoBlurayPlayerBindingProvider firstMatchingProvider = null;

		for (OppoBlurayPlayerBindingProvider provider : this.providers) {
			OppoBlurayPlayerCommandType commmandType = provider.getCommandType(itemName);

			if (commmandType != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}
	
	public String findFirstMatchingItemForCommand(String playerName, OppoBlurayPlayerCommand command) throws OppoBlurayPlayerNoMatchingItemException {
		
		String itemName = null;
		
		for (OppoBlurayPlayerBindingProvider provider : this.providers) {
			itemName = provider.findFirstMatchingBindingItemName(playerName, command);
			if (itemName != null){
				return itemName;
			}
		}
		throw new OppoBlurayPlayerNoMatchingItemException("Could not find a configured Item of type " + command.name() + " You may want to add an entry to your *.items file for it.");
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true : false);
		
		if (config != null) {
			if (deviceConfigCache == null) {
				deviceConfigCache = new HashMap<String, DeviceConfig>();
			}
			
			String granularityString = (String) config.get("granularity");
			if (StringUtils.isNotBlank(granularityString)) {
				granularity = Integer.parseInt(granularityString);
			}

			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.warn("given config key '"
							+ key
							+ "' does not follow the expected pattern '<id>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					logger.debug("Added new device {}", deviceId);
					deviceConfig = new DeviceConfig(deviceId, this);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("serialPort".equals(configKey)) {
					deviceConfig.serialPort = value;
				} else {
					throw new ConfigurationException(configKey,
						"the given configKey '" + configKey + "' is unknown");
				}
			}

		}
		openConnection();
	}
	
	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		String deviceId;
		String serialPort = null;
		OppoBlurayPlayerBinding oppoBlurayPlayerBinding = null;
		

		OppoBlurayPlayerDevice device = null;

		public DeviceConfig(String deviceId, OppoBlurayPlayerBinding binding) {
			this.deviceId = deviceId;
			this.oppoBlurayPlayerBinding = binding;
		}

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", serialPort=" + serialPort + "]";
		}

		OppoBlurayPlayerDevice getConnection() {
			if (device == null) {
				if (serialPort != null) {
					device = new OppoBlurayPlayerDevice(serialPort, deviceId, oppoBlurayPlayerBinding);
					try {
						logger.debug("Attempting to set player in verbose mode 2");
						device.sendCommand(OppoBlurayPlayerCommand.VERBOSE_MODE_2);
					} catch (OppoBlurayPlayerException e) {
						logger.warn("Player may not be in verbose mode 2", e);
					}
				}
			}
			return device;
		}

	}	
	
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the 
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveCommand() is called!");
	}

	public void postUpdate(String deviceName, State state) {
		eventPublisher.postUpdate(deviceName, state);
	}
		
//	/**
//	 * @{inheritDoc}
//	 */
//	@Override
//	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
//		if (config != null) {
//			
//			// to override the default refresh interval one has to add a 
//			// parameter to openhab.cfg like <bindingName>:refresh=<intervalInMs>
//			String refreshIntervalString = (String) config.get("refresh");
//			if (StringUtils.isNotBlank(refreshIntervalString)) {
//				refreshInterval = Long.parseLong(refreshIntervalString);
//			}
//			
//			// read further config parameters here ...
//
//			setProperlyConfigured(true);
//		}
//	}
	

}
