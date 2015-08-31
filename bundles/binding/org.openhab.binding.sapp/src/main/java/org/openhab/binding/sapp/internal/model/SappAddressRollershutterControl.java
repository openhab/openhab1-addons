package org.openhab.binding.sapp.internal.model;


public class SappAddressRollershutterControl extends SappAddress {

	private int activateValue;

	public SappAddressRollershutterControl(String pnmasId, SappAddressType addressType, int address, String subAddress, int activateValue) {
		super(pnmasId, addressType, address, subAddress);
		this.activateValue = activateValue;
	}

	public int getActivateValue() {
		return activateValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(), activateValue);
	}
}
