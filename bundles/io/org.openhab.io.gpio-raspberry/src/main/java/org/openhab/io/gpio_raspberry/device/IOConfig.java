package org.openhab.io.gpio_raspberry.device;

import org.openhab.io.gpio_raspberry.internal.DeviceConfig;

public final class IOConfig extends DeviceConfig {
	public boolean in = false;
	public boolean activeLow = false;
	
	public IOConfig(String id) {
		super(id);
	}
	
	public IOConfig(String id, boolean in, boolean activeLow) {
		super(id);
		this.in = in;
		this.activeLow = activeLow;
	}
	
	public boolean isIn() {
		return in;
	}
	
	public void setIn(boolean in) {
		this.in = in;
	}
	
	public boolean isActiveLow() {
		return activeLow;
	}
	
	public void setActiveLow(boolean activeLow) {
		this.activeLow = activeLow;
	}
}
