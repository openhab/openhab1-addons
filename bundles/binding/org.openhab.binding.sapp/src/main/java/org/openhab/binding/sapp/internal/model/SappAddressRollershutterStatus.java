package org.openhab.binding.sapp.internal.model;


public class SappAddressRollershutterStatus extends SappAddress {

	private int openValue;
	private int closedValue;

	public SappAddressRollershutterStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int openValue, int closedValue) {
		super(pnmasId, addressType, address, subAddress);
		this.openValue = openValue;
		this.closedValue = closedValue;
	}

	public int getOpenValue() {
		return openValue;
	}

	public int getClosedValue() {
		return closedValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), openValue, closedValue);
	}
}
