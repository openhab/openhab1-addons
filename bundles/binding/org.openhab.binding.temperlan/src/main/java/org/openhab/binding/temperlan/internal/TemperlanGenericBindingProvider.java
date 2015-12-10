/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.temperlan.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.temperlan.TemperlanBindingProvider;
import org.openhab.binding.temperlan.internal.TemperlanBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author snoerenberg
 * @since 1.0
 */
public class TemperlanGenericBindingProvider extends AbstractGenericBindingProvider implements TemperlanBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "temperlan";
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
			// parse configuration
			Map<String, String> props = parseProperties(bindingConfig);
			String id = props.get(TemperlanBindingConfig.KEY_DEVICE_UID);
			String bindingType = props.get(TemperlanBindingConfig.KEY_BINDING_TYPE);
			// create configuration
			TemperlanBindingConfig config = new TemperlanBindingConfig(
					id, bindingType, item.getName());
			// add binding configuration
			addBindingConfig(item, config);
		}
	}
	
	public static Map<String, String> parseProperties(String config) {
		Map<String, String> props = new HashMap<String, String>();
		String[] tokens = config.trim().split(",");
		for (String token : tokens) {
			token = token.trim();
			String[] confStatement = token.split("=");
			String key = confStatement[0];
			String value = confStatement[1];
			props.put(key, value);
		}
		return props;
	}
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author snoerenberg
	 * @since 1.0
	 */
	class temperlanBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
	}


	@Override
	public TemperlanBindingConfig getItemConfig(String itemName) {
		return ((TemperlanBindingConfig) bindingConfigs.get(itemName));
	}

	@Override
	public void getDeviceConfigs(String deviceUid,
			Map<String, TemperlanBindingConfig> configs) {
		for (BindingConfig config : bindingConfigs.values()) {
			TemperlanBindingConfig temperlanConfig = (TemperlanBindingConfig) config;
			if (temperlanConfig.getDeviceUid().equals(deviceUid)) {
				configs.put(temperlanConfig.getItemName(), temperlanConfig);
			}
		}
		
	}
	
	
}
