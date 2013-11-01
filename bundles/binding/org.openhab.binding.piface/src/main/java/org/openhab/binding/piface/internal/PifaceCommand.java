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
package org.openhab.binding.piface.internal;

/**
 * Enumeration for supported PiFace commands. These are copied from the Pfion python library
 * which we use to communicate with the PiFace extension board. Not all commands are used
 * by this binding.
 * 
 * @author Ben Jones
 * @since 1.3.0
 */
public enum PifaceCommand {

	ERROR_ACK(0),

	// piface.pfion command constants
	WRITE_OUT_CMD(1),
	WRITE_OUT_ACK(2),
	READ_OUT_CMD(3),
	READ_OUT_ACK(4),
	READ_IN_CMD(5),
	READ_IN_ACK(6),
	DIGITAL_WRITE_CMD(7),
	DIGITAL_WRITE_ACK(8),
	DIGITAL_READ_CMD(9),
	DIGITAL_READ_ACK(10),
	
	// watchdog command constants
	WATCHDOG_CMD(14),
	WATCHDOG_ACK(15);
	
	private final int command;

	PifaceCommand(int command) {
		this.command = command;
	}
	
	public byte toByte() {
		return (byte) command;
	}
}
