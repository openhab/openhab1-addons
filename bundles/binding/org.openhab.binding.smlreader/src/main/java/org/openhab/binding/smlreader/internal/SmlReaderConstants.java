/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.smlreader.internal;

/**
 * Constants used in SmlReaderBinding
 * 
 * @author Mathias Gilhuber
 * @since 1.7.0
 */
public final class SmlReaderConstants {
	
	public final class Configuration {
		public static final String DEFAULT_SERIAL_PORT = "COM1";
		public static final String CONFIGURATION_KEY_SERIAL_PORT = "serialPort";
		public static final String CONFIGURATION_KEY_TSAP_PORT = "tsapPort";
		public static final String CONFIGURATION_KEY_TSAP_HOST = "tsapHost";
		public static final String CONFIGURATION_KEY_TSAP_SECURE = "tsapSecure";
		public static final String CONFIGURATION_KEY_CONNECTION_TYPE = "connectionType";
		public static final String CONFIGURATION_PATTERN = "^(.*?)\\.(" + CONFIGURATION_KEY_CONNECTION_TYPE + "|" + CONFIGURATION_KEY_SERIAL_PORT +"|"+ CONFIGURATION_KEY_TSAP_PORT +"|"+ CONFIGURATION_KEY_TSAP_HOST + "|" + CONFIGURATION_KEY_TSAP_SECURE + ")$";
		public static final String CONFIGURATION_OBIS_FORMAT = "%d-%d:%d.%d.%d";
		public static final String ITEM_DEFINITION_OBIS = "obis";
		public static final String ITEM_DEFINITION_CONVERSION = "conversion";
		public static final String ITEM_DEFINITION_DEVICE_ID = "device";
	}
/*
	public final class LoggingConstants {
		public static final String INFO_SERIAL_PORT_USAGE = "Meter Device '{}' uses serial port '{}'";
	}
	
	public final class ErrorConstants {
		public static final String EMPTY_SERIAL_RECEIVER = "{}: the serial receiver isn't initialized!";
	}
*/
	public final class UnitConstants {
		public static final String YEAR = "YEAR"; 
		public static final String MONTH = "MONTH";
		public static final String WEEK = "WEEK";
		public static final String DAY = "DAY";
		public static final String HOUR = "HOUR";
		public static final String MIN = "MIN";
		public static final String SECOND = "SECOND";
		public static final String DEGREE = "DEGREE";
		public static final String DEGREE_CELSIUS = "DEGREE_CELSIUS";
		public static final String CURRENCY = "CURRENCY";
		public static final String METRE = "METRE";
		public static final String METRE_PER_SECOND = "METRE_PER_SECOND";
		public static final String CUBIC_METRE = "CUBIC_METRE";
		public static final String CUBIC_METRE_CORRECTED = "CUBIC_METRE_CORRECTED";
		public static final String CUBIC_METRE_PER_HOUR = "CUBIC_METRE_PER_HOUR";
		public static final String CUBIC_METRE_PER_HOUR_CORRECTED = "CUBIC_METRE_PER_HOUR_CORRECTED";
		public static final String CUBIC_METRE_PER_DAY = "CUBIC_METRE_PER_DAY";
		public static final String CUBIC_METRE_PER_DAY_CORRECTED = "CUBIC_METRE_PER_DAY_CORRECTED";
		public static final String LITRE = "LITRE";
		public static final String KILOGRAM = "KILOGRAM";
		public static final String NEWTON = "NEWTON";
		public static final String NEWTONMETER = "NEWTONMETER";
		public static final String PASCAL = "PASCAL";
		public static final String BAR = "BAR";
		public static final String JOULE = "JOULE";
		public static final String JOULE_PER_HOUR = "JOULE_PER_HOUR";
		public static final String WATT = "WATT";
		public static final String VOLT_AMPERE = "VOLT_AMPERE";
		public static final String VAR = "VAR";
		public static final String WATT_HOUR = "WATT_HOUR";
		public static final String VOLT_AMPERE_HOUR = "VOLT_AMPERE_HOUR";
		public static final String VAR_HOUR = "VAR_HOUR";
		public static final String AMPERE = "AMPERE";
		public static final String COULOMB = "COULOMB";
		public static final String VOLT = "VOLT";
		public static final String VOLT_PER_METRE = "VOLT_PER_METRE";
		public static final String FARAD = "FARAD";
		public static final String OHM = "OHM";
		public static final String OHM_METRE = "OHM_METRE";
		public static final String WEBER = "WEBER";
		public static final String TESLA = "TESLA";
		public static final String AMPERE_PER_METRE = "AMPERE_PER_METRE";
		public static final String HENRY = "HENRY";
		public static final String HERTZ = "HERTZ";
		public static final String ACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = "ACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE";
		public static final String REACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = "REACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE";
		public static final String APPARENT_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = "APPARENT_ENERGY_METER_CONSTANT_OR_PULSE_VALUE";
		public static final String VOLT_SQUARED_HOURS = "VOLT_SQUARED_HOURS";
		public static final String AMPERE_SQUARED_HOURS = "AMPERE_SQUARED_HOURS";
		public static final String KILOGRAM_PER_SECOND = "KILOGRAM_PER_SECOND";
		public static final String KELVIN = "KELVIN";
		public static final String VOLT_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE = "VOLT_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE";
		public static final String AMPERE_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE = "AMPERE_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE";
		public static final String METER_CONSTANT_OR_PULSE_VALUE = "METER_CONSTANT_OR_PULSE_VALUE";
		public static final String PERCENTAGE = "PERCENTAGE";
		public static final String AMPERE_HOUR = "AMPERE_HOUR";
		public static final String ENERGY_PER_VOLUME = "ENERGY_PER_VOLUME";
		public static final String CALORIFIC_VALUE = "CALORIFIC_VALUE";
		public static final String MOLE_PERCENT = "MOLE_PERCENT";
		public static final String MASS_DENSITY = "MASS_DENSITY";
		public static final String PASCAL_SECOND = "PASCAL_SECOND";
		public static final String RESERVED = "RESERVED";
		public static final String OTHER_UNIT = "OTHER_UNIT";
		public static final String COUNT = "COUNT";
	}
}
