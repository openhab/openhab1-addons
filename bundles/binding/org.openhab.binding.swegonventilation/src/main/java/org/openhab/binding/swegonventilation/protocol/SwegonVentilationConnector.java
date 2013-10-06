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

package org.openhab.binding.swegonventilation.protocol;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;

/**
 * Base class for Swegon ventilation system communication.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public abstract class SwegonVentilationConnector {

	/**
	 * Procedure for connect to Swegon ventilation system.
	 * 
	 * @throws SwegonVentilationException
	 */
	public abstract void connect() throws SwegonVentilationException;

	/**
	 * Procedure for disconnect from Swegon ventilation system.
	 * 
	 * @throws SwegonVentilationException
	 */
	public abstract void disconnect() throws SwegonVentilationException;

	/**
	 * Procedure for receiving datagram from Swegon ventilation system.
	 * 
	 * @throws SwegonVentilationException
	 */
	public abstract byte[] receiveDatagram() throws SwegonVentilationException;

	public int calculateCRC(byte[] bytes, int len) {
		int crc = 0xFFFF; // initial value
		int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

		for (int index = 0; index < len; index++) {
			byte b = bytes[index];
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;

		return crc;
	}

	public int toInt(byte hb, byte lb) {
		return (((int) hb << 8) & 0xFF00) | ((int) lb & 0xFF);
	}

}
