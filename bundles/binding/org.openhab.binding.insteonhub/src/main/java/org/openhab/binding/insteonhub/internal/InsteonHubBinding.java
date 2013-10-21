/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */
package org.openhab.binding.insteonhub.internal;

import java.io.IOException;
import java.util.Collection;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.insteonhub.InsteonHubBindingProvider;
import org.openhab.binding.insteonhub.internal.InsteonHubBindingConfig.BindingType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyListener;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingConfigUtil;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingLogUtil;
import org.openhab.core.binding.AbstractActiveBinding;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.UpDownType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
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

	public static final String CONFIG_KEY_HOST = "host";
	public static final String CONFIG_KEY_PORT = "port";
	public static final String CONFIG_KEY_USER = "user";
	public static final String CONFIG_KEY_PASS = "pass";
	public static final long DEFAULT_REFRESH_INTERVAL = 60000;
	public static final String DEFAULT_HUB_ID = "default";

	private static final String BINDING_NAME = "InsteonHubBinding";
	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubBinding.class);

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
		// Iterate through all proxies
		for (Map.Entry<String, InsteonHubProxy> entry : proxies.entrySet()) {
			try {
				// Send updates for all devices in the proxy
				String deviceUid = entry.getKey();
				InsteonHubProxy receiverProxy = entry.getValue();
				sendUpdates(receiverProxy, deviceUid);
			} catch (Throwable t) {
				logger.error("Error polling device states on Insteon Hub '"
						+ entry.getKey() + "'", t);
			}
		}
	}

	private void sendUpdates(InsteonHubProxy proxy, String hubId) {
		// Get all item configurations belonging to this proxy
		Collection<InsteonHubBindingConfig> configs = InsteonHubBindingConfigUtil
				.getConfigsForHub(providers, hubId);
		for (InsteonHubBindingConfig config : configs) {
			try {
				int level = proxy.getLevel(config.getDevice());
				if (config.getBindingType() == BindingType.digital) {
					// Digital => Send value as ON or OFF
					State update = level > 0 ? OnOffType.ON : OnOffType.OFF;
					eventPublisher.postUpdate(config.getItemName(), update);
				} else if (config.getBindingType() == BindingType.analog) {
					// Analog => Send value as 0 to 100 percent
					State update = new PercentType(levelToPercent(level));
					eventPublisher.postUpdate(config.getItemName(), update);
				}
			} catch (IOException e) {
				InsteonHubBindingLogUtil.warnCommunicationFailure(logger,
						proxy, config.getDevice(), e);
				break;
			}
		}
	}

	private static int levelToPercent(int level) {
		return (int) (100 * level / 255.0);
	}

	@Override
	protected void internalReceiveCommand(String itemName, Command command) {
		InsteonHubBindingConfig config = InsteonHubBindingConfigUtil
				.getConfigForItem(providers, itemName);
		if (config == null) {
			logger.error(BINDING_NAME + " received command for unknown item '" + itemName + "'");
			return;
		}
		InsteonHubProxy proxy = proxies.get(config.getHubId());
		if (proxy == null) {
			logger.error(BINDING_NAME + " received command for unknown hub id '"
					+ config.getHubId() + "'");
			return;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(BINDING_NAME + " processing command '" + command
					+ "' of type '" + command.getClass().getSimpleName()
					+ "' for item '" + itemName + "'");
		}

		try {
			BindingType type = config.getBindingType();

			if (type == BindingType.digital) {
				// set value on or off
				if (command instanceof OnOffType) {
					proxy.setPower(config.getDevice(), command == OnOffType.ON,
							false);
				}
			} else if (type == BindingType.analog) {
				if (command instanceof OnOffType) {
					// ON or OFF => Set level to 255 or 0
					int level = command == OnOffType.ON ? 255 : 0;
					proxy.setLevel(config.getDevice(), level);
				} else if (command instanceof IncreaseDecreaseType
						|| command instanceof UpDownType) {
					// UP/DOWN or INC/DEC => adjust level by 25/255
					int adjustAmt = command == IncreaseDecreaseType.INCREASE
							|| command == UpDownType.UP ? 30 : -30;
					proxy.adjustLevel(config.getDevice(), adjustAmt);
				} else {
					// set level from 0 to 100 percent value
					byte percentByte = Byte.parseByte(command.toString());
					float percent = percentByte * .01f;
					int level = (int) (255 * percent);
					proxy.setLevel(config.getDevice(), level);
				}

			}
		} catch (Throwable t) {
			logger.error("Error processing command '" + command
					+ "' for item '" + itemName + "'", t);
		}
	}

	@Override
	public synchronized void updated(Dictionary<String, ?> config)
			throws ConfigurationException {
		logger.debug(BINDING_NAME + " updated");
		try {
			// Process device configuration
			if (config != null) {
				// Stop all existing proxy async threads
				for (InsteonHubProxy proxy : proxies.values()) {
					proxy.stop();
				}
				// Clear proxy map. It will be rebuilt.
				proxies.clear();
				
				String refreshIntervalString = (String) config.get("refresh");
				if (StringUtils.isNotBlank(refreshIntervalString)) {
					refreshInterval = Long.parseLong(refreshIntervalString);
				}
				// parse all configured receivers
				// ( insteonhub:<hubid>.host=10.0.0.2 )
				// ( insteonhub:<hubid>.user=username )
				// ( insteonhub:<hubid>.pass=password )
				Enumeration<String> keys = config.keys();
				while (keys.hasMoreElements()) {
					String key = keys.nextElement();
					if (key.endsWith(CONFIG_KEY_HOST)) {
						// parse host
						String host = (String) config.get(key);
						int separatorIdx = key.indexOf('.');
						String hubId, keyPrefix;
						if (separatorIdx == -1) {
							// no prefix/hubid => one hub => use default hub ID
							hubId = DEFAULT_HUB_ID;
							keyPrefix = "";
						} else {
							// prefix => use it as the hub ID
							hubId = key.substring(0, separatorIdx);
							keyPrefix = hubId + ".";
						}
						String user = (String) config.get(keyPrefix
								+ CONFIG_KEY_USER);
						String pass = (String) config.get(keyPrefix
								+ CONFIG_KEY_PASS);
						String portStr = (String) config.get(keyPrefix
								+ CONFIG_KEY_PORT);
						int port = StringUtils.isBlank(portStr) ? InsteonHubProxy.DEFAULT_PORT
								: Integer
										.parseInt(config.get(
												keyPrefix + CONFIG_KEY_PORT)
												.toString());
						// Create proxy, and add it to map
						InsteonHubProxy proxy = new InsteonHubProxy(host, port,
								user, pass, new AsyncEventPublisher(hubId));
						proxies.put(hubId, proxy);
						// If activated, start proxy now
						if (activated) {
							proxy.start();
						}
					}
				}
				setProperlyConfigured(true);
			}
		} catch (Throwable t) {
			logger.error("Error configuring " + getName(), t);
		}
	}

	@Override
	public synchronized void activate() {
		logger.debug(BINDING_NAME + " activated");
		activated = true;
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
	 * This class listens for updates from the InsteonHubProxy. Currently this
	 * allows arbitrary "increase/decrease" commands to report the actual value
	 * back immediately to send an update to the openhab bus. In the future this
	 * may be able to be used for asynchronous events from the hub.
	 */
	private class AsyncEventPublisher implements InsteonHubProxyListener {

		private final String hubId;

		public AsyncEventPublisher(String hubId) {
			this.hubId = hubId;
		}

		@Override
		public void onAnalogUpdate(String device, int level) {
			State update = new PercentType(levelToPercent(level));
			sendUpdate(device, update);
		}

		@Override
		public void onDigitalUpdate(String device, boolean power) {
			State update = power ? OnOffType.ON : OnOffType.OFF;
			sendUpdate(device, update);
		}

		private void sendUpdate(String device, State update) {
			InsteonHubBindingConfig config = InsteonHubBindingConfigUtil
					.getConfigForHubDevice(providers, hubId, device);
			eventPublisher.postUpdate(config.getItemName(), update);
		}
	}

}
