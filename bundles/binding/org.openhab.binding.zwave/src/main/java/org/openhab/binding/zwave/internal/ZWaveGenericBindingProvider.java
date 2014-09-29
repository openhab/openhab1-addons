/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.zwave.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.zwave.ZWaveBindingConfig;
import org.openhab.binding.zwave.ZWaveBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * @author Victor Belov
 * @author Brian Crosby
 * @since 1.3.0
 */
public class ZWaveGenericBindingProvider extends AbstractGenericBindingProvider implements ZWaveBindingProvider {

	private static final Logger logger = LoggerFactory.getLogger(ZWaveGenericBindingProvider.class);
	private final Map<String, Item> items = new HashMap<String, Item>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "zwave";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// All types are valid
		logger.trace("validateItemType({}, {})", item.getName(), bindingConfig);
	}

	/**
	 * Processes Z-Wave binding configuration string.
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		logger.trace("processBindingConfiguration({}, {})", item.getName(), bindingConfig);
		super.processBindingConfiguration(context, item, bindingConfig);
		String[] segments = bindingConfig.split(":");
		
		if (segments.length < 1 || segments.length > 3)
			throw new BindingConfigParseException("invalid number of segments in binding: " + bindingConfig);

		int nodeId;
		try{
			nodeId = Integer.parseInt(segments[0]);
		} catch (Exception e){
			throw new BindingConfigParseException(segments[1] + " is not a valid node id.");
		}
		
		int endpoint = 1;
		Integer refreshInterval = null;
		Map<String, String> arguments = new HashMap<String, String>();
		
		for (int i = 1; i < segments.length; i++) {
			try {
				if (segments[i].contains("=")) {
					for (String keyValuePairString : segments[i].split(",")) {
						String[] pair = keyValuePairString.split("=");
						String key = pair[0].trim().toLowerCase();
						
						if (key.equals("refresh_interval"))
							refreshInterval = Integer.parseInt(pair[1].trim());
						else
							arguments.put(key, pair[1].trim().toLowerCase());
					}
				} else {
						endpoint = Integer.parseInt(segments[i]); 
				}
			} catch (Exception e){
				throw new BindingConfigParseException(segments[i] + " is not a valid argument.");
			}
		}

		ZWaveBindingConfig config = new ZWaveBindingConfig(nodeId, endpoint, refreshInterval, arguments);
		addBindingConfig(item, config);
		items.put(item.getName(), item);
	}
	
	/**
	 * Returns the binding configuration for a string.
	 * @return the binding configuration.
	 */
	public ZWaveBindingConfig getZwaveBindingConfig(String itemName) {
		return (ZWaveBindingConfig) this.bindingConfigs.get(itemName);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Boolean autoUpdate(String itemName) {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Item getItem(String itemName) {
		return items.get(itemName);
	}
	
}
