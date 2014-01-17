/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.em.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.em.EMBindingProvider;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
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
public class EMBinding extends AbstractActiveBinding<EMBindingProvider> implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory.getLogger(EMBinding.class);

	private final static String CONFIG_KEY_DEVICE_NAME = "device";
	private long refreshInterval = 60000;
	private String deviceName;

	private CULHandler cul;

	private Map<String, Integer> counterMap = new HashMap<String, Integer>();

	public EMBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
		closeCUL();
	}

	private void closeCUL() {
		if (cul != null) {
			cul.unregisterListener(this);
			CULManager.close(cul);
		}
	}

	/**
	 * If the device name has changed, try to close the old device handler and
	 * create a new one
	 * 
	 * @param deviceName
	 *            The new deviceName
	 */
	private void setNewDeviceName(String deviceName) {
		if (deviceName != null && this.deviceName != null && this.deviceName.equals(deviceName)) {
			return;
		}
		if (this.deviceName != null && cul != null) {
			closeCUL();
		}
		try {
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
			this.deviceName = deviceName;
		} catch (CULDeviceException e) {
			logger.error("Can't get CULHandler", e);
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
		return "EM Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Nothing to do
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {
		if (config != null) {

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}

			String deviceName = (String) config.get(CONFIG_KEY_DEVICE_NAME);
			if (!StringUtils.isEmpty(deviceName)) {
				setNewDeviceName(deviceName);
			} else {
				setProperlyConfigured(false);
				throw new ConfigurationException(CONFIG_KEY_DEVICE_NAME, "Device name not configured");
			}
			setProperlyConfigured(true);
		}
	}

	@Override
	public void dataReceived(String data) {
		if (!StringUtils.isEmpty(data) && data.startsWith("E")) {
			parseDataLine(data);
		}

	}

	/**
	 * Parse the received line of data and create updates for configured items
	 * 
	 * @param data
	 */
	private void parseDataLine(String data) {
		String address = ParsingUtils.parseAddress(data);
		if (!checkNewMessage(address, ParsingUtils.parseCounter(data))) {
			logger.warn("Received message from " + address + " more than once");
			return;
		}
		EMType type = ParsingUtils.parseType(data);
		EMBindingConfig emConfig = findConfig(type, address, Datapoint.CUMULATED_VALUE);
		if (emConfig != null) {
			updateItem(emConfig, ParsingUtils.parseCumulatedValue(data));
		}
		if (data.length() > 10) {
			emConfig = findConfig(type, address, Datapoint.LAST_VALUE);
			if (emConfig != null) {
				updateItem(emConfig, ParsingUtils.parseCurrentValue(data));
			}
			emConfig = findConfig(type, address, Datapoint.TOP_VALUE);
			if (emConfig != null) {
				updateItem(emConfig, ParsingUtils.parsePeakValue(data));
			}
		}
	}

	/**
	 * Update an item given in the configuration with the given value multiplied
	 * by the correction factor
	 * 
	 * @param config
	 * @param value
	 */
	private void updateItem(EMBindingConfig config, int value) {
		DecimalType status = new DecimalType(value * config.getCorrectionFactor());
		eventPublisher.postUpdate(config.getItem().getName(), status);
	}

	/**
	 * Check if we have received a new message to not consume repeated messages
	 * 
	 * @param address
	 * @param counter
	 * @return
	 */
	private boolean checkNewMessage(String address, int counter) {
		Integer lastCounter = counterMap.get(address);
		if (lastCounter == null) {
			lastCounter = -1;
		}
		if (counter > lastCounter) {
			return true;
		}
		return false;
	}

	private EMBindingConfig findConfig(EMType type, String address, Datapoint datapoint) {
		EMBindingConfig emConfig = null;
		for (EMBindingProvider provider : this.providers) {
			emConfig = provider.getConfigByTypeAndAddressAndDatapoint(type, address, Datapoint.CUMULATED_VALUE);
			if (emConfig != null) {
				return emConfig;
			}
		}
		return null;
	}

	@Override
	public void error(Exception e) {
		logger.error("Exception instead of new data from CUL", e);

	}

}
