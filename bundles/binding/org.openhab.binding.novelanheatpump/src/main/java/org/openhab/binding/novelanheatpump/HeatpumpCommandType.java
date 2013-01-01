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
package org.openhab.binding.novelanheatpump;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;


/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Jan-Philipp Bolle
 * @since 1.0.0
 */
public enum HeatpumpCommandType {


	//in german Außentemperatur
	TYPE_TEMPERATURE_OUTSIDE {
		{
			command = "temperature_outside";
			itemClass = NumberItem.class;
		}
	},

	//in german Rücklauf
	TYPE_TEMPERATURE_RETURN {
		{
			command = "temperature_return";
			itemClass = NumberItem.class;
		}
	},

	//in german Rücklauf Soll
	TYPE_TEMPERATURE_REFERENCE_RETURN {
		{
			command = "temperature_reference_return";
			itemClass = NumberItem.class;
		}
	},

	//in german Vorlauf
	TYPE_TEMPERATURE_SUPPLAY {
		{
			command = "temperature_supplay";
			itemClass = NumberItem.class;
		}
	},

	// in german Brauchwasser Soll
	TYPE_TEMPERATURE_SERVICEWATER_REFERENCE {
		{
			command = "temperature_servicewater_reference";
			itemClass = NumberItem.class;
		}
	},

	// in german Brauchwasser Ist
	TYPE_TEMPERATURE_SERVICEWATER {
		{
			command = "temperature_servicewater";
			itemClass = NumberItem.class;
		}
	},

	TYPE_HEATPUMP_STATE {
		{
			command = "state";
			itemClass = StringItem.class;
		}
	},

	TYPE_HEATPUMP_SOLAR_COLLECTOR {
		{
			command = "temperature_solar_collector";
			itemClass = NumberItem.class;
		}
	},
	
	TYPE_HEATPUMP_SOLAR_STORAGE {
		{
			command = "temperature_solar_storage";
			itemClass = NumberItem.class;
		}
	};

	
	/** Represents the heatpump command as it will be used in *.items configuration */
	String command;
	Class<? extends Item> itemClass;

	public String getCommand() {
		return command;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * 
	 * @param bindingConfig command string e.g. state, temperature_solar_storage,..
	 * @param itemClass class to validate
	 * @return true if item class can bound to heatpumpCommand
	 */
	public static boolean validateBinding(HeatpumpCommandType bindingConfig, Class<? extends Item> itemClass) {
		boolean ret = false;
		for (HeatpumpCommandType c : HeatpumpCommandType.values()) {
			if (c.getCommand().equals(bindingConfig.getCommand())
					&& c.getItemClass().equals(itemClass)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	public static HeatpumpCommandType fromString(String heatpumpCommand) {

		if ("".equals(heatpumpCommand)) {
			return null;
		}
		for (HeatpumpCommandType c : HeatpumpCommandType.values()) {

			if (c.getCommand().equals(heatpumpCommand)) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find novelanHeatpumpCommand for '"
				+ heatpumpCommand + "'");

	}

}