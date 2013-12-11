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

	public static double parseWind(String data) {
		return valueFromChars(data.charAt(9), data.charAt(10), data.charAt(7));
	}

	public static double parseRain(String data) {
		return valueFromChars(data.charAt(14), data.charAt(11), data.charAt(12));
	}

	public static boolean isRaining(String data) {
		int firstbyte = Integer.parseInt(data.substring(1, 2), 16);
		return ((firstbyte & 2) > 0);
	}

	public static double parseHumidity(String data) {
		return valueFromChars(data.charAt(7), data.charAt(8), data.charAt(5));
	}

	public static String parseS300THAddress(String data) {
		int firstbyte = Integer.parseInt(String.valueOf(data.charAt(1)), 16);
		int addressValue = (firstbyte & 7) + 1;
		String address = Integer.toHexString(addressValue);
		return address;
	}

}
