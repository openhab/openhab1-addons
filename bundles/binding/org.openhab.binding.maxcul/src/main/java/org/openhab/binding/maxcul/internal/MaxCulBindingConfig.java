/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Properties;
import org.openhab.binding.maxcul.internal.messages.ConfigTemperaturesMsg;
import org.openhab.core.binding.BindingConfig;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Parse and hold the configuration information for the Max!CUL bindings.
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class MaxCulBindingConfig implements BindingConfig {
	private MaxCulDevice deviceType = MaxCulDevice.UNKNOWN;
	private MaxCulFeature feature = MaxCulFeature.UNKNOWN;
	private String serialNumber = "";
	private String devAddr = "";
	private boolean paired = false;

	private double comfortTemp = ConfigTemperaturesMsg.DEFAULT_COMFORT_TEMP;
	private double ecoTemp = ConfigTemperaturesMsg.DEFAULT_ECO_TEMP;
	private double maxTemp = ConfigTemperaturesMsg.DEFAULT_MAX_TEMP;
	private double minTemp = ConfigTemperaturesMsg.DEFAULT_MIN_TEMP;
	private double windowOpenTemperature = ConfigTemperaturesMsg.DEFAULT_WINDOW_OPEN_TEMP;
	private double windowOpenDuration = ConfigTemperaturesMsg.DEFAULT_WINDOW_OPEN_TIME;
	private double measurementOffset = ConfigTemperaturesMsg.DEFAULT_OFFSET;
	private boolean temperatureConfigSet = false;
	private HashSet<String> associatedSerialNum = new HashSet<String>();

	private final String CONFIG_PROPERTIES_BASE = "etc/maxcul";

	private static final Logger logger = LoggerFactory
			.getLogger(MaxCulBindingConfig.class);

	MaxCulBindingConfig(String bindingConfig)
			throws BindingConfigParseException {

		MaxCulBindingConfigParser.parseMaxCulBindingString(bindingConfig, this);
	}

	public double getComfortTemp() {
		return comfortTemp;
	}

	public double getEcoTemp() {
		return ecoTemp;
	}

	public double getMaxTemp() {
		return maxTemp;
	}

	public double getMinTemp() {
		return minTemp;
	}

	public double getWindowOpenTemperature() {
		return windowOpenTemperature;
	}

	public double getWindowOpenDuration() {
		return windowOpenDuration;
	}

	public double getMeasurementOffset() {
		return measurementOffset;
	}

	public boolean isTemperatureConfigSet() {
		return temperatureConfigSet;
	}

	void setPairedInfo(String dstAddr) {
		this.devAddr = dstAddr;
		this.paired = true;
		saveStoredConfig();
	}

	private String generateConfigFilename() {
		String base = CONFIG_PROPERTIES_BASE;
		String filename = String.format("%s/%s.properties", base,
				this.getSerialNumber());
		return filename;
	}

	/**
	 * Load the stored configuration information if it exists. This information
	 * is established during the pairing process.
	 */
	public void loadStoredConfig() {
		File cfgFile = new File(generateConfigFilename());

		if (cfgFile.exists()) {
			try {
				FileInputStream fiStream = new FileInputStream(cfgFile);
				Properties propertiesFile = new Properties();
				propertiesFile.load(fiStream);

				this.devAddr = propertiesFile.getProperty("devAddr");
				this.paired = true;

				fiStream.close();
			} catch (IOException e) {
				logger.warn("Unable to load information for "
						+ this.getDeviceType() + " " + this.getSerialNumber()
						+ " it may not yet be paired. Error was "
						+ e.getMessage());
				this.paired = false;
				return;
			}
			logger.debug("Successfully loaded pairing info for "
					+ this.getSerialNumber());
		} else {
			logger.warn("Unable to locate information for "
					+ this.getDeviceType() + " " + this.getSerialNumber()
					+ " it may not yet be paired");
			this.paired = false;
		}
	}

	/**
	 * Save the stored configuration information. Will update it if it already
	 * exists. The information is primarily established during the pairing
	 * process.
	 */
	private void saveStoredConfig() {
		if (this.paired) {
			File cfgFile = new File(generateConfigFilename());
			File cfgDir = new File(CONFIG_PROPERTIES_BASE);

			if (!cfgFile.exists()) {
				try {
					if (!cfgDir.exists())
						cfgDir.mkdirs();
					cfgFile.createNewFile();
				} catch (IOException e) {
					logger.warn("Unable to create new properties file for "
							+ this.getDeviceType()
							+ " "
							+ this.getSerialNumber()
							+ ". Data won't be saved so pairing will be lost. Error was "
							+ e.getMessage());
					return;
				}
			}

			Properties propertiesFile = new Properties();
			propertiesFile.setProperty("devAddr", this.devAddr);

			try {
				FileOutputStream foStream = new FileOutputStream(cfgFile);
				Date updateTime = new Date();
				propertiesFile.store(
						foStream,
						"Autogenerated by MaxCul binding on "
								+ updateTime.toString());
				this.paired = true;
				foStream.close();
			} catch (IOException e) {
				logger.warn("Unable to load information for "
						+ this.getDeviceType() + " " + this.getSerialNumber()
						+ " it may not yet be paired. Error was "
						+ e.getMessage());
				this.paired = false;
				return;
			}
			logger.debug("Successfully wrote pairing info for "
					+ this.getSerialNumber());
		} else
			logger.error("Tried saving configuration for "
					+ this.getSerialNumber() + " which is not paired.");
	}

	public MaxCulDevice getDeviceType() {
		return deviceType;
	}

	public MaxCulFeature getFeature() {
		return feature;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public String getDevAddr() {
		return devAddr;
	}

	public boolean isPaired() {
		return paired;
	}

	public HashSet<String> getAssociatedSerialNum() {
		return associatedSerialNum;
	}

	public void setDeviceType(MaxCulDevice deviceType) {
		this.deviceType = deviceType;
	}

	public void setFeature(MaxCulFeature feature) {
		this.feature = feature;
	}

	public void setTemperatureConfigSet(boolean temperatureConfigSet) {
		this.temperatureConfigSet = temperatureConfigSet;
	}

	public void setMeasurementOffset(double measurementOffset) {
		this.measurementOffset = measurementOffset;
	}

	public void setWindowOpenDuration(double windowOpenDuration) {
		this.windowOpenDuration = windowOpenDuration;
	}

	public void setWindowOpenTemperature(double windowOpenTemperature) {
		this.windowOpenTemperature = windowOpenTemperature;
	}

	public void setMinTemp(double minTemp) {
		this.minTemp = minTemp;
	}

	public void setMaxTemp(double maxTemp) {
		this.maxTemp = maxTemp;
	}

	public void setEcoTemp(double ecoTemp) {
		this.ecoTemp = ecoTemp;
	}

	public void setComfortTemp(double comfortTemp) {
		this.comfortTemp = comfortTemp;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public void clearAssociatedSerialNum() {
		this.associatedSerialNum.clear();
	}

	public void addAssociatedSerialNum(String assocDeviceSerial) {
		this.associatedSerialNum.add(assocDeviceSerial);
	}
}
