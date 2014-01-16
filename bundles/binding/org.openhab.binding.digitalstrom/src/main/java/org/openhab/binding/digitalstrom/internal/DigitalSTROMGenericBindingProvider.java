/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.digitalstrom.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openhab.binding.digitalstrom.DigitalSTROMBindingProvider;
import org.openhab.binding.digitalstrom.internal.config.DigitalSTROMBindingConfig;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;

/**
 * @author 	Alexander Betker
 * @author Alex Maier
 * @since 1.3.0
 * 
 */
public class DigitalSTROMGenericBindingProvider extends AbstractGenericBindingProvider implements DigitalSTROMBindingProvider {
	
	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "digitalstrom";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if (!(item instanceof SwitchItem || item instanceof DimmerItem || item instanceof StringItem || item instanceof NumberItem || item instanceof RollershutterItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Switch-, Dimmer-, String-, Number- and RollershutterItem are allowed - please check your *.items configuration");
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		addBindingConfig(item, parseBindingConfigString(context, item, bindingConfig));	
	}
	
	
	protected DigitalSTROMBindingConfig parseBindingConfigString(String context, Item item,
			String bindingConfig) throws BindingConfigParseException{
		
		DigitalSTROMBindingConfig configItem = new DigitalSTROMBindingConfig();
		configItem.init(item, bindingConfig);
		
		if (!configItem.isValid()) {
			throw new BindingConfigParseException("itemType mismatch ... wrong item:"+item.getName()+" for digitalstrom hardware");
		}
		return configItem;
	}

	@Override
	public DigitalSTROMBindingConfig getItemConfig(
			String itemName) {
		return (DigitalSTROMBindingConfig)bindingConfigs.get(itemName);
	}

	@Override
	public void removeConfigurations(String context) {
		super.removeConfigurations(context);
	}

	@Override
	public List<String> getItemNamesByDsid(String dsid) {
		List<String> itemNames = new ArrayList<String>();
		for (BindingConfig bindingConf : bindingConfigs.values()) {
			DigitalSTROMBindingConfig digitalSTROM2BindingConf = (DigitalSTROMBindingConfig)bindingConf;
			if (digitalSTROM2BindingConf.dsid !=null && digitalSTROM2BindingConf.dsid.getValue().equals(dsid)) {
				itemNames.add(digitalSTROM2BindingConf.itemName);
			}
		}
		return itemNames;
		
	}

	@Override
	public List<DigitalSTROMBindingConfig> getAllCircuitConsumptionItems() {
		List<DigitalSTROMBindingConfig> circuitConsumptionItems = new ArrayList<DigitalSTROMBindingConfig>();
		for (BindingConfig bindingConf : bindingConfigs.values()) {
			DigitalSTROMBindingConfig digitalSTROM2BindingConf = (DigitalSTROMBindingConfig)bindingConf;
			if (digitalSTROM2BindingConf.isValidMeterItem()) {
				circuitConsumptionItems.add(digitalSTROM2BindingConf);
			}
		}
		return circuitConsumptionItems;
	}

	@Override
	public List<DigitalSTROMBindingConfig> getAllDeviceConsumptionItems() {
		List<DigitalSTROMBindingConfig> deviceConsumptionItems = new ArrayList<DigitalSTROMBindingConfig>();
		for (BindingConfig bindingConf : bindingConfigs.values()) {
			DigitalSTROMBindingConfig digitalSTROM2BindingConf = (DigitalSTROMBindingConfig)bindingConf;
			if (digitalSTROM2BindingConf.isValidDeviceMeterItem()) {
				deviceConsumptionItems.add(digitalSTROM2BindingConf);
			}
		}
		return deviceConsumptionItems;
	}

	@Override
	public Set<Item> getItemNamesByContext(String context) {
		return new HashMap<String, Set<Item>>(super.contextMap).get(context);
	}
}