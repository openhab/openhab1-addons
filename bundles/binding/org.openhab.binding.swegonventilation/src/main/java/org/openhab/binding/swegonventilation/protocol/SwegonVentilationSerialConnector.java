/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for serial port communication.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationSerialConnector extends
		SwegonVentilationConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(SwegonVentilationSerialConnector.class);

	static final int BAUDRATE = 38400;
	
	String portName = null;
	SerialPort serialPort = null;
	InputStream in = null;

	public SwegonVentilationSerialConnector(String portName) {

		this.portName = portName;
	}

	@Override
	public void connect() throws SwegonVentilationException {

		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier
					.getPortIdentifier(portName);
			CommPort commPort = portIdentifier.open(this.getClass().getName(),
					2000);
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(BAUDRATE, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			in = serialPort.getInputStream();

			logger.debug("Swegon ventilation Serial Port message listener started");

		} catch (Exception e) {
			throw new SwegonVentilationException(e);
		}
	}

	@Override
	public void disconnect() throws SwegonVentilationException {
		logger.debug("Disconnecting");
		
		if (in != null) {
			logger.debug("Close serial in stream");
			IOUtils.closeQuietly(in);
		}
		if (serialPort != null) {
			logger.debug("Close serial port");
			serialPort.close();
		}
	
		serialPort = null;
		in = null;

		logger.debug("Closed");
	}

	@Override
	public byte[] receiveDatagram() throws SwegonVentilationException {

		if (in == null) {
			connect();
		}

		byte[] buffer = new byte[1024];
		byte[] message = new byte[1024];

		int len = -1;
		int msgLen = 0;
		boolean start_found = false;
		int index = 0;

		try {
			while ((len = this.in.read(buffer)) > 0) {
				for (int i = 0; i < len; i++) {

					if (buffer[i] == (byte) 0xCC) {

						start_found = true;
						index = 0;
						msgLen = 0;

					} else if (start_found) {

						message[index++] = buffer[i];

						if (index == 5) {

							if (message[0] == (byte) 0x64) {
								msgLen = message[4];
							}
						}

						if (msgLen > 0 && index == (5 + msgLen + 2)) {

							int calculatedCRC = calculateCRC(message, index - 2);
							int msgCRC = toInt(message[5 + msgLen],
									message[5 + msgLen + 1]);

							if (msgCRC == calculatedCRC) {

								byte[] data = new byte[5 + msgLen];

								for (int j = 0; j < (5 + msgLen); j++)
									data[j] = message[j];

								return data;

							} else {
								throw new SwegonVentilationException(
										"CRC does not match");
							}
						}
					}

				}
			}
		} catch (IOException e) {
			throw new SwegonVentilationException(
					"Error occured while receiving data", e);
		}

		return null;
	}
}
