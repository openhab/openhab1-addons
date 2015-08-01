package org.openhab.binding.sapp.internal;

public class SappAddressOpenClosedStatus extends SappAddress {

	private int openValue;

	public SappAddressOpenClosedStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int openValue) {
		super(pnmasId, addressType, address, subAddress);
		this.openValue = openValue;
	}

	public int getOpenValue() {
		return openValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), openValue);
	}
}
