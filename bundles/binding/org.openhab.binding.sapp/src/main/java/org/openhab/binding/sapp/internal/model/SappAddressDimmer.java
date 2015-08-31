package org.openhab.binding.sapp.internal.model;

public class SappAddressDimmer extends SappAddressDecimal {
	
	private int increment;

	public SappAddressDimmer(String pnmasId, SappAddressType addressType, int address, String subAddress, int minScale, int maxScale, int increment) {
		super(pnmasId, addressType, address, subAddress, minScale, maxScale);
		
		this.increment = increment;
	}
	
	public int getIncrement() {
		return increment;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), getMinScale(), getMaxScale(), increment);
	}
}
