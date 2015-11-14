/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.myq.myqBindingProvider;
import org.openhab.binding.myq.internal.myqBindingConfig.ITEMTYPE;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Scott Hanson
 * @since 1.8.0
 */
public class myqGenericBindingProvider extends AbstractGenericBindingProvider
		implements myqBindingProvider {
	static final Logger logger = LoggerFactory
			.getLogger(myqGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "myq";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof ContactItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems, ContactItem or StringItem are allowed "
							+ "- please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		myqBindingConfig config = parseBindingConfig(item, bindingConfig);

		// parse bindingconfig here ...
		addBindingConfig(item, config);
	}

	/**
	 * Parse item type to see what the action is used for
	 */
	private myqBindingConfig parseBindingConfig(Item item, String bindingConfig)
			throws BindingConfigParseException {
		final myqBindingConfig config = new myqBindingConfig();

		if (item instanceof SwitchItem) {
			config.Type = ITEMTYPE.Switch;
		} else if (item instanceof ContactItem) {
			config.Type = ITEMTYPE.ContactStatus;
		} else if (item instanceof StringItem) {
			config.Type = ITEMTYPE.StringStatus;
		}
		config.DeviceID = Integer.parseInt(bindingConfig);
		return config;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public myqBindingConfig getItemConfig(String itemName) {
		return (myqBindingConfig) bindingConfigs.get(itemName);
	}

	public List<String> getInBindingItemNames() {
		List<String> inBindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			inBindings.add(itemName);
		}
		return inBindings;
	}
}