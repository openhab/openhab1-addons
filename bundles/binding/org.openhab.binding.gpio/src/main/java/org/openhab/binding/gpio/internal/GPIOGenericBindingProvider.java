/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-${year}, openHAB.org <admin@openhab.org>
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


package org.openhab.binding.gpio.internal;

import org.openhab.binding.gpio.GPIOBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.io.gpio.GPIOPin;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage GPIO binding configuration.
 * 
 * @author Dancho Penev
 * @since 1.3.1
 */
public class GPIOGenericBindingProvider extends AbstractGenericBindingProvider implements GPIOBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(GPIOGenericBindingProvider.class);

	public String getBindingType() {
		return "gpio";
	}

	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {

		/* Only 'Switch' and 'Contact' types are allowed */
		if (!((item instanceof SwitchItem) || (item instanceof ContactItem))) {
			logger.error("Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
					"' while only 'Switch' or 'Contact' types are allowed");
			throw new BindingConfigParseException("Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
					"' while only 'Switch' or 'Contact' types are allowed");
		}
	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		/* Not sure when must be called, other bindings seems to call it at the beginning */
		super.processBindingConfiguration(context, item, bindingConfig);

		GPIOPinBindingConfig config = new GPIOPinBindingConfig();

		/* Configuration string should be in the form "pin:NUMBER [debounse:NUMBER] [activelow:yes|no]" */
		String[] properties = bindingConfig.split(" ");

		if (properties.length > 3) {
			logger.error("Wrong number of agruments (" + properties.length + ") in configuration string '" + bindingConfig + "'");
			throw new BindingConfigParseException("Wrong number of agruments (" + properties.length + ") in configuration string '"
					+ bindingConfig + "'");
		}

		for (String property : properties) {

			String[] keyValueStructure = property.split(":");

			if (keyValueStructure.length != 2) {
				logger.error("Incorrect key:value structure (" + property + ") in configuration string '" + bindingConfig + "'");
				throw new BindingConfigParseException("Incorrect key:value structure (" + property + ") in configuration string '"
						+ bindingConfig + "'");
			}

			String key = keyValueStructure[0];
			String value = keyValueStructure[1];

			if (key.compareToIgnoreCase("pin") == 0) {
				try {
					config.pinNumber = Integer.parseInt(value);
					if (config.pinNumber < 0) {
						logger.error("Unsupported, negative value for pin number (" + value + ") in configuration string '" + bindingConfig + "'");
						throw new BindingConfigParseException("Unsupported, negative value for pin number (" + value + ") in configuration string '"
								+ bindingConfig + "'");
					}
				} catch (NumberFormatException e) {
					logger.error("Unsupported, not numeric value for pin number (" + value + ") in configuration string '" + bindingConfig + "'");
					throw new BindingConfigParseException("Unsupported, not numeric value for pin number (" + value + ") in configuration string '"
							+ bindingConfig + "'");
				}
			} else if (key.compareToIgnoreCase("debounce") == 0) {
				try {
					config.debounceInterval = Long.parseLong(value);
					if (config.debounceInterval < 0) {
						logger.error("Unsupported, negative value for debounce (" + value + ") in configuration string '" + bindingConfig + "'");
						throw new BindingConfigParseException("Unsupported, negative value for debounce (" + value + ") in configuration string '"
								+ bindingConfig + "'");
					}
				} catch (NumberFormatException e) {
					logger.error("Unsupported, not numeric value for debounce (" + value + ") in configuration string '" + bindingConfig + "'");
					throw new BindingConfigParseException("Unsupported, not numeric value for debounce (" + value + ") in configuration string '"
							+ bindingConfig + "'");
				}
			} else if (key.compareToIgnoreCase("activelow") == 0) {
				if (value.compareToIgnoreCase("yes") == 0) {
					config.activeLow = GPIOPin.ACTIVELOW_ENABLED;
				} else if (value.compareToIgnoreCase("no") != 0) {
					logger.error("Unsupported value for activelow (" + value + ") in configuration string '" + bindingConfig + "'");
					throw new BindingConfigParseException("Unsupported value for activelow (" + value + ") in configuration string '"
							+ bindingConfig + "'");
				}
			} else {
				logger.error("Unsupported key (" + key + ") in configuration string '" + bindingConfig + "'");
				throw new BindingConfigParseException("Unsupported key (" + key + ") in configuration string '" + bindingConfig + "'");
			}
		}
		
		/* Pin number wasn't configured */
		if (config.pinNumber == GPIOBindingProvider.PINNUMBER_UNDEFINED) {
			logger.error("Mandatory paratemer (pin) is missing in configuration string '" + bindingConfig + "'");
			throw new BindingConfigParseException("Mandatory paratemer (pin) is missing in configuration string '" + bindingConfig + "'");
		}

		if (item instanceof ContactItem) {
			config.direction = GPIOPin.DIRECTION_IN;
		} else {
			/* Item type 'Switch' */
			config.direction = GPIOPin.DIRECTION_OUT;
		}

		addBindingConfig(item, config);
	}

	public int getPinNumber(String itemName) {
		
		GPIOPinBindingConfig config = (GPIOPinBindingConfig) bindingConfigs.get(itemName);

		if (config == null) {
			throw new IllegalArgumentException("The item name '" + itemName + "'is invalid or the item isn't configured");
		}

		return config.pinNumber; 
	}

	public long getDebounceInterval(String itemName) {
		
		GPIOPinBindingConfig config = (GPIOPinBindingConfig) bindingConfigs.get(itemName);

		if (config == null) {
			throw new IllegalArgumentException("The item name '" + itemName + "'is invalid or the item isn't configured");
		}

		return config.debounceInterval; 
	}

	public int getActiveLow(String itemName) {

		GPIOPinBindingConfig config = (GPIOPinBindingConfig) bindingConfigs.get(itemName);

		if (config == null) {
			throw new IllegalArgumentException("The item name '" + itemName + "'is invalid or the item isn't configured");
		}

		return config.activeLow; 
	}

	public int getDirection(String itemName) {

		GPIOPinBindingConfig config = (GPIOPinBindingConfig) bindingConfigs.get(itemName);

		if (config == null) {
			throw new IllegalArgumentException("The item name '" + itemName + "'is invalid or the item isn't configured");
		}

		return config.direction; 
	}

	public boolean isItemConfigured(String itemName) {

		if (bindingConfigs.containsKey(itemName)) {
			return true;
		}

		return false;
	}

	/**
	 * GPIO binding configuration data structure.
	 * 
	 * @author Dancho Penev
	 * @since 1.3.1
	 */
	public class GPIOPinBindingConfig implements BindingConfig {

		/** Configured pin number */
		public int pinNumber = GPIOBindingProvider.PINNUMBER_UNDEFINED;

		/** Configured pin debounce interval in milliseconds */
		public long debounceInterval = GPIOBindingProvider.DEBOUNCEINTERVAL_UNDEFINED;

		/** Configured activelow state */
		public int activeLow = GPIOPin.ACTIVELOW_DISABLED;

		/** Pin direction. If item type is <code>Switch</code> the pin
		 * direction is out, if <code>Contact</code> - in
		 */
		public int direction;
	}
}
