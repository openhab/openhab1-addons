/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
 */

package org.openhab.binding.swegonventilation.protocol;

import java.io.IOException;
import java.io.InputStream;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

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

	String portName = null;
	SerialPort serialPort = null;
	InputStream in = null;

	public SwegonVentilationSerialConnector(String portName) {

		logger.debug("Swegon ventilation Serial Port message listener started");
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
			serialPort.setSerialPortParams(38400, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			in = serialPort.getInputStream();

		} catch (Exception e) {
			throw new SwegonVentilationException(e);
		}
	}

	@Override
	public void disconnect() throws SwegonVentilationException {
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
			throw new SwegonVentilationException(e);
		}
		
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
