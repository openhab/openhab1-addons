/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freeswitch.internal;

import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.freeswitch.FreeswitchBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.library.tel.items.CallItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Dan Cunningham
 * @since 1.4.0
 */
public class FreeswitchGenericBindingProvider extends AbstractGenericBindingProvider implements FreeswitchBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "freeswitch";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof StringItem || item instanceof CallItem || item instanceof SwitchItem ||
				item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only String, Number, Call and Switch Items are allowed");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		String[] configParts = bindingConfig.trim().split(":", 2);
		
		String command = configParts[0];
		
		String argument = null;
		
		if(configParts.length > 1)
			argument  = configParts[1];
		
		FreeswitchBindingType type = FreeswitchBindingType.fromString(command);
		
		if(type == null)
			throw new BindingConfigParseException("Unknown item type " + configParts[0] );
		
		FreeswitchBindingConfig config = new FreeswitchBindingConfig(item.getName(),item.getClass(), type, argument);
		
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
	public FreeswitchBindingConfig getFreeswitchBindingConfig(String itemName) {
		return (FreeswitchBindingConfig) this.bindingConfigs.get(itemName);
	}
	
}
