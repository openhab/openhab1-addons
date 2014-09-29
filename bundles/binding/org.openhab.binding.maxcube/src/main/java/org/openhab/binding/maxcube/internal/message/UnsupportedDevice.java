package org.openhab.binding.maxcube.internal.message;

import java.util.Calendar;

public class UnsupportedDevice extends Device {

	public UnsupportedDevice(Configuration c) {
		super(c);
	}

	@Override
	public DeviceType getType() {
		return DeviceType.Invalid;
	}

	@Override
	public String getName() {
		return "Unsupported device";
	}

	@Override
	public Calendar getLastUpdate() {
		// TODO Auto-generated method stub
		return null;
	}

}
