/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
