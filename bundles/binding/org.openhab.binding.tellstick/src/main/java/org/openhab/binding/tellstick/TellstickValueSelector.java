/**
 * Copyright (c) 2013-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.RollershutterItem;

/**
 * Represents all valid value selectors which could be processed by this
 * binding.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public enum TellstickValueSelector {

	RAW_DATA("RawData", StringItem.class), SHUTTER("Shutter", RollershutterItem.class), COMMAND("Command",
			SwitchItem.class), SIGNAL_LEVEL("SignalLevel", NumberItem.class), DIMMING_LEVEL("DimmingLevel",
			DimmerItem.class), DIMMABLE("Dimmable", SwitchItem.class), TEMPERATURE("Temperature", NumberItem.class), HUMIDITY(
			"Humidity", NumberItem.class), HUMIDITY_STATUS("HumidityStatus", StringItem.class), BATTERY_LEVEL(
			"BatteryLevel", NumberItem.class), PRESSURE("Pressure", NumberItem.class), FORECAST("Forecast",
			NumberItem.class), RAIN_RATE("RainRate", NumberItem.class), RAIN_TOTAL("RainTotal", NumberItem.class), WIND_DIRECTION(
			"WindDirection", NumberItem.class), WIND_SPEED("WindSpeed", NumberItem.class), INSTANT_POWER(
			"InstantPower", NumberItem.class), MOTION("Motion", SwitchItem.class), TOTAL_USAGE("TotalUsage",
			NumberItem.class);

	private final String text;
	private Class<? extends Item> itemClass;

	private TellstickValueSelector(final String text, Class<? extends Item> itemClass) {
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
	public static boolean validateBinding(String valueSelector, Class<? extends Item> itemClass)
			throws IllegalArgumentException, InvalidClassException {

		for (TellstickValueSelector c : TellstickValueSelector.values()) {
			if (c.text.equals(valueSelector)) {

				if (c.getItemClass().equals(itemClass))
					return true;
				else
					throw new InvalidClassException("Not valid class for value selector");
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
	public static TellstickValueSelector getValueSelector(String valueSelectorText) throws IllegalArgumentException {

		for (TellstickValueSelector c : TellstickValueSelector.values()) {
			if (c.text.equals(valueSelectorText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid value selector");
	}

}
