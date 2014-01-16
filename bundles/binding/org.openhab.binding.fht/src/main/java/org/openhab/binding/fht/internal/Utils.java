/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.fht.internal;

/**
 * Some utils to make communcation with the CUL easier.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public class Utils {

	/**
	 * Convert a decimal string to its hexadecimal representation
	 * 
	 * @param in
	 * @return
	 */
	public static String convertDecimalStringToHexString(String in) {
		int integer = Integer.parseInt(in);
		String hexString = Integer.toHexString(integer);
		if (hexString.length() == 1) {
			hexString = '0' + hexString;
		}
		return hexString;
	}

}
