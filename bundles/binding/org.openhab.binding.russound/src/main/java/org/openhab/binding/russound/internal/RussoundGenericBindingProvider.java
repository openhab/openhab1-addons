/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.russound.internal;

import org.openhab.binding.russound.RussoundBindingConfig;
import org.openhab.binding.russound.RussoundBindingProvider;
import org.openhab.binding.russound.internal.command.RussoundCommand;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Hamilton
 * @since 1.7.0
 */
public class RussoundGenericBindingProvider extends
		AbstractGenericBindingProvider implements RussoundBindingProvider {
	private static final Logger logger = LoggerFactory
			.getLogger(RussoundGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "russound";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem
				|| item instanceof DimmerItem || item instanceof StringItem || item instanceof ContactItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItem, NumberItem1, DimmerItem and StringItem are allowed - please check your *.items configuration");
		}
		logger.debug("Validated - Item: " + item.toString() + ", config="
				+ bindingConfig);
	}

	/**
	 * {@inheritDoc}
	 */
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig.contains("connection")) {
			addBindingConfig(item, new BindingConfig() {
			});
		} else {
			String[] configParts = bindingConfig.split("/");
			int controller = Integer.parseInt(configParts[0]);
			int zone = Integer.parseInt(configParts[1]);
			ZoneAddress address = new ZoneAddress(controller, zone);
			// TODO, do i really need to use address twice
			RussoundBindingConfig config = new RussoundBindingConfig(address,
					RussoundCommand.getCommandFromItemFileDescription(
							configParts[2], address));
			addBindingConfig(item, config);
		}

	}

	public RussoundBindingConfig getBindingConfig(String itemName) {
		return (RussoundBindingConfig) bindingConfigs.get(itemName);
	}

}
