/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
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
package org.openhab.binding.rfxcom;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid value selectors which could be processed by this
 * binding.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public enum RFXComValueSelector {

	RAW_DATA ("RawData", StringItem.class),
	COMMAND ("Command", SwitchItem.class),
	SIGNAL_LEVEL ("SignalLevel", NumberItem.class),
	DIMMING_LEVEL ("DimmingLevel", DimmerItem.class),
	TEMPERATURE ("Temperature", NumberItem.class),
	HUMIDITY ("Humidity", NumberItem.class),
	HUMIDITY_STATUS ("HumidityStatus", StringItem.class),
	BATTERY_LEVEL ("BatteryLevel", NumberItem.class),
	PRESSURE("Pressure", NumberItem.class),
	FORECAST("Forecast", NumberItem.class),
	RAIN_RATE("RainRate", NumberItem.class),
	RAIN_TOTAL("RainTotal", NumberItem.class),
	WIND_DIRECTION("WindDirection", NumberItem.class),
	WIND_SPEED("WindSpeed", NumberItem.class),
	GUST("Gust", NumberItem.class),
	CHILL_FACTOR("ChillFactor", NumberItem.class),
	INSTANT_POWER("InstantPower", NumberItem.class),
	TOTAL_USAGE("TotalUsage", NumberItem.class),
	VOLTAGE("Voltage", NumberItem.class);

	private final String text;
	private Class<? extends Item> itemClass;

	private RFXComValueSelector(final String text, Class<? extends Item> itemClass) {
		this.text = text;
		this.itemClass = itemClass;
	}

	@Override
	public String toString() {
		return text;
	}

	public Class<? extends Item> getItemClass() {
		return itemClass;
	}

	/**
	 * Procedure to validate selector string.
	 * 
	 * @param valueSelector
	 *            selector string e.g. RawData, Command, Temperature
	 * @return true if item is valid.
	 * @throws IllegalArgumentException
	 *             Not valid value selector.
	 * @throws InvalidClassException
	 *             Not valid class for value selector.
	 */
	public static boolean validateBinding(String valueSelector,
			Class<? extends Item> itemClass) throws IllegalArgumentException,
			InvalidClassException {

		for (RFXComValueSelector c : RFXComValueSelector.values()) {
			if (c.text.equals(valueSelector)) {

				if (c.getItemClass().equals(itemClass))
					return true;
				else
					throw new InvalidClassException(
							"Not valid class for value selector");
			}
		}

		throw new IllegalArgumentException("Not valid value selector");

	}

	/**
	 * Procedure to convert selector string to value selector class.
	 * 
	 * @param valueSelectorText
	 *            selector string e.g. RawData, Command, Temperature
	 * @return corresponding selector value.
	 * @throws InvalidClassException
	 *             Not valid class for value selector.
	 */
	public static RFXComValueSelector getValueSelector(String valueSelectorText)
			throws IllegalArgumentException {

		for (RFXComValueSelector c : RFXComValueSelector.values()) {
			if (c.text.equals(valueSelectorText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid value selector");
	}

}