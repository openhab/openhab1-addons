/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal;

import org.openhab.core.binding.BindingConfig;
import org.openhab.core.library.types.HSBType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This represents the configuration of a openHAB item that is binded to a Hue
 * bulb. It contains the following information:
 * 
 * <ul>
 * <li>The device number the bulb has on the Hue bridge. The bulbs should have
 * numbers from 1 up to the number of connected bulbs.</li>
 * <li>The binding type of the hue item</li>
 * <ul>
 * <li>Switch</li>
 * <li>Brightness</li>
 * <li>Color temperature</li>
 * <li>RGB</li>
 * <li>The optionally configurable step size that will be used when the bulb is
 * dimmed up or down. Default is 25.</li>
 * </ul>
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 */
public class HueBindingConfig implements BindingConfig {

	static final Logger logger = LoggerFactory
			.getLogger(HueBindingConfig.class);

	/**
	 * The binding type of the hue item.
	 * <ul>
	 * <li>Switch</li>
	 * <li>Brightness</li>
	 * <li>Color temperature</li>
	 * <li>RGB</li>
	 * </ul>
	 */
	public enum BindingType {
		switching, brightness, colorTemperature, rgb
	}

	/**
	 * The number under which the bulb is filed in the Hue bridge.
	 */
	private final int deviceNumber;

	/**
	 * The binding type of the hue item.
	 */
	private final BindingType type;

	/**
	 * The optionally configurable step size that will be used when the bulb is
	 * dimmed up or down. Default is 25.
	 */
	private final int stepSize;
	
	/**
	 * On / Off Item State
	 */
	public OnOffType itemStateOnOffType;
	
	/**
	 * Percentage Item State
	 */
	public PercentType itemStatePercentType;
	
	/**
	 * HSBType Item State
	 */
	public HSBType itemStateHSBType;
	
	/**
	 * Constructor of the HueBindingConfig.
	 * 
	 * @param deviceNumber
	 *            The number under which the bulb is filed in the Hue bridge.
	 * @param type
	 *            The optional binding type of the hue binding.
	 *            <ul>
	 *            <li>Switch</li>
	 *            <li>Brightness (default)</li>
	 *            <li>Color temperature</li>
	 *            <li>RGB</li>
	 *            </ul>
	 * @param stepSize
	 *            The optionally configurable step size that will be used when
	 *            the bulb is dimmed up or down. Default is 25.
	 * @throws BindingConfigParseException
	 */
	public HueBindingConfig(String deviceNumber, String type, String stepSize)
			throws BindingConfigParseException {

		this.deviceNumber = parseDeviceNumberConfigString(deviceNumber);

		if (type != null) {
			this.type = parseBindingTypeConfigString(type);
		} else {
			this.type = HueBindingConfig.BindingType.brightness;
		}

		if (stepSize != null) {
			this.stepSize = parseStepSizeConfigString(stepSize);
		} else {
			this.stepSize = 25;
		}

	}

	/**
	 * Parses a step size string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The step size as a string.
	 * @return The step size as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseStepSizeConfigString(String configString)
			throws BindingConfigParseException {
		try {
			return Integer.parseInt(configString);
		} catch (Exception e) {
			throw new BindingConfigParseException(
					"Error parsing step size.");
		}
	}

	/**
	 * Parses a binding type string that has been found in the configuration. If
	 * the string could not be parsed, the switch type is returned as default.
	 * 
	 * @param configString
	 *            The binding type as a string.
	 * @return The binding type as a {@link HueBindingConfig.BindingType}
	 * @throws BindingConfigParseException
	 */
	private HueBindingConfig.BindingType parseBindingTypeConfigString(
			String configString) throws BindingConfigParseException {

		if (configString != null) {
			try {
				return BindingType.valueOf(configString.trim());
			} catch (Exception ignore) {
			}
		}
		return HueBindingConfig.BindingType.switching;
	}

	/**
	 * Parses a device number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The device number as a string.
	 * @return The device number as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseDeviceNumberConfigString(String configString)
			throws BindingConfigParseException {
		try {
			return Integer.parseInt(configString);
		} catch (Exception e) {
			throw new BindingConfigParseException(
					"Error parsing device number.");
		}
	}

	/**
	 * @return The device number that has been declared in the binding
	 *         configuration.
	 */
	public int getDeviceNumber() {
		return deviceNumber;
	}

	/**
	 * @return The binding type as a {@link HueBindingConfig.BindingType} that
	 *         has been declared in the binding configuration.
	 */
	public BindingType getType() {
		return type;
	}

	/**
	 * @return The step size that has been declared in the binding
	 *         configuration. This is the amount of increase and decrease of
	 *         bulb values like hue or brightness.
	 */
	public int getStepSize() {
		return stepSize;
	}

}
