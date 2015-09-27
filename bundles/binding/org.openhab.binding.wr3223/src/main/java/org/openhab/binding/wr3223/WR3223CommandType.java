/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223;

import org.openhab.binding.wr3223.internal.WR3223Commands;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Michael Fraefel
 * @since 1.8.0
 */
public enum WR3223CommandType {
	
	/** T1 (de: Verdampfertemepratur) */
	TEMPERATURE_EVAPORATOR {
		{
			command = "temperature_evaporator";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T1;
		}
	},
	
	/** T2 (de: Kondensatortemperatur) */
	TEMPERATURE_CONDENSER {
		{
			command = "temperature_condenser";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T2;
		}
	},
	
	/** T3 (de: Aussentemperatur) */
	TEMPERATURE_OUTSIDE {
		{
			command = "temperature_outside";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T3;			
		}
	},
	
	/** T4 (de: Ablufttemperatur (Raumtemperatur)) */
	TEMPERATURE_OUTGOING_AIR {
		{
			command = "temperature_outgoing_air";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T4;			
		}
	},	
	
	/** T5 (de: Temperatur nach Wärmetauscher (Fortluft)) */
	TEMPERATURE_AFTER_HEAT_EXCHANGER {
		{
			command = "temperature_after_heat_exchanger";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T5;
		}
	},	
	
	/** T6 (de: Zulufttemperatur) */
	TEMPERATURE_SUPPLY_AIR {
		{
			command = "temperature_supply_air";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T6;
		}
	},
	
	/** T7 (de: Temperatur nach Solevorerwärmung) */
	TEMPERATURE_AFTER_BRINE_PREHEATING {
		{
			command = "temperature_after_brine_preheating";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T7;
		}
	},		

	/** T8 (de: Temperatur nach Vorheizregister) */
	TEMPERATURE_AFTER_PREHEATING_RADIATOR {
		{
			command = "temperature_after_preheating_radiator";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.T8;			
		}
	},		
	
	/** (de: Drehzahl Zuluftmotor) */
	ROTATION_SPEED_SUPPLY_AIR_MOTOR {
		{
			command = "rotation_speed_supply_air_motor";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.NZ;			
		}
	},
	
	/** (de: Drehzahl Abluftmotor) */
	ROTATION_SPEED_EXHAUST_AIR_MOTOR {
		{
			command = "rotation_speed_exhaust_air_motor";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.NA;			
		}
	},
	
	/** (de: Bypass) */
	BYPASS {
		{
			command = "bypass";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Kompressor Relais) */
	COMPRESSOR {
		{
			command = "compressor";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},
	
	/** (de: Zusatzheizung Relais) */
	ADDITIONAL_HEATER {
		{
			command = "additional_heater";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Netzrelais Bypass) */
	BYPASS_RELAY {
		{
			command = "bypass_relay";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},
	
	/** (de: Vorheizen aktiv) */
	PREHEATING_RADIATOR_ACTIVE {
		{
			command = "preheating_radiator_active";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Bedienteil aktiv) */
	CONTROL_DEVICE_ACTIVE {
		{
			command = "control_device_active";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Erdwärmetauscher) */
	EARTH_HEAT_EXCHANGER {
		{
			command = "earth_heat_exchanger";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Magnetventil) */
	MAGNET_VALVE {
		{
			command = "magnet_valve";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Bedienung über RS Schnittstelle) */
	OPENHAB_INTERFACE_ACTIVE {
		{
			command = "openhab_interface_active";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},
	
	/** (de: Vorheizregister) */
	PREHEATING_RADIATOR {
		{
			command = "preheating_radiator";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},
	
	
	/** (de: WW_Nachheizrgister) */
	WARM_WATER_POST_HEATER {
		{
			command = "warm_water_post_heater";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},	
	
	/** (de: Luftstufe vorhanden) */
	VENTILATION_LEVEL_AVAILABLE {
		{
			command = "ventilation_level_available";
			itemClass = ContactItem.class;
			wr3223Command = null;			
		}
	},
	
	/** (de: aktuelle Luftstufe) */
	VENTILATION_LEVEL {
		{
			command = "ventilation_level";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.LS;			
		}
	},	
	
	/** (de: Betriebsart) */
	OPERATION_MODE {
		{
			command = "operation_mode";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.MD;			
		}
	},	
	
	
	
	/** (de: Zuluftsoll Temperatur) */
	TEMPERATURE_SUPPLY_AIR_TARGET {
		{
			command = "temperature_supply_air_target";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.SP;			
		}
	}
	;	
	
	/** Represents the WR3223 command as it will be used in *.items configuration */
	String command;
	Class<? extends Item> itemClass;
	WR3223Commands wr3223Command;

	public String getCommand() {
		return command;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}	
	
	public WR3223Commands getWr3223Command() {
		return wr3223Command;
	}

	/**
	 * Find the right command for an item string.
	 * 
	 * @param bindingConfigString
	 * @return
	 */
	public static WR3223CommandType fromString(String bindingConfigString) {

		if ("".equals(bindingConfigString)) {
			return null;
		}
		for (WR3223CommandType c : WR3223CommandType.values()) {

			if (c.getCommand().equalsIgnoreCase(bindingConfigString)) {
				return c;
			}
		}
		throw new IllegalArgumentException("cannot find wr3223Command for '" + bindingConfigString + "'");
	}	
	
	

	public static boolean validateBinding(WR3223CommandType bindingConfig, Class<? extends Item> itemClass) {
		if(bindingConfig != null && itemClass != null){
			return bindingConfig.getItemClass().equals(itemClass);
		}
		return false;
	}	

}
