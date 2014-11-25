/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.openhab.binding.pilight.PilightBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Jeroen Idserda
 * @since 1.0
 */
public class PilightGenericBindingProvider extends AbstractGenericBindingProvider implements PilightBindingProvider {
	
	/*
	 * Matches: instance#location:device
	 */
	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("^(.)+#(.)+:(.)+$");
	
	private static final Logger logger = LoggerFactory.getLogger(PilightGenericBindingProvider.class);	

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "pilight";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch and Dimmer are supported for now- please check your *.items configuration");
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
			PilightBindingConfig config = new PilightBindingConfig();
			
			String[] parts = bindingConfig.split("#");
			String[] deviceInfo = parts[1].split(":");
			
			config.setItemName(item.getName());
			config.setInstance(parts[0]);
			config.setLocation(deviceInfo[0]);
			config.setDevice(deviceInfo[1]);
			
			logger.info("pilight:{} item '{}' bound to device '{}' in location '{}'", config.getInstance(), config.getItemName(), config.getDevice(), 
					config.getLocation());
			
			addBindingConfig(item, config);
		} else {
			logger.error("Item config {} does not match instance#location:device pattern", bindingConfig);
		}
	}
	
	public PilightBindingConfig getBindingConfig(String itemName) {
		return (PilightBindingConfig) bindingConfigs.get(itemName);
	}
	
	public PilightBindingConfig getBindingConfig(String instance, String location, String device) {
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			PilightBindingConfig config = (PilightBindingConfig) entry.getValue();
			if (config.getInstance().equals(instance) && config.getLocation().equals(location) && config.getDevice().equals(device))
				return config;
		}
		return null;
	}
}
