/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelink.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.jeelink.JeeLinkBindingConfig;
import org.openhab.binding.jeelink.JeeLinkBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class JeeLinkGenericBindingProvider extends AbstractGenericBindingProvider
		implements JeeLinkBindingProvider {

	private Map<String, JeeLinkBindingConfig> addressMap = new HashMap<String, JeeLinkBindingConfig>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "jeelink";
	}

	/**
	 * @{inheritDoc
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {


	}

	/**
	 * Binding config is in the style of {jeelink="HHHHAA"} {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		JeeLinkBindingConfig config = new JeeLinkBindingConfig(bindingConfig, item);

		// parse bindingconfig here ...
		addressMap.put(config.getAddress(), config);
		addBindingConfig(item, config);
	}


	public JeeLinkBindingConfig getConfigForItemName(String itemName) {
		if (super.bindingConfigs.containsKey(itemName)) {
			return (JeeLinkBindingConfig) super.bindingConfigs.get(itemName);
		}
		return null;
	}
	


	public JeeLinkBindingConfig getConfigForAddress(String address) {
		return addressMap.get(address);
	}

}
