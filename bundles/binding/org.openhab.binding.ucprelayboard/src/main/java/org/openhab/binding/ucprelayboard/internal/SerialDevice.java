/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ucprelayboard.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents a serial device
 * 
 * @author Robert Michalak
 * @since 1.8.0
 */

public class SerialDevice {

	private static final Logger logger = LoggerFactory
			.getLogger(SerialDevice.class);

	private SerialDeviceConfig config;

	private CommPortIdentifier portId;
	private SerialPort serialPort;

	private InputStream inputStream;
	private OutputStream outputStream;

	public SerialDevice(SerialDeviceConfig config) {
		this.config = config;
	}
	
	public SerialDeviceConfig getConfig() {
		return config;
	}

	/**
	 * Initialize this device and open the serial port
	 * 
	 * @throws InitializationException
	 *             if port can not be opened
	 */
	public void initialize() throws InitializationException {
		try {
			portId = CommPortIdentifier.getPortIdentifier(config.getPort());

			if (portId != null) {
				// initialize serial port
				serialPort = (SerialPort) portId.open("openHAB", 2000);

				// set port parameters
				serialPort.setSerialPortParams(config.getBaud(),
						SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				inputStream = serialPort.getInputStream();

				outputStream = serialPort.getOutputStream();

			} else {
				throw new InitializationException("Serial port '"
						+ config.getPort() + "' could not be found.\n");
			}
		} catch (UnsupportedCommOperationException e) {
			throw new InitializationException(e);
		} catch (IOException e) {
			throw new InitializationException(e);
		} catch (PortInUseException e) {
			throw new InitializationException(e);
		} catch (NoSuchPortException e) {
			throw new InitializationException(e);
		}
	}

	/**
	 * Sends a string to the serial port of this device
	 * 
	 * @param relayBoardCommand
	 *            the string to send
	 */
	public void writeBytes(byte[] relayBoardCommand) {
		try {
			// write bytes to serial port
			outputStream.write(relayBoardCommand);
			outputStream.flush();
		} catch (IOException e) {
			logger.error("Error writing to serial port {}: {}", new Object[] {
					config.getPort(), e.getMessage() });
		}
	}

	public byte[] readBytes(final byte[] buffer) {
		try {
			if (inputStream.available() > 0) {
				inputStream.read(buffer);
			}
		} catch (IOException e) {
			logger.error("Error reading from serial port {}: {}", new Object[] {
					config.getPort(), e.getMessage() });
		}
		return buffer;
	}

	/**
	 * Close this serial device
	 */
	public void close() {
		IOUtils.closeQuietly(outputStream);
		IOUtils.closeQuietly(inputStream);
		serialPort.close();
	}

}
