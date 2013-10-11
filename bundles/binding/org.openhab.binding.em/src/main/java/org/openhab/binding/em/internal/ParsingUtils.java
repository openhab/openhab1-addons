package org.openhab.binding.em.internal;

import org.openhab.binding.em.internal.EMBindingConfig.EMType;

public class ParsingUtils {

	public static String parseAddress(String data) {
		return data.substring(3, 5);
	}

	public static int parseCounter(String data) {
		return Integer.parseInt(data.substring(5, 7), 16);
	}

	public static int parseCumulatedValue(String data) {
		return getIntFromChars(data.charAt(9), data.charAt(10), data.charAt(7), data.charAt(8));
	}

	public static int parseCurrentValue(String data) {
		return getIntFromChars(data.charAt(13), data.charAt(14), data.charAt(11), data.charAt(12));
	}

	public static int parsePeakValue(String data) {
		return getIntFromChars(data.charAt(17), data.charAt(18), data.charAt(15), data.charAt(16));
	}

	private static int getIntFromChars(char... chars) {
		StringBuffer buffer = new StringBuffer(chars.length);
		for (int i = 0; i < chars.length; i++) {
			buffer.append(chars[i]);
		}
		return Integer.parseInt(buffer.toString(), 16);
	}

	public static EMType parseType(String data) {
		String typeString = data.substring(1, 3);
		return EMType.getFromTypeValue(typeString);
	}

}
