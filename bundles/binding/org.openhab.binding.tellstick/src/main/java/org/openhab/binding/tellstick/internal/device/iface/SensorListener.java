package org.openhab.binding.tellstick.internal.device.iface;

import java.util.EventListener;

import org.openhab.binding.tellstick.internal.device.TellstickSensorEvent;

public interface SensorListener extends EventListener {
	void onRequest(TellstickSensorEvent newDevices);
}
