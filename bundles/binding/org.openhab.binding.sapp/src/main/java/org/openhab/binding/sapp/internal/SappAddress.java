package org.openhab.binding.sapp.internal;

public abstract class SappAddress {

	private String pnmasId;
	private SappAddressType addressType;
	private int address;
	private String subAddress;

	public SappAddress(String pnmasId, SappAddressType addressType, int address, String subAddress) {
		super();
		this.pnmasId = pnmasId;
		this.addressType = addressType;
		this.address = address;
		this.subAddress = subAddress;
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

	@Override
	public String toString() {
		return String.format("[ %s:%s:%d:%s ]", pnmasId, addressType, address, subAddress);
	}
}
