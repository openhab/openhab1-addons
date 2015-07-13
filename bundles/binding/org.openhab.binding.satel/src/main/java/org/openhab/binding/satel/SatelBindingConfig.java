/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openhab.binding.satel.internal.event.SatelEvent;
import org.openhab.binding.satel.internal.protocol.SatelMessage;
import org.openhab.binding.satel.internal.types.IntegraType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * Base class that all Satel configuration classes must extend. Provides methods
 * to convert data between openHAB and Satel module.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public abstract class SatelBindingConfig implements BindingConfig {

	public enum Options {
		COMMANDS_ONLY, FORCE_ARM, INVERT_STATE
	}

	private static final DecimalType DECIMAL_ONE = new DecimalType(1);

	private Map<String, String> options;

	/**
	 * Checks whether given option is set to <code>true</code>.
	 * 
	 * @param option option to check
	 * @return <code>true</code> if option is enabled
	 */
	public boolean hasOptionEnabled(Options option) {
		return Boolean.parseBoolean(getOption(option));
	}

	/**
	 * Returns value of given option.
	 * 
	 * @param option option to get value for
	 * @return string value or <code>null</code> if option is not present
	 */
	public String getOption(Options option) {
		return this.options.get(option.name());
	}

	/**
	 * Returns string representation of option map.
	 * 
	 * @return string as pairs of [name]=[value] separated by comma
	 */
	public String optionsAsString() {
		return this.options.toString();
	}

	/**
	 * Converts data from {@link SatelEvent} to openHAB state of specified item.
	 * 
	 * @param item
	 *            an item to get new state for
	 * @param event
	 *            incoming event
	 * @return new item state
	 */
	public abstract State convertEventToState(Item item, SatelEvent event);

	/**
	 * Converts openHAB command to proper Satel message that changes state of
	 * bound object (output, zone).
	 * 
	 * @param command
	 *            command to convert
	 * @param integraType
	 *            type of connected Integra
	 * @param userCode
	 *            user's password
	 * @return a message to send
	 */
	public abstract SatelMessage convertCommandToMessage(Command command, IntegraType integraType, String userCode);

	/**
	 * Returns message needed to get current state of bound object.
	 * 
	 * @param integraType
	 *            type of connected Integra
	 * @return a message to send
	 */
	public abstract SatelMessage buildRefreshMessage(IntegraType integraType);

	protected SatelBindingConfig(Map<String, String> options) {
		this.options = options;
	}

	/**
	 * Helper class to iterate over elements of binding configuration.
	 */
	protected static class ConfigIterator implements Iterator<String> {
		private String bindingConfig;
		private String[] configElements;
		private int idx;

		public ConfigIterator(String bindingConfig) {
			this.bindingConfig = bindingConfig;
			this.configElements = bindingConfig.split(":");
			this.idx = 0;
		}

		public String getBindingConfig() {
			return this.bindingConfig;
		}

		public String nextUpperCase() {
			return next().toUpperCase();
		}

		public <T extends Enum<T>> T nextOfType(Class<T> enumType, String description)
				throws BindingConfigParseException {
			try {
				return Enum.valueOf(enumType, next().toUpperCase());
			} catch (Exception e) {
				throw new BindingConfigParseException(String.format("Invalid %s: %s", description, this.bindingConfig));
			}
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

	/**
	 * Parses binding configuration options. This must be the last element of
	 * the configuration.
	 * 
	 * @param iterator
	 *            config iterator
	 * @return parsed options as a map
	 * @throws BindingConfigParseException
	 *             in case there are more elements after options
	 */
	protected static Map<String, String> parseOptions(ConfigIterator iterator) throws BindingConfigParseException {
		// parse options: comma separated pairs of <name>=<value>
		Map<String, String> options = new HashMap<String, String>();

		if (iterator.hasNext()) {

			for (String option : iterator.next().split(",")) {
				if (option.contains("=")) {
					String[] keyVal = option.split("=", 2);
					options.put(keyVal[0].toUpperCase(), keyVal[1]);
				} else {
					options.put(option.toUpperCase(), "true");
				}
			}

			if (iterator.hasNext()) {
				// options are always the last element
				// if anything left, throw exception
				throw new BindingConfigParseException(String.format("Too many elements: %s",
						iterator.getBindingConfig()));
			}
		}
		return options;
	}

	protected State booleanToState(Item item, boolean value) {
		if (item instanceof ContactItem) {
			return value ? OpenClosedType.OPEN : OpenClosedType.CLOSED;
		} else if (item instanceof SwitchItem) {
			return value ? OnOffType.ON : OnOffType.OFF;
		} else if (item instanceof NumberItem) {
			return value ? DECIMAL_ONE : DecimalType.ZERO;
		}

		return null;
	}
}
