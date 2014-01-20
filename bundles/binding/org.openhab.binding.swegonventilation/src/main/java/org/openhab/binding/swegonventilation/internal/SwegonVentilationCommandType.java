/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.internal;

import java.io.InvalidClassException;

import org.openhab.core.items.Item;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.SwitchItem;

/**
 * Represents all valid command types which could be processed by this binding.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public enum SwegonVentilationCommandType {

	T1 ("T1", NumberItem.class),	// Temperature sensor, outdoor air
	T2 ("T2", NumberItem.class),	// Temperature sensor, supply air
	T3 ("T3", NumberItem.class),	// Temperature sensor, extract air
	T4 ("T4", NumberItem.class),	// Temperature sensor, supply air, reheating
	T5 ("T5", NumberItem.class),
	T6 ("T6", NumberItem.class),	// The freeze protection sensor of the water-heated air heater
	T7 ("T7", NumberItem.class),	// Excess temperature sensor for the preheating air heater
	T8 ("T8", NumberItem.class),	// Temperature sensor, exhaust air, freeze protection
	OUTDOOR_TEMP ("OutdoorTemperature", NumberItem.class), 					// T1
	SUPPLY_TEMP ("SupplyAirTemperature", NumberItem.class),					// T2
	EXTRACT_TEMP ("ExtractAirTemperature", NumberItem.class),				// T3
	SUPPLY_TEMP_HEATED ("SupplyAirTemperatureReheated", NumberItem.class),	// T4
	EXHAUST_TEMP ("ExhaustAirTemperature", NumberItem.class),				// T8
	SUPPLY_AIR_FAN_SPEED ("SupplyAirFanSpeed", NumberItem.class),
	EXTRACT_AIR_FAN_SPEED ("ExtractAirFanSpeed", NumberItem.class),
	EFFICIENCY ("Efficiency", NumberItem.class),
	EFFICIENCY_SUPPLY ("EfficiencySupply", NumberItem.class),
	EFFICIENCY_EXTRACT ("EfficiencyExtract", NumberItem.class),
	FAN_SPEED ("FanSpeed", NumberItem.class),
	PREHEAT_STATE ("PreheatState", SwitchItem.class),
	REHEAT_STATE ("ReheatState", SwitchItem.class),
	;

	private final String text;
	private Class<? extends Item> itemClass;

	private SwegonVentilationCommandType(final String text,
			Class<? extends Item> itemClass) {
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
	 * Procedure to validate command type string.
	 * 
	 * @param commandTypeText
	 *            command string e.g. T1
	 * @return true if item is valid.
	 * @throws IllegalArgumentException
	 *             Not valid command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static boolean validateBinding(String commandTypeText,
			Class<? extends Item> itemClass) throws IllegalArgumentException,
			InvalidClassException {

		for (SwegonVentilationCommandType c : SwegonVentilationCommandType
				.values()) {
			if (c.text.equals(commandTypeText)) {

				if (c.getItemClass().equals(itemClass))
					return true;
				else
					throw new InvalidClassException(
							"Not valid class for command type");
			}
		}

		throw new IllegalArgumentException("Not valid command type");

	}

	/**
	 * Procedure to convert command type string to command type class.
	 * 
	 * @param commandTypeText
	 *            command string e.g. T1
	 * @return corresponding command type.
	 * @throws InvalidClassException
	 *             Not valid class for command type.
	 */
	public static SwegonVentilationCommandType getCommandType(
			String commandTypeText) throws IllegalArgumentException {

		for (SwegonVentilationCommandType c : SwegonVentilationCommandType
				.values()) {
			if (c.text.equals(commandTypeText)) {
				return c;
			}
		}

		throw new IllegalArgumentException("Not valid command type");
	}

}