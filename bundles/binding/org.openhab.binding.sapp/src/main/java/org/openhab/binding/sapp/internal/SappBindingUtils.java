package org.openhab.binding.sapp.internal;

public class SappBindingUtils {
	
	/**
	 * mask the value against the subAddress
	 * 
	 * @param subAddress subAddress (*, H, L, bit)
	 * @param value value to be masked against the subAddress
	 */
	public static int maskWithSubAddress(String subAddress, int value) {
		
		if (subAddress.equals("*")) {
			return value & 0xFFFF;
		} else if (subAddress.equals("L")) {
			return (value & 0x00FF);
		} else if (subAddress.equals("H")) {
			return ((value >> 8) & 0x00FF);
		} else {
			int shift = Integer.parseInt(subAddress);
			return ((0x0001 << (shift - 1) & value) >> (shift - 1));
		}
	}
	
	/**
	 * mask the value against the subAddress and overwrite the previous value with the masked value
	 * 
	 * @param subAddress subAddress (*, H, L, bit)
	 * @param newValue value to be masked against the subAddress
	 * @param previousValue previous word value to be masked against the new masked value
	 */
	public static int maskWithSubAddressAndSet(String subAddress, int newValue, int previousValue) {
		
		if (subAddress.equals("*")) {
			return newValue & 0xFFFF;
		} else if (subAddress.equals("L")) {
			return (newValue & 0x00FF) | (previousValue & 0xFF00);
		} else if (subAddress.equals("H")) {
			return ((newValue << 8) & 0xFF00) | (previousValue & 0x00FF);
		} else {
			int shift = Integer.parseInt(subAddress);
			if ((newValue & 0x0001) != 0) {
				return previousValue | (1 << (shift - 1));
			} else {
				return previousValue & ~(1 << (shift - 1));
			}
		}
	}
}
