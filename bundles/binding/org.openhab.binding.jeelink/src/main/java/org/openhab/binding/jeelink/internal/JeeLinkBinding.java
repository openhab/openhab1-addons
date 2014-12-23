/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelink.internal;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.jeelink.JeeLinkBindingConfig;
import org.openhab.binding.jeelink.JeeLinkBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.items.ItemRegistry;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
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
 * This class implements the communcation between openHAB and JeeLink devices.
 * Via RF received updates are received directly, there is no polling.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class JeeLinkBinding extends
		AbstractActiveBinding<JeeLinkBindingProvider> implements
		ManagedService, CULListener {

	private static final Logger logger = LoggerFactory
			.getLogger(JeeLinkBinding.class);

	private final static String KEY_DEVICE_NAME = "device";
	@SuppressWarnings("unused")
	private ItemRegistry itemRegistry;

	public void setItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = itemRegistry;
	}

	public void unsetItemRegistry(ItemRegistry itemRegistry) {
		this.itemRegistry = null;
	}

	private String deviceName;

	private CULHandler cul;

	/**
	 * the refresh interval which is used to poll values from the JeeLink server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public JeeLinkBinding() {
	}

	public void activate() {
		logger.debug("Activating JeeLink binding");
	}

	private void setNewDeviceName(String deviceName) {
		if (cul != null) {
			CULManager.close(cul);
		}
		this.deviceName = deviceName;
		getCULHandler();
	}

	private void getCULHandler() {
		try {
			logger.debug("Opening CUL device on " + deviceName);
			Map<String,Integer> options = new HashMap<String,Integer>();
			options.put("baudrate", 57600);
			options.put("parity", 0); //parity none
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF,options);
			cul.registerListener(this);
		} catch (CULDeviceException e) {
			logger.error("Can't open cul device", e);
			cul = null;
		}
	}

	public void deactivate() {
		logger.debug("Deactivating JeeLink binding");
		cul.unregisterListener(this);
		CULManager.close(cul);
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
		return "JeeLink Refresh Service";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void execute() {
		// Nothing to do here
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		
	}

	/**
	 * @{inheritDoc
	 */

	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
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
				throw new ConfigurationException(KEY_DEVICE_NAME,
						"The device name can't be empty");
			} else {
				setNewDeviceName(deviceName);
			}

			setProperlyConfigured(true);
			// read further config parameters here ...

		}
	}

	public void dataReceived(String data) {
		// It is possible that we see here messages of other protocols
		if (data.startsWith("OK")) {
			logger.debug("Received JeeLink message: " + data);
			handleReceivedMessage(data);
		}

	}

	private void handleReceivedMessage(String message) {
		String parts[] = message.split(" ");
		String id = parts[2];
		int a = new Integer(parts[4]);
		int b = new Integer(parts[5]);
		double temp = (a * 256 + b - 1000) * 0.1;
		temp = Math.round(10.0 * temp) / 10.0;
		int lf = Integer.parseInt(parts[6]) & 0x7f;
		int batteryLow = (Integer.parseInt(parts[6]) & 0x80) >> 7;
		
		JeeLinkBindingConfig configHum = null;
		for (JeeLinkBindingProvider provider : providers) {
			configHum = provider.getConfigForAddress(id + ";H");
			if (configHum != null) {
				break;
			}
		}
		
		if (configHum != null && needsUpate(configHum) && configHum.getItem() != null) {
			State state = new DecimalType(lf);
			eventPublisher.postUpdate(configHum.getItem().getName(), state);
			configHum.setTimestamp(System.currentTimeMillis());
		}

		JeeLinkBindingConfig configTemp = null;
		for (JeeLinkBindingProvider provider : providers) {
			configTemp = provider.getConfigForAddress(id + ";T");
			if (configTemp != null) {
				break;
			}
		}
		if (configTemp != null && needsUpate(configTemp) && configTemp.getItem() != null) {
			State state = new DecimalType(temp);
			eventPublisher.postUpdate(configTemp.getItem().getName(), state);
			configTemp.setTimestamp(System.currentTimeMillis());
		}

		JeeLinkBindingConfig configBat = null;
		for (JeeLinkBindingProvider provider : providers) {
			configBat = provider.getConfigForAddress(id + ";B");
			if (configBat != null) {
				break;
			}
		}
		if (configBat != null && needsUpate(configBat) && configBat.getItem() != null) {
			State state = new DecimalType(batteryLow);
			eventPublisher.postUpdate(configBat.getItem().getName(), state);
			configBat.setTimestamp(System.currentTimeMillis());
		}
		
		if (configTemp == null) {
			logger.info("Received message for unknown device " + id);
		}
	}

	public void error(Exception e) {
		logger.error("Error while communicating with CUL", e);

	}
	
	private boolean needsUpate(JeeLinkBindingConfig config)
	{
		return System.currentTimeMillis() > config.getTimestamp() + refreshInterval;
	}
}
