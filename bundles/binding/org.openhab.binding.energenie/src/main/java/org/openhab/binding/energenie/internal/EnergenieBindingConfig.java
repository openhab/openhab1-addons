/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.energenie.internal;


import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
	
	public class EnergenieBindingConfig extends HashMap<String, String> implements BindingConfig {
		/** generated serialVersion UID */
		private static final long serialVersionUID = -8702006872563774395L;

		
		static final Logger logger = LoggerFactory
				.getLogger(EnergenieBindingConfig.class);

		/**
		 * The deviceId of the energenie pms-lan (you could have more than one).
		 */
		private final String deviceId;

		/**
		 * The itemConfig
		 */
		private final String itemConfig;

		/**
		 * The itemType
		 */
		private final String itemType;

		/**
		 * The channel of the energenie pwm-lan (you could have more than one) for energy measurement.
		 */
		//private final ChannelTypeDef pwmChannel;
		
		public enum ChannelTypeDef {
			NONE, VOLTAGE, CURRENT, POWER, ENERGY
		}
		
		

	public EnergenieBindingConfig(String deviceId, String itemConfig, String itemType)
			throws BindingConfigParseException {
			this.deviceId = parseDeviceIdConfigString(deviceId);
			this.itemConfig = parseItemConfigString(itemConfig);
			this.itemType = parseItemType(itemType);
			}
	/**
	 * Parses a deviceId string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The deviceId as a string.
	 * @return The deviceId as a string.
	 * @throws BindingConfigParseException
	 */
	private String parseDeviceIdConfigString(String configString) throws BindingConfigParseException {
		try {
			return configString;
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing deviceId.");
		}
	}

	private String parseItemConfigString(String configString) throws BindingConfigParseException {

		if (configString != null) {
			try {
				return configString;
			} catch (Exception e) {
				throw new BindingConfigParseException("Error parsing item config.");
			}
			
		}
		return null;
	}
	private String parseItemType(String configString) throws BindingConfigParseException {

		if (configString != null) {
			try {
				return configString;
			} catch (Exception e) {
				throw new BindingConfigParseException("Error parsing item type.");
			}
			
		}
		return null;
	}

	/**
	 * @return The deviceId that has been declared in the binding
	 *         configuration.
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * @return The itemConfig that has been declared in the binding
	 *         configuration.
	 */
	public String getItemConfig() {
		return itemConfig;
	}

	public String getItemType() {
		return itemType;
	}

	
}
