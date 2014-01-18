/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.protocol;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for serial port communication.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorSerialConnector extends
		OpenEnergyMonitorConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorSerialConnector.class);

	static final int BAUDRATE = 9600;

	String portName = null;
	SerialPort serialPort = null;
	InputStream in = null;

	public OpenEnergyMonitorSerialConnector(String portName) {

		this.portName = portName;
	}

	@Override
	public void connect() throws OpenEnergyMonitorException {

		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(BAUDRATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			in = serialPort.getInputStream();
			logger.debug("Open Energy Monitor Serial Port message listener started");

		} catch (Exception e) {
			throw new OpenEnergyMonitorException(e);
		}
	}

	@Override
	public void disconnect() throws OpenEnergyMonitorException {
		logger.debug("Disconnecting");
		
		try {
			logger.debug("Close serial streams");

			if (in != null) {
				in.close();
			}

			in = null;

			if (serialPort != null) {
				logger.debug("Close serial port");
				serialPort.close();
				serialPort = null;
			}

		} catch (IOException e) {
			throw new OpenEnergyMonitorException(e);
		}
		
		logger.debug("Closed");
	}

	@Override
	public byte[] receiveDatagram() throws OpenEnergyMonitorException {

		if (in == null) {
			connect();
		}

		throw new OpenEnergyMonitorException("Not implemented");
		
		/*
		byte[] buffer = new byte[1024];

		int len = -1;
		try {
			while ((len = this.in.read(buffer)) > 0) {
				for (int i = 0; i < len; i++) {

				}
			}
		} catch (IOException e) {
			throw new OpenEnergyMonitorException(
					"Error occured while receiving data", e);
		}
		*/

	}
}
