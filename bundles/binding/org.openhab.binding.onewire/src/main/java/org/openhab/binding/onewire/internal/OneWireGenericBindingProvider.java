/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal;

import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides OneWire binding information from it. It registers as a 
 * {@link OneWireBindingProvider} service as well.</p>
 * 
 * <p>The syntax of the binding configuration strings accepted is the following:<p>
 * <p><code>
 * 	onewire="&lt;familyCode&gt;.&lt;serialId&gt;#temperature|humidity"
 * </code></p>
 * where 'temperature' or 'humidity' classifies whether the sensor's value should be 
 * interpreted as temperature (unit '°C') or as humidity (unit '%') value.
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>onewire="26.AF9C32000000#temperature"</code></li>
 * 	<li><code>onewire="26.AF9C32000000#humidity"</code></li>
 * </ul>
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 0.6.0
 */
public class OneWireGenericBindingProvider extends AbstractGenericBindingProvider implements OneWireBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "onewire";
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {
		if ((item instanceof NumberItem) || (item instanceof ContactItem) || (item instanceof SwitchItem)) {
			return;
		}
		throw new BindingConfigParseException("item '" + item.getName()
			+ "' is of type '" + item.getClass().getSimpleName()
			+ "', only Number- Contact- and Switch type is allowed - please check your *.items configuration");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		String[] configParts = bindingConfig.trim().split("#");
		if (configParts.length != 2) {
			throw new BindingConfigParseException("Onewire sensor configuration must contain of two parts separated by a '#'");
		}
		
		OneWireBindingConfig config = new OneWireBindingConfig();
		
		config.sensorId = configParts[0];
		config.unit = configParts[1];
									
		addBindingConfig(item, config);
		
		Set<Item> items = contextMap.get(context);
		if (items == null) {
			items = new HashSet<Item>();
			contextMap.put(context, items);
		}
		items.add(item);
	}
		
	
	/**
	 * {@inheritDoc}
	 */
	public String getSensorId(String itemName) {
		OneWireBindingConfig config = (OneWireBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.sensorId : null;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getUnitId(String itemName) {
		OneWireBindingConfig config = (OneWireBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.unit : null;
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the OneWire binding 
	 * provider.
	 * 
	 * @author Thomas.Eichstaedt-Engelen
	 */
	static private class OneWireBindingConfig implements BindingConfig {
		public String sensorId;
		public String unit;
	}


	@Override
	public Item getItem(String itemName) {
		for (Set<Item> items : contextMap.values()) {
			if (items != null) {
				for (Item item : items) {
					if (itemName.equals(item.getName())) {
						return item;
					}
				}
			}
		}
		return null;
	}	
	

}
