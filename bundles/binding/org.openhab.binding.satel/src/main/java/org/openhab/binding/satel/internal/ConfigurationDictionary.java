/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal;

import java.util.Dictionary;

import org.apache.commons.lang.StringUtils;
import org.osgi.service.cm.ConfigurationException;

/**
 * Helper class for getting binding configuration values.
 * 
 * @author Krzysztof Goworek
 * @since 1.8.0
 */
public class ConfigurationDictionary {

	private Dictionary<String, ?> configuration;

	/**
	 * Construct class basing on given binding configuration.
	 * 
	 * @param configuration
	 *            dictionary with binding configuration
	 */
	public ConfigurationDictionary(Dictionary<String, ?> configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns string value for configuration parameter with given name.
	 * 
	 * @param name
	 *            name of the configuration parameter
	 * @return string value or <code>null</code> in case parameter with this
	 *         name is absent
	 */
	public String getString(String name) {
		return getString(name, null);
	}

	/**
	 * Returns string value for configuration parameter with given name or given
	 * default value if the parameter is not defined.
	 * 
	 * @param name
	 *            name of the configuration parameter
	 * @param defaultValue
	 *            default value for this parameter
	 * @return string value or <code>defaultValue</code> in case parameter with
	 *         this name is absent
	 */
	public String getString(String name, String defaultValue) {
		String val = (String) configuration.get(name);
		if (StringUtils.isNotBlank(val)) {
			return val;
		} else {
			return defaultValue;
		}
	}

	/**
	 * Returns integer value for configuration parameter with given name or
	 * given default value if the parameter is not defined.
	 * 
	 * @param name
	 *            name of the configuration parameter
	 * @param defaultValue
	 *            default value for this parameter
	 * @return integer value or <code>defaultValue</code> in case parameter with
	 *         this name is absent
	 * @throws ConfigurationException
	 *             in case value cannot be parsed as an integer
	 */
	public int getInt(String name, int defaultValue) throws ConfigurationException {
		String val = (String) configuration.get(name);
		try {
			if (StringUtils.isNotBlank(val)) {
				return Integer.parseInt(val);
			} else {
				return defaultValue;
			}
		} catch (Exception e) {
			throw new ConfigurationException(name, "invalid integer value");
		}
	}

	/**
	 * Returns long value for configuration parameter with given name or
	 * given default value if the parameter is not defined.
	 * 
	 * @param name
	 *            name of the configuration parameter
	 * @param defaultValue
	 *            default value for this parameter
	 * @return long value or <code>defaultValue</code> in case parameter with
	 *         this name is absent
	 * @throws ConfigurationException
	 *             in case value cannot be parsed as a long
	 */
	public long getLong(String name, long defaultValue) throws ConfigurationException {
		String val = (String) configuration.get(name);
		try {
			if (StringUtils.isNotBlank(val)) {
				return Long.parseLong(val);
			} else {
				return defaultValue;
			}
		} catch (Exception e) {
			throw new ConfigurationException(name, "invalid long value");
		}
	}
}
