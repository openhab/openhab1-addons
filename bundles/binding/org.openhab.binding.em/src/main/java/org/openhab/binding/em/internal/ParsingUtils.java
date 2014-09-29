/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.em.internal;

import org.openhab.binding.em.internal.EMBindingConfig.EMType;

/**
 * Simple util class to parse data from the received Strings
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class ParsingUtils {

	/**
	 * Get the address in the received data.
	 * 
	 * @param data
	 *            the received String
	 * @return the device address as String
	 */
	public static String parseAddress(String data) {
		return data.substring(3, 5);
	}

	/**
	 * Get the counter information from the received data
	 * 
	 * @param data
	 * @return integer representation of the packet count
	 */
	public static int parseCounter(String data) {
		return Integer.parseInt(data.substring(5, 7), 16);
	}

	/**
	 * Get the cumulated value
	 * 
	 * @param data
	 * @return the cumulated value as integers
	 */
	public static int parseCumulatedValue(String data) {
		return getIntFromChars(data.charAt(9), data.charAt(10), data.charAt(7), data.charAt(8));
	}

	/**
	 * Get the current value
	 * 
	 * @param data
	 * @return the current value as integer
	 */
	public static int parseCurrentValue(String data) {
		return getIntFromChars(data.charAt(13), data.charAt(14), data.charAt(11), data.charAt(12));
	}

	/**
	 * Get the peak value
	 * 
	 * @param data
	 * @return the peak value as int
	 */
	public static int parsePeakValue(String data) {
		return getIntFromChars(data.charAt(17), data.charAt(18), data.charAt(15), data.charAt(16));
	}

	private static int getIntFromChars(char... chars) {
		StringBuffer buffer = new StringBuffer(chars.length);
		for (int i = 0; i < chars.length; i++) {
			buffer.append(chars[i]);
		}
		return Integer.parseInt(buffer.toString(), 16);
	}

	/**
	 * Get the type of the EM device
	 * 
	 * @param data
	 * @return an enum of type EMType representing the device type
	 */
	public static EMType parseType(String data) {
		String typeString = data.substring(1, 3);
		return EMType.getFromTypeValue(typeString);
	}

}
