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

/**
 * Manage GPIO binding configuration.
 * 
 * @author Dancho Penev
 * @since 1.3.1
 */
public class GPIOGenericBindingProvider extends AbstractGenericBindingProvider implements GPIOBindingProvider {

	public String getBindingType() {
		return "gpio";
	}

	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {

		/* Only 'Switch' and 'Contact' types are allowed */
		if (!((item instanceof SwitchItem) || (item instanceof ContactItem))) {
			throw new BindingConfigParseException("Item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName() +
					"' while only 'Switch' or 'Contact' types are allowed");
		}
	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {

		GPIOPinBindingConfig config = new GPIOPinBindingConfig();

		String[] properties = bindingConfig.split(",");

		/* Not sure when must be called, other bindings seems to call it at the beginning so do we */
		super.processBindingConfiguration(context, item, bindingConfig);

		switch (properties.length) {
		case 2:
			/* Optional activelow is specified */
			if (properties[1].trim().compareToIgnoreCase("activelow") == 0) {
				config.activeLow = GPIOPin.ACTIVELOW_ENABLED;
			} else {
				throw new BindingConfigParseException("Unsupported value for activelow (" + properties[1].trim() + ")");
			}
		case 1:
			try {
				config.pinNumber = Integer.parseInt(properties[0].trim());
			} catch (NumberFormatException e) {
				throw new BindingConfigParseException("Unsupported, not numeric value for pin number (" + properties[0].trim() + ")");
			}
			break;
		default:
			throw new BindingConfigParseException("Unsupported number of configuration items (" + properties.length + ")");
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
		public int pinNumber;

		/** Configured activelow state */
		public int activeLow = GPIOPin.ACTIVELOW_DISABLED;

		/** Pin direction. If item type is <code>Switch</code> the pin
		 * direction is out, if <code>Contact</code> - in
		 */
		public int direction;
	}
}
