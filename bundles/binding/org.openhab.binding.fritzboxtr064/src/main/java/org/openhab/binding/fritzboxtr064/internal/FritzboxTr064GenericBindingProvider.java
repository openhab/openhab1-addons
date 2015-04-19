/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fritzboxtr064.internal;

import org.openhab.binding.fritzboxtr064.FritzboxTr064BindingProvider;
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
 * @author Boris Bock
 * @since 0.1.0
 */
public class FritzboxTr064GenericBindingProvider extends AbstractGenericBindingProvider implements FritzboxTr064BindingProvider {
	
	//to access logger object in this class
	static final Logger logger = LoggerFactory.getLogger(FritzboxTr064GenericBindingProvider.class);

	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "fritzboxtr064";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		//if (!(item instanceof SwitchItem || item instanceof DimmerItem)) {
		//	throw new BindingConfigParseException("item '" + item.getName()
		//			+ "' is of type '" + item.getClass().getSimpleName()
		//			+ "', only Switch- and DimmerItems are allowed - please check your *.items configuration");
		//}
	}
	
	public FritzboxTr064GenericBindingProvider() {
		super();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		if(bindingConfig == null){
			throw new BindingConfigParseException("binding Config String in *.cfg is null for "+item.getName());
		}
		FritzboxTr064BindingConfig config = new FritzboxTr064BindingConfig(item.getClass(), bindingConfig);
		
		addBindingConfig(item, config);
		logger.debug("Adding item "+item.getName() + " with config " +config.getConfigString());
	}
	
	
	/**
	 * This is a helper class holding binding specific configuration details
	 * 
	 * @author Boris Bock
	 * @since 0.1.0
	 */
	public class FritzboxTr064BindingConfig implements BindingConfig {
		private Class<? extends Item> itemType; //type is expressed as class instance
		private String configString;
		
		
		public FritzboxTr064BindingConfig(Class<? extends Item> type, String configString) {
			this.itemType = type;
			this.configString = configString;
		}


		public Class<? extends Item> getItemType() {
			return itemType;
		}


		public String getConfigString() {
			return configString;
		}


		@Override
		public String toString() {
			return "FritzboxTr064BindingConfig [itemType=" + itemType
					+ ", configString=" + configString + "]";
		}
		
		
		
	}


	@Override
	public FritzboxTr064BindingConfig getBindingConfigByItemName(String itemName) {
		FritzboxTr064BindingConfig conf = null;

		if (bindingConfigs.containsKey(itemName)){
			conf = (FritzboxTr064BindingConfig) bindingConfigs.get(itemName);
			
		}
		return conf;
	}
	
	
}
