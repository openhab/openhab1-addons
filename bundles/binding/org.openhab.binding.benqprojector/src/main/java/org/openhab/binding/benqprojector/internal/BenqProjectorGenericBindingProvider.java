/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal;

import org.openhab.binding.benqprojector.BenqProjectorBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration
 * of the BenQ projector binding
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class BenqProjectorGenericBindingProvider extends AbstractGenericBindingProvider implements BenqProjectorBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "benqprojector";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem )) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch Items are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		BenqProjectorBindingConfig config = new BenqProjectorBindingConfig();
		
		switch (bindingConfig.toLowerCase())
		{
		case "power":
			config.mode = BenqProjectorItemMode.POWER;
			break;
		case "mute":
			config.mode = BenqProjectorItemMode.MUTE;
			break;
		default:
			throw new BindingConfigParseException("Unable to parse '"+bindingConfig+"' to create a valid item binding.");
		}
		
		addBindingConfig(item, config);		
	}	
	
	public BenqProjectorBindingConfig getConfigForItemName(String itemName) {
		BenqProjectorBindingConfig config = null;
		if (super.bindingConfigs.containsKey(itemName)) {
			config = (BenqProjectorBindingConfig) super.bindingConfigs.get(itemName);
		}
		return config;
	}
}
