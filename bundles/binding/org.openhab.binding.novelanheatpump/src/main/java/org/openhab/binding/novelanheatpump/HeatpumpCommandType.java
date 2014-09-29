/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	//in german Außentemperatur
	TYPE_TEMPERATURE_OUTSIDE_AVG {
		{
			command = "temperature_outside_avg";
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

	TYPE_HEATPUMP_EXTENDED_STATE {
		{
			command = "extended_state";
			itemClass = StringItem.class;
		}
	},

	TYPE_HEATPUMP_SOLAR_COLLECTOR {
		{
			command = "temperature_solar_collector";
			itemClass = NumberItem.class;
		}
	},
	
	// in german Temperatur Heissgas
	TYPE_TEMPERATURE_HOT_GAS {
		{
			command = "temperature_hot_gas";
			itemClass = NumberItem.class;
		}
	},

	// in german Sondentemperatur WP Eingang
	TYPE_TEMPERATURE_PROBE_IN {
		{
			command = "temperature_probe_in";
			itemClass = NumberItem.class;
		}
	},

	// in german Sondentemperatur WP Ausgang
	TYPE_TEMPERATURE_PROBE_OUT {
		{
			command = "temperature_probe_out";
			itemClass = NumberItem.class;
		}
	},

	// in german Vorlauftemperatur MK1 IST
	TYPE_TEMPERATURE_MK1 {
		{
			command = "temperature_mk1";
			itemClass = NumberItem.class;
		}
	},
	
	// in german Vorlauftemperatur MK1 SOLL
	TYPE_TEMPERATURE_MK1_REFERENCE {
		{
			command = "temperature_mk1_reference";
			itemClass = NumberItem.class;
		}
	},
	
	// in german Vorlauftemperatur MK1 IST
	TYPE_TEMPERATURE_MK2 {
		{
			command = "temperature_mk2";
			itemClass = NumberItem.class;
		}
	},
	
	// in german Vorlauftemperatur MK1 SOLL
	TYPE_TEMPERATURE_MK2_REFERENCE {
		{
			command = "temperature_mk2_reference";
			itemClass = NumberItem.class;
		}
	},

	// in german Temperatur externe Energiequelle
	TYPE_TEMPERATURE_EXTERNAL_SOURCE {
		{
			command = "temperature_external_source";
			itemClass = NumberItem.class;
		}
	},
	
	// in german Betriebsstunden Verdichter1
	TYPE_HOURS_COMPRESSOR1 {
		{
			command = "hours_compressor1";
			itemClass = StringItem.class;
		}
	},

	// in german Impulse (Starts) Verdichter 1
	TYPE_STARTS_COMPRESSOR1 {
		{
			command = "starts_compressor1";
			itemClass = NumberItem.class;
		}
	},

	// in german Betriebsstunden Verdichter2
	TYPE_HOURS_COMPRESSOR2 {
		{
			command = "hours_compressor2";
			itemClass = StringItem.class;
		}
	},

	// in german Impulse (Starts) Verdichter 2
	TYPE_STARTS_COMPRESSOR2 {
		{
			command = "starts_compressor2";
			itemClass = NumberItem.class;
		}
	},

	// Temperatur_TRL_ext
	TYPE_TEMPERATURE_OUT_EXTERNAL {
		{
			command = "temperature_out_external";
			itemClass = NumberItem.class;
		}
	},

	// in german Betriebsstunden ZWE1
	TYPE_HOURS_ZWE1 {
		{
			command = "hours_zwe1";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden ZWE1
	TYPE_HOURS_ZWE2 {
		{
			command = "hours_zwe2";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden ZWE1
	TYPE_HOURS_ZWE3 {
		{
			command = "hours_zwe3";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden Wärmepumpe
	TYPE_HOURS_HETPUMP {
		{
			command = "hours_heatpump";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden Heizung
	TYPE_HOURS_HEATING {
		{
			command = "hours_heating";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden Brauchwasser
	TYPE_HOURS_WARMWATER {
		{
			command = "hours_warmwater";
			itemClass = StringItem.class;
		}
	},

	// in german Betriebsstunden Brauchwasser
	TYPE_HOURS_COOLING {
		{
			command = "hours_cooling";
			itemClass = StringItem.class;
		}
	},

	// in german Waermemenge Heizung
	TYPE_THERMALENERGY_HEATING {
		{
			command = "thermalenergy_heating";
			itemClass = NumberItem.class;
		}
	},

	// in german Waermemenge Brauchwasser
	TYPE_THERMALENERGY_WARMWATER {
		{
			command = "thermalenergy_warmwater";
			itemClass = NumberItem.class;
		}
	},

	// in german Waermemenge Schwimmbad
	TYPE_THERMALENERGY_POOL {
		{
			command = "thermalenergy_pool";
			itemClass = NumberItem.class;
		}
	},

	// in german Waermemenge gesamt seit Reset
	TYPE_THERMALENERGY_TOTAL {
		{
			command = "thermalenergy_total";
			itemClass = NumberItem.class;
		}
	},

	// in german Massentrom
	TYPE_MASSFLOW {
		{
			command = "massflow";
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