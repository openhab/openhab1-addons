/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.yamahareceiver.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.yamahareceiver.YamahaReceiverBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Eric Thill
 * @author Ben Jones
 * @since 1.6.0
 */
public class YamahaReceiverGenericBindingProvider extends
		AbstractGenericBindingProvider implements YamahaReceiverBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "yamahareceiver";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if (bindingConfig != null) {
			// parse configuration
			Map<String, String> props = parseProperties(bindingConfig);
			String id = props.get(YamahaReceiverBindingConfig.KEY_DEVICE_UID);
			String zone = props.get(YamahaReceiverBindingConfig.KEY_ZONE);
			String bindingType = props
					.get(YamahaReceiverBindingConfig.KEY_BINDING_TYPE);
			// create configuration
			YamahaReceiverBindingConfig config = new YamahaReceiverBindingConfig(
					id, zone, bindingType, item.getName());
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

	@Override
	public YamahaReceiverBindingConfig getItemConfig(String itemName) {
		return ((YamahaReceiverBindingConfig) bindingConfigs.get(itemName));
	}

	@Override
	public void getDeviceConfigs(String deviceUid,
			Map<String, YamahaReceiverBindingConfig> configs) {
		for (BindingConfig config : bindingConfigs.values()) {
			YamahaReceiverBindingConfig yamahaConfig = (YamahaReceiverBindingConfig) config;
			if (yamahaConfig.getDeviceUid().equals(deviceUid)) {
				configs.put(yamahaConfig.getItemName(), yamahaConfig);
			}
		}
	}

}
