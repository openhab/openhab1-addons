package org.openhab.binding.sapp.internal;

public class SappAddress {

	private String pnmasId;
	private SappAddressType addressType;
	private int address;
	private String subAddress;
	private int onValue;
	private int offValue;

	public SappAddress(String pnmasId, SappAddressType addressType, int address, String subAddress, int onValue, int offValue) {
		super();
		this.pnmasId = pnmasId;
		this.addressType = addressType;
		this.address = address;
		this.subAddress = subAddress;
		this.onValue = onValue;
		this.offValue = offValue;
	}

	public String getPnmasId() {
		return pnmasId;
	}

	public SappAddressType getAddressType() {
		return addressType;
	}

	public int getAddress() {
		return address;
	}

	public String getSubAddress() {
		return subAddress;
	}

	public int getOnValue() {
		return onValue;
	}

	public int getOffValue() {
		return offValue;
	}

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s ]", pnmasId, addressType, address, subAddress);
	}
}
