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
package org.openhab.binding.novelanheatpump.internal;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.openhab.binding.novelanheatpump.HeatPumpBindingProvider;
import org.openhab.binding.novelanheatpump.HeatpumpCommandType;
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <p>This class can parse information from the generic binding format and 
 * provides Heatpump binding information from it. It registers as a 
 * {@link HeatpumpBindingProvider} service as well.</p>
 * 
 * <p>Here are some examples for valid binding configuration strings:
 * <ul>
 * 	<li><code>{ novelanheatpump="temperature_outside"}</code> - receives status of the external thermometer</li>
 * 	<li><code>{ novelanheatpump="temperature_return"}</code> - receives status updates of the return temperature of the floor heating</li>
 * 	<li><code>{ novelanheatpump="temperature_reference_return"  }</code> - receives status updates the reference temperature of the floor heating</li>
 * 	<li><code>{ novelanheatpump="temperature_supplay"  }</code> - receives status updates the temperature supplayed by the floor heating</li>
 * 	<li><code>{ novelanheatpump="temperature_servicewater_reference"  }</code> - receives status updates the reference temperature of the servicewater</li>
 * 	<li><code>{ novelanheatpump="temperature_servicewater"  }</code> - receives status updates on the temperature of the servicewater</li>
 * 	<li><code>{ novelanheatpump="temperature_solar_collector" }</code> - receives status updates of the sensor in the solar collector</li>
 * 	<li><code>{ novelanheatpump="temperature_solar_storage"  }</code> - receives status updates of the solar storage</li>
 * 	<li><code>{ novelanheatpump="state"  }</code> - receives status updates of the state (what and since when)</li>
 * </ul>
 * </p>
 * 
 * @author Jan-Philipp Bolle
 * 
 * @since 1.0.0
 */
public class HeatPumpGenericBindingProvider extends AbstractGenericBindingProvider implements HeatPumpBindingProvider {
	
	static final Logger logger = LoggerFactory.getLogger(HeatPumpGenericBindingProvider.class);

	public String getBindingType() {
		return "novelanheatpump";
	}

	/**
	 * @{inheritDoc}
	 */
	public void validateItemType(Item item, String bindingConfig)
			throws BindingConfigParseException {
		if (!(item instanceof NumberItem || item instanceof StringItem)) {
			throw new BindingConfigParseException("item '" + item.getName()
					+ "' is of type '" + item.getClass().getSimpleName()
					+ "', only Number- and StringItems are allowed - please check your *.items configuration");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String heatpumpCommand) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, heatpumpCommand);
		if (heatpumpCommand != null) {
			HeatPumpBindingConfig config = parseBindingConfig(item, HeatpumpCommandType.fromString(heatpumpCommand));
			addBindingConfig(item, config);
		}
		else {
			logger.warn("bindingConfig is NULL (item=" + item + ") -> processing bindingConfig aborted!");
		}
	}

	/**
	 * Checks if the bindingConfig contains a valid binding type and returns an appropriate instance.
	 * 
	 * @param item
	 * @param bindingConfig
	 * 
	 * @throws BindingConfigParseException if bindingConfig is no valid binding type
	 */
	protected HeatPumpBindingConfig parseBindingConfig(Item item, HeatpumpCommandType bindingConfig) throws BindingConfigParseException {
		if (HeatpumpCommandType.validateBinding(bindingConfig, item.getClass())) {
			return new HeatPumpBindingConfig(bindingConfig);
		} else {
			throw new BindingConfigParseException("'" + bindingConfig + "' is no valid binding type");
		}
	}
	
	public String[] getItemNamesForType(HeatpumpCommandType eventType) {
		Set<String> itemNames = new HashSet<String>();
		for(Entry<String, BindingConfig> entry : bindingConfigs.entrySet()) {
			HeatPumpBindingConfig heatpumpConfig = (HeatPumpBindingConfig) entry.getValue();
			if(heatpumpConfig.getType().equals(eventType)) {
				itemNames.add(entry.getKey());
			}
		}
		return itemNames.toArray(new String[itemNames.size()]);
	}
	
	
	static class HeatPumpBindingConfig implements BindingConfig {
		
		final private HeatpumpCommandType type;
		
		public HeatPumpBindingConfig(HeatpumpCommandType type) {
			this.type = type;
		}

		public HeatpumpCommandType getType() {
			return type;
		}		
	}
	
}
