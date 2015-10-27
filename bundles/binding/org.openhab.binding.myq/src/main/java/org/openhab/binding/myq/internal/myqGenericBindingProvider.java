/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.myq.internal;

import org.openhab.binding.myq.myqBindingProvider;
import org.openhab.binding.myq.internal.myqGenericBindingProvider.myqBindingConfig.ITEMTYPE;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
//import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author scooter_seh
 * @since 1.8.0
 */
public class myqGenericBindingProvider extends AbstractGenericBindingProvider implements myqBindingProvider 
{

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "myq";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException 
	{
		if (!(item instanceof SwitchItem || item instanceof ContactItem|| item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
				+ "', only SwitchItems, ContactItem or StringItem are allowed "
				+ "- please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException 
	{
		super.processBindingConfiguration(context, item, bindingConfig);
		myqBindingConfig config = parseBindingConfig(item, bindingConfig);
		
		//parse bindingconfig here ...
		
		addBindingConfig(item, config);		
	}
	
	private myqBindingConfig parseBindingConfig(Item item, String bindingConfig) throws BindingConfigParseException {
		final myqBindingConfig config = new myqBindingConfig();

		if(item instanceof SwitchItem)
			config.type = ITEMTYPE.Switch;
		else if(item instanceof ContactItem)
			config.type = ITEMTYPE.ContactStatus;
		else if(item instanceof StringItem)
			config.type = ITEMTYPE.StringStatus;
		config.MyQName = bindingConfig;

		return config;
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author scooter_seh
	 * @since 1.8.0
	 */
	static class myqBindingConfig implements BindingConfig 
	{
		// put member fields here which holds the parsed values
		enum ITEMTYPE {	Switch, StringStatus, ContactStatus
		};
		
		ITEMTYPE type;
		String id;
		String MyQName;		
		
	}
	
}
