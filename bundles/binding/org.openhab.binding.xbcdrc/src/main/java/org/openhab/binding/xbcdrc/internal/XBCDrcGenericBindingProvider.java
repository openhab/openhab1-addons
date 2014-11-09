/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.xbcdrc.internal;

import java.util.Map;


import org.openhab.binding.xbcdrc.XBCDrcBindingConfig;
import org.openhab.binding.xbcdrc.XBCDrcBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author magcode
 * @since 1.6.0-SNAPSHOT
 */
public class XBCDrcGenericBindingProvider extends
		AbstractGenericBindingProvider implements XBCDrcBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "xbcdrc";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof SwitchItem))
			throw new BindingConfigParseException(
					"item '"
							+ item.getName()
							+ "' is of type '"
							+ item.getClass().getSimpleName()
							+ "', only SwitchItems are allowed - please check your *.items configuration");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item,
			String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		XBCDrcBindingConfig config = new XBCDrcBindingConfig(bindingConfig.toUpperCase(),item);
		addBindingConfig(item, config);
	}

	public XBCDrcBindingConfig getConfigForRcCode(String rcCode) {
		for (Map.Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			XBCDrcBindingConfig config = (XBCDrcBindingConfig) entry.getValue();
			if (config.getRcCode().equals(rcCode)) {
				return config;
			}
		}

		return null;
	}
}
