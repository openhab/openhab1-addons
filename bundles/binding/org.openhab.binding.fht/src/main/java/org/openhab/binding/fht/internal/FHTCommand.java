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
