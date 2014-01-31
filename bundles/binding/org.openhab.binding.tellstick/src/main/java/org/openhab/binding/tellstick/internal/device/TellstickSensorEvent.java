package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA.DataType;

public class TellstickSensorEvent {
	private int sensorId;
	private String data;
	private DataType method;
	private String protocol;
	private String model;

	public TellstickSensorEvent(int sensorId, String data, DataType method,
			String protocol, String model) {
		super();
		this.sensorId = sensorId;
		this.data = data;
		this.method = method;
		this.protocol = protocol;
		this.model = model;
	}
	public int getSensorId() {
		return sensorId;
	}
	public String getData() {
		return data;
	}
	public DataType getDataType() {
		return method;
	}
	public String getProtocol() {
		return protocol;
	}
	public String getModel() {
		return model;
	}
}
