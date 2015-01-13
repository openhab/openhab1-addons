/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lgtv.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lgtv.LgtvBindingProvider;
import org.openhab.binding.lgtv.lginteraction.LgTvCommand;
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
 * Binding listening to openHAB event bus and send commands to LGTV devices when
 * certain commands are received.
 * 
 * @author Martin Fluch
 * @since 1.6.0
 */
public class LgtvBinding extends AbstractBinding<LgtvBindingProvider> implements ManagedService, BindingChangeListener,
		LgtvEventListener {

	private static final Logger logger = LoggerFactory.getLogger(LgtvBinding.class);

	protected static final String ADVANCED_COMMAND_KEY = "#";
	protected static final String WILDCARD_COMMAND_KEY = "*";

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(host|port|serverport|pairkey|xmldatafiles|checkalive)$");

	/** LGTV default tcp port */
	private final static int DEFAULT_PORT = 8080;

	/** Map table to store all available receivers configured by the user */
	protected Map<String, DeviceConfig> deviceConfigCache = null;

	private ScheduledExecutorService executor = null;

	public LgtvBinding() {
	}

	@Override
	public void activate() {
		logger.debug("Activate");

		Runnable checkalivetask = new Runnable() {
			public void run() {
				// System.out.println("Hello world");
				checkalive();
			}
		};

		executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(checkalivetask, 0, 10, TimeUnit.SECONDS);

	}

	@Override
	public void deactivate() {
		logger.debug("Deactivate");
		closeAllConnections();
		executor.shutdown();
		executor = null;
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
			LgtvBindingProvider provider = findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug("Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(), command.getClass().toString() });

			String tmp = provider.getDeviceCommand(itemName, command.toString());

			if (tmp == null) {
				tmp = provider.getDeviceCommand(itemName, WILDCARD_COMMAND_KEY);
			}

			String[] commandParts = tmp.split(":");
			String deviceId = commandParts[0];
			String deviceCmd = commandParts[1];

			LgtvConnection remoteController = null;
			DeviceConfig device = deviceConfigCache.get(deviceId);
			if (device == null) {
				logger.error("cannot find device " + deviceId);
			} else {
				remoteController = device.getConnection();
			}

			if (device != null && remoteController != null) {

				if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {

					// advanced command

					deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");

					if (deviceCmd.contains("%")) {

						// lgtv command is a template where value should be
						// updated
						deviceCmd = convertOpenHabCommandToDeviceCommand(command, deviceCmd);
					}

				} else {

					// normal command

					LgTvCommand cmd = LgTvCommand.valueOf(deviceCmd);
					deviceCmd = cmd.getCommand();

					if (deviceCmd.contains("%")) {

						// eISCP command is a template where value should be
						// updated
						deviceCmd = convertOpenHabCommandToDeviceCommand(command, deviceCmd);
					}
				}

				if (deviceCmd != null) {

					remoteController.send(deviceCmd, command.toString());

				} else {
					logger.warn("Cannot convert value '{}' to LgTV format", command);
				}

			} else {
				logger.warn("Cannot find connection details for device id '{}'", deviceId);
			}
		}
	}

	/**
	 * Convert OpenHAB commmand to LGTV command.
	 * 
	 * @param command
	 * @param cmdTemplate
	 * 
	 * @return
	 */
	private String convertOpenHabCommandToDeviceCommand(Command command, String cmdTemplate) {
		String deviceCmd = null;

		if (command instanceof OnOffType) {
			deviceCmd = String.format(cmdTemplate, command == OnOffType.ON ? 1 : 0);

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
	 * Find the first matching {@link LgtvBindingProvider} according to
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private LgtvBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		LgtvBindingProvider firstMatchingProvider = null;

		for (LgtvBindingProvider provider : this.providers) {
			String tmp = provider.getDeviceCommand(itemName, command.toString());
			if (tmp != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		if (firstMatchingProvider == null) {
			for (LgtvBindingProvider provider : this.providers) {
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

	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		logger.debug("Configuration updated, config {}", config != null ? true : false);

		if (config != null) {
			Enumeration<String> keys = config.keys();

			if (deviceConfigCache == null) {
				deviceConfigCache = new HashMap<String, DeviceConfig>();
			}

			while (keys.hasMoreElements()) {
				String key = keys.nextElement();

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
				} else if ("serverport".equals(configKey)) {
					deviceConfig.localport = Integer.valueOf(value);
				} else if ("pairkey".equals(configKey)) {
					deviceConfig.pairkey = value;
				} else if ("xmldatafiles".equals(configKey)) {
					deviceConfig.xmldatafiles = value;
				} else if ("checkalive".equals(configKey)) {
					deviceConfig.checkalive = Integer.valueOf(value);
				} else
					throw new ConfigurationException(configKey, "the given configKey '" + configKey + "' is unknown");

			}
		}

		// open connection to all receivers
		for (String device : deviceConfigCache.keySet()) {
			LgtvConnection connection = deviceConfigCache.get(device).getConnection();
			if (connection != null) {
				connection.addEventListener(this); // MF25012014
				connection.openConnection();

			}
		}

		// initializeallitems();

	}

	private void initializeallitems(String currentdeviceid) {
		for (LgtvBindingProvider provider : this.providers) {

			for (String itemName : provider.getItemNames()) {

				String initCmd = provider.getItemInitCommand(itemName);

				if (initCmd != null) {

					String[] commandParts = initCmd.split(":");
					String deviceId = commandParts[0];

					if (currentdeviceid == "" || deviceId.equals(currentdeviceid)) {
						initializeItem(itemName);
					}

				}

			}

		}
	}

	private void checkalive() {

		if (deviceConfigCache != null && deviceConfigCache.size() > 0) {
			for (String device : deviceConfigCache.keySet()) {
				// logger.debug("checkalive: device="+device);
				LgtvConnection connection = deviceConfigCache.get(device).getConnection();
				if (connection != null) {
					connection.checkalive();
				}
			}

		}

	}

	private void closeAllConnections() {
		if (deviceConfigCache != null) {
			for (String device : deviceConfigCache.keySet()) {
				LgtvConnection connection = deviceConfigCache.get(device).getConnection();
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

	// mfcheck @Override
	public void statusUpdateReceived(EventObject event, String ip, String data) {
		// find correct device from device cache
		DeviceConfig deviceConfig = findDevice(ip);

		if (deviceConfig != null) {
			logger.debug("Received status update '{}' from ip {} for deviceId {}", data, ip, deviceConfig.deviceId);

			if (data.startsWith("CONNECTION_STATUS=1")) {
				initializeallitems(deviceConfig.deviceId);
			}

			String[] dataparts = data.split("=");
			String cmdname = dataparts[0];
			String cmdval = dataparts[1];

			for (LgtvBindingProvider provider : providers) {
				for (String itemName : provider.getItemNames()) {
					// Update all items which refer to command
					HashMap<String, String> values = provider.getDeviceCommands(itemName);

					for (String cmd : values.keySet()) {
						String[] commandParts = values.get(cmd).split(":");
						String deviceCmd = commandParts[1];
						String deviceId = commandParts[0];
						// logger.debug("check: "+values.get(cmd));
						boolean match = false;

						if (deviceCmd.equals(cmdname) && deviceId.equals(deviceConfig.deviceId)) {
							match = true;
						}

						if (match) {
							logger.debug("statusupdaterec match found itemname=" + itemName + " cmd=" + cmdname
									+ " deviceId=" + deviceConfig.deviceId + " value=" + cmdval);
							Class<? extends Item> itemType = provider.getItemType(itemName);
							State v = convertDeviceValueToOpenHabState(itemType, cmdval);
							eventPublisher.postUpdate(itemName, v);
							// break;
							match = false;
						} // match
					} // for (String cmd : values.keySet()) {

				} // itemnames
			} // bindingproviders
		} // ip of device found
	} // function

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

			if (itemType == SwitchItem.class) {
				index = Integer.parseInt(data);
				state = index == 0 ? OnOffType.OFF : OnOffType.ON;

			} else if (itemType == NumberItem.class) {
				index = Integer.parseInt(data);
				state = new DecimalType(index);

			} else if (itemType == DimmerItem.class) {
				index = Integer.parseInt(data);
				state = new PercentType(index);

			} else if (itemType == RollershutterItem.class) {
				index = Integer.parseInt(data);
				state = new PercentType(index);

			} else if (itemType == StringItem.class) {
				// s = data.substring(3, data.length());
				state = new StringType(data);
			}
		} catch (Exception e) {
			logger.debug("Cannot convert value '{}' to data type {}", data, itemType);
		}
		// logger.debug("Convert value=" + data + " to openhab type="
		// + itemType.toString() + " val=" + state.toString());
		return state;
	}

	/**
	 * Initialize item value. Method send query to receiver if init query is
	 * configured to binding item configuration
	 * 
	 * @param itemType
	 * 
	 */
	private void initializeItem(String itemName) {
		for (LgtvBindingProvider provider : providers) {
			String initCmd = provider.getItemInitCommand(itemName);

			if (initCmd != null) {

				String[] commandParts = initCmd.split(":");
				String deviceId = commandParts[0];
				String deviceCmd = commandParts[1];

				LgtvConnection remoteController = null;
				DeviceConfig device = deviceConfigCache.get(deviceId);
				if (device == null) {
					logger.error("cannot find device " + deviceId);
				} else {
					remoteController = device.getConnection();
				}

				if (remoteController == null) {
					logger.debug("Initialize item {} canceled - remotecontroller not initialized", itemName);
					return;
				}

				if (remoteController.ispaired() == false) {
					logger.debug("Initialize item {} canceled - not paired", itemName);
					return;
				}

				logger.debug("Initialize item {}", itemName);

				if (device != null && remoteController != null) {
					if (deviceCmd.startsWith(ADVANCED_COMMAND_KEY)) {
						deviceCmd = deviceCmd.replace(ADVANCED_COMMAND_KEY, "");
					} else {
						LgTvCommand cmd = LgTvCommand.valueOf(deviceCmd);
						deviceCmd = cmd.getCommand();
					}

					String result = "";
					if (remoteController != null) {
						result = remoteController.send(deviceCmd, "");

					}

					logger.debug("send result=" + result);
					if (!result.startsWith("#")) {
						Class<? extends Item> itemType = provider.getItemType(itemName);
						State v = convertDeviceValueToOpenHabState(itemType, result);
						eventPublisher.postUpdate(itemName, v);
					}

				} else {
					logger.warn("Cannot find connection details for device id '{}'", deviceId);
				}
			}
		}
	}

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		public String pairkey = "";
		public String xmldatafiles = "";
		public int checkalive = 0;
		String host;
		int port = DEFAULT_PORT;
		int localport = 0;

		LgtvConnection connection = null;
		String deviceId;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		public String getHost() {
			return host;
		}

		public int getPort() {
			return port;
		}

		public int getLocalPort() {
			return localport;
		}

		public String getxmldatafiles() {
			return xmldatafiles;
		}

		public String getPairKey() {
			return pairkey;
		}

		public int checkalive() {
			return checkalive;
		}

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + ", serverport=" + localport + "]";
		}

		LgtvConnection getConnection() {
			if (connection == null) {
				connection = new LgtvConnection(host, port, localport, pairkey, xmldatafiles, checkalive);
			}
			return connection;
		}

	}

}
