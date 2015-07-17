package org.openhab.binding.sapp.internal;

public class SappBindingUtils {
	
	public static int filter(String subAddress, int value) {
		
		if (subAddress.equals("*")) {
			return value;
		} else if (subAddress.equals("L")) {
			return (value & 0xFF);
		} else if (subAddress.equals("H")) {
			return ((value >> 8) & 0xFF);
		} else {
			int shift = Integer.parseInt(subAddress);
			return (1 << (shift - 1) & value) >> (shift - 1);
		}
	}
}
