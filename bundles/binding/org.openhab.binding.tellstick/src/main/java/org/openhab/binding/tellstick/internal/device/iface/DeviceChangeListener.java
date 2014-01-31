package org.openhab.binding.tellstick.internal.device.iface;

import java.util.EventListener;

import org.openhab.binding.tellstick.internal.device.TellstickDeviceEvent;


public interface DeviceChangeListener extends EventListener {

	/**
	 * This event listener must be implemented.
	 * This is the method that will get called if we got requests.
	 */
	void onRequest(TellstickDeviceEvent newDevices);
	
}
