/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Helper class to iterate over elements of binding configuration.
 * 
 * @author Krzysztof Goworek
 * @since 1.8.0
 */
class ConfigIterator implements Iterator<String> {

	private String bindingConfig;
	private String[] configElements;
	private int idx;

	/**
	 * Construct iterator object for given configuration string.
	 * 
	 * @param bindingConfig
	 *            configuration to iterate over
	 */
	public ConfigIterator(String bindingConfig) {
		this.bindingConfig = bindingConfig;
		this.configElements = bindingConfig.split(":");
		this.idx = 0;
	}

	/**
	 * Returns original configuration string.
	 * 
	 * @return configuration string
	 */
	public String getBindingConfig() {
		return this.bindingConfig;
	}

	/**
	 * Return an Enum for next configuration element.
	 * 
	 * @param enumType
	 *            type of the enum
	 * @param description
	 *            description of enum for error message
	 * @return parsed value of enum type
	 * @throws BindingConfigParseException
	 *             in case next element cannot be parsed as given enumeration
	 */
	public <T extends Enum<T>> T nextOfType(Class<T> enumType, String description) throws BindingConfigParseException {
		try {
			return Enum.valueOf(enumType, next().toUpperCase());
		} catch (Exception e) {
			throw new BindingConfigParseException(String.format("Invalid %s: %s", description, getBindingConfig()));
		}
	}

	/**
	 * Parses binding configuration options. This must be the last element of
	 * the configuration.
	 * 
	 * @return parsed options as a map
	 * @throws BindingConfigParseException
	 *             in case there are more elements after options
	 */
	public Map<String, String> parseOptions() throws BindingConfigParseException {
		// parse options: comma separated pairs of <name>=<value>
		Map<String, String> options = new HashMap<String, String>();

		if (hasNext()) {

			for (String option : next().split(",")) {
				if (option.contains("=")) {
					String[] keyVal = option.split("=", 2);
					options.put(keyVal[0].toUpperCase(), keyVal[1]);
				} else {
					options.put(option.toUpperCase(), "true");
				}
			}

			if (hasNext()) {
				// options are always the last element
				// if anything left, throw exception
				throw new BindingConfigParseException(String.format("Too many elements: %s", getBindingConfig()));
			}
		}
		return options;
	}

	@Override
	public boolean hasNext() {
		return idx < this.configElements.length;
	}

	@Override
	public String next() {
		return this.configElements[idx++];
	}

	@Override
	public void remove() {
		// ignore
	}
}
