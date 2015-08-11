package org.openhab.binding.sapp.internal.model;

public class SappAddressDecimal extends SappAddress {

	private int minScale;
	private int maxScale;

	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress, int minScale, int maxScale) {
		super(pnmasId, addressType, address, subAddress);

		this.minScale = minScale;
		this.maxScale = maxScale;
	}

	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress) {
		super(pnmasId, addressType, address, subAddress);

		if (subAddress.equals("*")) {
			minScale = 0;
			maxScale = 0xFFFF;
		} else if (subAddress.equals("L")) {
			minScale = 0;
			maxScale = 0x00FF;
		} else if (subAddress.equals("H")) {
			minScale = 0;
			maxScale = 0x00FF;
		} else {
			minScale = 0;
			maxScale = 0x0001;
		}
	}

	public int getMinScale() {
		return minScale;
	}

	public int getMaxScale() {
		return maxScale;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), minScale, maxScale);
	}
}
