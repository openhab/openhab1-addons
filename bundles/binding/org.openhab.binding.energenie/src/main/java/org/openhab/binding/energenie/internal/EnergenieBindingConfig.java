/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
		 * The socket number to control.
		 */
		private final int socketNumber;
		

	public EnergenieBindingConfig(String deviceId, String socketNumber)
			throws BindingConfigParseException {
			this.deviceId = parseDeviceIdConfigString(deviceId);
			this.socketNumber = parseSocketNumberConfigString(socketNumber);
			
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
	/**
	 * Parses a socket number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The socket number as a string.
	 * @return The socket number as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseSocketNumberConfigString(String configString) throws BindingConfigParseException {
		try {
			return Integer.parseInt(configString);
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing channel number.");
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
	 * @return The socket number that has been declared in the binding
	 *         configuration.
	 */
	public int getSocketNumber() {
		return socketNumber;
	}
	
}
