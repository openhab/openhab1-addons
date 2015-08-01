package org.openhab.binding.sapp.internal;

public class SappAddressSwitchItemStatus extends SappAddress {

	private int onValue;

	public SappAddressSwitchItemStatus(String pnmasId, SappAddressType addressType, int address, String subAddress, int onValue) {
		super(pnmasId, addressType, address, subAddress);
		this.onValue = onValue;
	}

	public int getOnValue() {
		return onValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), onValue);
	}
}
