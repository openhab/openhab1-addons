/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire_cuno.internal;

import java.util.Dictionary;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.onewire_cuno.OnewireCunoBindingConfig;
import org.openhab.binding.onewire_cuno.OnewireCunoBindingProvider;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Command;
import org.openhab.io.transport.cul.CULCommunicationException;
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
 * This class implements the communcation between openHAB and Onewire (Cuno) devices. Via
 * RF received updates are received directly, there is no polling.
 * 
 * @author Robert Delbrück
 * @since 1.4.0
 */
public class OnewireCunoBinding extends AbstractActiveBinding<OnewireCunoBindingProvider>
		implements ManagedService, CULListener {

	private static final Logger logger = LoggerFactory
			.getLogger(OnewireCunoBinding.class);
	
	private ReentrantLock lock = new ReentrantLock(true);
	private Condition condition = lock.newCondition();

	private final static String KEY_DEVICE_NAME = "device";
	private final static String KEY_HMS_EMULATION = "hms-emulation";

	private String deviceName;
	private boolean hmsEmulation;
	private String lastReceived;

	private CULHandler cul;

	/**
	 * the refresh interval which is used to poll values from the FS20 server
	 * (optional, defaults to 60000ms)
	 */
	private long refreshInterval = 60000;

	public OnewireCunoBinding() {
	}

	public void activate() {
		logger.debug("Activating Onewire (Cuno) binding");
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
			cul = CULManager.getOpenCULHandler(deviceName, CULMode.SLOW_RF);
			cul.registerListener(this);
		} catch (CULDeviceException e) {
			logger.error("Can't open cul device", e);
			cul = null;
		}
	}

	public void deactivate() {
		logger.debug("Deactivating Onewire (Cuno) binding");
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
		return "Onewire (Cuno) Refresh Service";
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
		// nothing to do
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug("Received new config");
		if (config != null) {
			logger.trace("parsing config...");

			// to override the default refresh interval one has to add a
			// parameter to openhab.cfg like
			// <bindingName>:refresh=<intervalInMs>
			String refreshIntervalString = (String) config.get("refresh");
			if (StringUtils.isNotBlank(refreshIntervalString)) {
				refreshInterval = Long.parseLong(refreshIntervalString);
			}
			String deviceName = (String) config.get(KEY_DEVICE_NAME);
			logger.trace("reading devicename: {}", deviceName);
			if (!StringUtils.isEmpty(deviceName)) {
				setNewDeviceName(deviceName);
			} else {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_DEVICE_NAME,
						"The device name can't be empty");
			}
			
			try {
				hmsEmulation = BooleanUtils.toBoolean(config
						.get(KEY_HMS_EMULATION) + "");
			} catch (Exception e) {
				logger.error("No device name configured");
				setProperlyConfigured(false);
				throw new ConfigurationException(KEY_HMS_EMULATION,
						"cannot detect boolean value for '" + KEY_HMS_EMULATION + "'");
			}
			setProperlyConfigured(true);
			
			setupOnewire();
		} else {
			logger.trace("config is null, do nothing");
		}
	}
	
	private void setupOnewire() {
		try {
			// Reset Onewire Bus
			cul.send("ORb");
			lock.lock();
			condition.await();
			lock.unlock();
			if (this.lastReceived == null) {
				logger.warn("internal error, expecting received message from cul, but was null");
				throw new IllegalStateException("internal error, expecting received message from cul, but was null");
			}
			logger.info("onewire devices on bus: {}", this.lastReceived);
			
			// set HMS simulation
			cul.send("OHo");
			lock.lock();
			condition.await();
			lock.unlock();
			if (this.lastReceived == null) {
				throw new IllegalStateException("internal error, expecting received message from cul, but was null");
			}
			boolean currentState = BooleanUtils.toBoolean(this.lastReceived, "ON", "OFF");
			logger.info("hms emulation currently: {}", currentState);
			if (this.hmsEmulation != currentState) {
				logger.debug("hms emulation is not as expected, switching again");
				
				cul.send("OHo");
				lock.lock();
				condition.await();
				lock.unlock();
				if (this.lastReceived == null) {
					throw new IllegalStateException("internal error, expecting received message from cul, but was null");
				}
				currentState = BooleanUtils.toBoolean(this.lastReceived, "ON", "OFF");
				logger.info("hms emulation currently: {}", currentState);
				if (this.hmsEmulation != currentState) {
					throw new IllegalStateException("can not toggle hms state!");
				}
			}
			
		} catch (CULCommunicationException e) {
			throw new IllegalStateException("communication failed", e);
		} catch (InterruptedException e) {
			throw new IllegalStateException("wait interrupted", e);
		}
	}

	@Override
	public void dataReceived(String data) {
		lock.lock();
		this.lastReceived = data;
		condition.signalAll();
		lock.unlock();
		
		// It is possible that we see here messages of other protocols
		if (data.startsWith("H")) {
			logger.debug("Received Onewire (Cuno) message: " + data);
			handleReceivedMessage(data);
		}
	}

	/**
	 * example: H5EBC01530100FF
	 * @param message
	 */
	private void handleReceivedMessage(String message) {
		String houseCode = (message.substring(1, 5));
		String value = message.substring(7, 11);
		OnewireCunoBindingConfig config = null;
		for (OnewireCunoBindingProvider provider : providers) {
			logger.trace("looking in provider: {}", provider);
			config = provider.getConfigForAddress(houseCode);
			logger.trace("find config for '{}': {}", houseCode, config);
			if (config != null) {
				break;
			}
		}
		if (config != null) {
			float converted = 0;
			// switch two bytes
			value = value.substring(2) + value.substring(0, 2);
			converted = Float.parseFloat(value.substring(1));
			converted /= 10;
			if (value.startsWith("8")) {
				converted = converted * -1;
			}
			eventPublisher.postUpdate(config.getItem().getName(), new DecimalType(converted));
		} else {
			logger.debug("Received message for unknown device " + houseCode);
		}
	}
	
	@Override
	public void error(Exception e) {
		logger.error("Error while communicating with CUL", e);

	}

}
