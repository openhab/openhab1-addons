/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.heatmiser.HeatmiserBindingProvider;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat;
import org.openhab.binding.heatmiser.internal.thermostat.HeatmiserThermostat.Functions;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DateTimeItem;
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
 * @author Chris Jackson
 * @since 1.4.0
 */
public class HeatmiserGenericBindingProvider extends AbstractGenericBindingProvider implements HeatmiserBindingProvider {

	/** {@link Pattern} which matches an In-Binding */
	private static final Pattern BINDING_PATTERN = Pattern
			.compile("([0-9]+):([A-Z]+)");

	static final Logger logger = LoggerFactory.getLogger(HeatmiserGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "heatmiser";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			HeatmiserBindingConfig config = new HeatmiserBindingConfig();

			config.itemType = item.getClass();

			Matcher bindingMatcher = BINDING_PATTERN.matcher(bindingConfig);

			if (!bindingMatcher.matches()) {
				throw new BindingConfigParseException(getBindingType()+
						" binding configuration must consist of two parts [config="+bindingMatcher+"]");
			} else {
				config.address = Integer.parseInt(bindingMatcher.group(1));
				config.function = Functions.valueOf(bindingMatcher.group(2));

				// Check the type for different functions
				switch(config.function) {
					case SETTEMP:
					case FROSTTEMP:
					case ROOMTEMP:
					case FLOORTEMP:
						if(config.itemType != NumberItem.class && config.itemType != StringItem.class) {
							logger.error("Only Number and String allowed for Heatmiser:{} function", config.function);
							config = null;
						}
						break;
					case HOLDTIME:
					case HOLIDAYTIME:
						if(config.itemType != SwitchItem.class && config.itemType != DateTimeItem.class) {
							logger.error("Only Switch and DateTime allowed for Heatmiser:{} function", config.function);
							config = null;
						}
						break;
					case HOLIDAYSET:
						if(config.itemType != SwitchItem.class && config.itemType != NumberItem.class) {
							logger.error("Only Switch and Number allowed for Heatmiser:{} function", config.function);
							config = null;
						}
						break;
					case HOLDMODE:
					case HOLIDAYMODE:
						if(config.itemType != SwitchItem.class) {
							logger.error("Only Switch allowed for Heatmiser:{} function", config.function);
							config = null;
						}
						break;
					case WATERSTATE:
					case HEATSTATE:
					case STATE:
					case ONOFF:
						if(config.itemType != SwitchItem.class && config.itemType != StringItem.class) {
							logger.error("Only Switch and String allowed for Heatmiser:{} function", config.function);
							config = null;
						}
						break;
					default:
						config = null;
						logger.error("Unknown or unsupported Heatmiser function: {}", bindingConfig);
						break;
				}
			}

			if(config != null) {
				addBindingConfig(item, config);
			}
		} else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}
	
	

	/**
	 * @{inheritDoc
	 */
	public List<String> getBindingItemsAtAddress(int address) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			HeatmiserBindingConfig itemConfig = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
			if (itemConfig.hasAddress(address)) {
				bindings.add(itemName);
			}
		}
		return bindings;
	}

	public Functions getFunction(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.function : null;
	}
	
	public int getAddress(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.address : -1;
	}
	
	public Class<? extends Item> getItemType(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	class HeatmiserBindingConfig implements BindingConfig {
		Class<? extends Item> itemType;
		int address;
		HeatmiserThermostat.Functions function;

		boolean hasAddress(int addr) {
			if(address == addr)
				return true;
			return false;
		}
	}
}
