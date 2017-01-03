/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.diyonxbee.internal;

/**
 * Utility to convert xbee addresses to and from string (hexadecimal)
 * representation.
 * 
 * <p>
 * Inspired from com.rapplogic.xbee.util.ByteUtils
 * (https://github.com/andrewrapp
 * /xbee-api/blob/master/src/com/rapplogic/xbee/util/ByteUtils.java)
 * 
 * @author juergen.richtsfeld@gmail.com
 * @since 1.9
 */
public abstract class FormatUtil {
	private FormatUtil() {
	}

	/**
	 * convert a int[] to a hexadecimal String representation. All values in the
	 * int[] must be smaller 0xFF
	 */
	public static String readableAddress(int[] data) {
		final StringBuilder sb = new StringBuilder();

		for (int i : data) {
			if (i > 0xFF) {
				throw new IllegalArgumentException();
			}
			if (i < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(i).toUpperCase());
		}

		return sb.toString();
	}

	public static int[] fromReadableAddress(String remote) {
		if (remote == null || remote.length() != 16)
			throw new IllegalArgumentException("cannot parse XBeeAddress64 from " + remote);

		final int[] address = new int[8];
		for (int i = 0; i < 8; i++) {
			final String number = remote.substring(i * 2, i * 2 + 2);
			assert number.length() == 2;
			address[i] = Integer.parseInt(number, 16);
		}
		return address;
	}
}
