/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.s300th.internal;

/**
 * Small utility class to help parsing the binary messages
 * 
 * @author Till Klocke
 * @since 1.4.0
 * 
 */
public class ParseUtils {

	public static double parseTemperature(String data) {

		double temp = valueFromChars(data.charAt(6), data.charAt(3), data.charAt(4));
		int firstbyte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		if ((firstbyte & 8) == 0) {
			return temp;
		} else {
			return -temp;
		}
	}

	private static double valueFromChars(char... chars) {
		StringBuffer buffer = new StringBuffer(chars.length + 1);
		for (int i = 0; i < chars.length; i++) {
			if (i == chars.length - 1) {
				buffer.append('.');
			}
			buffer.append(chars[i]);
		}
		return Double.parseDouble(buffer.toString());
	}

	public static double parseKS300Wind(String data) {
		return valueFromChars(data.charAt(9), data.charAt(10), data.charAt(7));
	}

	/**
	 * each count value is a rain amount of 295ml
	 * http://www.elv.de/controller.aspx?cid=824&detail=10&detail2=3232
	 * 
	 * @param data
	 * @return
	 */
	public static int parseKS300RainCounter(String data) {

		StringBuffer buffer = new StringBuffer(3);
		buffer.append(data.charAt(14));
		buffer.append(data.charAt(11));
		buffer.append(data.charAt(12));

		return Integer.parseInt(buffer.toString(), 16);
	}

	public static boolean isKS300Raining(String data) {
		int firstbyte = Integer.parseInt(data.substring(1, 2), 16);
		return ((firstbyte & 2) > 0);
	}

	public static double parseKS300Humidity(String data) {
		return valueFromChars(data.charAt(8), data.charAt(5), '0');
	}

	public static double parseS300THHumidity(String data) {
		return valueFromChars(data.charAt(7), data.charAt(8), data.charAt(5));
	}

	public static String parseS300THAddress(String data) {
		int firstbyte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		int addressValue = (firstbyte & 7) + 1;
		String address = Integer.toHexString(addressValue);
		return address;
	}

}
