package org.openhab.binding.diyonxbee.internal;

public abstract class FormatUtil {
	private FormatUtil() {
	}
	
	/**
	 * convert a int[] to a hexadecimal String representation. All values in the int[] must be smaller 0xFF
	 */
	public static String readableAddress(int[] data) {
		final StringBuilder sb = new StringBuilder();
		
		for (int i : data) {
			if(i > 0xFF) {
				throw new IllegalArgumentException();
			}
			if(i < 0x10) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(i).toUpperCase());
		}
		
		return sb.toString();
	}
}
