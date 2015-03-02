/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

import org.openhab.binding.smlreader.SmlReaderBindingProvider;
import org.openhab.binding.smlreader.conversion.IUnitConverter;
import org.openhab.binding.smlreader.conversion.KiloConverter;
import org.openhab.binding.smlreader.conversion.TargetConversion;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * This class is responsible for parsing the binding configuration.
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public class SmlReaderGenericBindingProvider extends AbstractGenericBindingProvider implements SmlReaderBindingProvider {

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
		
		String[] configSections = bindingConfig.trim().split(",");
		// "deviceId#edl300,obis#1-0:2.8.0,conversion#kWh"		

		for(String configSection : configSections){
			
			String[] keyValues = configSection.split("#");
			
			String configName = keyValues[0];
			String configValue = keyValues[1];
			
			switch(configName){
			case SmlReaderConstants.Configuration.ITEM_DEFINITION_DEVICE_ID:
				config.deviceId = configValue;
				break;
			case SmlReaderConstants.Configuration.ITEM_DEFINITION_OBIS:
				config.obis = configValue;
				break;
			case SmlReaderConstants.Configuration.ITEM_DEFINITION_CONVERSION:
				config.targetConversion = configValue == null || configValue == "" ? TargetConversion.none : TargetConversion.valueOf(configValue);
				break;
			}
		}

		config.itemType = item.getClass();
		addBindingConfig(item, config);		
	}
	
	class SmlReaderBindingConfig implements BindingConfig {
		public String obis;
		public String deviceId;
		public TargetConversion targetConversion;
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
	@SuppressWarnings("rawtypes")
	public Class<? extends IUnitConverter> getUnitConverter(String itemName) {
		Class<? extends IUnitConverter> converterType = null;
		TargetConversion targetConversion = TargetConversion.none;
		SmlReaderBindingConfig config = (SmlReaderBindingConfig)bindingConfigs.get(itemName);

		if(config != null){
			targetConversion = config.targetConversion;
			
			switch(targetConversion){
				case kilo:
					converterType = KiloConverter.class;
					break;
				default:
				case none:
					converterType = null;
					break;
			}
		}		
		return converterType;
	}	
}
