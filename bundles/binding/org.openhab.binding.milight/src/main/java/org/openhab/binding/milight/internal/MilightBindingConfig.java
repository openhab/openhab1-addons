/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.milight.internal;

import java.util.HashMap;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of a openHAB item that is binded to a Mi-Light
 * bulb. It contains the following information:
 * 
 * <ul>
 * <li>The channel number the bulb has on the Milight bridge where 0 represents all white bulbs, 1-4 white channels an 5 RGB bulbs.</li>
 * <li>The binding type of the Mi-Light item</li>
 * <ul>
 * <li>Brightness</li>
 * <li>Color temperature</li>
 * <li>RGB</li>
 * <li>Night Mode</li>
 * <li>Disco Mode</li>
 * <li>Disco Speed</li>
 * </ul>
 * 
 * @author Hans-Joerg Merk
 * @author Kai Kreuzer
 * @since 1.3.0
 */
public class MilightBindingConfig extends HashMap<String, String> implements BindingConfig {
	/** generated serialVersion UID */
	private static final long serialVersionUID = -8702006872563774395L;

	
	static final Logger logger = LoggerFactory
			.getLogger(MilightBindingConfig.class);

	/**
	 * The binding type of the Mi-Light item.
	 * <ul>
	 * <li>Brightness</li>
	 * <li>Color temperature</li>
	 * <li>RGB</li>
	 * <li>Night Mode</li>
	 * <li>Disco Mode</li>
	 * <li>Disco Speed</li>
	 * <li>White Mode</li>
	 * </ul>
	 */
	public enum BindingType {
		brightness, colorTemperature, rgb, nightMode, discoMode, discoSpeed, whiteMode
	}

	/**
	 * The deviceId of the Mi-Light bridge (you could have more than one).
	 */
	private final String deviceId;

	/**
	 * The channel number under which the bulb is filed in the Mi-Light bridge.
	 */
	private final int channelNumber;

	/**
	 * The binding type of the Mi-Light item.
	 */
	private final BindingType commandType;
	
	/**
	 * The number of dimming steps for RGBW item.
	 */
	private final int steps;

	/**
	 * Constructor of the MilightBindingConfig.
	 * 
	 * @param deviceNumber
	 *            The number under which the bulb is filed in the Mi-Light bridge.
	 * @param type
	 *            The optional binding type of the Mi-Light binding.
	 *            <ul>
	 *            <li>Switch</li>
	 *            <li>Brightness (default)</li>
	 *            <li>Color temperature</li>
	 *            <li>RGB</li>
	 *            <li>Night Mode</li>
	 *            <li>Disco Mode</li>
	 *            <li>Disco Speed</li>
	 *            <li>White Mode</li>
	 *            </ul>
	 * @throws BindingConfigParseException
	 */
	public MilightBindingConfig(String deviceId, String channelNumber, String commandType, String steps)
			throws BindingConfigParseException {

		this.deviceId = parseDeviceIdConfigString(deviceId);
		
		this.channelNumber = parseChannelNumberConfigString(channelNumber);

		this.commandType = parseBindingTypeConfigString(commandType);
		
		if (steps != null) {
			this.steps = parseStepsConfigString(steps);
		} else {
				this.steps = 27; // http://www.limitlessled.com/dev/
		}
	}

	/**
	 * Parses a binding type string that has been found in the configuration. If
	 * the string could not be parsed, the switch type is returned as default.
	 * 
	 * @param configString
	 *            The binding type as a string.
	 * @return The binding type as a {@link MilightBindingConfig.BindingType}
	 * @throws BindingConfigParseException
	 */
	private MilightBindingConfig.BindingType parseBindingTypeConfigString(
			String configString) throws BindingConfigParseException {

		if (configString != null) {
			try {
				return BindingType.valueOf(configString.trim());
			} catch (Exception ignore) {}
		}
		return BindingType.brightness;
	}

	/**
	 * Parses a IP-Address string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The IP Address as a string.
	 * @return The IP Address as a string.
	 * @throws BindingConfigParseException
	 */
	private String parseDeviceIdConfigString(String configString) throws BindingConfigParseException {
		try {
			return configString;
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing deviceId.");
		}
	}

	/**
	 * Parses a channel number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The channel number as a string.
	 * @return The channel number as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseChannelNumberConfigString(String configString) throws BindingConfigParseException {
		try {
			return Integer.parseInt(configString);
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing channel number.");
		}
	}

	/**
	 * Parses a channel number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The channel number as a string.
	 * @return The channel number as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseStepsConfigString(String configString) throws BindingConfigParseException {
		try {
			return Integer.parseInt(configString);
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing steps.");
		}
	}
	
	/**
	 * @return The deviceId that has been declared in the binding
	 *         configuration.
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return The channel number that has been declared in the binding
	 *         configuration.
	 */
	public int getChannelNumber() {
		return channelNumber;
	}

	/**
	 * @return The binding type as a {@link MilightBindingConfig.BindingType} that
	 *         has been declared in the binding configuration.
	 */
	public BindingType getCommandType() {
		return commandType;
	}

	/**
	 * @return The number of dimming steps as a {@link MilightBindingConfig.BindingType} that
	 *         has been declared in the binding configuration.
	 */
	public int getSteps() {
		return steps;
	}

}
