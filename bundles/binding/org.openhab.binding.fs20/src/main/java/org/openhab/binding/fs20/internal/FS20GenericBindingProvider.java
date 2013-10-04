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
package org.openhab.binding.fs20.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.fs20.FS20BindingConfig;
import org.openhab.binding.fs20.FS20BindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class FS20GenericBindingProvider extends AbstractGenericBindingProvider
		implements FS20BindingProvider {

	private Map<String, FS20BindingConfig> addressMap = new HashMap<String, FS20BindingConfig>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "fs20";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		}

		if (bindingConfig.length() != 6) {
			throw new BindingConfigParseException(
					"The configured address must consist of 2 bytes housecode and 1 byte device address");
		}
	}

	/**
	 * Binding config is in the style of {fs20="HHHHAA"} {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		FS20BindingConfig config = new FS20BindingConfig(bindingConfig, item);

		// parse bindingconfig here ...
		addressMap.put(config.getAddress(), config);
		addBindingConfig(item, config);
	}

	@Override
	public FS20BindingConfig getConfigForItemName(String itemName) {
		if (super.bindingConfigs.containsKey(itemName)) {
			return (FS20BindingConfig) super.bindingConfigs.get(itemName);
		}
		return null;
	}

	@Override
	public FS20BindingConfig getConfigForAddress(String address) {
		return addressMap.get(address);
	}

}
