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
package org.openhab.binding.nibeheatpump.internal;

import org.openhab.binding.nibeheatpump.NibeHeatPumpBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * <p>
 * This class can parse information from the generic binding format and provides
 * Nibe heat pump binding information from it.
 * 
 * Value identifiers on binding configuration are MODBUS coil addresses.
 * 
 * <p>
 * Examples for valid binding configuration strings:
 * 
 * <ul>
 * <li><code>nibeheatpump="43005"</code></li>
 * <li><code>nibeheatpump="40004"</code></li>
 * <li><code>nibeheatpump="40008"</code></li>
 * </ul>
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class NibeHeatPumpGenericBindingProvider extends
		AbstractGenericBindingProvider implements NibeHeatPumpBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "nibeheatpump";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '" + item.getName() + "' is of type '"
						+ item.getClass().getSimpleName()
						+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		NibeHeatpumpBindingConfig config = new NibeHeatpumpBindingConfig();

		String itemId = bindingConfig.trim();
		if (itemId.startsWith("0x")) {
			config.itemId = Integer.parseInt(itemId.replace("0x", ""), 16);
		} else {
			config.itemId = Integer.parseInt(itemId);
		}

		addBindingConfig(item, config);
	}
	
	
	@Override
	public int getItemId(String itemName) {
		NibeHeatpumpBindingConfig config = (NibeHeatpumpBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemId : null;
	}


	class NibeHeatpumpBindingConfig implements BindingConfig {
		public int itemId;
	}

}
