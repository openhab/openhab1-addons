/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.io.PrintWriter;
import java.io.StringWriter;
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
			retVal |= (bits[i] ? 1 : 0) << i;
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
	
	/**
	 * Returns a bit representation as boolean values in a reversed manner.
	 * A bit string <code>0001 0010</code> would be returnd as <code>0100 1000</code>.
	 * That way, the least significant bit can be addressed by bits[0], the second by bits[1] and so on. 
 	 *
	 * @param value
	 * 		the integer value to be converted in a bit array
	 * @return
	 * 		the bit array of the input value in a reversed manner.
	 */
	public static boolean[] getBits(int value) {

		boolean[] bits = new boolean[8];
		
		for (int i = 0; i < 8; i++) {
			bits[i] = (((value>>i) & 0x1) == 1);
		}

		return bits;
	}
	
	/**
	 *  Convert a string representation of hexadecimal to a byte array.
	 *   
	 * For example:
	 * String s = "00010203"
	 * returned byte array is {0x00, 0x01, 0x03}
	 *
	 * @param s
	 * @return byte array equivalent to hex string
	 **/
	public static byte[] hexStringToByteArray(String s) {

		int len = s.length();
		byte[] data = new byte[len / 2];

		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
		}

		return data;
	}

	/**
	 *  Convert a byte array to a string representation of hexadecimals.
	 *   
	 * For example:
	 * byte array is {0x00, 0x01, 0x03}
	 * returned String s = "00 01 02 03"
	 *
	 * @param byte array 
	 * @return String equivalent to hex string
	 **/
	static final String HEXES = "0123456789ABCDEF";
	public static String getHex( byte [] raw ) {
	    if ( raw == null ) {
	        return null;
	    }
	    final StringBuilder hex = new StringBuilder( 3 * raw.length );
	    for ( final byte b : raw ) {
	        hex.append(HEXES.charAt((b & 0xF0) >> 4))
	            .append(HEXES.charAt((b & 0x0F)))
	            .append(" ");
	    }
	    hex.delete(hex.length() -1,hex.length());
	    return hex.toString();
	}
	
	/**
	 * Retrieves the stacktrace of an exception as string.
	 * @param e
	 * 		the exception to resolve the stacktrace from
	 * @return
	 * 		the stacktrace from the exception provided
	 */
	public static String getStackTrace(Exception e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}
}