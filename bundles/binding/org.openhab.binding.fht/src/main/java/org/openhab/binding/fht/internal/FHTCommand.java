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

public enum FHTCommand {

	FHT_ACTUATOR_0(0x00), FHT_ACTUATOR_1(0x01), FHT_ACTUATOR_2(0x02), FHT_ACTUATOR_3(
			0x03), FHT_ACTUATOR_4(0x04), FHT_ACTUATOR_5(0x05), FHT_ACTUATOR_6(
			0x06), FHT_ACTUATOR_7(0x07), FHT_ACTUATOR_8(0x08), FHT_MODE(0x3e), FHT_DESIRED_TEMP(
			0x41), FHT_MEASURED_TEMP_LOW(0x42), FHT_MEASURED_TEMP_HIGH(0x43), FHT_STATE(
			0x44), FHT_TEMP_DAY(0x82), FHT_TEMP_NIGHT(0x83), FHT_TEMP_WINDOW_OPEN(
			0x8a);

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
