/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm;

/**
 * Various utility functions for e.g. hex string parsing
 * @author Daniel Pfrommer
 */

public class Utils {
	public static String getHexString(int b) {
		String result =  String.format("%02X", b & 0xFF);
		return result;
	}

	public static String getHexString(byte[] b) {
		return getHexString(b, b.length);
	}
	
	public static String getHexString(byte[] b, int len) {
		String result = "";
		for (int i=0; i < b.length && i < len; i++) {
			result += String.format("%02X ", b[i] & 0xFF);
		}
		return result;
	}
	
	public static int fromHexString(String string) {
		return Integer.parseInt(string, 16);
	}
	
	public static int from0xHexString(String string) {
		String hex = string.substring(2);
		return fromHexString(hex);
	}
	
	public static String getHexByte(byte b) {
		return String.format("0x%02X", b & 0xFF);
	}
	
	public static String getHexByte(int b) {
		return String.format("0x%02X", b);
	}
}
