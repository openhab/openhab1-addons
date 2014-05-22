/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal;

import org.openhab.binding.maxcul.MaxCulBindingProvider;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;
import org.openhab.binding.maxcul.internal.MaxCulBindingConfig;


/**
 * This class is responsible for parsing the binding configuration 
 * and registering the {@link MaxCulBindingProvider}.
 * 
 * The following devices have the following valid types:
 * <li>RadiatorThermostat - thermostat,temperature,battery</li>
 * <li>WallThermostat - thermostat,temperature,battery</li>
 * 
 * Examples:
 * <li><code>{ maxcul="RadiatorThermostat:JEQ1234565" }</code> - will return/set the thermostat temperature of radiator thermostat with the serial number JEQ0304492</li>
 * <li><code>{ maxcul="RadiatorThermostat:JEQ1234565:battery" }</code> - will return the battery level of JEQ0304492</li>
 * <li><code>{ maxcul="WallThermostat:JEQ1234566:temperature" }</code> - will return the temperature of a wall mounted thermostat with serial number JEQ0304447</li>
 * <li><code>{ maxcul="PushButton:JEQ1234567" }</code> - will default to 'switch' mode</li>
 * <li><code>{ maxcul="PairMode" }</code> - Switch only, enables pair mode for 60s</li>
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.5.0
 */
public class MaxCulGenericBindingProvider extends AbstractGenericBindingProvider implements MaxCulBindingProvider {

	/**
	 * {@inheritDoc}
	 */
	public String getBindingType() {
		return "maxcul";
	}

	/**
	 * @{inheritDoc}
	 */
	@Override
	public void validateItemType(Item item, String bindingConfig) throws BindingConfigParseException {		
		MaxCulBindingConfig config = new MaxCulBindingConfig(bindingConfig);
		
		switch (config.deviceType)
		{
		case PAIR_MODE:
			if (!(item instanceof SwitchItem))
				throw new BindingConfigParseException("Invalid item type. PairMode can only be a switch");
			break;
		case PUSH_BUTTON:
		case SHUTTER_CONTACT:
			if (config.feature == MaxCulFeature.BATTERY && !(item instanceof NumberItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'battery' can only be a Number");
			if (config.feature == MaxCulFeature.SWITCH && !(item instanceof SwitchItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'switch' can only be a Switch");
			break;
		case RADIATOR_THERMOSTAT:
		case RADIATOR_THERMOSTAT_PLUS:
		case WALL_THERMOSTAT:
			if (config.feature == MaxCulFeature.TEMPERATURE && !(item instanceof NumberItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'temperature' can only be a Number");
			if (config.feature == MaxCulFeature.TEMPERATURE && !(item instanceof NumberItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'thermostat' can only be a Number");
			if (config.feature == MaxCulFeature.BATTERY && !(item instanceof NumberItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'battery' can only be a Number");
			if (config.feature == MaxCulFeature.MODE && !(item instanceof NumberItem))
				throw new BindingConfigParseException("Invalid item type. Feature 'mode' can only be a Number");
			break;
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processBindingConfiguration(String context, Item item, String bindingConfig) throws BindingConfigParseException {
		super.processBindingConfiguration(context, item, bindingConfig);
		
		MaxCulBindingConfig config = new MaxCulBindingConfig(bindingConfig);
					
		addBindingConfig(item, config);		
	}

	@Override
	public MaxCulBindingConfig getConfigForItemName(String itemName) {
		MaxCulBindingConfig config = null;
		if (super.bindingConfigs.containsKey(itemName)) {
			config = (MaxCulBindingConfig) super.bindingConfigs.get(itemName);
		}
		return config;
	}
}
