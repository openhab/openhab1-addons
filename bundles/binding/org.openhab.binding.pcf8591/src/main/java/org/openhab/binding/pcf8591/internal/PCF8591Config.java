package org.openhab.binding.pcf8591.internal;

import org.openhab.io.gpio_raspberry.device.I2CConfig;

public class PCF8591Config extends I2CConfig {

	public PCF8591Config(String id) {
		super(id);
	}

	public PCF8591Config(String id, byte address) {
		super(id, address);
	}
	
}
