/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * @author Victor Belov
 * @author Brian Crosby
 * @author Chris Jackson
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
		
		if (segments.length < 1 || segments.length > 3) {
			throw new BindingConfigParseException("Invalid number of segments in binding: " + bindingConfig);
		}

		int nodeId;
		try {
			nodeId = Integer.parseInt(segments[0]);
		} catch (Exception e) {
			logger.error("{}: Invalid node ID '{}'", item.getName(), segments[0]);
			throw new BindingConfigParseException(segments[0] + " is not a valid node id.");
		}

		if(nodeId <= 0 || nodeId > 232) {
			logger.error("{}: Invalid node ID '{}'", item.getName(), nodeId);
			throw new BindingConfigParseException(nodeId + " is not a valid node number.");			
		}

		int endpoint = 0;
		Integer refreshInterval = null;
		Map<String, String> arguments = new HashMap<String, String>();

		for (int i = 1; i < segments.length; i++) {
			try {
				if (segments[i].contains("=")) {
					for (String keyValuePairString : segments[i].split(",")) {
						String[] pair = keyValuePairString.split("=");
						String key = pair[0].trim().toLowerCase();
						String value = pair[1].trim().toLowerCase();

						if (key.equals("refresh_interval")) {
							refreshInterval = Integer.parseInt(value);
						} else {
							arguments.put(key, value);
						}

						// Sanity check the command class
						if (key.equals("command")) {
							if(ZWaveCommandClass.CommandClass.getCommandClass(pair[1]) == null &&
									value.equals("info") == false) {
								logger.error("{}: Invalid command class '{}'", item.getName(), pair[1].toUpperCase());
								throw new BindingConfigParseException("Invalid command class " + pair[1].toUpperCase());
							}
						}
					}
				} else {
					try {
						endpoint = Integer.parseInt(segments[i]); 
					} catch (Exception e) {
						logger.error("{}: Invalid endpoint ID '{}'", item.getName(), segments[i]);
						throw new BindingConfigParseException(segments[i] + " is not a valid endpoint.");
					}
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
