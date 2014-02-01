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
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig == null) {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
			return;
		}

		HeatmiserBindingConfig config = new HeatmiserBindingConfig();

		config.itemType = item.getClass();

		String[] configOptions = bindingConfig.split(":");
		if (configOptions == null || (configOptions.length < 2 || configOptions.length > 3)) {
			throw new BindingConfigParseException(
					getBindingType()
							+ " binding configuration must consist of two or three parts [config='Connector:(Address:)Function]");
		}

		config.setConnector(configOptions[0]);

		// If there are two parts to the connector, then this should be a WiFi
		// thermostat
		// In this case, address is set to 0, and function is [1]
		if (configOptions.length == 2) {
			config.setAddress(0);
			config.setFunction(Functions.valueOf(configOptions[1]));
		} else {
			config.setAddress(Integer.parseInt(configOptions[1]));
			config.setFunction(Functions.valueOf(configOptions[2]));
		}

		// Check the type for different functions
		switch (config.function) {
		case SETTEMP:
		case FROSTTEMP:
		case ROOMTEMP:
		case FLOORTEMP:
			if (config.itemType != NumberItem.class && config.itemType != StringItem.class) {
				logger.error("Only Number and String allowed for Heatmiser:{} function", config.function);
				config = null;
			}
			break;
		case HOLDTIME:
		case HOLIDAYTIME:
			if (config.itemType != SwitchItem.class && config.itemType != DateTimeItem.class) {
				logger.error("Only Switch and DateTime allowed for Heatmiser:{} function", config.function);
				config = null;
			}
			break;
		case HOLIDAYSET:
			if (config.itemType != SwitchItem.class && config.itemType != NumberItem.class) {
				logger.error("Only Switch and Number allowed for Heatmiser:{} function", config.function);
				config = null;
			}
			break;
		case HOLDMODE:
		case HOLIDAYMODE:
			if (config.itemType != SwitchItem.class) {
				logger.error("Only Switch allowed for Heatmiser:{} function", config.function);
				config = null;
			}
			break;
		case WATERSTATE:
		case HEATSTATE:
		case STATE:
		case ONOFF:
			if (config.itemType != SwitchItem.class && config.itemType != StringItem.class) {
				logger.error("Only Switch and String allowed for Heatmiser:{} function", config.function);
				config = null;
			}
			break;
		default:
			config = null;
			logger.error("Unknown or unsupported Heatmiser function: {}", bindingConfig);
			break;
		}

		if (config != null) {
			addBindingConfig(item, config);
		}
	}


	/**
	 * @{inheritDoc
	 */
	public List<String> getBindingItemsAtAddress(String connector, int address) {
		List<String> bindings = new ArrayList<String>();
		for (String itemName : bindingConfigs.keySet()) {
			HeatmiserBindingConfig itemConfig = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
			if (itemConfig.hasAddress(connector, address)) {
				bindings.add(itemName);
			}
		}
		return bindings;
	}

	public Functions getFunction(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.function : null;
	}

	public String getConnector(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.connector: null;
	}
	
	public String getId(String itemName) {
		HeatmiserBindingConfig config = (HeatmiserBindingConfig) bindingConfigs.get(itemName);
		return config != null ? (config.connector + ':' + config.address): null;
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
		private Class<? extends Item> itemType;
		private String connector;
		private int address = 0;
		private HeatmiserThermostat.Functions function;

		
		public void setAddress(int addr) {
			address = addr;
		}
		
		public void setConnector(String conn) {
			connector = conn;
		}

		public void setFunction(HeatmiserThermostat.Functions func) {
			function = func;
		}

		boolean hasAddress(String conn, int addr) {
			if(!connector.equalsIgnoreCase(conn))
				return false;
			if(address == addr)
				return true;
			
			return false;
		}
	}
}
