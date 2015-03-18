/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.resolvbus.internal;

import java.util.HashMap;
import java.util.Map;

import org.openhab.binding.resolvbus.ResolVBUSBindingProvider;
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
 * @author Michael Heckmann
 * @since 1.7.0
 */
public class ResolVBUSGenericBindingProvider extends AbstractGenericBindingProvider implements ResolVBUSBindingProvider {
	
	static final Logger logger = LoggerFactory.getLogger(ResolVBUSGenericBindingProvider.class);
	private Map<String, Item> items = new HashMap<String, Item>();

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "resolvbus";
	}

	/**
	 * @{inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		ResolVBUSBindingConfig config = new ResolVBUSBindingConfig(bindingConfig);
		
		//parse bindingconfig here ...
		items.put(item.getName(), item);
		addBindingConfig(item, config);		
	}
	
	public String getName(String itemName) {
		ResolVBUSBindingConfig config = (ResolVBUSBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.name : null;
	}
	
	public Item getItem(String itemName) {
		return items.get(itemName);
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Michael Heckmann
	 * @since 1.7.0
	 */
	class ResolVBUSBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		public String name;
		
		public ResolVBUSBindingConfig(String name) {
			this.name = name;
		}
		
	}
	
	
}
