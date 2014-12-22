package org.openhab.io.gpio_raspberry.internal;

public abstract class DeviceConfig {
	private String id;

	public DeviceConfig() {
		super();
	}

	public DeviceConfig(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
