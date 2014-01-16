/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.epsonprojector.EpsonProjectorBindingProvider;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.AspectRatio;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Background;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Color;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.ColorMode;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Gamma;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Luminance;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.PowerStatus;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Source;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorDevice.Switch;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding which communicates with (one or many) Epson projectors.
 * 
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorBinding extends
		AbstractActiveBinding<EpsonProjectorBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(EpsonProjectorBinding.class);

	private final static int DEFAULT_PORT = 60128;

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

	
	public void activate() {
		logger.debug("Activate");
	}

	public void deactivate() {
		logger.debug("Deactivate");
		closeConnection();
	}

	private void closeConnection() {
		if (deviceConfigCache != null) {
			// close all connections
			for (Entry<String, DeviceConfig> entry : deviceConfigCache.entrySet()) {
				DeviceConfig deviceCfg = entry.getValue();
				if (deviceCfg != null) {
					EpsonProjectorDevice device = deviceCfg.getConnection();

					if (device != null) {
						try {
							logger.debug("Closing connection to device '{}' ", deviceCfg.deviceId);
							device.disconnect();
						} catch (EpsonProjectorException e) {
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
	
	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return granularity;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "Epson projector Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void execute() {
		for (EpsonProjectorBindingProvider provider : providers) {
			for (String itemName : provider.getInBindingItemNames()) {

				int refreshInterval = provider.getRefreshInterval(itemName);

				Long lastUpdateTimeStamp = lastUpdateMap.get(itemName);
				if (lastUpdateTimeStamp == null) {
					lastUpdateTimeStamp = 0L;
				}

				long age = System.currentTimeMillis() - lastUpdateTimeStamp;
				boolean needsUpdate = age >= refreshInterval;

				if (needsUpdate) {
					boolean refreshOnlyWhenPowerOn = provider.refreshOnlyWhenPowerOn(itemName);
					String deviceId = provider.getDeviceId(itemName);
					
					if (refreshOnlyWhenPowerOn) {
						OnOffType state = (OnOffType) queryDataFromDevice(
							deviceId, EpsonProjectorCommandType.POWER, SwitchItem.class);
						
						 if (state != OnOffType.ON) {
							logger.debug("projector power is OFF, skip refresh for item '{}'", itemName);
							lastUpdateMap.put(itemName, System.currentTimeMillis());
							return;
						 }
					}
					
					logger.debug("item '{}' is about to be refreshed now", itemName);

					EpsonProjectorCommandType commmandType = provider.getCommandType(itemName);
					Class<? extends Item> itemType = provider.getItemType(itemName);

					State state = queryDataFromDevice(deviceId, commmandType, itemType);

					if (state != null) {
						eventPublisher.postUpdate(itemName, state);
					} else {
						logger.error("No response received from command '{}'", commmandType);
					}

					lastUpdateMap.put(itemName, System.currentTimeMillis());
				}
			}
		}
	}

	private State queryDataFromDevice(String deviceId,
		EpsonProjectorCommandType commmandType, Class<? extends Item> itemType) {

		DeviceConfig device = deviceConfigCache.get(deviceId);

		if (device == null) {
			logger.error("Could not find device '{}'", deviceId);
			return null;
		}

		EpsonProjectorDevice remoteController = device.getConnection();

		if (remoteController == null) {
			logger.error("Could not find device '{}'", deviceId);
			return null;
		}

		try {
			if (remoteController.isConnected() == false)
				remoteController.connect();

			switch (commmandType) {
			case AKEYSTONE:
				int autoKeystone = remoteController.getAutoKeystone();
				return new DecimalType(autoKeystone);
			case ASPECT_RATIO:
				AspectRatio aspectRatio = remoteController.getAspectRatio();
				return new StringType(aspectRatio.toString());
			case BACKGROUND:
				Background background = remoteController.getBackground();
				return new StringType(background.toString());
			case BRIGHTNESS:
				int brightness = remoteController.getBrightness();
				return new DecimalType(brightness);
			case COLOR:
				Color color = remoteController.getColor();
				return new StringType(color.toString());
			case COLOR_MODE:
				ColorMode colorMode = remoteController.getColorMode();
				return new StringType(colorMode.toString());
			case COLOR_TEMP:
				int ctemp = remoteController.getColorTemperature();
				return new DecimalType(ctemp);
			case CONTRAST:
				int contrast = remoteController.getContrast();
				return new DecimalType(contrast);
			case DENSITY:
				int density = remoteController.getDensity();
				return new DecimalType(density);
			case DIRECT_SOURCE:
				int directSource = remoteController.getDirectSource();
				return new DecimalType(directSource);
			case ERR_CODE:
				int err = remoteController.getError();
				return new DecimalType(err);
			case ERR_MESSAGE:
				String errString = remoteController.getErrorString();
				return new StringType(errString);
			case FLESH_TEMP:
				int fleshColor = remoteController.getFleshColor();
				return new DecimalType(fleshColor);
			case GAIN_BLUE:
				int gainBlue = remoteController.getGainBlue();
				return new DecimalType(gainBlue);
			case GAIN_GREEN:
				int gainGreen = remoteController.getGainGreen();
				return new DecimalType(gainGreen);
			case GAIN_RED:
				int gainRed = remoteController.getGainRed();
				return new DecimalType(gainRed);
			case GAMMA:
				Gamma gamma = remoteController.getGamma();
				return new StringType(gamma.toString());
			case GAMMA_STEP:
				logger.warn("Get '{}' not implemented!",
						commmandType.toString());
				return null;
			case HKEYSTONE:
				int hKeystone = remoteController.getHorizontalKeystone();
				return new DecimalType(hKeystone);
			case HPOSITION:
				int hPosition = remoteController.getHorizontalPosition();
				return new DecimalType(hPosition);
			case HREVERSE:
				Switch hReverse = remoteController.getHorizontalReverse();
				return hReverse == Switch.ON ? OnOffType.ON : OnOffType.OFF;
			case KEY_CODE:
				break;
			case LAMP_TIME:
				int lampTime = remoteController.getLampTime();
				return new DecimalType(lampTime);
			case LUMINANCE:
				Luminance luminance = remoteController.getLuminance();
				return new StringType(luminance.toString());
			case MUTE:
				Switch mute = remoteController.getMute();
				return mute == Switch.ON ? OnOffType.ON : OnOffType.OFF;
			case OFFSET_BLUE:
				int offsetBlue = remoteController.getOffsetBlue();
				return new DecimalType(offsetBlue);
			case OFFSET_GREEN:
				int offsetGreen = remoteController.getOffsetGreen();
				return new DecimalType(offsetGreen);
			case OFFSET_RED:
				int offsetRed = remoteController.getOffsetRed();
				return new DecimalType(offsetRed);
			case POWER:
				PowerStatus powerStatus = remoteController.getPowerStatus();

				if (powerStatus == PowerStatus.ON)
					return OnOffType.ON;
				else
					return OnOffType.OFF;
			case POWER_STATE:
				PowerStatus powerStatus1 = remoteController.getPowerStatus();
				return new StringType(powerStatus1.toString());
			case SHARP:
				logger.warn("Get '{}' not implemented!",
						commmandType.toString());
				return null;
			case SOURCE:
				Source source = remoteController.getSource();
				return new StringType(source.toString());
			case SYNC:
				int sync = remoteController.getSync();
				return new DecimalType(sync);
			case TINT:
				int tint = remoteController.getTint();
				return new DecimalType(tint);
			case TRACKING:
				int tracking = remoteController.getTracking();
				return new DecimalType(tracking);
			case VKEYSTONE:
				int vKeystone = remoteController.getVerticalKeystone();
				return new DecimalType(vKeystone);
			case VPOSITION:
				int vPosition = remoteController.getVerticalPosition();
				return new DecimalType(vPosition);
			case VREVERSE:
				Switch vReverse = remoteController.getVerticalReverse();
				return vReverse == Switch.ON ? OnOffType.ON : OnOffType.OFF;
			default:
				logger.warn("Unknown '{}' command!", commmandType);
				return null;
			}

		} catch (EpsonProjectorException e) {
			logger.warn("Couldn't execute command '{}', {}",
					commmandType.toString(), e);

		} catch (Exception e) {
			logger.warn("Couldn't create state of type '{}'", itemType);
			return null;
		}

		return null;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void internalReceiveCommand(String itemName, Command command) {
		EpsonProjectorBindingProvider provider = findFirstMatchingBindingProvider( itemName, command);

		if (provider == null) {
			logger.warn(
					"doesn't find matching binding provider [itemName={}, command={}]",
					itemName, command);
			return;
		}

		if (provider.isOutBinding(itemName)) {
			EpsonProjectorCommandType commmandType = provider.getCommandType(itemName);
			String deviceId = provider.getDeviceId(itemName);
			if (commmandType != null) {
				sendDataToDevice(deviceId, commmandType, command);
			}
		} else {
			logger.warn("itemName={} is not out binding", itemName);
		}
	}

	private void sendDataToDevice(String deviceId, EpsonProjectorCommandType commmandType, Command command) {
		DeviceConfig device = deviceConfigCache.get(deviceId);

		if (device == null) {
			logger.error("Could not find device '{}'", deviceId);
			return;
		}

		EpsonProjectorDevice remoteController = device.getConnection();

		if (remoteController == null) {
			logger.error("Could not find device '{}'", deviceId);
			return;
		}

		try {

			if (remoteController.isConnected() == false)
				remoteController.connect();

			switch (commmandType) {
			case AKEYSTONE:
				remoteController.setAutoKeystone(((DecimalType) command).intValue());
				break;
			case ASPECT_RATIO:
				remoteController.setAspectRatio(AspectRatio.valueOf(command.toString()));
				break;
			case BACKGROUND:
				remoteController.setBackground(Background.valueOf(command.toString()));
				break;
			case BRIGHTNESS:
				remoteController.setBrightness(((DecimalType) command).intValue());
				break;
			case COLOR:
				remoteController.setColor(Color.valueOf(command.toString()));
				break;
			case COLOR_MODE:
				remoteController.setColorMode(ColorMode.valueOf(command.toString()));
				break;
			case COLOR_TEMP:
				remoteController.setColorTemperature(((DecimalType) command).intValue());
				break;
			case CONTRAST:
				remoteController.setContrast(((DecimalType) command).intValue());
				break;
			case DENSITY:
				remoteController.setDensity(((DecimalType) command).intValue());
				break;
			case DIRECT_SOURCE:
				remoteController.setDirectSource(((DecimalType) command).intValue());
				break;
			case ERR_CODE:
				logger.error("'{}' is read only parameter", commmandType);
				break;
			case ERR_MESSAGE:
				logger.error("'{}' is read only parameter", commmandType);
				break;
			case FLESH_TEMP:
				remoteController.setFleshColor(((DecimalType) command)
						.intValue());
				break;
			case GAIN_BLUE:
				remoteController
						.setGainBlue(((DecimalType) command).intValue());
				break;
			case GAIN_GREEN:
				remoteController.setGainGreen(((DecimalType) command).intValue());
				break;
			case GAIN_RED:
				remoteController.setGainRed(((DecimalType) command).intValue());
				break;
			case GAMMA:
				remoteController.setGamma(Gamma.valueOf(command.toString()));
				break;
			case GAMMA_STEP:
				logger.warn("Set '{}' not implemented!", commmandType.toString());
				break;
			case HKEYSTONE:
				remoteController.setHorizontalKeystone(((DecimalType) command).intValue());
				break;
			case HPOSITION:
				remoteController.setHorizontalPosition(((DecimalType) command).intValue());
				break;
			case HREVERSE:
				remoteController.setHorizontalReverse((command == OnOffType.ON ? Switch.ON : Switch.OFF));
				break;
			case KEY_CODE:
				remoteController.sendKeyCode(((DecimalType) command).intValue());
				break;
			case LAMP_TIME:
				logger.error("'{}' is read only parameter", commmandType);
				break;
			case LUMINANCE:
				remoteController.setLuminance(Luminance.valueOf(command.toString()));
				break;
			case MUTE:
				remoteController.setMute((command == OnOffType.ON ? Switch.ON : Switch.OFF));
				break;
			case OFFSET_BLUE:
				remoteController.setOffsetBlue(((DecimalType) command).intValue());
				break;
			case OFFSET_GREEN:
				remoteController.setOffsetGreen(((DecimalType) command).intValue());
				break;
			case OFFSET_RED:
				remoteController.setOffsetRed(((DecimalType) command).intValue());
				break;
			case POWER:
				remoteController.setPower((command == OnOffType.ON ? Switch.ON : Switch.OFF));
				break;
			case POWER_STATE:
				logger.error("'{}' is read only parameter", commmandType);
				break;
			case SHARP:
				logger.warn("Set '{}' not implemented!", commmandType.toString());
				break;
			case SOURCE:
				remoteController.setSource(Source.valueOf(command.toString()));
				break;
			case SYNC:
				remoteController.setSync(((DecimalType) command).intValue());
				break;
			case TINT:
				remoteController.setTint(((DecimalType) command).intValue());
				break;
			case TRACKING:
				remoteController.setTracking(((DecimalType) command).intValue());
				break;
			case VKEYSTONE:
				remoteController.setVerticalKeystone(((DecimalType) command).intValue());
				break;
			case VPOSITION:
				remoteController.setVerticalPosition(((DecimalType) command).intValue());
				break;
			case VREVERSE:
				remoteController.setVerticalReverse((command == OnOffType.ON ? Switch.ON : Switch.OFF));
				break;
			default:
				logger.warn("Unknown '{}' command!", commmandType);
				break;
			}

		} catch (EpsonProjectorException e) {
			logger.error("Couldn't execute command '{}', {}", commmandType, e);

		}
	}

	/**
	 * Find the first matching {@link ExecBindingProvider} according to
	 * <code>itemName</code> and <code>command</code>. If no direct match is
	 * found, a second match is issued with wilcard-command '*'.
	 * 
	 * @param itemName
	 * @param command
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private EpsonProjectorBindingProvider findFirstMatchingBindingProvider(
			String itemName, Command command) {

		EpsonProjectorBindingProvider firstMatchingProvider = null;

		for (EpsonProjectorBindingProvider provider : this.providers) {
			EpsonProjectorCommandType commmandType = provider.getCommandType(itemName);

			if (commmandType != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	/**
	 * @{inheritDoc
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
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("serialPort".equals(configKey)) {
					deviceConfig.serialPort = value;
				} else if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.valueOf(value);
				} else {
					throw new ConfigurationException(configKey,
						"the given configKey '" + configKey + "' is unknown");
				}
			}

			setProperlyConfigured(true);
		}
	}

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		String deviceId;
		String serialPort = null;
		String host = null;
		int port = DEFAULT_PORT;

		EpsonProjectorDevice device = null;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + "]";
		}

		EpsonProjectorDevice getConnection() {
			if (device == null) {
				if (serialPort != null) {
					device = new EpsonProjectorDevice(serialPort);
				} else if (host != null) {
					device = new EpsonProjectorDevice(host, port);
				}
			}
			return device;
		}

	}

}
