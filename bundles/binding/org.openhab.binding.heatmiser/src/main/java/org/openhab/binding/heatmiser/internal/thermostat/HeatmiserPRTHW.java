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
package org.openhab.binding.heatmiser.internal.thermostat;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Thermostat class for the PRTHW thermostat (Programable Room Thermostat - Hot Water)
 * Most functions are handled by the base class
 * 
 * @author Chris Jackson
 * @since 1.3.0
 *
 */
public class HeatmiserPRTHW extends HeatmiserThermostat {

	@Override
	public boolean setData(byte in[]) {
		if (super.setData(in) == false)
			return false;

		dcbState = data[30];
		dcbHeatState = data[44];
		dcbFrostTemperature = data[26];
		dcbRoomTemperature = getTemp(41);
		dcbSetTemperature = data[27];

		dcbWaterState = data[45];

		return true;
	}

	public State getFloorTemperature(Class<? extends Item> itemType) {
		return null;
	}

	private byte[] setWaterState(Command command) {
		byte[] cmdByte = new byte[1];

		if (command.toString().contentEquals("ON"))
			cmdByte[0] = 1;
		else
			cmdByte[0] = 0;
		return makePacket(true, 42, 1, cmdByte);
	}

	public byte[] formatCommand(Functions function, Command command) {
		switch (function) {
		case WATERSTATE:
			return setWaterState(command);
		default:
			// Default to calling the parent class.
			return super.formatCommand(function, command);
		}
	}
}
