/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
