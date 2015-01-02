/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.frontiersiliconradio.internal;


import org.openhab.binding.frontiersiliconradio.FrontierSiliconRadioBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Rainer Ostendorf
 * @since 1.7.0
 */
public class FrontierSiliconRadioGenericBindingProvider extends AbstractGenericBindingProvider implements FrontierSiliconRadioBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "frontiersiliconradio";
	}
	
	public String getProperty( String itemName ) {
		FrontierSiliconRadioBindingConfig config = (FrontierSiliconRadioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.property : null;
	}
	
	public String getDeviceID( String itemName ) {
		FrontierSiliconRadioBindingConfig config = (FrontierSiliconRadioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.deviceId : null;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		FrontierSiliconRadioBindingConfig config = new FrontierSiliconRadioBindingConfig();
		
		String[] configParts = bindingConfig.trim().split(":");
		if (configParts.length != 2) {
			throw new BindingConfigParseException("FrontierSiliconRadio configuration must contain of two parts separated by a ':'");
		}
		
		config.deviceId = configParts[0];
		config.property = configParts[1];
		config.itemType = item.getClass();
		
		addBindingConfig(item, config);		
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		FrontierSiliconRadioBindingConfig config = (FrontierSiliconRadioBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.itemType : null;
	}
	
	
	class FrontierSiliconRadioBindingConfig implements BindingConfig {
		// device id, identifying the radio, e.g. "RadioSleepingroom"
		String deviceId;
		
		// Parameter, e.g. "Power", "Mute" or "Volume"
		String property; 
		
		// item type
		Class<? extends Item> itemType;
	}
}
