/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
