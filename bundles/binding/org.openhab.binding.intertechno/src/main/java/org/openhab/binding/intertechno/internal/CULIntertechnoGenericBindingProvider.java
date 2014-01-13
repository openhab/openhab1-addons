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
package org.openhab.binding.intertechno.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.intertechno.CULIntertechnoBindingProvider;
import org.openhab.binding.intertechno.IntertechnoBindingConfig;
import org.openhab.binding.intertechno.internal.parser.AddressParserFactory;
import org.openhab.binding.intertechno.internal.parser.IntertechnoAddressParser;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULIntertechnoGenericBindingProvider extends
		AbstractGenericBindingProvider implements CULIntertechnoBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "culintertechno";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * config of style
	 * {intertechno="type=<classic|fls|rev>;group=<group>;address=<address>"}
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		String[] configParts = bindingConfig.split(";");
		String type = configParts[0].split("=")[1];
		List<String> addressParts = new ArrayList<String>(3);
		for (int i = 1; i < configParts.length; i++) {
			addressParts.add(configParts[i].split("=")[1]);
		}
		IntertechnoAddressParser parser = AddressParserFactory.getParser(type);
		String address = parser.parseAddress(addressParts
				.toArray(new String[addressParts.size()]));
		String commandOn = parser.getCommandValueON();
		String commandOff = parser.getCOmmandValueOFF();

		IntertechnoBindingConfig config = new IntertechnoBindingConfig(address,
				commandOn, commandOff);

		addBindingConfig(item, config);
	}

	@Override
	public IntertechnoBindingConfig getConfigForItemName(String itemName) {
		return (IntertechnoBindingConfig) bindingConfigs.get(itemName);
	}

}
