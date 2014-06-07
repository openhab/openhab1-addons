/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecotouch;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;

/**
 * Represents all valid commands which could be processed by this binding
 * 
 * @author Sebastian Held <sebastian.held@gmx.de>
 * @since 1.5.0
 */
public enum EcoTouchTags {

	// German: Außentemperatur
	TYPE_TEMPERATURE_OUTSIDE {
		{
			command = "temperature_outside";
			itemClass = NumberItem.class;
			tagName = "A1";
		}
	},

	// German: Außentemperatur gemittelt über 1h
	TYPE_TEMPERATURE_OUTSIDE_1H {
		{
			command = "temperature_outside_1h";
			itemClass = NumberItem.class;
			tagName = "A2";
		}
	},

	// German: Außentemperatur gemittelt über 24h
	TYPE_TEMPERATURE_OUTSIDE_24H {
		{
			command = "temperature_outside_24h";
			itemClass = NumberItem.class;
			tagName = "A3";
		}
	},

	// German: Quelleneintrittstemperatur
	TYPE_TEMPERATURE_SOURCE_IN {
		{
			command = "temperature_source_in";
			itemClass = NumberItem.class;
			tagName = "A4";
		}
	},

	// German: Quellenaustrittstemperatur
	TYPE_TEMPERATURE_SOURCE_OUT {
		{
			command = "temperature_source_out";
			itemClass = NumberItem.class;
			tagName = "A5";
		}
	},

	// German: Verdampfungstemperatur
	TYPE_TEMPERATURE_EVAPORATION {
		{
			command = "temperature_evaporation";
			itemClass = NumberItem.class;
			tagName = "A6";
		}
	},

	// German: Sauggastemperatur
	TYPE_TEMPERATURE_SUCTION {
		{
			command = "temperature_suction";
			itemClass = NumberItem.class;
			tagName = "A7";
		}
	},

	// German: Verdampfungsdruck
	TYPE_PRESSURE_EVAPORATION {
		{
			command = "pressure_evaporation";
			itemClass = NumberItem.class;
			tagName = "A8";
		}
	},

	// German: Temperatur Rücklauf Soll
	TYPE_TEMPERATURE_RETURN_SET {
		{
			command = "temperature_return_set";
			itemClass = NumberItem.class;
			tagName = "A10";
		}
	},

	// German: Temperatur Rücklauf
	TYPE_TEMPERATURE_RETURN {
		{
			command = "temperature_return";
			itemClass = NumberItem.class;
			tagName = "A11";
		}
	},

	// German: Temperatur Vorlauf
	TYPE_TEMPERATURE_FLOW {
		{
			command = "temperature_flow";
			itemClass = NumberItem.class;
			tagName = "A12";
		}
	},

	// German: Kondensationstemperatur
	TYPE_TEMPERATURE_CONDENSATION {
		{
			command = "temperature_condensation";
			itemClass = NumberItem.class;
			tagName = "A14";
		}
	},

	// German: Kondensationsdruck
	TYPE_PRESSURE_CONDENSATION {
		{
			command = "pressure_condensation";
			itemClass = NumberItem.class;
			tagName = "A15";
		}
	},

	// German: Speichertemperatur
	TYPE_TEMPERATURE_STORAGE {
		{
			command = "temperature_storage";
			itemClass = NumberItem.class;
			tagName = "A16";
		}
	},

	// German: Raumtemperatur
	TYPE_TEMPERATURE_ROOM {
		{
			command = "temperature_room";
			itemClass = NumberItem.class;
			tagName = "A17";
		}
	},

	// German: Raumtemperatur gemittelt über 1h
	TYPE_TEMPERATURE_ROOM_1H {
		{
			command = "temperature_room_1h";
			itemClass = NumberItem.class;
			tagName = "A18";
		}
	},

	// German: Warmwassertemperatur
	TYPE_TEMPERATURE_WATER {
		{
			command = "temperature_water";
			itemClass = NumberItem.class;
			tagName = "A19";
		}
	},

	// German: Pooltemperatur
	TYPE_TEMPERATURE_POOL {
		{
			command = "temperature_pool";
			itemClass = NumberItem.class;
			tagName = "A20";
		}
	},

	// German: Solarkollektortemperatur
	TYPE_TEMPERATURE_SOLAR {
		{
			command = "temperature_solar";
			itemClass = NumberItem.class;
			tagName = "A21";
		}
	},

	// German: Solarkreis Vorlauf
	TYPE_TEMPERATURE_SOLAR_FLOW {
		{
			command = "temperature_solar_flow";
			itemClass = NumberItem.class;
			tagName = "A22";
		}
	},

	// German: Ventilöffnung elektrisches Expansionsventil
	TYPE_POSITION_EXPANSION_VALVE {
		{
			command = "position_expansion_valve";
			itemClass = NumberItem.class;
			tagName = "A23";
		}
	},

	// German: elektrische Leistung Verdichter
	TYPE_POWER_COMPRESSOR {
		{
			command = "power_compressor";
			itemClass = NumberItem.class;
			tagName = "A25";
		}
	},

	// German: abgegebene thermische Heizleistung der Wärmepumpe
	TYPE_POWER_HEATING {
		{
			command = "power_heating";
			itemClass = NumberItem.class;
			tagName = "A26";
		}
	},

	// German: abgegebene thermische KälteLeistung der Wärmepumpe
	TYPE_POWER_COOLING {
		{
			command = "power_cooling";
			itemClass = NumberItem.class;
			tagName = "A27";
		}
	},

	// German: COP Heizleistung
	TYPE_COP_HEATING {
		{
			command = "cop_heating";
			itemClass = NumberItem.class;
			tagName = "A28";
		}
	},

	// German: COP Kälteleistungleistung
	TYPE_COP_COOLING {
		{
			command = "cop_cooling";
			itemClass = NumberItem.class;
			tagName = "A29";
		}
	},

	// German: Aktuelle Heizkreistemperatur
	TYPE_TEMPERATURE_HEATING {
		{
			command = "temperature_heating_return";
			itemClass = NumberItem.class;
			tagName = "A30";
		}
	},

	// German: Geforderte Temperatur im Heizbetrieb
	TYPE_TEMPERATURE_HEATING_SET {
		{
			command = "temperature_heating_set";
			itemClass = NumberItem.class;
			tagName = "A31";
		}
	},

	// German: Sollwertvorgabe Heizkreistemperatur
	TYPE_TEMPERATURE_HEATING_SET2 {
		{
			command = "temperature_heating_set2";
			itemClass = NumberItem.class;
			tagName = "A32";
		}
	},

	// German: Aktuelle Kühlkreistemperatur
	TYPE_TEMPERATURE_COOLING {
		{
			command = "temperature_cooling_return";
			itemClass = NumberItem.class;
			tagName = "A33";
		}
	},

	// German: Geforderte Temperatur im Kühlbetrieb
	TYPE_TEMPERATURE_COOLING_SET {
		{
			command = "temperature_cooling_set";
			itemClass = NumberItem.class;
			tagName = "A34";
		}
	},

	// German: Sollwertvorgabe Kühlbetrieb
	TYPE_TEMPERATURE_COOLING_SET2 {
		{
			command = "temperature_cooling_set2";
			itemClass = NumberItem.class;
			tagName = "A35";
		}
	},

	// German: Sollwert Warmwassertemperatur
	TYPE_TEMPERATURE_WATER_SET {
		{
			command = "temperature_water_set";
			itemClass = NumberItem.class;
			tagName = "A37";
		}
	},

	// German: Sollwertvorgabe Warmwassertemperatur
	TYPE_TEMPERATURE_WATER_SET2 {
		{
			command = "temperature_water_set2";
			itemClass = NumberItem.class;
			tagName = "A38";
		}
	},

	// German: Sollwert Poolwassertemperatur
	TYPE_TEMPERATURE_POOL_SET {
		{
			command = "temperature_pool_set";
			itemClass = NumberItem.class;
			tagName = "A40";
		}
	},

	// German: Sollwertvorgabe Poolwassertemperatur
	TYPE_TEMPERATURE_POOL_SET2 {
		{
			command = "temperature_pool_set2";
			itemClass = NumberItem.class;
			tagName = "A41";
		}
	},

	// German: geforderte Verdichterleistung
	TYPE_COMPRESSOR_POWER {
		{
			command = "compressor_power";
			itemClass = NumberItem.class;
			tagName = "A50";
		}
	},

	// German: Status der Wärmepumpenkomponenten
	TYPE_STATE {
		{
			command = "state";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Word;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Quellenpumpe
	TYPE_STATE_SOURCEPUMP {
		{
			command = "state_sourcepump";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 0;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Heizungsumwälzpumpe
	TYPE_STATE_HEATINGPUMP {
		{
			command = "state_heatingpump";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 1;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Freigabe Regelung EVD /
	// Magnetventil
	TYPE_STATE_EVD {
		{
			command = "state_evd";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 2;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Verdichter 1
	TYPE_STATE_compressor1 {
		{
			command = "state_compressor1";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 3;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Verdichter 2
	TYPE_STATE_compressor2 {
		{
			command = "state_compressor2";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 4;
		}
	},

	// German: Status der Wärmepumpenkomponenten: externer Wärmeerzeuger
	TYPE_STATE_extheater {
		{
			command = "state_extheater";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 5;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Alarmausgang
	TYPE_STATE_alarm {
		{
			command = "state_alarm";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 6;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Motorventil Kühlbetrieb
	TYPE_STATE_cooling {
		{
			command = "state_cooling";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 7;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Motorventil Warmwasser
	TYPE_STATE_water {
		{
			command = "state_water";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 8;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Motorventil Pool
	TYPE_STATE_pool {
		{
			command = "state_pool";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 9;
		}
	},

	// German: Status der Wärmepumpenkomponenten: Solarbetrieb
	TYPE_STATE_solar {
		{
			command = "state_solar";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 10;
		}
	},

	// German: Status der Wärmepumpenkomponenten: 4-Wegeventil im Kältekreis
	TYPE_STATE_cooling4way {
		{
			command = "state_cooling4way";
			itemClass = NumberItem.class;
			tagName = "I51";
			type = Type.Bitfield;
			bitnum = 11;
		}
	},

	;

	/**
	 * Represents the heatpump command as it will be used in *.items
	 * configuration
	 */
	String command;
	/**
	 * Represents the internal raw heatpump command as it will be used in
	 * querying the heat pump
	 */
	String tagName;
	Class<? extends Item> itemClass;

	/**
	 * The heatpump always returns 16-bit integers encoded as ASCII. They need
	 * to be interpreted according to the context.
	 */
	public enum Type {
		Analog, Word, Bitfield
	};

	/**
	 * The format of the response of the heat pump
	 */
	Type type = Type.Analog;
	/**
	 * If \c type is Type.Bitfield, this determines the bit number (0-based)
	 */
	int bitnum = 0;

	/**
	 * @return command name (uses in *.items files)
	 */
	public String getCommand() {
		return command;
	}

	/**
	 * @return tag name (raw communication with heat pump)
	 */
	public String getTagName() {
		return tagName;
	}

	/**
	 * @return type: how to interprete the response from the heat pump
	 */
	public Type getType() {
		return type;
	}

	/**
	 * @return bitnum: if the value is a bit field, this indicates the bit
	 *         number (0-based)
	 */
	public int getBitNum() {
		return bitnum;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * 
	 * @param bindingConfig
	 *            command e.g. TYPE_TEMPERATURE_OUTSIDE,..
	 * @param itemClass
	 *            class to validate
	 * @return true if item class can bound to heatpumpCommand
	 */
	public static boolean validateBinding(EcoTouchTags bindingConfig,
			Class<? extends Item> itemClass) {
		boolean ret = false;
		for (EcoTouchTags c : EcoTouchTags.values()) {
			if (c.getCommand().equals(bindingConfig.getCommand())
					&& c.getItemClass().equals(itemClass)) {
				ret = true;
				break;
			}
		}
		return ret;
	}

	/**
	 * Searches the available heat pump commands and returns the matching one.
	 * 
	 * @param heatpumpCommand
	 *            command string e.g. "temperature_outside"
	 * @return matching EcoTouchTags instance, if available
	 */
	public static EcoTouchTags fromString(String heatpumpCommand) {
		if ("".equals(heatpumpCommand)) {
			return null;
		}
		for (EcoTouchTags c : EcoTouchTags.values()) {
			if (c.getCommand().equals(heatpumpCommand)) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find EcoTouch tag for '"
				+ heatpumpCommand + "'");
	}

	/**
	 * Searches the available heat pump commands and returns the first matching
	 * one.
	 * 
	 * @param tag
	 *            raw heatpump tag e.g. "A1"
	 * @return first matching EcoTouchTags instance, if available
	 */
	public static EcoTouchTags fromTag(String tag) {
		for (EcoTouchTags c : EcoTouchTags.values()) {
			if (c.getTagName().equals(tag)) {
				return c;
			}
		}

		throw new IllegalArgumentException("cannot find EcoTouch tag for '"
				+ tag + "'");
	}
}
