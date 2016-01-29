/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.openhab.binding.edimax.EdimaxBindingProvider;
import org.openhab.binding.edimax.internal.EdimaxBindingConfiguration.Type;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Binding main class.
 * 
 * @author Heinz
 *
 */
public class EdimaxBinding extends AbstractActiveBinding<EdimaxBindingProvider> {

	/**
	 * Logger.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(EdimaxBinding.class);

	/**
	 * Real devices discovered.
	 */
	private EdimaxDevice[] discoveredDevices;

	/**
	 * How many exception occur until it is considered to be a real error.
	 * Should be used in conjunction with {@link #getRefreshInterval()}. May
	 * also be configurable by managed service.
	 */
	private static final int EXCEPTION_COUNT_TO_REAL_ERROR = 4;

	/**
	 * How many errors occured in succession.
	 */
	private int errorCount;

	@Override
	protected void execute() {

		// discover
		if (shouldDiscover()) {
			discover();
		}

		// check device's state -> post if it changed.
		for (EdimaxBindingProvider provider : providers) {
			for (String itemName : provider.getItemNames()) {
				EdimaxBindingConfiguration config = ((EdimaxGenericBindingProvider) provider)
						.getConfig(itemName);
				String macAddress = config.getMacAddress();
				String deviceIP = getDeviceIP(macAddress);
				if (deviceIP == null) {
					logger.error("Device with MAC: " + macAddress
							+ " not found/discovered.");
					continue;
				}

				// if type is null, default type is STATE
				EdimaxBindingConfiguration.Type type = config.getType();
				if (type == null) {
					type = Type.STATE;
				}

				State newState = null;
				try {
					switch (type) {
					case CURRENT:
						BigDecimal current = createSender(config).getCurrent(
								deviceIP);
						newState = new DecimalType(current);
						break;
					case POWER:
						BigDecimal power = createSender(config).getPower(
								deviceIP);
						newState = new DecimalType(power);
						break;
					case STATE:
						Boolean state = createSender(config).getState(deviceIP);
						if (state) {
							newState = OnOffType.ON;
						} else {
							newState = OnOffType.OFF;
						}
						break;
					}
				} catch (IOException e) {
					logger.error(
							"Error in communication with device. Device's MAC: "
									+ macAddress
									+ ". Cannot get update from device.", e);
				}
				if (newState != null) {
					eventPublisher.postUpdate(itemName, newState);
				}
			}
		}
	}

	/**
	 * Creates sender based on the configured password.
	 * 
	 * @param config
	 * @return
	 */
	private HTTPSend createSender(EdimaxBindingConfiguration config) {
		String password = config.getPassword();
		if (password == null) {
			return new HTTPSend();
		} else {
			return new HTTPSend(password);
		}
	}

	/**
	 * Discovery tool.
	 */
	private Discoverer discoverer = new UDPDiscoverer();

	/**
	 * Discovery.
	 */
	protected void discover() {
		logger.debug("Edimax discovery running.");
		EdimaxDevice[] discovered = null;
		try {
			discovered = discoverer.discoverDevices();
			// no error present - all fine
			errorCount = 0;
			discoveredDevices = discovered;
		} catch (DiscoveryException e1) {
			// errors may occur
			errorCount++;
			if (errorCount >= EXCEPTION_COUNT_TO_REAL_ERROR) {
				// real error occured - set current devices to those
				discoveredDevices = discovered;
				logger.error(
						"Error discovering Edimax devices. Amount of exceptions: "
								+ EXCEPTION_COUNT_TO_REAL_ERROR, e1);
			} else {
				logger.debug("Interim error discovering Edimax devices.", e1);
			}
		}
	}

	/**
	 * Discover step counter.
	 */
	private int discoverStep = 0;

	/**
	 * The amount of executes() which are skipped until another discover
	 * happens. @See #getRefreshInterval.
	 */
	private static final int DISCOVER_SKIP_STEP = 60;

	/**
	 * Checks whether to discover or not (not always discover when thread runs).
	 * 
	 * @return
	 */
	protected boolean shouldDiscover() {
		boolean doDiscover = false;
		if (discoverStep % DISCOVER_SKIP_STEP == 0) {
			doDiscover = true;
			discoverStep = 0; // under- overrun
		}

		discoverStep++;
		return doDiscover;
	}

	private String getDeviceIP(String aMac) {
		aMac = aMac.toUpperCase();
		for (EdimaxDevice device : discoveredDevices) {
			if (aMac.equals(device.getMac())) {
				return device.getIp();
			}
		}
		return null;
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		for (EdimaxBindingProvider provider : providers) {
			EdimaxBindingConfiguration config = ((EdimaxGenericBindingProvider) provider)
					.getConfig(itemName);
			String deviceIP = getDeviceIP(config.getMacAddress());
			if (deviceIP == null) {
				logger.debug("No real device for item: " + itemName + " found.");
				continue;
			}
			changeValue(itemName, deviceIP, config, command);
			break;
		}
	}

	private void changeValue(String itemName, String deviceIP,
			EdimaxBindingConfiguration config, Command cmd) {
		if (cmd instanceof OnOffType) {
			try {
				Boolean currentState = createSender(config).getState(deviceIP);
				OnOffType targetState = (OnOffType) cmd;
				if (targetState == OnOffType.ON && !currentState) {
					createSender(config).switchState(deviceIP, Boolean.TRUE);
				} else if (targetState == OnOffType.OFF && currentState) {
					createSender(config).switchState(deviceIP, Boolean.FALSE);
				}

			} catch (IOException e) {
				logger.error("Error in communication with device: " + itemName
						+ ". Cannot set update to device.", e);
			}

		} else {
			logger.error("Unsupported command: " + cmd);
		}
	}

	/**
	 * Called by SCR to activate component.
	 * 
	 * @param bundleContext
	 * @param configuration
	 */
	public void activate(final BundleContext bundleContext,
			final Map<String, Object> configuration) {
		setProperlyConfigured(true);
	}

	@Override
	protected long getRefreshInterval() {
		// once every 30 seconds.
		return 1000 * 30;
	}

	@Override
	protected String getName() {
		return "Edimax update/discovery";
	}

}
