/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.freebox.internal;

import org.apache.commons.lang.StringUtils;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.binding.freebox.FreeboxBindingProvider;
import org.openhab.binding.freebox.FreeboxBindingConfig;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * The syntax of the binding configuration strings accepted is the following: <br>
 * freebox="commandtype" <br>
 * where "commandtype" matches one of the enumerated commands present in
 * org.openhab.binding.freebox.internal.CommandType
 * 
 * @author clinique
 * @since 1.5.0
 */
public class FreeboxGenericBindingProvider extends AbstractGenericBindingProvider implements FreeboxBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "freebox";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(	item instanceof SwitchItem || 
				item instanceof NumberItem ||
				item instanceof DateTimeItem ||
				item instanceof StringItem) ) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only String, Switch and Number are allowed - please check your *.items configuration");
		}		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		FreeboxBindingConfig config = parseBindingConfig(bindingConfig,item);
		addBindingConfig(item, config);	
	}
	
	private FreeboxBindingConfig parseBindingConfig(String bindingConfig, Item item) throws BindingConfigParseException {

		String command = StringUtils.trim(bindingConfig);
		CommandType commandType = CommandType.fromString(command);

		return new FreeboxBindingConfig(commandType, item);
	}
	

	@Override
	public FreeboxBindingConfig getConfig(String itemName) {
		FreeboxBindingConfig config = (FreeboxBindingConfig) bindingConfigs.get(itemName);
		return config;
	}
	
	
}

