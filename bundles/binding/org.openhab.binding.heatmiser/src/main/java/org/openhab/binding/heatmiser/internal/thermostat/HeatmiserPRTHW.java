/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.heatmiser.internal.thermostat;

import org.openhab.core.types.Command;


/**
 * Thermostat class for the PRTHW thermostat (Programmable Room Thermostat - Hot Water)
 * Most functions are handled by the base class
 * 
 * @author Chris Jackson
 * @since 1.4.0
 *
 */
public class HeatmiserPRTHW extends HeatmiserThermostat {

	public HeatmiserPRTHW() {
		DCB_READ_FLOOR_TEMPERATURE = 0;
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
