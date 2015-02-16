/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire_cuno.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.onewire_cuno.OnewireCunoBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Robert Delbrück
 * @since 1.7.0
 */
public class OnewireCunoGenericBindingProvider extends AbstractGenericBindingProvider
		implements OnewireCunoBindingProvider {
	private static final Logger LOG = LoggerFactory.getLogger(OnewireCunoGenericBindingProvider.class);

	private Map<String, OnewireCunoBindingConfig> addressMap = new HashMap<String, OnewireCunoBindingConfig>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "onewireCuno";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only NumberItems are allowed - please check your *.items configuration");
		}

		if (bindingConfig.length() != 4) {
			throw new BindingConfigParseException(
					"The configured address must consist of just 2 bytes housecode");
		}
	}

	/**
	 * Binding config is in the style of {onewire_cuno="HHHH"} {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		OnewireCunoBindingConfig config = new OnewireCunoBindingConfig(bindingConfig, item);

		// parse bindingconfig here ...
		LOG.debug("adding item with address: {}", config.getAddress());
		addressMap.put(config.getAddress(), config);
		addBindingConfig(item, config);
	}

	@Override
	public OnewireCunoBindingConfig getConfigForItemName(String itemName) {
		if (super.bindingConfigs.containsKey(itemName)) {
			return (OnewireCunoBindingConfig) super.bindingConfigs.get(itemName);
		}
		return null;
	}

	@Override
	public OnewireCunoBindingConfig getConfigForAddress(String address) {
		return addressMap.get(address);
	}

}
