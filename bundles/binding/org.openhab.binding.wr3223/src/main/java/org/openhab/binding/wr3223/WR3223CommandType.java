package org.openhab.binding.wr3223;

import org.openhab.binding.wr3223.internal.WR3223Commands;
import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;

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
	
	/** (de: aktuelle Luftstufe) */
	VENTILATION_LEVEL {
		{
			command = "ventilation_level";
			itemClass = NumberItem.class;
			wr3223Command = WR3223Commands.LS;			
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
