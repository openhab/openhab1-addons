package org.openhab.binding.sapp.internal;

public class SappAddressOnOffControl extends SappAddress {

	private int onValue;
	private int offValue;

	public SappAddressOnOffControl(String pnmasId, SappAddressType addressType, int address, String subAddress, int onValue, int offValue) {
		super(pnmasId, addressType, address, subAddress);
		this.onValue = onValue;
		this.offValue = offValue;
	}

	public int getOnValue() {
		return onValue;
	}

	public int getOffValue() {
		return offValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), onValue, offValue);
	}
}
