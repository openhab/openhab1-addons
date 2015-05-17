/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
		if( item == null ) {
			throw new BindingConfigParseException(
					"item is not permitted to be null.  item must be a non-null NumberItem or StringItem - please check your *.items configuration");
		}
		else if (!(item instanceof NumberItem || item instanceof StringItem)) {
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

		if( item == null ) {
			throw new BindingConfigParseException("item is not permitted to be null");
		}
		else if( bindingConfig == null ) {
			throw new BindingConfigParseException("bindingConfig is not permitted to be null");
		}

		SysteminfoBindingConfig config = new SysteminfoBindingConfig();

		String[] configParts = bindingConfig.trim().split(":");

		if(configParts.length < 2) {
			throw new BindingConfigParseException("Systeminfo binding must contain at least 2 parts separated by ':'");
		}

		String commandType = configParts[0].trim();
		if( configParts.length > 3 ) {
			try {
				int index1 = bindingConfig.indexOf(":");
				int index2 = bindingConfig.indexOf(":", index1+1);
				if( index1 > 0 && index2 > index1+1) {
					config.target = bindingConfig.substring(index2+1);
				}
				else {
					throw new BindingConfigParseException("Systeminfo binding must contain 2-3 parts separated by ':'");
				}
			}catch(Exception e) {
				throw new BindingConfigParseException("Systeminfo binding must contain 2-3 parts separated by ':'");
			 }
		}

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

		if( config.target == null ) {
			if (configParts.length > 2) {
				config.target = configParts[2].trim();
			}
		}

		addBindingConfig(item, config);
	}

	@Override
	public SysteminfoCommandType getCommandType(String itemName) {
		if( itemName == null ) {
			return null;
		}

		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.commandType : null;
	}

	@Override
	public Class<? extends Item> getItemType(String itemName) {
		if( itemName == null ) {
			return null;
		}

		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}

	@Override
	public int getRefreshInterval(String itemName) {
		if( itemName == null ) {
			return 0;
		}

		SysteminfoBindingConfig config = (SysteminfoBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.refreshInterval : 0;
	}

	@Override
	public String getTarget(String itemName) {
		if( itemName == null ) {
			return null;
		}

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
