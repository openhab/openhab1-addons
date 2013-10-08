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
package org.openhab.binding.s300th.internal;

import org.openhab.binding.s300th.S300THBindingProvider;
import org.openhab.binding.s300th.internal.S300THGenericBindingProvider.S300THBindingConfig.Datapoint;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class S300THGenericBindingProvider extends AbstractGenericBindingProvider implements S300THBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "s300th";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName() + "' is of type '"
					+ item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * item config in the style {s300th="address=A; datapoint=DATAPOINT"}
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		S300THBindingConfig config = new S300THBindingConfig();
		String[] parts = bindingConfig.split(";");
		for (String part : parts) {
			String[] keyValue = part.split("=");
			if (keyValue[0].equals("address")) {
				config.address = keyValue[1];
			}
			if (keyValue[0].equals("datapoint")) {
				config.datapoint = Datapoint.valueOf(keyValue[1]);
			}
		}
		config.item = item;

		addBindingConfig(item, config);
	}

	public static class S300THBindingConfig implements BindingConfig {
		public enum Datapoint {
			TEMPERATURE, HUMIDITY, RAIN, WIND, IS_RAINING;
		}

		public String address;
		public Item item;
		public Datapoint datapoint;
	}

	@Override
	public S300THBindingConfig getBindingConfigForAddressAndDatapoint(String address, Datapoint datapoint) {
		for (BindingConfig config : this.bindingConfigs.values()) {
			S300THBindingConfig s300thConfig = (S300THBindingConfig) config;
			if (s300thConfig.address.equals(address) && s300thConfig.datapoint == datapoint) {
				return (S300THBindingConfig) config;
			}
		}
		return null;
	}

}
