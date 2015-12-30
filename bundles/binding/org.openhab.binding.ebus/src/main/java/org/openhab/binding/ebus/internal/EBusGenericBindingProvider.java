/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.ebus.EBusBindingProvider;
import org.openhab.binding.ebus.internal.utils.EBusUtils;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class can parse information from the binding format and provides eBus
 * binding informations.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusGenericBindingProvider extends
AbstractGenericBindingProvider implements EBusBindingProvider {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusGenericBindingProvider.class);

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#getBindingType()
	 */
	@Override
	public String getBindingType() {
		return "ebus";
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getItemName(java.lang.String)
	 */
	@Override
	public List<String> getItemNames(String uniqueId) {

		ArrayList<String> list = new ArrayList<String>();

		// Search for id
		for (Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			EBusBindingConfig cfg = (EBusBindingConfig) entry.getValue();
			
			String clazz = (String) cfg.map.get("class");
			String id = (String) cfg.map.get("id");
			
			if(clazz != null) {
				//logger.warn("Use of property class \"{}\" for id \"{}\" is deprecated!", clazz, id);
			}
			
			id = clazz == null ? id : clazz + "." + id;
			
			if(StringUtils.equals(id, uniqueId)) {
				list.add(entry.getKey());
			}
		}

		return list;
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.AbstractGenericBindingProvider#processBindingConfiguration(java.lang.String, org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		logger.debug("Process binding cfg for {} with settings {} [Context:{}]",
				item.getName(), bindingConfig, context);

		EBusBindingConfig config = new EBusBindingConfig();
		for (String set : bindingConfig.trim().split(",")) {
			String[] configParts = set.split(":");
			if (configParts.length > 2) {
				throw new BindingConfigParseException("eBus binding configuration must not contain more than two parts");
			}

			configParts[0] = configParts[0].trim().toLowerCase();
			configParts[1] = configParts[1].trim();

			if(configParts[0].equals("data")) {
				config.map.put(configParts[0], EBusUtils.toByteArray(configParts[1]));

			} else if(configParts[0].equals("src")) {
				config.map.put(configParts[0], DatatypeConverter.parseHexBinary(configParts[1])[0]);

			} else if(configParts[0].equals("dst")) {
				config.map.put(configParts[0], DatatypeConverter.parseHexBinary(configParts[1])[0]);

			} else if(configParts[0].equals("refresh")) {
				config.map.put(configParts[0], Integer.parseInt(configParts[1]));

			} else if(configParts[0].startsWith("data-")) {
				if(!config.map.containsKey("data-map")) {
					config.map.put("data-map", new HashMap<String, byte[]>());
				}

				@SuppressWarnings("unchecked")
				HashMap<String, byte[]> m = (HashMap<String, byte[]>) config.map.get("data-map");
				String key = configParts[0].substring(5);
				m.put(key, EBusUtils.toByteArray(configParts[1]));

			} else {
				config.map.put(configParts[0], configParts[1]);
			}
		}

		addBindingConfig(item, config);
	}

	/* (non-Javadoc)
	 * @see org.openhab.model.item.binding.BindingConfigReader#validateItemType(org.openhab.core.items.Item, java.lang.String)
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// noop
	}

	/**
	 * This is an internal data structure to store information from the binding
	 * config strings. Simple map used.
	 */
	class EBusBindingConfig implements BindingConfig {
		public HashMap<String, Object> map = new HashMap<String, Object>();
	}

	/**
	 * Simple get a value from binding configuration or use default value.
	 * @param itemName The openhab item name
	 * @param type The eBus item type
	 * @param defaultValue The default value if not set
	 * @return The value or default if set
	 */
	@SuppressWarnings("unchecked")
	protected <T> T get(String itemName, String type, T defaultValue) {
		EBusBindingConfig bindingConfig = (EBusBindingConfig) bindingConfigs.get(itemName);
		if(bindingConfig != null) {
			if(bindingConfig.map.containsKey(type)) {
				return (T) bindingConfig.map.get(type);
			}
		}
		return defaultValue;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getCommandData(java.lang.String)
	 */
	@Override
	public byte[] getTelegramData(String itemName) {
		return get(itemName, "data", null);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getRefreshRate(java.lang.String)
	 */
	@Override
	public int getRefreshRate(String itemName) {
		return get(itemName, "refresh", 0);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getCommandData(java.lang.String, java.lang.String)
	 */
	@Override
	public byte[] getTelegramData(String itemName, String type) {
		Map<String, Object> m = get(itemName, "data-map", null);
		if(m != null && m.containsKey(type)) {
			return (byte[]) m.get(type);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getCommand(java.lang.String)
	 */
	@Override
	public String getId(String itemName) {
		return get(itemName, "id", null);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getCommand(java.lang.String)
	 */
	@Override
	public String getCommand(String itemName) {
		return get(itemName, "cmd", null);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getCommand(java.lang.String)
	 */
	@Override
	public String getSet(String itemName) {
		return get(itemName, "set", null);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getTelegramSource(java.lang.String)
	 */
	@Override
	public Byte getTelegramSource(String itemName) {
		return get(itemName, "src", null);
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.EBusBindingProvider#getTelegramDestination(java.lang.String)
	 */
	@Override
	public Byte getTelegramDestination(String itemName) {
		return get(itemName, "dst", null);
	}
}
