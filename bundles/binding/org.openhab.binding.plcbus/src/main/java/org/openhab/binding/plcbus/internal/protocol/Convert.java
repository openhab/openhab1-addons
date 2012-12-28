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
package org.openhab.binding.plcbus.internal.protocol;

import java.util.*;

/**
 * Methods for converting Datatypes
 * 
 * @author Robin Lenz
 * @since 1.1.0
 */
public class Convert {

	/**
	 * Converts a int value to byte
	 * 
	 * @param value
	 *            int to convert
	 * @return byte representation of value
	 */
	public static byte toByte(int value) {
		return (byte) ((value & 0xFF));
	}

	/**
	 * Converts a byte value from string
	 * 
	 * @param value string to convert
	 * @param dimension of result
	 * @return byte representation of value
	 */
	public static byte toByte(String value, int dimension) {
		return (byte) Integer.parseInt(value, dimension);
	}

	/**
	 * Creates a byte array from a list of Byte
	 * 
	 * @param list of Bytes
	 * @return byte array
	 */
	public static byte[] toByteArray(List<Byte> list) {
		byte[] result = new byte[list.size()];

		for (int pos = 0; pos < list.size(); pos++) {
			result[pos] = list.get(pos);
		}

		return result;
	}
	
}
