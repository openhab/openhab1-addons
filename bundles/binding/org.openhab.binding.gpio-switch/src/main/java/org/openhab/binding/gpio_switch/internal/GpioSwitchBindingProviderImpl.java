/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gpio_switch.internal;

import org.openhab.binding.gpio_switch.GpioSwitchBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.io.gpio_raspberry.item.GpioIOItemConfig;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Robert Delbrück
 * @since 1.6.0
 */
public class GpioSwitchBindingProviderImpl extends AbstractGenericBindingProvider implements GpioSwitchBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(GpioSwitchBindingProviderImpl.class);
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "gpioSwitch";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only SwitchItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
//		if (bindingConfig == null) {
//			logger.debug("binding-configuration is currently not set for item: " + item.getName());
//		}
		
		logger.debug("reading item: " + item);
		super.bindingConfigs.put(item.getName(), new GpioIOItemConfig());
	}
	
	@Override
	public boolean isItemConfigured(String itemName) {
		return super.bindingConfigs.get(itemName) != null;
	}

	@Override
	public GpioIOItemConfig getItemConfig(String itemName) {
		return (GpioIOItemConfig) super.bindingConfigs.get(itemName);
	}
	
	
}
