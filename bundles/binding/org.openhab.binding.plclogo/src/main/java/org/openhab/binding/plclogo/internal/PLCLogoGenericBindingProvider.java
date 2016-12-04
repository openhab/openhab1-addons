/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plclogo.internal;

import org.openhab.binding.plclogo.PLCLogoBindingProvider;
import org.openhab.binding.plclogo.PLCLogoBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.*;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author g8kmh
 * @since 1.5.0
 */
public class PLCLogoGenericBindingProvider extends AbstractGenericBindingProvider implements PLCLogoBindingProvider {
	private static final Logger logger = 
			LoggerFactory.getLogger(PLCLogoBinding.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "plclogo";
	}
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Item getItemType(String itemName) {
		PLCLogoBindingConfig config = (PLCLogoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.getItemType() : null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// @TODO may add additional checking based on the memloc
		if (!(item instanceof SwitchItem || item instanceof ContactItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch - Contact Items & Number are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException 
	{
		super.processBindingConfiguration(context, item, bindingConfig);
	
		PLCLogoBindingConfig config = new PLCLogoBindingConfig(item.getName(), item, bindingConfig);
		addBindingConfig(item, config);		
	}

	@Override
	public PLCLogoBindingConfig getBindingConfig(String itemName) {
		return (PLCLogoBindingConfig) this.bindingConfigs.get(itemName);

	}

	
	
}
