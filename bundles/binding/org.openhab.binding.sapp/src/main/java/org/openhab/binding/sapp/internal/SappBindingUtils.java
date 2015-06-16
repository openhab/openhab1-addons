package org.openhab.binding.sapp.internal;

public class SappBindingUtils {
	
	public static int filter(SappBindingConfig sappBindingConfig, int value) {
		
		if (sappBindingConfig.getSubAddress().equals("*")) {
			return value;
		} else if (sappBindingConfig.getSubAddress().equals("L")) {
			return (value & 0xFF);
		} else if (sappBindingConfig.getSubAddress().equals("H")) {
			return ((value >> 8) & 0xFF);
		} else {
			int shift = Integer.parseInt(sappBindingConfig.getSubAddress());
			return (1 << (shift - 1) & value) >> (shift - 1);
		}
	}
}
