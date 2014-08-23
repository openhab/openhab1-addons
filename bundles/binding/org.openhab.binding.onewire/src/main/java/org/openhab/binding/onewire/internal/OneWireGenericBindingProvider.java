/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

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
	private static final TreeSet<String> filterTypes = new TreeSet<String>(Arrays.asList("tukey"));
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
		parseParameters(config, configParts[1]); // extract unit and parameters
		addBindingConfig(item, config);
		
		Set<Item> items = contextMap.get(context);
		if (items == null) {
			items = new HashSet<Item>();
			contextMap.put(context, items);
		}
		items.add(item);
	}
	/**
	 * Parses everything after the # separator. Parameters such as filters are
	 * separated by the pipe '|' character.
	 * @param config reference to configuration to be set
	 * @param unitString binding configuration text after the '#' separator
	 * @throws BindingConfigParseException thrown if parameters are not of the form key=value
	 */
	private void parseParameters(OneWireBindingConfig config, String unitString) throws BindingConfigParseException {
		String[] unitParts = unitString.split("\\|");
		if (unitParts.length > 1) {
			// parameters are present, parse them
			config.unit = unitParts[0]; // first token is just the unit 
			for (int i = 1; i < unitParts.length; i++) {
				String[] keyValue = unitParts[i].trim().split("=");
				if (keyValue.length != 2) {
					throw new BindingConfigParseException("Onewire sensor parameters " +
							"must be of form parameter=value, you have: " + unitParts[i].trim());
				}
				if (keyValue[0].equals("filter")) {
					if (filterTypes.contains(keyValue[1])) {
						config.filter = keyValue[1];
					} else {
						throw new BindingConfigParseException("Onewire sensor unknown filter type: " + keyValue[1]);
					}
				} else {
					throw new BindingConfigParseException("Onewire sensor unknown parameter: " + keyValue[0]);
				}
			}
		} else {
			// unit string has no parameters
			config.unit = unitString;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSensorId(String itemName) {
		OneWireBindingConfig config = (OneWireBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.sensorId : null;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getUnitId(String itemName) {
		OneWireBindingConfig config = (OneWireBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.unit : null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFilter(String itemName) {
		OneWireBindingConfig config = (OneWireBindingConfig) bindingConfigs.get(itemName);
		return config != null ? config.filter : null;
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
		public String filter = null;
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
