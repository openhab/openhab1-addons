package org.openhab.binding.iec6205621meter.internal;

/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

/**
 * Class defining the config parameter for metering device
 * 
 * @author Peter Kreutzer
 * @since 1.5.0
 */
public class MeterDevice {

	private String serialPort;
	private int baudRateChangeDelay;
	private boolean echoHandling;

	public MeterDevice(String serialPort, int baudRateChangeDelay,
			boolean echoHandling) {
		this.serialPort = serialPort;
		this.baudRateChangeDelay = baudRateChangeDelay;
		this.echoHandling = echoHandling;
	}

	public void setSerialPort(String serialPort) {
		this.serialPort = serialPort;
	}

	public String getSerialPort() {
		return this.serialPort;
	}

	public void setBaudRateChangeDelay(int baudRateChangeDelay) {
		this.baudRateChangeDelay = baudRateChangeDelay;
	}

	public int getBaudRateChangeDelay() {
		return this.baudRateChangeDelay;
	}

	public void setEchoHandling(boolean echoHandling) {
		this.echoHandling = echoHandling;
	}

	public boolean getEchoHandling() {
		return this.echoHandling;
	}

}
