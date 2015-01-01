/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelinkec3k.internal;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.jeelinkec3k.JeeLinkEC3KBindingConfig;
import org.openhab.binding.jeelinkec3k.JeeLinkEC3KBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author magcode
 * @since 1.0.0
 */
public class JeeLinkEC3KGenericBindingProvider extends
		AbstractGenericBindingProvider implements JeeLinkEC3KBindingProvider {
	private Map<String, JeeLinkEC3KBindingConfig> addressMap = new HashMap<String, JeeLinkEC3KBindingConfig>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "jeelinkec3k";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// +
		// "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		String id = StringUtils.substringBefore(bindingConfig, ";");
		String type = StringUtils.substringAfter(bindingConfig, ";");

		JeeLinkEC3KBindingConfig config = null;
		if (addressMap.containsKey(id)) {
			config = addressMap.get(id);
		} else {
			config = new JeeLinkEC3KBindingConfig(id);
		}

		if (type.equals("cw")) {
			config.setCurrentWattItem(item);
		}

		if (type.equals("mw")) {
			config.setMaxWattItem(item);
		}

		if (type.equals("tc")) {
			config.setTotalConsumptionItem(item);
		}

		if (type.equals("lu")) {
			config.setLastUpdatedItem(item);
		}

		if (type.equals("ct")) {
			config.setConsumptionTodayItem(item);
		}

		if (type.equals("rt")) {
			config.setSwitchRealTimeItem(item);
		}

		if (type.equals("pt")) {
			config.setPriceToday(item);
		}

		addressMap.put(id, config);
		addBindingConfig(item, config);
	}

	public JeeLinkEC3KBindingConfig getConfigForAddress(String address) {
		return addressMap.get(address);
	}

	public JeeLinkEC3KBindingConfig getConfigForItemName(String itemName) {
		if (super.bindingConfigs.containsKey(itemName)) {
			return (JeeLinkEC3KBindingConfig) super.bindingConfigs
					.get(itemName);
		}
		return null;
	}

}
