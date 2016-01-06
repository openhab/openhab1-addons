/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.primare.internal.protocol;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Class for Primare-specific utility functions
 * 
 * @author Veli-Pekka Juslin
 * @since 1.7.0
 */
public class PrimareUtils {

	private static final Logger logger = 
		LoggerFactory.getLogger(PrimareUtils.class);


	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
		}
		return data;
	}

	public static String byteArrayToHex(byte[] a) {
		StringBuilder sb = new StringBuilder();
		for(byte b: a)
			sb.append(String.format("%02x ", b&0xff));
		return sb.toString().trim();
	}

	public static String byteArraysToHex(byte[][] a) {
		StringBuilder sb = new StringBuilder();
		for(byte[] b: a)
			sb.append(String.format("%s ", byteArrayToHex(b)));
		return sb.toString().trim();
	}



	// DLE (0x10) requires ecaping (double DLE) before sending to device
	// Do not escape DLE at a.length-2
	public static byte[] escapeMessage(byte[] a) {
		byte[] buffer = new byte[a.length*2];
		int target_ix = 0;

		for (int i = 0; i < a.length; i++) {
			if ((buffer[target_ix++]=a[i]) == 0x10 && (i != a.length-2))
				buffer[target_ix++] = a[i];
		}

		return Arrays.copyOf(buffer, target_ix);
	}

	// DLE (0x10) has been escaped (double DLE) when receiving message
	public static byte[] unescapeMessage(byte[] a) {
		byte[] buffer = new byte[a.length];
		int target_ix = 0;
		int double_dle_start = -2;
		for (int i = 0; i < a.length; i++) {
			if (a[i] == (byte)0x10) {
				if (double_dle_start == i-1) {
					double_dle_start = -1;
					continue; // skip second DLE
				} else {
					double_dle_start = i;
				}
			}
			buffer[target_ix++] = a[i];
		}
		return Arrays.copyOf(buffer, target_ix);
	}
}
