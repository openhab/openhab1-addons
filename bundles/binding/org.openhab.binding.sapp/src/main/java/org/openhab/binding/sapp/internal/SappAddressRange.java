package org.openhab.binding.sapp.internal;

public class SappAddressRange {
	
	private int loRange;
	private int hiRange;
	
	public SappAddressRange(int loRange, int hiRange) {
		this.loRange = loRange;
		this.hiRange = hiRange;
	}

	public int getLoRange() {
		return loRange;
	}

	public int getHiRange() {
		return hiRange;
	}
}
