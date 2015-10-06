/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal;

import java.util.Set;

import org.openhab.binding.satel.SatelBindingConfig;
import org.openhab.binding.satel.SatelBindingProvider;
import org.openhab.binding.satel.config.SatelBindingConfigFactory;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SatelGenericBindingProvider extends AbstractGenericBindingProvider implements SatelBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(SatelGenericBindingProvider.class);
	
	private SatelBindingConfigFactory bindingConfigFactory = new SatelBindingConfigFactory();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "satel";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		logger.trace("Processing binding configuration for item {}", item.getName());

		SatelBindingConfig bc = bindingConfigFactory.createBindingConfig(bindingConfig);
		logger.trace("Adding binding configuration for item {}: {}", item.getName(), bc);
		addBindingConfig(item, bc);
		super.processBindingConfiguration(context, item, bindingConfig);
	}

	/**
	 * {@inheritDoc}
	 */
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	public SatelBindingConfig getItemConfig(String itemName) {
		return (SatelBindingConfig) this.bindingConfigs.get(itemName);
	}
}
