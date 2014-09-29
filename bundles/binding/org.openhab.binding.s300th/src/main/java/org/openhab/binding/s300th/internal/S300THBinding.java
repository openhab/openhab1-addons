/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.s300th.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.s300th.S300THBindingProvider;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULHandler;
import org.openhab.io.transport.cul.CULListener;
import org.openhab.io.transport.cul.CULManager;
import org.openhab.io.transport.cul.CULMode;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implement this class if you are going create an actively polling service like
 * querying a Website/Device.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class S300THBinding extends AbstractActiveBinding<S300THBindingProvider> implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory.getLogger(S300THBinding.class);

	private final static String KS_300_ADDRESS = "ks300";

	/**
	 * the refresh interval which is used to poll values from the S300TH server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	private final static String KEY_DEVICE_NAME = "device";

	private String deviceName;

	private CULHandler cul;

	public S300THBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		closeCUL();
	}

	private void setNewDeviceName(String deviceName) {
		if (deviceName == null) {
			logger.error("Device name was null");
			return;
		}
		if (this.deviceName != null && this.deviceName.equals(deviceName)) {
			return;
		}
		closeCUL();
		this.deviceName = deviceName;
		openCUL();
	}

	private void openCUL() {
		try {
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
		} catch (CULDeviceException e) {
			logger.error("Can't open CUL handler for device " + deviceName, e);
		}
	}

	private void closeCUL() {
		if (cul != null) {
			cul.unregisterListener(this);
			CULManager.close(cul);
		}
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected String getName() {
		return "S300TH Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Ignore
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		logger.debug("Received new config");
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			if (StringUtils.isEmpty(deviceName)) {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME, "The device name can't be empty");
			} else {
				setNewDeviceName(deviceName);
			}

			setProperlyConfigured(true);
			// read further config parameters here ...

		}
	}

	@Override
	public void dataReceived(String data) {
		if (data.startsWith("K")) {
			int firstByte = Integer.parseInt(data.substring(1, 2), 16);
			int typByte = Integer.parseInt(data.substring(2, 3), 16) & 7;
			int sfirstByte = firstByte & 7;

			if (sfirstByte == 7) {
				logger.debug("Received WS7000 message, but parsing for WS7000 is not implemented");
				// TODO parse different sensors from WS7000 (?)
			} else {
				if (data.length() > 8 && data.length() < 13) {
					// S300TH default size = 9 characters
					parseS300THData(data);
				} else if (data.length() > 14 && data.length() < 20) {
					// KS300 default size = 15 characters.
					// sometime we got values with more characters.
					parseKS300Data(data);
				} else {
					logger.warn("Received unparseable message: " + data);
				}
			}
		}
	}

	/**
	 * Parse KS 300 data
	 * 
	 * @param data
	 */
	private void parseKS300Data(String data) {
		// TODO parse address and other bytes
		int rainValue = ParseUtils.parseKS300RainCounter(data);
		double windValue = ParseUtils.parseKS300Wind(data);
		double humidity = ParseUtils.parseKS300Humidity(data);
		double temperature = ParseUtils.parseTemperature(data);
		boolean isRaining = ParseUtils.isKS300Raining(data);

		logger.debug("Received data '" + data + "' from device with address ks300 : temperature: " + temperature
				+ " humidity: " + humidity + " wind: " + windValue + " rain: " + rainValue + " isRain: " + isRaining );

		for (Datapoint datapoint : Datapoint.values()) {
			S300THBindingConfig config = findConfig(KS_300_ADDRESS, datapoint);
			if (config == null) {
				continue;
			}
			double value = 0.0;
			switch (datapoint) {
			case TEMPERATURE:
				value = temperature;
				break;
			case HUMIDITY:
				value = humidity;
				break;
			case WIND:
				value = windValue;
				break;
			case RAIN:
				value = rainValue;
				break;
			case IS_RAINING:
				value = isRaining ? 1 : 0;
				break;
			}
			updateItem(config.item, value);
		}
	}

	/**
	 * Parse S300TH data
	 * 
	 * @param data
	 */
	private void parseS300THData(String data) {
		String address = ParseUtils.parseS300THAddress(data);
		double temperature = ParseUtils.parseTemperature(data);
		double humidity = ParseUtils.parseS300THHumidity(data);
		logger.debug("Received data '" + data + "' from device with address " + address + " : temperature: " + temperature
				+ " humidity: " + humidity);

		S300THBindingConfig temperatureConfig = findConfig(address, Datapoint.TEMPERATURE);
		if (temperatureConfig != null) {
			updateItem(temperatureConfig.item, temperature);
		}
		S300THBindingConfig humidityConfig = findConfig(address, Datapoint.HUMIDITY);
		if (humidityConfig != null) {
			updateItem(humidityConfig.item, humidity);
		}
	}

	private void updateItem(Item item, double value) {
		DecimalType type = new DecimalType(value);
		eventPublisher.postUpdate(item.getName(), type);
	}

	private S300THBindingConfig findConfig(String address, Datapoint datapoint) {
		for (S300THBindingProvider provider : this.providers) {
			S300THBindingConfig config = provider.getBindingConfigForAddressAndDatapoint(address, datapoint);
			if (config != null) {
				return config;
			}
		}
		return null;
	}

	@Override
	public void error(Exception e) {
		logger.error("Received error from CUL instead fo data", e);

	}

}
