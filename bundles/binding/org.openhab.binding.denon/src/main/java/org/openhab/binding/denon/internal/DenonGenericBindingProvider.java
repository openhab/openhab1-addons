/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.openhab.binding.denon.DenonBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the Denon binding configuration.
 * 
 * Some valid binding configuration strings:
 * 
 * {denon="avr2000#PW"} - Power on/off
 * {denon="avr2000#SURROUNDMODE"} - Current surround mode
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonGenericBindingProvider extends AbstractGenericBindingProvider implements DenonBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(DenonGenericBindingProvider.class);	
	
	// Example: avr2000#PW, avr3000#SURROUNDMODE
	private static final Pattern CONFIG_PATTERN = Pattern.compile("^(.)+#(.)+$");

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "denon";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Dimmer- and StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		bindingConfig =	bindingConfig.trim();
		
		if (CONFIG_PATTERN.matcher(bindingConfig).matches()) {
			DenonBindingConfig config = new DenonBindingConfig();

			String[] parts = bindingConfig.split("#");
			config.setItemName(item.getName());
			config.setInstance(parts[0]);
			config.setProperty(parts[1]);
			
			logger.debug("denon:{} item {} bound to property {}", config.getInstance(), config.getItemName(), config.getProperty());
			
			addBindingConfig(item, config);		
		}
	}

	@Override
	public DenonBindingConfig getConfig(String itemName) {
		return (DenonBindingConfig) bindingConfigs.get(itemName);
	}

	@Override
	public DenonBindingConfig getConfig(String instance, String property) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			DenonBindingConfig config = (DenonBindingConfig) entry.getValue();
			if (config.getInstance().equals(instance) && config.getProperty().equals(property)) {
				return config;
			}
		}
		return null;
	}

}
