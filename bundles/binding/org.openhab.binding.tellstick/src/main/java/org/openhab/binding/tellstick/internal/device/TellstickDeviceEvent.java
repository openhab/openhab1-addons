package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA.Method;

public class TellstickDeviceEvent {

	private TellstickDevice device;
	private Method method; // Look in JNA -TELLSTICK_TURNON and below
	private String data;
	
	public TellstickDeviceEvent(TellstickDevice device, Method method, String data) {
		super();
		this.device = device;
		this.method = method;
		this.data = data;
	}
	public TellstickDevice getDevice() {
		return device;
	}
	public Method getMethod() {
		return method;
	}
	public String getData() {
		return data;
	}

}
