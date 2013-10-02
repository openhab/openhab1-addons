package org.openhab.binding.intertechno.internal.parser;

public abstract class AbstractIntertechnoParser implements
		IntertechnoAddressParser {

	protected static String getEncodedString(int length, int value, char char1,
			char char0) {
		StringBuffer buffer = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			buffer.append(char0);
		}
		if (value == 0) {
			return buffer.toString();
		}
		for (int i = length - 1; i >= 0; i--) {
			int currentBitValue = (int) Math.pow(2, i);
			char bit = char0;
			if (currentBitValue >= value) {
				bit = char1;
				value = value - currentBitValue;
			}
			buffer.setCharAt(i, bit);
		}

		return buffer.toString();

	}

}
