/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010, openHAB.org <admin@openhab.org>
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.strandbygaard.onewire.device.OwSensor.Reading;

import org.openhab.binding.onewire.OneWireBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.model.item.binding.BindingConfigReader;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides OneWire binding information from it. It registers as a 
 * {@link OneWireBindingProvider} service as well.</p>
 * 
 * <p>The syntax of the binding configuration strings accepted is the following:<p>
 * <p><code>
 * 	onewire="&lt;familyCode&gt;.&lt;serialId&gt;#temp|hum"
 * </code></p>
 * where 'temp' or 'hum' classifies whether the sensor's value should be 
 * interpreted as temperature (unit '¡C') or as humidity (unit '%') value.
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>onewire="26.AF9C32000000#temp"</code></li>
 * 	<li><code>onewire="26.AF9C32000000#hum"</code></li>
 * </ul>
 * 
 * @author thomasee
 * @since 0.6.0
 */
public class OneWireBinding implements OneWireBindingProvider, BindingConfigReader {

	/** the binding type to register for as a binding config reader */
	public static final String BINDING_TYPE = "onewire";

	/** caches binding configurations. maps itemNames to {@link BindingConfig}s */
	private Map<String, BindingConfig> owDeviceConfigs = new HashMap<String, BindingConfig>();

	/** 
	 * stores information about the context of items. The map has this content
	 * structure: context -> Set of itemNames
	 */ 
	private Map<String, Set<String>> contextMap = new HashMap<String, Set<String>>();
		

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getBindingType() {
		return BINDING_TYPE;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		
		if (item instanceof NumberItem) {
			
			String[] configParts = bindingConfig.trim().split("#");
			if (configParts.length != 2) {
				throw new IllegalArgumentException("Onewire sensor configuration must contain of two parts separated by a '#'");
			}
			
			if (!checkSensorId(configParts[0])) {
				throw new IllegalArgumentException("SensorId '" + configParts[0] +
					"' isn't a correct id-pattern. A correct pattern looks like '26.AF9C32000000' (<familycode 8bit>.<serialid 48bit>)");
			}
			
			BindingConfig config = new BindingConfig();
			
			config.sensorId = configParts[0];
			config.unit = Reading.valueOf(configParts[1].trim().toUpperCase());
			config.itemName = item.getName();
										
			owDeviceConfigs.put(item.getName(), config);
		}
					
		Set<String> itemNames = contextMap.get(context);
		if(itemNames==null) {
			itemNames = new HashSet<String>();
			contextMap.put(context, itemNames);
		}
			
		itemNames.add(item.getName());
	}
	
	/**
	 * Checks statically (by regex) whether the given <code>sensorIdString</code>
	 * is a correct pattern to configure OneWire-Sensors. A correct pattern looks
	 * like '26.AF9C32000000' (<familycode 8bit>.<serialid 48bit>).
	 * 
	 * @param sensorIdString the sensor to check
	 * 
	 * @return <code>true</code> if the given sensorIdString is configured 
	 * correctly and <code>false</code> otherwise
	 */
	protected boolean checkSensorId(String sensorIdString) {
		return sensorIdString.matches("\\d{2}\\.[A-F0-9]{12}");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeConfigurations(String context) {
		Set<String> itemNames = contextMap.get(context);
		if(itemNames!=null) {
			for(String itemName : itemNames) {
				// we remove all information in the serial devices
				BindingConfig owDeviceConfig = owDeviceConfigs.get(itemName);
				owDeviceConfigs.remove(owDeviceConfig);
			}
			contextMap.remove(context);
		}
	}
	
	
	/**
	 * This is an internal data structure to store information from the binding
	 * config strings and use it to answer the requests to the KNX binding 
	 * provider.
	 * 
	 * @author thomasee
	 */
	private class BindingConfig {
		public String itemName;
		public String sensorId;
		public Reading unit;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getSensorId(String itemName) {
		return owDeviceConfigs.get(itemName).sensorId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Reading getUnitId(String itemName) {
		return owDeviceConfigs.get(itemName).unit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterable<String> getItemNames() {
		return owDeviceConfigs.keySet();
	}
	

}
