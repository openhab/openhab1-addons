/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pilight.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.pilight.PilightBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
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
	 * Matches: instance#device,property=optional
	 */
	private static final Pattern CONFIG_PATTERN = Pattern
			.compile("^(?<instance>(\\w)+)+#(?<device>(\\w)+)+(,(?<properties>(\\w)+=(\\w)+)+)*$");
	
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
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ContactItem
				|| item instanceof StringItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch, Dimmer, Contact, String and Number are supported for now- please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		PilightBindingConfig config = parseBindingConfig(item, bindingConfig);

		if (config != null) {
			addBindingConfig(item, config);
		}
	}

	protected PilightBindingConfig parseBindingConfig(Item item, String bindingConfig) {
		bindingConfig =	bindingConfig.replace(" ", "");
		Matcher matcher = CONFIG_PATTERN.matcher(bindingConfig);
		
		if (matcher.matches()) {
			PilightBindingConfig config = new PilightBindingConfig();
			
			String instance = matcher.group("instance");
			String device = matcher.group("device");
			
			config.setItemName(item.getName());
			config.setItemType(item.getClass());
			config.setInstance(instance);
			config.setDevice(device);

			String values = matcher.group("properties");

			if (!StringUtils.isEmpty(values)) {
				String[] pairs = values.split(",");
				for (String pair : pairs) {
					String[] kv = pair.split("=");
					String key = kv[0];
					String value = kv[1];
					if (key.equals("property")) {
						config.setProperty(value);
					}
				}
			}
			
			boolean isValueItem = item.getClass().equals(NumberItem.class) || item.getClass().equals(StringItem.class);
				
			if (isValueItem && StringUtils.isEmpty(config.getProperty())) {
				logger.error("No property specified for item {}", config.getItemName());
			} else {
				logger.info("pilight:{} item {} bound to device {}{}", config.getInstance(), config.getItemName(), config.getDevice(), 
				config.getProperty() != null ? ", property " + config.getProperty() : "");
				return config;
			}
		} else {
			logger.error("Item config {} does not match instance#location:device,property=optional pattern", bindingConfig);
		}
		
		return null;
	}
	
	public PilightBindingConfig getBindingConfig(String itemName) {
		return (PilightBindingConfig) bindingConfigs.get(itemName);
	}
	
	public List<PilightBindingConfig> getBindingConfigs(String instance, String device) {
		List<PilightBindingConfig> configs = new ArrayList<PilightBindingConfig>();
		
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			PilightBindingConfig config = (PilightBindingConfig) entry.getValue();
			if (config.getInstance().equals(instance) && config.getDevice().equals(device))
				configs.add(config);
		}
		
		return configs;
	}
}
