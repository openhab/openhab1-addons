/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.anel.internal.AnelBinding.IInternalAnelBinding;
import org.osgi.service.cm.ConfigurationException;

/**
 * Reads the configuration and initializes connector threads for them.
 * 
 * @since 1.6.0
 * @author koenemann
 */
public class AnelConfigReader {

	/**
	 * Refresh rate with which the state is regularly updated.
	 */
	private final static long DEFAULT_REFRESH_INTERVAL = 60000;

	/**
	 * If cache period is set to a positive integer, then this specifies the
	 * amount of minutes the switch states are cached. During that time, only
	 * changes are reported to the event bus. E.g. when set to 60, the cache is
	 * cleared once per hour.
	 */
	private final static long DEFAULT_CACHE_PERIOD = 0;

	/**
	 * Config data initialized with default values.
	 */
	private static class AnelConfig {
		String user = "user7";
		String password = "anel";
		String host = "net-control";
		int sendPort = 75;
		int receivePort = 77;
	}

	private final static Pattern CONFIG_PATTERN = Pattern.compile(
			"^(.+?)\\.(host|user|password|udpReceivePort|udpSendPort)$", Pattern.CASE_INSENSITIVE);

	/**
	 * Read configuration for the Anel binding by creating the connector threads
	 * (they are not yet started) and returning the refresh rate and cache
	 * period.
	 * 
	 * @param config
	 *            The configuration passed from the core.
	 * @param threads
	 *            A map of threads that will be filled by this call.
	 * @param bindingFacade
	 *            The binding facade that is needed by the connector threads.
	 * @return The refresh rate.
	 */
	static long readConfig(Dictionary<String, ?> config, Map<String, AnelConnectorThread> threads,
			IInternalAnelBinding bindingFacade) throws ConfigurationException {
		if (config == null || config.isEmpty())
			return 0;

		long cachePeriod = DEFAULT_CACHE_PERIOD;
		long refresh = DEFAULT_REFRESH_INTERVAL;
		final Map<String, AnelConfig> anelConfigs = new HashMap<String, AnelConfig>();

		for (final Enumeration<String> e = config.keys(); e.hasMoreElements();) {
			final String key = e.nextElement();
			final String value = (String) config.get(key);

			// skip empty values
			if (value == null || value.trim().isEmpty())
				continue;

			// skip keys that we don't want to process here ...
			if ("service.pid".equals(key))
				continue;

			try {
				// refresh is global setting, get value and continue
				if ("refresh".equalsIgnoreCase(key)) {
					refresh = Long.parseLong(((String) config.get(key)).trim());
					continue;
				}

				// cache period is global setting, get value and continue
				if ("cachePeriod".equalsIgnoreCase(key)) {
					cachePeriod = Integer.parseInt(((String) config.get(key)).trim());
					continue;
				}

				// check for config keys
				final Matcher matcher = CONFIG_PATTERN.matcher(key);
				if (matcher.matches()) {
					final String device = matcher.group(1);
					final String property = matcher.group(2);

					// device config
					AnelConfig anelConfig = anelConfigs.get(device);
					if (anelConfig == null) {
						anelConfig = new AnelConfig();
						anelConfigs.put(device, anelConfig);
					}

					// dispatch individual properties
					if ("host".equalsIgnoreCase(property)) {
						anelConfig.host = value.trim();
					} else if ("user".equalsIgnoreCase(property)) {
						anelConfig.user = value.trim();
					} else if ("password".equalsIgnoreCase(property)) {
						anelConfig.password = value.trim();
					} else if ("udpSendPort".equalsIgnoreCase(property)) {
						anelConfig.sendPort = Integer.parseInt(value.trim());
					} else if ("udpReceivePort".equalsIgnoreCase(property)) {
						anelConfig.receivePort = Integer.parseInt(value.trim());
					}
				} else {
					throw new ConfigurationException(key, "Invalid config key");
				}
			} catch (NumberFormatException ex) {
				throw new ConfigurationException(key, "Invalid value: '" + value + "'");
			}
		}

		// we collected all configs, now let's create the actual threads
		for (String device : anelConfigs.keySet()) {
			final AnelConfig anelConfig = anelConfigs.get(device);
			final AnelConnectorThread thread = new AnelConnectorThread(anelConfig.host, anelConfig.receivePort,
					anelConfig.sendPort, anelConfig.user, anelConfig.password, bindingFacade, cachePeriod);
			threads.put(device, thread);
		}
		return refresh;
	}

}
