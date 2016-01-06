/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.DataOutputStream;
import java.io.IOException;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

public class SerialPortConnector extends SerialConnector {
	private String device;
	private int baudrate;
	SerialPort serialPort = null;
	
	public SerialPortConnector(String device, int baudrate) {
			this.device = device;
			this.baudrate = baudrate;
	}

	@Override
	protected void connectPort() throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(device);

		CommPort commPort = portIdentifier
				.open(this.getClass().getName(), 2000);

		serialPort = (SerialPort) commPort;
		setSerialPortParameters(baudrate);

		in = serialPort.getInputStream();
		out = new DataOutputStream(serialPort.getOutputStream());
	}

	@Override
	protected void disconnectPort() {
		serialPort.close();
	}
	
	/**
	 * Sets the serial port parameters to xxxxbps-8N1
	 * 
	 * @param baudrate
	 *            used to initialize the serial connection
	 */
	protected void setSerialPortParameters(int baudrate) throws IOException {

		try {
			// Set serial port to xxxbps-8N1
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException ex) {
			throw new IOException(
					"Unsupported serial port parameter for serial port");
		}
	}
}
