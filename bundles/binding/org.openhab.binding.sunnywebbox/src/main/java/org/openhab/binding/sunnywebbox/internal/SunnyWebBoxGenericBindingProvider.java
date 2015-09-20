/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
 
package org.openhab.binding.sunnywebbox.internal;

import org.openhab.binding.sunnywebbox.SunnyWebBoxBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Manolis Nikiforakis
 * @author Yiannis Gkoufas
 * @since 1.5.0
 */
public class SunnyWebBoxGenericBindingProvider extends AbstractGenericBindingProvider implements SunnyWebBoxBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "sunnywebbox";
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		SunnyWebBoxBindingConfig config = new SunnyWebBoxBindingConfig();
		
		String[] values = bindingConfig.split(",");
		config.deviceId = values[0];
		config.meta = values[1];
		if(values.length>2)
		{
			config.urlKey = values[2];
		}
		
		addBindingConfig(item, config);		
	}
	
	
	class SunnyWebBoxBindingConfig implements BindingConfig {
		// put member fields here which holds the parsed values
		String deviceId;
		String meta;
		String urlKey;
		
	}

	@Override
	public String getDeviceId(String itemName) {
		SunnyWebBoxBindingConfig config = (SunnyWebBoxBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.deviceId : null;
	}
	
	@Override
	public String getMeta(String itemName) {
		SunnyWebBoxBindingConfig config = (SunnyWebBoxBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.meta : null;
	}
	
	@Override
	public String getUrlKey(String itemName) {
		SunnyWebBoxBindingConfig config = (SunnyWebBoxBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.urlKey : null;
	}
	
	
}
