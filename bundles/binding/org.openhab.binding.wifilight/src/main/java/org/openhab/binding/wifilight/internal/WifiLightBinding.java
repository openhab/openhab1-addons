/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wifilight.internal;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.wifilight.WifiLightBindingProvider;
import org.openhab.binding.wifilight.internal.WifiLightBindingConfig.BindingType;
import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WifiLightBinding extends AbstractBinding<WifiLightBindingProvider>
		implements ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(WifiLightBinding.class);

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_CONFIG_PATTERN = Pattern
			.compile("^(.*?)\\.(host|port)$");

	private final static int DEFAULT_PORT = 50000;

	protected Map<String, DeviceConfig> deviceConfigs = new HashMap<String, DeviceConfig>();

	protected Map<String, String> bridgeIpConfig = new HashMap<String, String>();

	protected Map<String, Integer> bridgePortConfig = new HashMap<String, Integer>();

	protected Map<String, PercentType> dimmerState = new HashMap<String, PercentType>();

	public WifiLightBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		super.internalReceiveCommand(itemName, command);

		WifiLightBindingConfig deviceConfig = getConfigForItemName(itemName);

		if (deviceConfig == null) {
			return;
		}

		try {
			String controllerId = deviceConfig.getDeviceId();

			if (deviceConfig.getCommandType().equals(BindingType.rgb)) {
				if (command instanceof HSBType) {
					sendColor(command, controllerId);
				}

			} else if (deviceConfig.getCommandType().equals(BindingType.red)
					|| deviceConfig.getCommandType().equals(BindingType.blue)
					|| deviceConfig.getCommandType().equals(BindingType.green)
					|| deviceConfig.getCommandType().equals(BindingType.white)) {
				if (OnOffType.ON.equals(command)) {
					sendOn(controllerId);
				} else if (OnOffType.OFF.equals(command)) {
					sendOff(controllerId);
				}
				if (IncreaseDecreaseType.INCREASE.equals(command)) {
					PercentType newValue = sendIncrease(
							deviceConfig.getCommandType(), controllerId);
					eventPublisher.postUpdate(itemName, newValue);
				} else if (IncreaseDecreaseType.DECREASE.equals(command)) {
					PercentType newValue = sendDecrease(
							deviceConfig.getCommandType(), controllerId);
					eventPublisher.postUpdate(itemName, newValue);
				} else if (command instanceof PercentType) {
					sendDecreaseOrIncrease(deviceConfig.getCommandType(), ((PercentType) command).intValue(), controllerId);
				}
			}
		} catch (Exception e) {
			logger.error("wifilight: Failed to send {} command ",
					deviceConfig.getCommandType(), e);
		}
	}

	private PercentType getCurrentState(String controllerId, BindingType type) {
		PercentType percent = dimmerState.get(controllerId + type);
		if (percent == null) {
			percent = PercentType.ZERO;
		}
		return percent;
	}

	private void setCurrentState(String controllerId, PercentType command,
			BindingType type) {
		dimmerState.put(controllerId + type, command);
	}

	private PercentType sendIncrease(BindingType type, String controllerId) {
		logger.debug("milight: sendIncrease");

		int currentPercent = getCurrentState(controllerId, type).intValue();
		if (currentPercent == 0) {
			try {
				sendOn(controllerId);
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}
		int newPercent = currentPercent + 10;
		if (newPercent > 100) {
			newPercent = 100;
		}
		PercentType newValue = sendDecreaseOrIncrease(type, newPercent,
				controllerId);
		return newValue;
	}

	private PercentType sendDecrease(BindingType type, String controllerId) {
		logger.debug("milight: sendDecrease");
		int newPercent = getCurrentState(controllerId, type).intValue() - 10;
		if (newPercent < 0) {
			newPercent = 0;
		}
		PercentType newValue = sendDecreaseOrIncrease(type, newPercent,
				controllerId);
		return newValue;
	}

	private PercentType sendDecreaseOrIncrease(BindingType type,
			int newPercent, String controllerId) {

		Double target = newPercent * 2.55;
		String red = "";
		String green = "";
		String blue = "";
		String white = "";

		switch (type) {

		case blue:
			red = getHexFromCurrentState(controllerId, BindingType.red);
			green = getHexFromCurrentState(controllerId, BindingType.green);
			blue = Integer.toHexString(target.intValue());
			white = getHexFromCurrentState(controllerId, BindingType.white);
			break;
		case green:
			red = getHexFromCurrentState(controllerId, BindingType.red);
			green = Integer.toHexString(target.intValue());
			blue = getHexFromCurrentState(controllerId, BindingType.blue);
			white = getHexFromCurrentState(controllerId, BindingType.white);
			break;
		case red:
			red = Integer.toHexString(target.intValue());
			green = getHexFromCurrentState(controllerId, BindingType.green);
			blue = getHexFromCurrentState(controllerId, BindingType.blue);
			white = getHexFromCurrentState(controllerId, BindingType.white);
			break;
		case white:
			red = getHexFromCurrentState(controllerId, BindingType.red);
			green = getHexFromCurrentState(controllerId, BindingType.green);
			blue = getHexFromCurrentState(controllerId, BindingType.blue);
			white = Integer.toHexString(target.intValue());

			int whiteOld = (int) (getCurrentState(controllerId, type)
					.doubleValue() * 2.55);
			logger.debug("old: " + whiteOld);
			logger.debug("new: " + target.intValue());
			if (whiteOld < target.intValue()) {
				// dimm up
				for (int i = whiteOld; i < target.intValue(); i = i + 2) {
					String currentWhite = Integer.toHexString(i);
					String messageBytes = "31:" + red + ":" + green + ":"
							+ blue + ":" + currentWhite + ":00:00";
					sendMessage(messageBytes, controllerId);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// ignore
					}
				}
			} else {
				// dimm down
				for (int i = whiteOld; i > target.intValue(); i = i - 2) {
					String currentWhite = Integer.toHexString(i);
					String messageBytes = "31:" + red + ":" + green + ":"
							+ blue + ":" + currentWhite + ":00:00";
					sendMessage(messageBytes, controllerId);
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// ignore
					}
				}
			}

		default:
			break;
		}

		String messageBytes = "31:" + red + ":" + green + ":" + blue + ":"
				+ white + ":00:00";
		sendMessage(messageBytes, controllerId);
		PercentType newValue = new PercentType(newPercent);
		setCurrentState(controllerId, newValue, type);
		return newValue;
	}

	private String getHexFromCurrentState(String controllerId, BindingType type) {
		return Integer.toHexString((int) (getCurrentState(controllerId, type)
				.doubleValue() * 2.55));
	}

	private void sendOn(String controllerId) {
		logger.debug("milight: sendOn");
		String messageBytes = "71:23";
		String checkSum = checkSum("71:23");
		logger.debug(checkSum);
		sendMessage(messageBytes, controllerId);
	}

	private void sendOff(String controllerId) {
		logger.debug("milight: sendOff");
		String messageBytes = "71:24";
		sendMessage(messageBytes, controllerId);
		setCurrentState(controllerId, PercentType.ZERO, BindingType.blue);
		setCurrentState(controllerId, PercentType.ZERO, BindingType.red);
		setCurrentState(controllerId, PercentType.ZERO, BindingType.green);
		setCurrentState(controllerId, PercentType.ZERO, BindingType.white);
		// setCurrentState(controllerId, PercentType.ZERO,
		// BindingType.brightness);
	}

	private void sendColor(Command command, String controllerId) {
		HSBType hsbCommand = (HSBType) command;

		String messageBytes = "31:"
				+ Integer.toHexString(hsbCommand.toColor().getRed()) + ":"
				+ Integer.toHexString(hsbCommand.toColor().getGreen()) + ":"
				+ Integer.toHexString(hsbCommand.toColor().getBlue())
				+ ":00:00:00";
		sendMessage(messageBytes, controllerId);
		// save the rgb selection
		setCurrentState(controllerId, new PercentType((int) (hsbCommand
				.toColor().getRed() / 2.55)), BindingType.red);
		setCurrentState(controllerId, new PercentType((int) (hsbCommand
				.toColor().getGreen() / 2.55)), BindingType.green);
		setCurrentState(controllerId, new PercentType((int) (hsbCommand
				.toColor().getBlue() / 2.55)), BindingType.blue);

	}

	private String checkSum(String message) {
		String[] parts = message.split(":");
		Integer check = 0;
		for (int i = 0; i < parts.length; i++) {
			int value = Integer.parseInt(parts[i], 16);
			check = check + value;
		}
		check = check % 256;
		return Integer.toHexString(check);
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		// the code being executed when a state was sent on the openHAB
		// event bus goes here. This method is only called if one of the
		// BindingProviders provide a binding for the given 'itemName'.
		logger.debug("internalReceiveUpdate() is called!");
	}

	protected void sendMessage(String messageBytes, String controllerId) {
		messageBytes = messageBytes + ":" + checkSum(messageBytes);
		String bridgeIp = bridgeIpConfig.get(controllerId);
		Integer bridgePort = bridgePortConfig.get(controllerId);

		SocketAddress address = new InetSocketAddress(bridgeIp, bridgePort);
		Socket clientSocket = new Socket();
		try {
			byte[] buffer = getMessageBytes(messageBytes);
			clientSocket.connect(address, 1000);
			DataOutputStream outToServer = new DataOutputStream(
					clientSocket.getOutputStream());
			outToServer.write(buffer);
			outToServer.close();
			clientSocket.close();
		} catch (IOException e1) {
			logger.error("Send prob", e1);
		}
	}

	private byte[] getMessageBytes(String messageBytes) {
		logger.debug("milight: messageBytes to transform: '{}'", messageBytes);
		if (messageBytes == null) {
			logger.error("messageBytes must not be null");
		}

		String[] hex = messageBytes.split("(\\:|\\-)");
		byte[] buffer = new byte[hex.length];
		int hexIndex = 0;
		for (hexIndex = 0; hexIndex < hex.length; hexIndex++) {
			buffer[hexIndex] = (byte) Integer.parseInt(hex[hexIndex], 16);
		}
		return buffer;
	}

	/**
	 * Lookup of the configuration of the named item.
	 * 
	 * @param itemName
	 *            The name of the item.
	 * @return The configuration, null otherwise.
	 */
	private WifiLightBindingConfig getConfigForItemName(String itemName) {
		for (WifiLightBindingProvider provider : this.providers) {
			if (provider.getItemConfig(itemName) != null) {
				return provider.getItemConfig(itemName);
			}
		}
		return null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		if (config != null) {
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
					logger.debug("given config key '"
							+ key
							+ "' does not follow the expected pattern '<id>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigs.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigs.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
					bridgeIpConfig.put(deviceId, value);
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.valueOf(value);
					bridgePortConfig.put(deviceId, Integer.valueOf(value));
				} else {
					throw new ConfigurationException(configKey,
							"the given configKey '" + configKey
									+ "' is unknown");
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

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port="
					+ port + "]";
		}
	}
}
