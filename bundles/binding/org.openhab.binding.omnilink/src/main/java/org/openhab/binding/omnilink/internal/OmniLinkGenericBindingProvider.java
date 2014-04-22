/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.omnilink.internal;

import java.util.HashSet;
import java.util.Set;
import org.openhab.binding.omnilink.OmniLinkBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Dan Cunningham
 * @since 1.5.0
 */
public class OmniLinkGenericBindingProvider extends
		AbstractGenericBindingProvider implements OmniLinkBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(OmniLinkGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "omnilink";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		//we accept just about everything!
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		/**
		 * UNIT:1
		 */
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length != 2) {
			throw new BindingConfigParseException(
					"Omnilink configuration must contain of two parts separated by a ':'");
		}

		OmniLinkItemType type = OmniLinkItemType
				.getOmniLinkItemType(configParts[0]);

		if (type == null)
			throw new BindingConfigParseException("Unknown item type "
					+ configParts[0]);

		int number = Integer.parseInt(configParts[1]);

		OmniLinkBindingConfig config = new OmniLinkBindingConfig(type, number);
		logger.debug(" Adding item {} {}",
				new Object[] { config.getObjectType(), config.getNumber() });
		addBindingConfig(item, config);

		Set<Item> items = contextMap.get(context);
		if (items == null) {
			items = new HashSet<Item>();
			contextMap.put(context, items);
		}
		items.add(item);
	}

	@Override
	public Item getItem(String itemName) {
		for (Set<Item> items : contextMap.values()) {
			if (items != null) {
				for (Item item : items) {
					if (itemName.equals(item.getName())) {
						return item;
					}
				}
			}
		}
		return null;
	}

	@Override
	public OmniLinkBindingConfig getOmniLinkBindingConfig(String itemName) {
		return (OmniLinkBindingConfig) this.bindingConfigs.get(itemName);
	}

}
