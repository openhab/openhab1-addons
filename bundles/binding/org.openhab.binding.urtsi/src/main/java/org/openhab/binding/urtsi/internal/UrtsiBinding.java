/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

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

	private static final String GNU_IO_RXTX_SERIAL_PORTS = "gnu.io.rxtx.SerialPorts";

	private final static Logger logger = LoggerFactory
			.getLogger(UrtsiBinding.class);

	private final static String COMMAND_UP = "U";
	private final static String COMMAND_DOWN = "D";
	private final static String COMMAND_STOP = "S";
	
	private final static String CONFIG_PORT = "port";
	private final static String CONFIG_INTERVAL = "interval";

	private final static Pattern EXTRACT_URTSI_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(" + CONFIG_PORT + "|" + CONFIG_INTERVAL + ")$");

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
		int address = provider.getAddress(itemName);
		
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
				String addressString = String.format("%02d", address);
				String command = addressString + channelString + actionKey;
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
	 * With openHAB 1.7.0 the state-update is not passed to the URTSI device anymore as this let to multiple actions in the past.
	 * 
	 */
	protected void internalReceiveUpdate(String itemName, State newState) {
		if (logger.isDebugEnabled()) {
			logger.debug("Received update for " + itemName + "! New state: " + newState);
		}
	}
	
	/**
	 * Parses the global configuration file.
	 * Expected values:
	 * urtsi.<deviceid>.port=<serialport>
	 * urtsi.<deviceid>.interval=<interval> (optional, default: 100)
	 */
	public void updated(Dictionary<String, ? > config)	throws ConfigurationException {
		
		if (config != null) {
			Map<String, String> errorMessages = new LinkedHashMap<String, String>();
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
							urtsiDevice = new UrtsiDevice();
							idToDeviceMap.put(deviceId, urtsiDevice);
						}
						String configKey = matcher.group(2);
						String value = (String)config.get(key);
						if (CONFIG_PORT.equals(configKey)) {
							urtsiDevice.setPort(value);
						} else if (CONFIG_INTERVAL.equals(configKey)) {
							urtsiDevice.setInterval(Integer.valueOf(value));
						} else {
							errorMessages.put(configKey, "the given config key '" + configKey + "' is unknown");
						}
					}	
				}
			}
			
			for (Iterator<Entry<String, UrtsiDevice>> deviceIterator = idToDeviceMap.entrySet().iterator(); deviceIterator.hasNext();) {
				Entry<String, UrtsiDevice> deviceEntry = deviceIterator.next();
				UrtsiDevice urtsiDevice = deviceEntry.getValue();
				try {
					String serialPortsProperty = System.getProperty(GNU_IO_RXTX_SERIAL_PORTS);
					Set<String> serialPorts = null;
					if (serialPortsProperty != null) {
						serialPorts = Sets.newHashSet(Splitter.on(":").split(serialPortsProperty));
					} else {
						serialPorts = new HashSet<String>();
					}
					if (serialPorts.add(urtsiDevice.getPort())) {
						logger.debug("Added {} to the {} system property.", urtsiDevice.getPort(), GNU_IO_RXTX_SERIAL_PORTS );
					}
					System.setProperty(GNU_IO_RXTX_SERIAL_PORTS, Joiner.on(":").join(serialPorts));
					urtsiDevice.initialize();
				}  catch (Throwable e) {
					deviceIterator.remove();
					errorMessages.put(deviceEntry.getKey(), e.getMessage());
				}
			}
			
			if (!errorMessages.isEmpty()) {
				StringBuilder errorMessageStringBuilder = new StringBuilder("The following errors occurred:\r\n");
				for (Iterator<Entry<String, String>> errorMessageIterator = errorMessages.entrySet().iterator(); errorMessageIterator
						.hasNext();) {
					Entry<String, String> errorMessageEntry = errorMessageIterator.next();
					errorMessageStringBuilder.append(errorMessageEntry.getKey()).append(": ").append(errorMessageEntry.getValue());
					if (errorMessageIterator.hasNext()) {
						errorMessageStringBuilder.append("\r\n");
					}
				}
				logger.error(errorMessageStringBuilder.toString());
				Entry<String, String> firstErrorMessageEntry = errorMessages.entrySet().iterator().next();
				throw new ConfigurationException(firstErrorMessageEntry.getKey(), firstErrorMessageEntry.getValue());
			}
			
		}
	}

}
