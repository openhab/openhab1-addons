package org.openhab.binding.intertechno.internal.parser;

/**
 * Base class for all parsers of intertechno configs.
 * 
 * @author Till Klocke
 * @since 1.4.0
 */
public abstract class AbstractIntertechnoParser implements
		IntertechnoAddressParser {

	/**
	 * Encode an integer as String for sending it via Intertechno. Numbers are
	 * repesented as "binary" Strings where each letter represents a byte. It si
	 * configurable which letters represents 0 and which represents 1.
	 * 
	 * @param length
	 *            The length of the String to generate.
	 * @param value
	 *            The value to encode. It is not checked if the value can be
	 *            represented by a String of the desired length.
	 * @param char1
	 *            The char representing 1.
	 * @param char0
	 *            The char representing 0.
	 * @return Returns the encoded String
	 */
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
