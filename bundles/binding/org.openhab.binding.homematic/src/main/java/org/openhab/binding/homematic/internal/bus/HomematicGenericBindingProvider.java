/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openhab.binding.homematic.HomematicBindingProvider;
import org.openhab.binding.homematic.internal.config.binding.HomematicBindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the generic binding format and provides
 * Homematic binding information.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class HomematicGenericBindingProvider extends AbstractGenericBindingProvider implements HomematicBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(HomematicGenericBindingProvider.class);

	private BindingConfigParser parser = new BindingConfigParser();

	private Map<String, Item> items = new HashMap<String, Item>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "homematic";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// validation is done in processBindingConfiguration
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		HomematicBindingConfig config = parser.parse(item, bindingConfig);
		logger.debug("Adding item {} with {}", item.getName(), config.toString());
		items.put(item.getName(), item);
		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Item> getItemsFor(HomematicBindingConfig bindingConfig) {
		List<Item> result = new ArrayList<Item>();
		for (String itemName : getItemNames()) {
			HomematicBindingConfig bc = (HomematicBindingConfig) bindingConfigs.get(itemName);
			if (bc.equals(bindingConfig)) {
				result.add(items.get(itemName));
			}
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HomematicBindingConfig getBindingFor(String itemName) {
		return (HomematicBindingConfig) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

}
