/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.samsungtv.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.samsungtv.SamsungTvBindingProvider;

import org.openhab.core.binding.AbstractBinding;
import org.openhab.core.types.Command;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * Binding listening OpenHAB bus and send commands to Samsung devices when
 * command is received.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class SamsungTvBinding extends AbstractBinding<SamsungTvBindingProvider> implements ManagedService {

	private static final Logger logger = 
		LoggerFactory.getLogger(SamsungTvBinding.class);

	/** RegEx to validate a config <code>'^(.*?)\\.(host|port)$'</code> */
	private static final Pattern EXTRACT_TV_CONFIG_PATTERN = 
		Pattern	.compile("^(.*?)\\.(host|port)$");
	
	private final static int DEFAULT_TV_PORT = 55000;

	protected Map<String, DeviceConfig> deviceConfigCache = new HashMap<String, DeviceConfig>();
	
	
	public SamsungTvBinding() {
	}

	public void activate() {
	}

	public void deactivate() {
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	protected void internalReceiveCommand(String itemName, Command command) {

		if (itemName != null) {
			SamsungTvBindingProvider provider = 
				findFirstMatchingBindingProvider(itemName, command.toString());

			if (provider == null) {
				logger.warn("Doesn't find matching binding provider [itemName={}, command={}]", itemName, command);
				return;
			}

			logger.debug(
					"Received command (item='{}', state='{}', class='{}')",
					new Object[] { itemName, command.toString(),
							command.getClass().toString() });

			String tmp = provider.getTVCommand(itemName, command.toString());

			String[] commandParts = tmp.split(":");
			String deviceId = commandParts[0];
			String cmd = commandParts[1];

			logger.debug("Get connection details for device id '{}'", deviceId);
			
			DeviceConfig tvConfig = deviceConfigCache.get(deviceId);

			if (tvConfig != null) {
				SamsungTvConnection remoteController = tvConfig.getConnection();

				if (remoteController != null) {
					remoteController.send(cmd);
				}
				
			} else {
				logger.warn("Cannot find connection details for device id '{}'", deviceId);
			}
		}
	}

	/**
	 * Find the first matching {@link SamsungTvBindingProvider} according to
	 * <code>itemName</code>.
	 * 
	 * @param itemName
	 * 
	 * @return the matching binding provider or <code>null</code> if no binding
	 *         provider could be found
	 */
	private SamsungTvBindingProvider findFirstMatchingBindingProvider(String itemName, String command) {
		
		SamsungTvBindingProvider firstMatchingProvider = null;

		for (SamsungTvBindingProvider provider : this.providers) {

			String tmp = provider.getTVCommand(itemName, command.toString());

			if (tmp != null) {
				firstMatchingProvider = provider;
				break;
			}
		}

		return firstMatchingProvider;
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void updated(Dictionary<String, ?> config) throws ConfigurationException {

		if (config != null) {
			Enumeration<String> keys = config.keys();
			while (keys.hasMoreElements()) {
				String key = (String) keys.nextElement();

				// the config-key enumeration contains additional keys that we
				// don't want to process here ...
				if ("service.pid".equals(key)) {
					continue;
				}

				Matcher matcher = EXTRACT_TV_CONFIG_PATTERN.matcher(key);

				if (!matcher.matches()) {
					logger.debug("given config key '"
							+ key
							+ "' does not follow the expected pattern '<id>.<host|port>'");
					continue;
				}

				matcher.reset();
				matcher.find();

				String deviceId = matcher.group(1);

				DeviceConfig deviceConfig = deviceConfigCache.get(deviceId);

				if (deviceConfig == null) {
					deviceConfig = new DeviceConfig(deviceId);
					deviceConfigCache.put(deviceId, deviceConfig);
				}

				String configKey = matcher.group(2);
				String value = (String) config.get(key);

				if ("host".equals(configKey)) {
					deviceConfig.host = value;
				} else if ("port".equals(configKey)) {
					deviceConfig.port = Integer.valueOf(value);
				} else {
					throw new ConfigurationException(configKey,
						"the given configKey '" + configKey + "' is unknown");
				}
			}
		}
	}

	/**
	 * Internal data structure which carries the connection details of one
	 * device (there could be several)
	 */
	static class DeviceConfig {

		String host;
		int port = DEFAULT_TV_PORT;

		SamsungTvConnection connection = null;
		String deviceId;

		public DeviceConfig(String deviceId) {
			this.deviceId = deviceId;
		}

		@Override
		public String toString() {
			return "Device [id=" + deviceId + ", host=" + host + ", port=" + port + "]";
		}

		SamsungTvConnection getConnection() {
			if (connection == null) {
				connection = new SamsungTvConnection(host, port);
			}
			return connection;
		}

	}

}
