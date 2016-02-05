/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.noolite.internal;

import org.openhab.binding.noolite.NooliteBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Petr Shatsillo
 * @since 1.0.0
 */
public class NooliteGenericBindingProvider extends AbstractGenericBindingProvider implements NooliteBindingProvider {
	
	private static final Logger logger = LoggerFactory
			.getLogger(NooliteGenericBindingProvider.class);

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "noolite";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		// if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		// throw new BindingConfigParseException("item '" + item.getName()
		// + "' is of type '" + item.getClass().getSimpleName()
		// + "', only Switch- and DimmerItems are allowed - please check your
		// *.items configuration");
		// }
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig)
			throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		if (bindingConfig != null) {
			NooliteBindingConfig config = new NooliteBindingConfig();

			config.itemType = item.getClass();
			String[] configParts = bindingConfig.trim().split(":");
			
			config.type = configParts.length > 0 ? configParts[0] : "NO_TYPE";
			config.channel = configParts.length > 1 ? configParts[1] : "NO_CHANNEL";
			config.deviceType = configParts.length > 2 ? configParts[2] : "NO_DEVICE_TYPE";
			

			addBindingConfig(item, config);
		} else {
			logger.warn("bindingConfig is NULL (item=" + item
					+ ") -> process bindingConfig aborted!");
		}
	}

	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Petros
	 * @since 1.0.0
	 */
	class NooliteBindingConfig implements BindingConfig {
		Class<? extends Item> itemType;
		public String type;
		public String channel;
		public String deviceType;
	}

	@Override
	public String getChannel(String itemName) {
		NooliteBindingConfig config = (NooliteBindingConfig) bindingConfigs
				.get(itemName);
		return config.channel;
	}

	@Override
	public String getType(String itemName) {
		NooliteBindingConfig config = (NooliteBindingConfig) bindingConfigs
				.get(itemName);
		return config.type;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		NooliteBindingConfig config = (NooliteBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public String getDeviceType(String itemName) {
		NooliteBindingConfig config = (NooliteBindingConfig) bindingConfigs
				.get(itemName);
		return config != null ? config.deviceType : null;
	}

}
