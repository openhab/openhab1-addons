package org.openhab.binding.fht.internal;

public class Utils {

	public static String convertDecimalStringToHexString(String in) {
		int integer = Integer.parseInt(in);
		String hexString = Integer.toHexString(integer);
		if (hexString.length() == 1) {
			hexString = '0' + hexString;
		}
		return hexString;
	}

}
