/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard.internal;

import java.util.Map;
import java.util.Set;

import org.openhab.binding.ucprelayboard.UCPRelayBoardBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author Robert Michalak
 * @since 1.8.0
 */
public class UCPRelayBoardGenericBindingProvider extends
		AbstractGenericBindingProvider implements UCPRelayBoardBindingProvider {

	@Override
	public String getBindingType() {
		return "ucprelayboard";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {

		super.processBindingConfiguration(context, item, bindingConfig);

		UCPRelayConfig config = UCPRelayConfig.fromString(bindingConfig);

		addBindingConfig(item, config);

	}

	@Override
	public UCPRelayConfig getRelayConfigForItem(String itemName) {
		return (UCPRelayConfig) bindingConfigs.get(itemName);
	}

	@Override
	public Item getItemForRelayConfig(UCPRelayConfig config) {
		for (Map.Entry<String, BindingConfig> bindingConfig : bindingConfigs.entrySet()) {
			if (bindingConfig.getValue().equals(config)) {
				for (Map.Entry<String, Set<Item>> context : contextMap.entrySet()) {
					for (Item item : context.getValue()) {
						if (item.getName().equals(bindingConfig.getKey())) {
							return item;
						}
					}
				}
			}
		}
		return null;
	}
}
