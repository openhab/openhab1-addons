/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wemo.internal;

import org.openhab.binding.wemo.WemoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Hans-Jörg Merk
 * @since 1.6.0
 */
public class WemoGenericBindingProvider extends AbstractGenericBindingProvider implements WemoBindingProvider {

	static final Logger logger = LoggerFactory
			.getLogger(WemoGenericBindingProvider.class);

	/**
	 * The friendly name given to your Wemo switch.
	 */
	public String wemoFriendlyName;

	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "wemo";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch- and NumberItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		try {
			if (bindingConfig != null) {
				WemoBindingConfig config = new WemoBindingConfig();
				item.getName();
				config.wemoFriendlyName = bindingConfig;
				addBindingConfig(item,config);
		
	} else {
		logger.warn("bindingConfig is NULL (item=" + item
				+ ") -> processing bindingConfig aborted!");
		}
	} catch (ArrayIndexOutOfBoundsException e) {
		logger.warn("bindingConfig is invalid (item=" + item
				+ ") -> processing bindingConfig aborted!");
		}
	
	}
	
	public String getWemoFriendlyName(String itemName) {
		WemoBindingConfig config = (WemoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.wemoFriendlyName : null;
	}
	
	static private class WemoBindingConfig implements BindingConfig {
	
		public String wemoFriendlyName;
	
	}
	
}
