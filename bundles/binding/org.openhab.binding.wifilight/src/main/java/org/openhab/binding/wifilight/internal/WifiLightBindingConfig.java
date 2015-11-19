/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wifilight.internal;

import java.util.HashMap;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WifiLightBindingConfig extends HashMap<String, String> implements
		BindingConfig {

	private static final long serialVersionUID = 1L;

	static final Logger logger = LoggerFactory
			.getLogger(WifiLightBindingConfig.class);

	public enum BindingType {
		rgb, blue, red, green, white
	}

	private final String deviceId;

	private final BindingType commandType;

	public WifiLightBindingConfig(String deviceId, String commandType,
			String steps) throws BindingConfigParseException {

		this.deviceId = parseDeviceIdConfigString(deviceId);
		this.commandType = parseBindingTypeConfigString(commandType);
	}

	private WifiLightBindingConfig.BindingType parseBindingTypeConfigString(
			String configString) throws BindingConfigParseException {
		if (configString != null) {
			try {
				return BindingType.valueOf(configString.trim());
			} catch (Exception ignore) {
			}
		}
		return BindingType.blue;
	}

	private String parseDeviceIdConfigString(String configString)
			throws BindingConfigParseException {
		try {
			return configString;
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing deviceId.");
		}
	}

	/**
	 * @return The deviceId that has been declared in the binding configuration.
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return The binding type as a {@link WifiLightBindingConfig.BindingType}
	 *         that has been declared in the binding configuration.
	 */
	public BindingType getCommandType() {
		return commandType;
	}
}