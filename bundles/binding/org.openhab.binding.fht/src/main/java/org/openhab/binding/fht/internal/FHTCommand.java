/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Enum representing possible commands received via RF from FHT devices. Some
 * commands can also be send.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public enum FHTCommand {

	/**
	 * Command is for valve actuator 0
	 */
	FHT_ACTUATOR_0(0x00),
	/**
	 * Command is for valve actuator 1
	 */
	FHT_ACTUATOR_1(0x01),
	/**
	 * Command is for valve actuator 2
	 */
	FHT_ACTUATOR_2(0x02),
	/**
	 * Command is for valve actuator 3
	 */
	FHT_ACTUATOR_3(0x03),
	/**
	 * Command is for valve actuator 4
	 */
	FHT_ACTUATOR_4(0x04),
	/**
	 * Command is for valve actuator 5
	 */
	FHT_ACTUATOR_5(0x05),
	/**
	 * Command is for valve actuator 6
	 */
	FHT_ACTUATOR_6(0x06),
	/**
	 * Command is for valve actuator 7
	 */
	FHT_ACTUATOR_7(0x07),
	/**
	 * Command is for valve actuator 8
	 */
	FHT_ACTUATOR_8(0x08),
	/**
	 * Command os for the FHT-80b mode. The mode can be automatic, manual or
	 * holiday mode
	 */
	FHT_MODE(0x3e),
	/**
	 * The command describes the desired temperature
	 */
	FHT_DESIRED_TEMP(0x41),
	/**
	 * The command describes the low byte of the measured temperature
	 */
	FHT_MEASURED_TEMP_LOW(0x42),
	/**
	 * The command describes the high byte of the measured tempreature.
	 */
	FHT_MEASURED_TEMP_HIGH(0x43),
	/**
	 * The command describes the state of the FHT-80b. This contains among other
	 * things the battery state.
	 */
	FHT_STATE(0x44),
	/**
	 * This command describes the desired day temperature
	 */
	FHT_TEMP_DAY(0x82),
	/**
	 * This command describes the desired night temperature
	 */
	FHT_TEMP_NIGHT(0x83),
	/**
	 * This command describes the desired temperature in case a window is open.
	 */
	FHT_TEMP_WINDOW_OPEN(0x8a);

	private int id;

	private static Map<Integer, FHTCommand> idToEvent = new LinkedHashMap<Integer, FHTCommand>();
	static {
		for (FHTCommand event : FHTCommand.values()) {
			idToEvent.put(event.getId(), event);
		}
	}

	private FHTCommand(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public static FHTCommand getEventById(int id) {
		return idToEvent.get(id);
	}

}
