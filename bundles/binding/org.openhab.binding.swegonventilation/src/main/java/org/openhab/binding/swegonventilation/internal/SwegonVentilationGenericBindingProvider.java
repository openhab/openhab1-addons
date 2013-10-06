/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.swegonventilation.internal;

import java.io.InvalidClassException;

import org.openhab.binding.swegonventilation.SwegonVentilationBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Swegon ventilation binding information from it.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>swegonventilation="OutdoorTemperature"</code></li>
 * <li><code>swegonventilation="T2"</code></li>
 * <li><code>swegonventilation="SupplyAirFanSpeed"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationGenericBindingProvider extends
		AbstractGenericBindingProvider implements
		SwegonVentilationBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "swegonventilation";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems or SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		SwegonVentilationBindingConfig config = new SwegonVentilationBindingConfig();

		config.itemType = item.getClass();
		String commandType = bindingConfig.trim();

		try {
			SwegonVentilationCommandType.validateBinding(commandType,
					config.itemType);
			config.commandType = SwegonVentilationCommandType
					.getCommandType(commandType);
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("'" + commandType
					+ "' is not a valid command type");
		} catch (InvalidClassException e) {
			throw new BindingConfigParseException(
					"Not valid class for command type '" + commandType + "'");
		}

		addBindingConfig(item, config);
	}

	class SwegonVentilationBindingConfig implements BindingConfig {
		public Class<? extends Item> itemType = null;
		public SwegonVentilationCommandType commandType;
		
		@Override
		public String toString() {
			return "SwegonVentilationBindingConfig ["
					+ ", itemType=" + itemType
					+ ", commandType=" + commandType
					+ "]";
		}


	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		SwegonVentilationBindingConfig config = (SwegonVentilationBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public SwegonVentilationCommandType getCommandType(String itemName) {
		SwegonVentilationBindingConfig config = (SwegonVentilationBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.commandType : null;
	}

}
