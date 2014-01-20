/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.systeminfo.internal;

import org.openhab.binding.systeminfo.SysteminfoBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class SysteminfoGenericBindingProvider extends
		AbstractGenericBindingProvider implements SysteminfoBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "systeminfo";
	}

	/**
	 * @{inheritDoc
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException(
					"item '" + item.getName() + "' is of type '" + item.getClass().getSimpleName()
					+ "', only NumberItem and StringItem are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);

		SysteminfoBindingConfig config = new SysteminfoBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		if (configParts.length < 2 && configParts.length > 3) {
			throw new BindingConfigParseException("Systeminf binding must contain 2-3 parts separated by ':'");
		}

		String commandType = configParts[0].trim();
		
		try {
			config.commandType = SysteminfoCommandType.getCommandType(commandType);	
		} catch (IllegalArgumentException e) {
			throw new BindingConfigParseException("'" + commandType + "' is not a valid command type" );
		}
		
		try {
			config.refreshInterval = Integer.valueOf(configParts[1]);
		} catch (NumberFormatException e) {
			throw new BindingConfigParseException("'" + configParts[1] + "' is not a valid refresh interval" );
		}

		if (configParts.length > 2) {
			config.target = configParts[2].trim();
		}

		addBindingConfig(item, config);
	}

	@Override
	public SysteminfoCommandType getCommandType(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public int getRefreshInterval(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.refreshInterval : 0;
	}

	@Override
	public String getTarget(String itemName) {
		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.target : null;
	}
	
	
	class SysteminfoBindingConfig implements BindingConfig {

		public Class<? extends Item> itemType = null;
		public SysteminfoCommandType commandType;
		public int refreshInterval = 0;
		public String target = null;

		@Override
		public String toString() {
			return "SysteminfoBindingConfigElement [" + ", itemType=" + itemType
					+ ", commandType=" + commandType + ", refreshInterval="
					+ refreshInterval + ", target=" + target + "]";
		}

	}
	
}
