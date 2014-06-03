/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device;

import org.openhab.binding.tellstick.internal.JNA.DataType;

/**
 * A sensor event from tellstick.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public class TellstickSensorEvent {
	private int sensorId;
	private String data;
	private DataType method;
	private String protocol;
	private String model;

	public TellstickSensorEvent(int sensorId, String data, DataType method, String protocol, String model) {
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
