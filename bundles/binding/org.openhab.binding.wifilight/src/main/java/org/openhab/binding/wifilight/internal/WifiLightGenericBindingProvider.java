/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wifilight.internal;


import org.openhab.binding.wifilight.WifiLightBindingProvider;
import org.openhab.binding.wifilight.internal.WifiLightBindingConfig.BindingType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ColorItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author magcode
 * @since 1.3.0
 */

public class WifiLightGenericBindingProvider extends AbstractGenericBindingProvider implements WifiLightBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(WifiLightGenericBindingProvider.class);

	@Override
	public String getBindingType() {
		return "wifilight";
	}

	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {

		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof ColorItem)) {
			throw new BindingConfigParseException(
					"Item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems, DimmerItems and ColorItems are allowed - please check your *.items configuration");
		}

	}

	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		try {
			if (bindingConfig != null) {
				String[] configParts = bindingConfig.split(":");
				if (configParts.length != 2) {
					throw new BindingConfigParseException(
							"WifiLight binding configuration must not have more than four parts");
				}

				if (item instanceof ColorItem) {
					BindingConfig milightBindingConfig = (BindingConfig) new WifiLightBindingConfig(
							configParts[0], BindingType.rgb.name(), null );
					addBindingConfig(item, milightBindingConfig);
				} else if (item instanceof DimmerItem || item instanceof SwitchItem) {
						BindingConfig milightBindingConfig = (BindingConfig) new WifiLightBindingConfig(
								configParts[0], configParts[1], null);
						addBindingConfig(item,milightBindingConfig);
				}

			} else {
				logger.warn("bindingConfig is NULL (item=" + item
						+ ") -> processing bindingConfig aborted!");
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			logger.warn("bindingConfig is invalid (item=" + item
					+ ") -> processing bindingConfig aborted!");
		}

	}

	@Override
	public WifiLightBindingConfig getItemConfig(String itemName) {
		return (WifiLightBindingConfig) bindingConfigs.get(itemName);
	}

}
