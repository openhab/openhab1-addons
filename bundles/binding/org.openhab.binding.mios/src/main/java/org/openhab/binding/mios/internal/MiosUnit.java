/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mios.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection properties for an MiOS Unit.
 * 
 * @author Mark Clark
 * @since 1.6.0
 */
public class MiosUnit {
	public static final String CONFIG_DEFAULT_UNIT = "default";

	/**
	 * The minimal permissible timeout to be specified in the Unit
	 * configuration. This allows us some headroom to tell the MiOS unit that we
	 * want to long-poll for less than this amount of time.
	 */
	private static final int CONFIG_MIN_TIMEOUT = 5000;
	private static final int CONFIG_DEFAULT_TIMEOUT = 60000;
	private static final int CONFIG_DEFAULT_PORT = 3480;
	private static final String CONFIG_DEFAULT_HOSTNAME = "127.0.0.1";

	private String name = null;
	private String hostname = CONFIG_DEFAULT_HOSTNAME;
	private int port = CONFIG_DEFAULT_PORT;
	private int timeout = CONFIG_DEFAULT_TIMEOUT;

	private static final Logger logger = LoggerFactory
			.getLogger(MiosUnit.class);

	/**
	 * All MiOS unit's have a name, that must be specified as the configuration
	 * is being loaded.
	 */
	public MiosUnit(String name) {
		this.name = name;
	}

	/**
	 * Get the Hostname setting for the MiOS unit configuration.
	 * 
	 * @return the Hostname/IP associated with this Unit
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * Get the Port setting for the MiOS unit configuration.
	 * 
	 * The default Port for a MiOS Unit is 3480, but this can be overridden in
	 * config as needed.
	 * 
	 * @return the Port associated with the MiOS Unit.
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Get the Timeout setting for the MiOS Unit configuration.
	 * 
	 * If this configuration is not specified, then it will default to 60000ms.
	 * 
	 * @return the Timeout of the MiOS Unit, in milliseconds.
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * Set the Hostname of the MiOS Unit configuration.
	 * 
	 * @param hostname
	 *            the hostname to use for this MiOS Unit configuration.
	 */
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	/**
	 * Set the Port of the MiOS Unit configuration.
	 * 
	 * @param port
	 *            the port to use for this MiOS Unit configuration.
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Set the Timeout of the MiOS Unit configuration.
	 * 
	 * @param timeout
	 *            the timeout to set for any connections associated with this
	 *            MiOS Unit.
	 */
	public void setTimeout(int timeout) {
		if (timeout < CONFIG_MIN_TIMEOUT) {
			timeout = CONFIG_MIN_TIMEOUT;
			logger.warn("Timeout of {} below minimum permitted, {} used.",
					timeout, CONFIG_MIN_TIMEOUT);
		}
		this.timeout = timeout;
	}

	/**
	 * Get the name of the MiOS Unit configuration.
	 * 
	 * Each MiOS Unit has a name within the configuration properties. If it's
	 * not named, then the "default" name is "default".
	 * 
	 * Otherwise, the name is that specified in the openHAB configuration.
	 * 
	 * eg. <tt>mios:venice.hostname=venice.myhouse.example.com</tt>
	 * 
	 * In this case, the MiOS Unit name is "<tt>venice</tt>".
	 * 
	 * @return the name of this MiOS Unit.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Provide any unit-specific prefix to a given property name.
	 * 
	 * @param property an unformatted property name.
	 * @return the property name, with the prefix for this MiOS Unit.
	 */
	public String formatProperty(String property) {
		if (isDefaultUnit()) {
			return property;
		} else {
			return "unit:" + getName() + ',' + property;
		}
	}

	public boolean isDefaultUnit() {
		return (getName() == null);
	}
}
