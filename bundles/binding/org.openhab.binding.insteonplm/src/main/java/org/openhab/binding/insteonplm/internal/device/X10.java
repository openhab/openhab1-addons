/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.device;

import java.util.HashMap;
import java.util.Map.Entry;
/*
 * This class has utilities related to the X10 protocol.
 * 
 * @author Bernd Pfrommer
 * @since 1.7.0
 */
public class X10 {
	/**
	 * Enumerates the X10 command codes.
	 * @author Bernd Pfrommer
	 *
	 */
	public enum Command {
		ALL_LIGHTS_OFF(0x6),
		STATUS_OFF(0xE),
		ON(0x2),
		PRESET_DIM_1(0xA),
		ALL_LIGHTS_ON(0x1),
		HAIL_ACKNOWLEDGE(0x9),
		BRIGHT(0x5),
		STATUS_ON(0xD),
		EXTENDED_CODE(0x9),
		STATUS_REQUEST(0xF),
		OFF(0x3),
		PRESET_DIM_2(0xB),
		ALL_UNITS_OFF(0x0),
		HAIL_REQUEST(0x8),
		DIM(0x4),
		EXTENDED_DATA(0xC);
		
		private final byte m_code;
		Command(int b) {
			m_code = (byte)b;
		}
		public byte code() { return m_code; }
	}
	/**
	 * converts house code to clear text
	 * @param c house code as per X10 spec
	 * @return clear text house code, i.e letter A-P 
	 */
	public static String s_houseToString(byte c) {
		String s = s_houseCodeToString.get(new Integer(c & 0xff));
		return (s == null) ? "X" : s;
	}
	/**
	 * converts unit code to regular integer
	 * @param c unit code per X10 spec
	 * @return decoded integer, i.e. number 0-16
	 */
	public static int s_unitToInt(byte c) {
		Integer i = s_unitCodeToInt.get(new Integer(c & 0xff));
		return (i == null) ? -1 : i;
	}
	/**
	 * Test if string has valid X10 address of form "H.U", e.g. A.10
	 * @param s string to test
	 * @return true if is valid X10 address
	 */
	public static boolean s_isValidAddress(String s) {
		String [] parts = s.split("\\.");
		if (parts.length != 2) return false;
		return parts[0].matches("[A-P]") &&
					parts[1].matches("\\d{1,2}");
	}
	/**
	 * Turn clear text address ("A.10") to byte code 
	 * @param addr clear text address
	 * @return byte that encodes house + unit code
	 */
	public static byte s_addressToByte(String addr) {
		String[] parts = addr.split("\\.");
		int ih = s_houseStringToCode(parts[0]);
		int iu = s_unitStringToCode(parts[1]);
		int itot = ih << 4 | iu;
		return (byte)(itot & 0xff);
	}
	/**
	 * converts String to house byte code
	 * @param s clear text house string
	 * @return coded house byte
	 */
	public static int s_houseStringToCode(String s) {
		Integer i = s_findKey(s_houseCodeToString, s);
		return (i == null) ? 0xf : i;
	}
	/**
	 * converts unit string to unit code 
	 * @param s string with clear text integer inside
	 * @return encoded unit byte
	 */
	public static int s_unitStringToCode(String s) {
		try {
			Integer key = Integer.parseInt(s); 
			Integer i = s_findKey(s_unitCodeToInt, key);
			return i;
		} catch (NumberFormatException e) {
		}
		return 0xf;
	}
	
	private static <T, E> T s_findKey(HashMap<T, E> map, E value) {
	    for (Entry<T, E> entry : map.entrySet()) {
	        if (value.equals(entry.getValue())) {
	            return entry.getKey();
	        }
	    }
	    return null;
	}
	/**
	 * Map between 4-bit X10 code and the house code.
	 */
	private static HashMap<Integer, String> s_houseCodeToString = new HashMap<Integer, String>();
	/**
	 * Map between 4-bit X10 code and the unit code.
	 */
	private static HashMap<Integer, Integer> s_unitCodeToInt = new HashMap<Integer, Integer>();
	static {
		s_houseCodeToString.put(0x6, "A"); s_unitCodeToInt.put(0x6, 1);
		s_houseCodeToString.put(0xe, "B"); s_unitCodeToInt.put(0xe, 2);
		s_houseCodeToString.put(0x2, "C"); s_unitCodeToInt.put(0x2, 3);
		s_houseCodeToString.put(0xa, "D"); s_unitCodeToInt.put(0xa, 4);
		s_houseCodeToString.put(0x1, "E"); s_unitCodeToInt.put(0x1, 5);
		s_houseCodeToString.put(0x9, "F"); s_unitCodeToInt.put(0x9, 6);
		s_houseCodeToString.put(0x5, "G"); s_unitCodeToInt.put(0x5, 7);
		s_houseCodeToString.put(0xd, "H"); s_unitCodeToInt.put(0xd, 8);
		s_houseCodeToString.put(0x7, "I"); s_unitCodeToInt.put(0x7, 9);
		s_houseCodeToString.put(0xf, "J"); s_unitCodeToInt.put(0xf, 10);
		s_houseCodeToString.put(0x3, "K"); s_unitCodeToInt.put(0x3, 11);
		s_houseCodeToString.put(0xb, "L"); s_unitCodeToInt.put(0xb, 12);
		s_houseCodeToString.put(0x0, "M"); s_unitCodeToInt.put(0x0, 13);
		s_houseCodeToString.put(0x8, "N"); s_unitCodeToInt.put(0x8, 14);
		s_houseCodeToString.put(0x4, "O"); s_unitCodeToInt.put(0x4, 15);
		s_houseCodeToString.put(0xc, "P"); s_unitCodeToInt.put(0xc, 16);
	}
}
