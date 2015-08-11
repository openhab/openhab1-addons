package org.openhab.binding.sapp.internal.model;

public class SappAddressDecimal extends SappAddress {

	private int originalMinScale;
	private int originalMaxScale;
	private int minScale;
	private int maxScale;

	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress, int minScale, int maxScale) {
		super(pnmasId, addressType, address, subAddress);

		setOriginalScale(subAddress);
		if (minScale != maxScale) { // check against bad parameters, division by zero
			this.minScale = minScale;
			this.maxScale = maxScale;
		} else {
			this.minScale = originalMinScale;
			this.maxScale = originalMaxScale;
		}
	}

	public SappAddressDecimal(String pnmasId, SappAddressType addressType, int address, String subAddress) {
		super(pnmasId, addressType, address, subAddress);
		
		setOriginalScale(subAddress);
		this.minScale = originalMinScale;
		this.maxScale = originalMaxScale;
	}

	public int getMinScale() {
		return minScale;
	}

	public int getMaxScale() {
		return maxScale;
	}
	
	private void setOriginalScale(String subAddress) {
		
		if (subAddress.equals("*")) {
			originalMinScale = 0;
			originalMaxScale = 0xFFFF;
		} else if (subAddress.equals("L")) {
			originalMinScale = 0;
			originalMaxScale = 0x00FF;
		} else if (subAddress.equals("H")) {
			originalMinScale = 0;
			originalMaxScale = 0x00FF;
		} else {
			originalMinScale = 0;
			originalMaxScale = 0x0001;
		}
	}
	
	public double scaledValue(int value) {
		return (((double) (value - originalMinScale)) * ((double) (maxScale - minScale)) / ((double) (originalMaxScale - originalMinScale))) + ((double) minScale);
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), minScale, maxScale);
	}
}
