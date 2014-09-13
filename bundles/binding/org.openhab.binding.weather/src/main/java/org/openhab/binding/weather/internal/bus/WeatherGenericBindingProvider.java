/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.bus;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.weather.WeatherBindingProvider;
import org.openhab.binding.weather.internal.common.binding.WeatherBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the binding format and provides Weather
 * binding informations.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherGenericBindingProvider extends AbstractGenericBindingProvider implements WeatherBindingProvider {
	private static final Logger logger = LoggerFactory.getLogger(WeatherGenericBindingProvider.class);

	private BindingConfigParser parser = new BindingConfigParser();
	private Map<String, Item> items = new HashMap<String, Item>();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return "weather";
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

		WeatherBindingConfig config = parser.parse(item, bindingConfig);
		logger.debug("Adding item {} with {}", item.getName(), config);
		items.put(item.getName(), item);
		addBindingConfig(item, config);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public WeatherBindingConfig getBindingFor(String itemName) {
		return (WeatherBindingConfig) bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean hasBinding(String locationId) {
		for (BindingConfig bindingConfig : bindingConfigs.values()) {
			if (((WeatherBindingConfig) bindingConfig).getLocationId().equals(locationId)) {
				return true;
			}
		}
		return false;
	}

}
