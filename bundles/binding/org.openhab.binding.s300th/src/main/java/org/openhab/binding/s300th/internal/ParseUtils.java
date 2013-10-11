package org.openhab.binding.s300th.internal;

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

	public static double parseHumidity(String data) {
		return valueFromChars(data.charAt(7), data.charAt(8), data.charAt(5));
	}

	public static String parseS300THAddress(String data) {
		int secondByte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		int addressValue = data.charAt(2) + (secondByte & 7);
		String address = Integer.toHexString(addressValue);
		return address;
	}

}
