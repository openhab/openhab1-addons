/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plcbus.internal.protocol;

import java.io.OutputStream;

import gnu.io.NRSerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortGateway implements ISerialPortGateway {

	private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
	private NRSerialPort serialPort;

	private SerialPortGateway(String serialPortName) {
		this.serialPort = new NRSerialPort(serialPortName, 9600);
		this.serialPort.connect();
	}

	public static ISerialPortGateway create(String serialPortName) {
		return new SerialPortGateway(serialPortName);
	}

	@Override
	public void send(TransmitFrame frame,
			IReceiveFrameContainer receivedFrameContainer) {
		try {
			byte[] paket = Convert.toByteArray(frame.getBytes());

			OutputStream out = serialPort.getOutputStream();

			out.write(paket);

			try {
				receivedFrameContainer.process(SerialPortByteProvider.create(serialPort));
			} catch (Exception e) {
				logger.error("Error while processing: " + e.getMessage());
			}

		} catch (Exception e) {
			logger.info("Error in write methode: " + e.getMessage());
		}
	}

	public void close() {
		serialPort.disconnect();
	}
	
}