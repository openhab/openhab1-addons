/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import java.util.regex.Matcher;
import org.openhab.binding.smlreader.SmlReaderBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for parsing the binding configuration.
 * 
 * <p>Here are some examples for valid binding configuration strings: 
 * <ul> 
 * <li><code>{ smlreader="device=edl300,obis=1-0:2.8.0,transform=JS(myConversion.js)" }</code></li> 
 * <li><code>{ smlreader="device=edl300,obis=1-0:2.8.0" }</code></li> 
 * </ul>
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public class SmlReaderGenericBindingProvider extends AbstractGenericBindingProvider implements SmlReaderBindingProvider {
	
	private static final Logger logger = LoggerFactory.getLogger(SmlReaderGenericBindingProvider.class);
	
	/**
	 * @{inheritDoc}
	 */
	public String getBindingType() {
		return "smlreader";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * @{inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		SmlReaderBindingConfig config = new SmlReaderBindingConfig();
		boolean withoutTransformation = false;
		
		logger.debug(bindingConfig);
		
		Matcher matcherItem = SmlReaderConstants.BINDING_PATTERN_TRANSFORM.matcher(bindingConfig.trim());
		
		if(!matcherItem.matches()){
			matcherItem = SmlReaderConstants.BINDING_PATTERN.matcher(bindingConfig.trim());
			
			if(!matcherItem.matches()){
				throw new BindingConfigParseException("bindingConfig '" + bindingConfig + "' doesn't contain a valid binding configuration");
			} else {
				withoutTransformation = true;
			}
		}
		
		matcherItem.reset();
		
		while (matcherItem.find()) { 
			config.itemType = item.getClass();
			config.deviceId = matcherItem.group(2);
			config.obis  = matcherItem.group(4);
			
			if(withoutTransformation){
				config.transformationType = null;
				config.transformationFunc = null;
			} else {
				config.transformationType = matcherItem.group(7);
				config.transformationFunc = matcherItem.group(8);
			}
			
			addBindingConfig(item, config);		
		}			
	}
	
	class SmlReaderBindingConfig implements BindingConfig {
		public String obis;
		public String deviceId;
		public String transformationType;
		public String transformationFunc;
		public Class<? extends Item> itemType;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getDeviceNameFromConfiguration(String itemName) {
		String deviceId = null;
		SmlReaderBindingConfig config = (SmlReaderBindingConfig)bindingConfigs.get(itemName);

		if(config != null){
			deviceId = config.deviceId;
		}	
	
		return deviceId;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public String getObis(String itemName) {
		String obis = null;
		SmlReaderBindingConfig config = (SmlReaderBindingConfig)bindingConfigs.get(itemName);

		if(config != null){
			obis = config.obis;
		}
		
		return obis;
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public Class<? extends Item> getItemType(String itemName) {
		SmlReaderBindingConfig config = (SmlReaderBindingConfig) bindingConfigs.get(itemName);
		
		return config != null ? config.itemType : null;
	}

	/**
	 * @{inheritDoc}
	 */
	public String getTransformationType(String itemName) {
		String transformationType = null;
		SmlReaderBindingConfig config = (SmlReaderBindingConfig)bindingConfigs.get(itemName);

		if(config != null){
			transformationType = config.transformationType;
		}
	
		return transformationType;
	}
	
	/**
	 * @{inheritDoc}
	 */
	public String getTransformationFunc(String itemName) {
		String transformationFunc = null;
		SmlReaderBindingConfig config = (SmlReaderBindingConfig)bindingConfigs.get(itemName);

		if(config != null){
			transformationFunc = config.transformationFunc;
		}
		
		return transformationFunc;
	}
}
