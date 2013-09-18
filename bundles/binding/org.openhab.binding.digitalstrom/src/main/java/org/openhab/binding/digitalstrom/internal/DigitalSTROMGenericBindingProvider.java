/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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