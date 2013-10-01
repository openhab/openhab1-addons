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
package org.openhab.binding.maxcube.internal;

import org.openhab.binding.maxcube.MaxCubeBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * MAX!Cube binding information from it. It registers as a
 * {@link MaxCubeBindingProvider} service as well.
 * </p>
 * 
 * <p>
 * Example for a valid binding configuration strings:
 * <code>{ maxcube="JEQ0304492" }</code> - returns the corrseponding value of the default attribute based on the MAX device type
 * 
 * @author Andreas Heil
 * 
 * @since 1.4.0
 */
public class MaxCubeGenericBindingProvider extends
		AbstractGenericBindingProvider implements MaxCubeBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "maxcube";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof DimmerItem || item instanceof ContactItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Numbers, Dimmer- and ContactItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length < 1) {
			throw new BindingConfigParseException(
					"MAX!Cube configuration requires at least serial number for a MAX!Cube device.");
		}

		MaxCubeBindingConfig config = new MaxCubeBindingConfig();

		item.getName();
		
		config.serialNumber = configParts[0];

		addBindingConfig(item, config);
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the MAX!Cube binding
	 * provider.
	 */
	static private class MaxCubeBindingConfig implements BindingConfig {
		public String serialNumber;
	}

/**
 * Return the serial number for the given <code>itemName</code>.	
 *
 * @param itemName
 * 			the itemName to return the corresponding MAX serial number
 */
	@Override
	public String getSerialNumber(String itemName) {
		MaxCubeBindingConfig config = (MaxCubeBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.serialNumber : null;
	}
}
