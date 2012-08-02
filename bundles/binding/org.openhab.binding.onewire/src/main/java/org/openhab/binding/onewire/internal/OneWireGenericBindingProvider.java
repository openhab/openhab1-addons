/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.onewire.internal;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
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
		if (!(item instanceof NumberItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only NumberItems are allowed - please check your *.items configuration");
		}
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
	

}
