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
import org.openhab.core.binding.BindingConfig;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.model.item.binding.AbstractGenericBindingProvider;
import org.openhab.model.item.binding.BindingConfigParseException;


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
	
	class MaxCulBindingConfig implements BindingConfig {
		MaxCulDevice deviceType;
		MaxCulFeature feature;		
		String serialNumber;
		
		MaxCulBindingConfig(String bindingConfig) throws BindingConfigParseException
		{
			String[] configParts = bindingConfig.trim().split(":");
			if (configParts.length <= 3)
			{
				throw new BindingConfigParseException("MaxCul configuration requires a configuration of at least the format <device_type>:<serial_num> for a MAX! device.");
			}
			
			/* handle device type */
			switch (configParts[0])
			{
				case "RadiatorThermostat":
					this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT;
					break;
				case "RadiatorThermostatPlus":
					this.deviceType = MaxCulDevice.RADIATOR_THERMOSTAT_PLUS;
					break;
				case "WallThermostat":
					this.deviceType = MaxCulDevice.WALL_THERMOSTAT;
					break;
				case "PushButton": 
					this.deviceType = MaxCulDevice.PUSH_BUTTON;
					break;
				case "ShutterContact":
					this.deviceType = MaxCulDevice.SHUTTER_CONTACT;
					break;
				default:
					throw new BindingConfigParseException("Invalid device type. Use RadiatorThermostat / RadiatorThermostatPlus / WallThermostat / PushButton / ShutterContact");
			}
			
			/* handle serial number */
			this.serialNumber = configParts[1];
			
			/* handle feature if set */
			if (configParts.length > 2)
			{
				switch (configParts[2])
				{
					case "thermostat":
						if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT ||
								this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS ||
								this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
							throw new BindingConfigParseException("Invalid device feature. Can only use 'thermostat' on radiator or wall thermostats");
						this.feature = MaxCulFeature.THERMOSTAT;
						break;
					case "temperature":
						if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT ||
								this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS ||
								this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
							throw new BindingConfigParseException("Invalid device feature. Can only use 'temperature' on radiator or wall thermostats");
						this.feature = MaxCulFeature.TEMPERATURE;
						break;
					case "battery":
						this.feature = MaxCulFeature.BATTERY;
						break;
					case "mode":
						if (this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT ||
								this.deviceType != MaxCulDevice.RADIATOR_THERMOSTAT_PLUS ||
								this.deviceType != MaxCulDevice.WALL_THERMOSTAT)
							throw new BindingConfigParseException("Invalid device feature. Can only use 'temperature' on radiator or wall thermostats");
						this.feature = MaxCulFeature.MODE;
						break;
					case "switch":
						if (this.deviceType != MaxCulDevice.PUSH_BUTTON ||
								this.deviceType != MaxCulDevice.SHUTTER_CONTACT)							
							throw new BindingConfigParseException("Invalid device feature. Can only use 'switch' on PushButton or ShutterContact");
						this.feature = MaxCulFeature.TEMPERATURE;
						break;
				}
			} else {
				/* use defaults - handle all device types*/
				switch (this.deviceType)
				{
					case PUSH_BUTTON:
						this.feature = MaxCulFeature.SWITCH;
						break;
					case RADIATOR_THERMOSTAT:
						this.feature = MaxCulFeature.THERMOSTAT;
						break;
					case RADIATOR_THERMOSTAT_PLUS:
						this.feature = MaxCulFeature.THERMOSTAT;
						break;
					case SHUTTER_CONTACT:
						this.feature = MaxCulFeature.SWITCH;
						break;
					case WALL_THERMOSTAT:
						this.feature = MaxCulFeature.THERMOSTAT;
						break;
				}
			}
		}
	}
	
	
}
