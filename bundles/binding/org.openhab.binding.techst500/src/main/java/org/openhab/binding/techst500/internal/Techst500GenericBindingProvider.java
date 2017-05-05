/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.techst500.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.techst500.Techst500BindingProvider;
import org.openhab.binding.techst500.internal.Techst500BindingConfig;
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
public class Techst500GenericBindingProvider extends AbstractGenericBindingProvider implements Techst500BindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "techst500";
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
			String id = props.get(Techst500BindingConfig.KEY_DEVICE_UID);
			String bindingType = props.get(Techst500BindingConfig.KEY_BINDING_TYPE);
			// create configuration
			Techst500BindingConfig config = new Techst500BindingConfig(
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
	class techst500BindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
	}


	@Override
	public Techst500BindingConfig getItemConfig(String itemName) {
		return ((Techst500BindingConfig) bindingConfigs.get(itemName));
	}

	@Override
	public void getDeviceConfigs(String deviceUid,
			Map<String, Techst500BindingConfig> configs) {
		for (BindingConfig config : bindingConfigs.values()) {
			Techst500BindingConfig techst500Config = (Techst500BindingConfig) config;
			if (techst500Config.getDeviceUid().equals(deviceUid)) {
				configs.put(techst500Config.getItemName(), techst500Config);
			}
		}
		
	}
	
	
}
