/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.urtsi.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.urtsi.UrtsiBindingProvider;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main implementation of the Somfy URTSI II Binding. This binding is
 * responsible for delegating the received commands and updates to the
 * {@link UrtsiDevice}.
 * 
 * @author Oliver Libutzki
 * @since 1.3.0
 * 
 */
public class UrtsiBinding extends AbstractBinding<UrtsiBindingProvider>
		implements ManagedService {

	private final static Logger logger = LoggerFactory
			.getLogger(UrtsiBinding.class);

	private final static String COMMAND_UP = "U";
	private final static String COMMAND_DOWN = "D";
	private final static String COMMAND_STOP = "S";

	private final static Pattern EXTRACT_URTSI_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(port)$");

	/**
	 * Maps the device id to a URTSI device. This is needed if you use multiple
	 * ports for multiple urtsi devices.
	 */
	private final Map<String, UrtsiDevice> idToDeviceMap = new HashMap<String, UrtsiDevice>();

	/**
	 * The method determines the appropriate
	 * {@link org.openhab.core.binding.BindingProvider} and uses it to get the
	 * corresponding URTSI device and channel. Bases on the given type a command
	 * is send to the device.
	 * 
	 * @param itemName
	 *            name of the item
	 * @param type
	 *            Type of the command or status update
	 * @return Returns true, if the command has been executed successfully.
	 *         Returns false otherwise.
	 * @throws BindingConfigParseException 
	 */
	private boolean sendToUrtsi(String itemName, Type type) {
		UrtsiBindingProvider provider = null;
		if (!providers.isEmpty()) {
			provider = providers.iterator().next();
		}
		if (provider == null) {
			if (logger.isErrorEnabled()) {
				logger.error("doesn't find matching binding provider [itemName={}, type={}]", itemName, type);
			}
			return false;
		}
		String urtsiDeviceId = provider.getDeviceId(itemName);
		UrtsiDevice urtsiDevice = idToDeviceMap.get(urtsiDeviceId);
		
		if (urtsiDevice == null) {
			if (logger.isErrorEnabled()) {
				logger.error("No serial port has been configured for urtsi device id '" + urtsiDeviceId +"'");
			}
			return false;
		}
		
		int channel = provider.getChannel(itemName);
		
		if (urtsiDevice != null) {
			if (logger.isDebugEnabled()) {
				logger.debug("Send to URTSI for item: " + itemName + "; Type: " + type);
			}
			String actionKey = null;
			if (type instanceof UpDownType) {
				switch ((UpDownType)type) {
					case UP : 
						actionKey = COMMAND_UP; 
						break;
					case 
					DOWN : actionKey = COMMAND_DOWN; 
					break;
				}
			} else if (type instanceof StopMoveType) {
				switch ((StopMoveType)type) {
					case STOP : 
						actionKey = COMMAND_STOP; 
						break;
					default:
						break;
				}
			}
			

			if (logger.isDebugEnabled()) {
				logger.debug("Action key: " + actionKey);
			}
			if (actionKey != null) {
				String channelString = String.format("%02d", channel);
				String command = "01" + channelString + actionKey;
				boolean executedSuccessfully = urtsiDevice.writeString(command);
				if (!executedSuccessfully) {
					if (logger.isErrorEnabled()) {
						logger.error("Command has not been processed [itemName={}, command={}]", itemName, command);
					}
				}
				return executedSuccessfully;
			}
		}
		return false;
	}

	/**
	 * The method delegates the received command to the URTSI device and updates
	 * the item's state, if the command has been executed successfully.
	 */
	protected void internalReceiveCommand(String itemName, Command command) {
		if (logger.isDebugEnabled()) {
			logger.debug("Received command for " + itemName + "! Command: " + command);
		}
		boolean executedSuccessfully = sendToUrtsi(itemName, command);
		if (executedSuccessfully && command instanceof State) {
			eventPublisher.postUpdate(itemName, (State)command);
		}
	}
	
	/**
	 * The method delegates the received state-update to the URTSI device.
	 */
	protected void internalReceiveUpdate(String itemName, State newState) {
		if (logger.isDebugEnabled()) {
			logger.debug("Received update for " + itemName + "! New state: " + newState);
		}
		sendToUrtsi(itemName, newState);
	}
	
	/**
	 * Parses the global configuration file.
	 * Expected values:
	 * urtsi.<deviceid>.port=<serialport>
	 */
	public void updated(Dictionary<String, ? > config)	throws ConfigurationException {
		
		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = keys.nextElement();
				logger.debug("Processing key '" + key + "'");
				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if (key != "service.pid") {
					
					Matcher matcher = EXTRACT_URTSI_CONFIG_PATTERN.matcher(key);
					if (!matcher.matches()) {
						logger.debug("given config key '"
								+ key
								+ "' does not follow the expected pattern '<id>.port'");
					} else {
						matcher.reset();
						matcher.find();
		
						String deviceId = matcher.group(1);
						UrtsiDevice urtsiDevice = idToDeviceMap.get(deviceId);
						if (urtsiDevice == null) {
							String configKey = matcher.group(2);
							String value = (String)config.get(key);
							String port = null;
							if ("port".equals(configKey)) {
								port = value;
							} else {
								throw new ConfigurationException(configKey, "the given config key '" + configKey + "' is unknown");
							}
							urtsiDevice = new UrtsiDevice(port);
							try {
								urtsiDevice.initialize();
							} catch (InitializationException e) {
								throw new ConfigurationException(configKey, 
										"Could not open serial port " + port + ": "
												+ e.getMessage());
							} catch (Throwable e) {
								throw new ConfigurationException(configKey,
										"Could not open serial port " + port + ": "
												+ e.getMessage());
							}
							idToDeviceMap.put(deviceId, urtsiDevice);
						}
					}	
				}
			}
		}
	}

}
