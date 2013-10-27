/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.util.Calendar;
import java.util.Date;

/**
* Utility class for common tasks within the MAX!Cube binding package. 
* 
* @author Andreas Heil (info@aheil.de)
* @since 1.4.0
*/
public final class Utils {
	
	/**
	 * Returns the integer value of an  hexadecimal number (base 16).
	 * 
	 * @param hex
	 * 			the hex value to be parsed into an integer 
	 * @return the given hex value as integer
	 */
	public static final int fromHex(String hex) {
		return Integer.parseInt(hex, 16);
	}
	
	/**
	 * Returns the hexadecimal number of a number of integer values.
	 * 
	 * @param values
	 * 			the integer values to be converted into hexadecimal numbers
	 * @return the given numbers as hexadecimal number
	 */
	public static final String toHex(int... values) {
		String returnValue = "";
		for (int v : values) {
			returnValue += v < 16 ? "0" + Integer.toHexString(v).toUpperCase() : Integer.toHexString(v).toUpperCase();
		}	
		return returnValue;
	}
	
	/**
	 * Returns the hexadecimal number of a bit array .
	 * 
	 * @param bits
	 * 			the boolean array representing the bits to be converted into hexadecimal numbers
	 * @return the hexadecimal number
	 */
	public static final String toHex(boolean[] bits) {
		int retVal = 0;
		for (int i = 0; i < bits.length; ++i) {
		    retVal = (retVal << 1) + (bits[i] ? 1 : 0);
		}
		return toHex(retVal);
	}

	/**
	 * Converts an Java signed byte into its general (unsigned) value as being used in other programming languages and platforms.
	 * 
	 * @param b
	 * 			the byte to be converted into its integer value
	 * @return the integer value represented by the given byte
	 */
	public static final int fromByte(byte b) {
		return b & 0xFF;
	}
	
	/**
	 * Resolves the date and time based based on a three byte encoded within a MAX!Cube L message.
	 *
	 * Date decoding (two byte)
	 * <pre>
	 * Hex     Binary
	 * 9D0B    1001110100001011
     *   MMMDDDDDM YYYYYY
     *   100     0        = 1000b  = 8d  = month
     *      11101         = 11101b = 29d = day
     *             001011 = 1011b  = 11d = year-2000  
	 * </pre>
	 * 
	 * Time decoding (one byte)
	 * <pre>
	 * Hex     Decimal
	 * 1F      31 * 0.5 hours = 15:30
	 * </pre>
	 * 
	 * @param date
	 * 			the date to be converted based on two bytes
	 * @param time
	 * 			the time to be converted based on a single byte
	 * @return the date time based on the values provided
	 */
	public static Date resolveDateTime(int date, int time) {
		
		int month = ((date & 0xE000) >> 12)+((date & 0x80) >> 7);
		int day = (date & 0x1F00) >> 8;
		int year = (date & 0x0F) + 2000;
	
		int hours = (int)(time * 0.5);
		int minutes = (int)(60 * ((time * 0.5)-hours));
		
		return new Date(year, month, day, hours, minutes);
	}
	
	public static boolean[] getBits(int value) {

		String zeroBitString = String.format("%0" + 8 + 'd', 0);
		String binaryString = Integer.toBinaryString(value);
		binaryString = zeroBitString.substring(binaryString.length())
				+ binaryString;

		boolean[] bits = new boolean[8];

		for (int pos = 7; pos > 0; pos--) {
			bits[7 - pos] = binaryString.substring(pos, pos + 1)
					.equalsIgnoreCase("1") ? true : false;
		}

		// bits are reverse order representing the original binary string
		// e.g. string "0001 0010" is bits[0] -> 0100 1000 <- bits[7]
		for (boolean bit : bits) {
			String b = bit == true ? "1" : "0";
			System.out.print(b);
		}

		return bits;
	}

}