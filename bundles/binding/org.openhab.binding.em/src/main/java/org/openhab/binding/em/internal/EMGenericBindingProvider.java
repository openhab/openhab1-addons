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
package org.openhab.binding.em.internal;

import org.openhab.binding.em.EMBindingProvider;
import org.openhab.binding.em.internal.EMBindingConfig.Datapoint;
import org.openhab.binding.em.internal.EMBindingConfig.EMType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class EMGenericBindingProvider extends AbstractGenericBindingProvider
		implements EMBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "em";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * Binding config in the style
	 * {em="type=01|02|03;address=AA;datapoint=CUMULATED_VALUE"} {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] parts = bindingConfig.split(";");
		String address = null;
		EMType type = null;
		Datapoint datapoint = null;
		for (String part : parts) {
			String[] keyValue = part.split("=");
			if ("type".equals(keyValue[0])) {
				type = EMType.getFromTypeValue(keyValue[1]);
			} else if ("address".equals(keyValue[0])) {
				address = keyValue[1];
			} else if ("datapoint".equals(keyValue[0])) {
				datapoint = Datapoint.valueOf(keyValue[1]);
			}
		}
		EMBindingConfig config = new EMBindingConfig(type, address, datapoint,
				item);

		addBindingConfig(item, config);
	}

	@Override
	public EMBindingConfig getConfigByTypeAndAddressAndDatapoint(EMType type,
			String address, Datapoint datapoint) {
		for (BindingConfig config : super.bindingConfigs.values()) {
			EMBindingConfig emConfig = (EMBindingConfig) config;
			if (emConfig.getAddress().equals(address)
					&& emConfig.getType() == type
					&& emConfig.getDatapoint() == datapoint) {
				return emConfig;
			}
		}
		return null;
	}

}
