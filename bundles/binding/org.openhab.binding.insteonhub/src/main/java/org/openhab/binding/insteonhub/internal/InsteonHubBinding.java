/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.binding.insteonhub.internal.InsteonHubBindingConfig.BindingType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubAdjustmentType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubLevelUpdateType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyListener;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingConfigUtil;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Insteon Hub binding. Handles all commands and polls configured devices to
 * process updates.
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubBinding extends
		AbstractActiveBinding<InsteonHubBindingProvider> implements
		ManagedService {

	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubBinding.class);

	public static final String DEFAULT_HUB_ID = "_default";
	private static final long DEFAULT_REFRESH_INTERVAL = 60000;
	private static final String BINDING_NAME = "InsteonHubBinding";

	private final Map<String, AtomicLong> itemDimTimeouts = Collections
			.synchronizedMap(new HashMap<String, AtomicLong>());
	private long refreshInterval = DEFAULT_REFRESH_INTERVAL;
	private volatile boolean activated;

	// Map of proxies. key=hubId, value=proxy
	// Used to keep track of proxies
	private final Map<String, InsteonHubProxy> proxies = new HashMap<String, InsteonHubProxy>();

	@Override
	protected long getRefreshInterval() {
		return refreshInterval;
	}

	@Override
	protected String getName() {
		return "InsteonHub Refresh Service";
	}

	@Override
	protected void execute() {
		logger.debug(BINDING_NAME + " execute");

		Set<InsteonHubBindingDeviceInfo> deviceInfos = InsteonHubBindingConfigUtil
				.getConfiguredDevices(providers);
		// loop through all configured devices
		for (InsteonHubBindingDeviceInfo deviceInfo : deviceInfos) {
			// lookup proxy for device
			InsteonHubProxy proxy = proxies.get(deviceInfo.getHubId());
			if (proxy != null) {
				// request device level from proxy
				// this will callback in AsyncEventPublisher if device exists
				proxy.requestDeviceLevel(deviceInfo.getDeviceId());
			}
		}

		logger.debug(BINDING_NAME + " execute complete");
	}

	private static int levelToPercent(int level) {
		return (int) (100 * level / 255.0);
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		// get configuration for this item
		InsteonHubBindingConfig config = InsteonHubBindingConfigUtil
				.getConfigForItem(providers, itemName);
		if (config == null) {
			logger.error(BINDING_NAME + " received command for unknown item '"
					+ itemName + "'");
			return;
		}

		// parse info from config
		BindingType type = config.getBindingType();
		String hubId = config.getDeviceInfo().getHubId();
		String deviceId = config.getDeviceInfo().getDeviceId();

		// lookup proxy from this configuration
		InsteonHubProxy proxy = proxies.get(hubId);
		if (proxy == null) {
			logger.error(BINDING_NAME
					+ " received command for unknown hub id '" + hubId + "'");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(BINDING_NAME + " processing command '" + command
					+ "' of type '" + command.getClass().getSimpleName()
					+ "' for item '" + itemName + "'");
		}

		try {
			// process according to type
			if (type == BindingType.SWITCH) {
				// set value on or off
				if (command instanceof OnOffType) {
					proxy.setDevicePower(deviceId, command == OnOffType.ON);
				}
			} else if (type == BindingType.DIMMER) {
				// INSTEON Dimmer supports Dimmer and RollerShutter types
				if (command instanceof OnOffType) {
					// ON or OFF => Set level to 255 or 0
					int level = command == OnOffType.ON ? 255 : 0;
					proxy.setDeviceLevel(deviceId, level);
				} else if (command instanceof IncreaseDecreaseType) {
					// Increase/Decrease => Incremental Brighten/Dim
					InsteonHubAdjustmentType adjustmentType;
					if (command == IncreaseDecreaseType.INCREASE)
						adjustmentType = InsteonHubAdjustmentType.BRIGHTEN;
					else
						adjustmentType = InsteonHubAdjustmentType.DIM;
					if (setDimTimeout(itemName)) {
						proxy.startDeviceAdjustment(deviceId, adjustmentType);
					}
				} else if (command instanceof UpDownType) {
					// Up/Down => Start Brighten/Dim
					InsteonHubAdjustmentType adjustmentType;
					if (command == UpDownType.UP)
						adjustmentType = InsteonHubAdjustmentType.BRIGHTEN;
					else
						adjustmentType = InsteonHubAdjustmentType.DIM;
					proxy.startDeviceAdjustment(deviceId, adjustmentType);
				} else if (command instanceof StopMoveType) {
					// Stop => Stop Brighten/Dim
					if (command == StopMoveType.STOP) {
						proxy.stopDeviceAdjustment(deviceId);
					}
				} else {
					// set level from 0 to 100 percent value
					byte percentByte = Byte.parseByte(command.toString());
					float percent = percentByte * .01f;
					int level = (int) (255 * percent);
					proxy.setDeviceLevel(deviceId, level);
				}

			}
		} catch (Throwable t) {
			logger.error("Error processing command '" + command
					+ "' for item '" + itemName + "'", t);
		}
	}

	// returns true if the timeout was not already set
	private boolean setDimTimeout(String itemName) {
		AtomicLong timeout = itemDimTimeouts.get(itemName);
		if (timeout == null) {
			timeout = new AtomicLong(System.currentTimeMillis() + 400);
			itemDimTimeouts.put(itemName, timeout);
			return true;
		} else {
			long existing = timeout.getAndSet(System.currentTimeMillis() + 400);
			return existing == 0;
		}
	}

	@Override
	public synchronized void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug(BINDING_NAME + " updated");
		try {
			// Process device configuration
			if (config != null) {
				String refreshIntervalString = (String) config.get("refresh");
				if (StringUtils.isNotBlank(refreshIntervalString)) {
					refreshInterval = Long.parseLong(refreshIntervalString);
				}

				// Stop all existing proxy async threads
				for (InsteonHubProxy proxy : proxies.values()) {
					proxy.stop();
				}
				// Clear proxy map. It will be rebuilt.
				proxies.clear();

				// Load new proxies
				Map<String, InsteonHubProxy> newProxies = InsteonHubProxyFactory
						.createInstances(config);
				proxies.putAll(newProxies);
				for (Map.Entry<String, InsteonHubProxy> entry : proxies
						.entrySet()) {
					String hubId = entry.getKey();
					InsteonHubProxy proxy = entry.getValue();
					proxy.addListener(new AsyncEventPublisher(hubId));
					// If activated, start proxy now
					if (activated) {
						proxy.start();
					}
				}

				// Set properly configured
				setProperlyConfigured(true);
			}
		} catch (Throwable t) {
			logger.error("Error configuring " + getName(), t);
			setProperlyConfigured(false);
		}
	}

	@Override
	public synchronized void activate() {
		logger.debug(BINDING_NAME + " activated");
		activated = true;
		dimStopThread.start();
		// start all proxy async threads
		for (InsteonHubProxy proxy : proxies.values()) {
			proxy.start();
		}
	}

	@Override
	public synchronized void deactivate() {
		logger.debug(BINDING_NAME + " deactivated");
		activated = false;
		// stop all proxy async threads
		for (InsteonHubProxy proxy : proxies.values()) {
			proxy.stop();
		}
	}

	@Override
	protected void internalReceiveUpdate(String itemName, State newState) {
		if (logger.isTraceEnabled()) {
			logger.trace(BINDING_NAME + " received update for '" + itemName
					+ "' of type '" + newState.getClass().getSimpleName()
					+ "' with value '" + newState + "'");
		}
		// ignore
	}

	/**
	 * This class listens for updates from the InsteonHubProxy.
	 */
	private class AsyncEventPublisher implements InsteonHubProxyListener {

		private final String hubId;

		public AsyncEventPublisher(String hubId) {
			this.hubId = hubId;
		}

		@Override
		public void onLevelUpdate(String device, int level,
				InsteonHubLevelUpdateType updateType) {
			Collection<InsteonHubBindingConfig> configs = InsteonHubBindingConfigUtil
					.getConfigsForDevice(providers, hubId, device);
			for (InsteonHubBindingConfig config : configs) {
				BindingType type = config.getBindingType();
				// FIXME Currently filtering STATUS_CHANGE out for non-dimmer types b/c it's not working properly. Need to learn more.
				if (type == BindingType.SWITCH
						&& updateType == InsteonHubLevelUpdateType.STATUS_CHANGE) {
					// switch => 0=OFF, else=ON
					State update = level == 0 ? OnOffType.OFF : OnOffType.ON;
					sendUpdate(config, update);
				} else if (type == BindingType.DIMMER
						&& updateType == InsteonHubLevelUpdateType.STATUS_CHANGE) {
					// dimmer => 0-255 to percent
					State update = new PercentType(levelToPercent(level));
					sendUpdate(config, update);
				} else if (type == BindingType.INPUT_ON_OFF
						&& updateType == InsteonHubLevelUpdateType.STATUS_CHANGE) {
					// on/off input => translate
					Integer onValue = config.getOnValue();
					Integer offValue = config.getOffValue();
					State update = parseDigitalUpdate(level, onValue, offValue,
							OnOffType.ON, OnOffType.OFF);
					sendUpdate(config, update);
				} else if (type == BindingType.INPUT_OPEN_CLOSED
						&& updateType == InsteonHubLevelUpdateType.STATUS_CHANGE) {
					// open/closed input => translate
					Integer openValue = config.getOpenValue();
					Integer closedValue = config.getClosedValue();
					State update = parseDigitalUpdate(level, openValue,
							closedValue, OpenClosedType.OPEN,
							OpenClosedType.CLOSED);
					sendUpdate(config, update);
				} else if (type == BindingType.INPUT_UBYTE) {
					// analog byte value => 0-255
					sendUpdate(config, new DecimalType(level));
				} else if (type == BindingType.INPUT_PERCENT) {
					// analog percentage => 0-255 to percent
					sendUpdate(config, new PercentType(levelToPercent(level)));
				}
			}
		}

		// Get the corresponding on/off value depending on the configured values
		private State parseDigitalUpdate(int value, Integer onValue,
				Integer offValue, State onState, State offState) {
			if (onValue != null && offValue != null) {
				// if on and off configured,
				// if either match => use state
				// otherwise => UNDEF
				if (value == onValue) {
					return onState;
				} else if (value == offValue) {
					return offState;
				} else {
					return UnDefType.UNDEF;
				}
			} else if (onValue != null) {
				// if only on configured,
				// if on matches => ON
				// otherwise => OFF
				if (value == onValue) {
					return onState;
				} else {
					return offState;
				}
			} else if (offValue != null) {
				// if only off configured,
				// if off matches => OFF
				// otherwise => ON
				if (value == offValue) {
					return offState;
				} else {
					return onState;
				}
			} else {
				// if neither configured,
				// if 0 => OFF
				// otherwise => ON
				if (value == 0) {
					return offState;
				} else {
					return onState;
				}
			}
		}

		private void sendUpdate(InsteonHubBindingConfig config, State update) {
			eventPublisher.postUpdate(config.getItemName(), update);
		}
	}

	private final Thread dimStopThread = new Thread() {
		@Override
		public void run() {
			while (activated) {
				long curTime = System.currentTimeMillis();
				synchronized (itemDimTimeouts) {
					// check all timeouts
					for (Map.Entry<String, AtomicLong> entry : itemDimTimeouts
							.entrySet()) {
						// parse from entry
						String itemName = entry.getKey();
						AtomicLong timeout = entry.getValue();
						// check if timeout is set and has elapsed
						if (timeout.get() > 0 && curTime > timeout.get()) {
							// timeout elapsed => reset timeout and stop dim/brt
							timeout.set(0);
							InsteonHubBindingConfig config = InsteonHubBindingConfigUtil
									.getConfigForItem(providers, itemName);
							InsteonHubProxy proxy = proxies.get(config
									.getDeviceInfo().getHubId());
							proxy.stopDeviceAdjustment(config.getDeviceInfo()
									.getDeviceId());
						}
					}
				}
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// ignore
				}
			}
		}
	};
}
