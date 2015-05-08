/*
 * Copyright 2009-14 Fraunhofer ISE
 *
 * This file is part of jSML.
 * For more information visit http://www.openmuc.org
 *
 * jSML is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * jSML is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with jSML.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openmuc.jsml.structures;

import java.lang.reflect.Field;

public class SML_Unit extends Unsigned8 {

	// DLMS-Unit-List, can be found in IEC 62056-62.
	public static final int YEAR = 1, MONTH = 2, WEEK = 3, DAY = 4, HOUR = 5, MIN = 6, SECOND = 7;
	public static final int DEGREE = 8, DEGREE_CELSIUS = 9, CURRENCY = 10, METRE = 11, METRE_PER_SECOND = 12;
	public static final int CUBIC_METRE = 13, CUBIC_METRE_CORRECTED = 14, CUBIC_METRE_PER_HOUR = 15;
	public static final int CUBIC_METRE_PER_HOUR_CORRECTED = 16, CUBIC_METRE_PER_DAY = 17;
	public static final int CUBIC_METRE_PER_DAY_CORRECTED = 18, LITRE = 19, KILOGRAM = 20, NEWTON = 21;
	public static final int NEWTONMETER = 22, PASCAL = 23, BAR = 24, JOULE = 25, JOULE_PER_HOUR = 26, WATT = 27;
	public static final int VOLT_AMPERE = 28, VAR = 29, WATT_HOUR = 30, VOLT_AMPERE_HOUR = 31, VAR_HOUR = 32;
	public static final int AMPERE = 33, COULOMB = 34, VOLT = 35, VOLT_PER_METRE = 36, FARAD = 37, OHM = 38;
	public static final int OHM_METRE = 39, WEBER = 40, TESLA = 41, AMPERE_PER_METRE = 42, HENRY = 43, HERTZ = 44;
	public static final int ACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = 45;
	public static final int REACTIVE_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = 46;
	public static final int APPARENT_ENERGY_METER_CONSTANT_OR_PULSE_VALUE = 47;
	public static final int VOLT_SQUARED_HOURS = 48, AMPERE_SQUARED_HOURS = 49, KILOGRAM_PER_SECOND = 50;
	public static final int KELVIN = 52, VOLT_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE = 53;
	public static final int AMPERE_SQUARED_HOUR_METER_CONSTANT_OR_PULSE_VALUE = 54;
	public static final int METER_CONSTANT_OR_PULSE_VALUE = 55, PERCENTAGE = 56, AMPERE_HOUR = 57;
	public static final int ENERGY_PER_VOLUME = 60, CALORIFIC_VALUE = 61, MOLE_PERCENT = 62, MASS_DENSITY = 63;
	public static final int PASCAL_SECOND = 64, RESERVED = 253, OTHER_UNIT = 254, COUNT = 255;

	public SML_Unit() {
	}

	/**
	 * @param val
	 *            number of unit (e.g. IEC 62056-62)
	 * @throws IllegalArgumentException
	 *             if val is no defined unit
	 */
	public SML_Unit(Unsigned8 val) {
		super(val.val);

		for (Field f : this.getClass().getDeclaredFields()) {
			try {
				if (val.val == (Integer) f.get(this)) {
					return;
				}
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		// if no Field has the value of i, throw Exception
		throw new IllegalArgumentException("SML_Unit: Wrong value for val! " + val.val + " is not allowed.");
	}

}
