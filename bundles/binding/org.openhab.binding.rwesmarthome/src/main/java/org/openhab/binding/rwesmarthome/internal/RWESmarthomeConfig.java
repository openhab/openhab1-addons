/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.osgi.service.cm.ConfigurationException;

/**
 * Parses the config in openhab.cfg.
 * <pre>
 * ############################## RWE Smarthome Binding ##############################
 * #
 * # Hostname / IP address of the RWE Smarthome server
 * rwesmarthome:host=
 *
 * # Username / password of the RWE Smarthome server
 * rwesmarthome:username=
 * rwesmarthome:password=
 * 
 * # The interval in milliseconds to poll the RWE Smarthome server for changes. (optional, default is 2000)
 * # Too low values may cause errors, too high values will lead to longer delays, until updates are seen
 * # in OpenHAB. Should not be smaller than 1000.
 * # rwesmarthome:poll.interval=2000
 * 
 * # The interval in seconds to check if the communication with the RWE Smarthome server is still alive.
 * # If no message receives from the RWE Smarthome server, the binding restarts. (optional, default is 300)
 * # rwesmarthome:alive.interval=300
 * 
 * # The interval in seconds to wait after the binding configuration has changed before the device states
 * # will be reloaded from the RWE SHC. (optional, default is 15)
 * # rwesmarthome:binding.changed.interval=15
 * </pre>
 * 
 * @author ollie-dev
 * @since 1.8.0
 */
public class RWESmarthomeConfig {
	private static final String CONFIG_KEY_RWESMARTHOME_HOST = "host";
	private static final String CONFIG_KEY_RWESMARTHOME_USERNAME = "username";
	private static final String CONFIG_KEY_RWESMARTHOME_PASSWORD = "password";
	private static final String CONFIG_KEY_ALIVE_INTERVAL = "alive.interval";
	private static final String CONFIG_KEY_BINDING_CHANGED_INTERVAL = "binding.changed.interval";

	private static final int DEFAULT_ALIVE_INTERVAL = 300;			// 5min
	private static final int DEFAULT_BINDING_CHANGED_INTERVAL = 15; // 15s

	private boolean valid;
	private String host;
	private String username;
	private String password;
	private Integer aliveInterval;
	private Integer bindingChangedInterval;

	/**
	 * Parses and validates the properties in the openhab.cfg.
	 */
	public void parse(Map<String, Object> configuration) throws ConfigurationException {
		valid = false;

		host = (String) configuration.get(CONFIG_KEY_RWESMARTHOME_HOST);
		if (StringUtils.isBlank(host)) {
			throw new ConfigurationException("rwesmarthome",
					"Parameter host is mandatory and must be configured. Please check your openhab.cfg!");
		}
		
		username = (String) configuration.get(CONFIG_KEY_RWESMARTHOME_USERNAME);
		if (StringUtils.isBlank(username)) {
			throw new ConfigurationException("rwesmarthome", "Parameter username is mandatory and must be configured. Please check your openhab.cfg!");
		}
		
		password = (String) configuration.get(CONFIG_KEY_RWESMARTHOME_PASSWORD);
		if (StringUtils.isBlank(password)) {
			throw new ConfigurationException("rwesmarthome", "Parameter password is mandatory and must be configured. Please check your openhab.cfg!");
		}

		aliveInterval = parseInt(configuration, CONFIG_KEY_ALIVE_INTERVAL, DEFAULT_ALIVE_INTERVAL);
		bindingChangedInterval = parseInt(configuration, CONFIG_KEY_BINDING_CHANGED_INTERVAL, DEFAULT_BINDING_CHANGED_INTERVAL);

		valid = true;
	}

	/**
	 * Parses a integer property.
	 */
	private Integer parseInt(Map<String, Object> configuration, String key, Integer defaultValue)
			throws ConfigurationException {
		String value = (String) configuration.get(key);
		if (StringUtils.isNotBlank(value)) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException ex) {
				throw new ConfigurationException("rwesmarthome", "Parameter " + key
						+ " in wrong format. Please check your openhab.cfg!");
			}

		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns the RWESmarthome server host.
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Returns the alive interval.
	 */
	public Integer getAliveInterval() {
		return aliveInterval;
	}
	
	/**
	 * Returns the binding-changed-interval.
	 * 
	 * @returns the bidningChangedInterval
	 */
	public long getBindingChangedInterval() {
		return bindingChangedInterval;
	}


	
	/**
	 * Returns the username.
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Returns the password.
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Returns true if this config is valid.
	 */
	public boolean isValid() {
		return valid;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
				.append("host", host)
				.append("username", username)
				.append("password", "*****")
				.append("aliveInterval", aliveInterval)
				.append("bindingChangedInterval", bindingChangedInterval).toString();
	}

}

