/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal.thermostat;

import org.openhab.core.items.Item;
import org.openhab.core.types.Command;
import org.openhab.core.types.State;

/**
 * Thermostat class for the PRTHW thermostat (Programmable Room Thermostat - Hot Water)
 * Most functions are handled by the base class
 * 
 * @author Chris Jackson
 * @since 1.4.0
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

		dcbHolidayTime = (data[34] & 0xFF) + ((data[33] & 0xFF) * 256);
		dcbHoldTime = (data[36] & 0xFF) + ((data[35] & 0xFF) * 256);

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
