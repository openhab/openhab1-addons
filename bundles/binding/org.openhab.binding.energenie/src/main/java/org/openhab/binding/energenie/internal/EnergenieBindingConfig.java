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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;
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
		 * The deviceId of the energenie pms-lan (you could have more than one).
		 */
		private final String itemType;

		/**
		 * The socket/measurement number to control.
		 */
		private final int socket_measurement_Number;		

		private double voltage = 0.0;
		private double current = 0.0;
		private double power = 0.0;
		private double energy = 0.0; 
		
		
	public EnergenieBindingConfig(String deviceId, String itemType, String socket_measurement_Number)
			throws BindingConfigParseException {
			this.deviceId = parseDeviceIdConfigString(deviceId);
			this.socket_measurement_Number = parseSocketMeasurementNumberConfigString(socket_measurement_Number);
			this.itemType = parseItemTypeConfigString(itemType);
			
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
	 * Parses a socket/measurement number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The item type as a string.
	 * @return The item type as a string.
	 * @throws BindingConfigParseException
	 */
	private String parseItemTypeConfigString(String configString) throws BindingConfigParseException {
		try {
			return configString;
		} catch (Exception e) {
			throw new BindingConfigParseException("Error parsing item type.");
		}
	}	
	
	/**
	 * Parses a socket/measurement number string that has been found in the configuration.
	 * 
	 * @param configString
	 *            The socket/measurement number as a string.
	 * @return The socket/measurement number as an integer value.
	 * @throws BindingConfigParseException
	 */
	private int parseSocketMeasurementNumberConfigString(String configString) throws BindingConfigParseException {
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
	 * @return The item Type that has been declared in the binding
	 *         configuration.
	 */
	public String getItemType() {
		return itemType;
	}

	/**
	 * @return The socket number that has been declared in the binding
	 *         configuration.
	 */
	public int getSocketMeasurementNumber() {
		return socket_measurement_Number;
	}
	

	/**
	 * Sets the actual temperature for this thermostat. 
	 * @param value the actual temperature raw value as provided by the L message
	 */
	public void setVoltage(double value) {
		this.voltage = value;
	}
	public void setCurrent(double value) {
		this.current = value;
	}
	public void setPower(double value) {
		this.power = value;
	}
	public void setEnergy(double value) {
		this.energy = value;
	}

	/**
	 * Returns the setpoint temperature  of this thermostat. 
	 * 4.5°C is displayed as OFF, 30.5°C is displayed as On at the thermostat display.
	 *
	 * @return 
	 * 			the setpoint temperature as <code>DecimalType</code>
	 */
	public State getVoltage() {
		return new DecimalType(this.voltage);
	}
	public State getCurrent() {
		return new DecimalType(this.current);
	}
	public State getPower() {
		return new DecimalType(this.power);
	}
	public State getEnergy() {
		return new DecimalType(this.energy);
	}
	
}
