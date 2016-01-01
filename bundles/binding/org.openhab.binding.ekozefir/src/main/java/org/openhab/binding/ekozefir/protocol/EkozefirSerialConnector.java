/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.protocol;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of EkozefirConnector.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public class EkozefirSerialConnector implements EkozefirConnector {

	private static final Logger logger = LoggerFactory.getLogger(EkozefirSerialConnector.class);
	private final int maxNumberTries = 5;
	private final int baud = 2400;
	private final int waitingTime = 300;
	private final String portName;
	private SerialPort serialPort = null;
	private InputStream in = null;
	private OutputStream out = null;

	public EkozefirSerialConnector(final String device) {
		Objects.requireNonNull(device);
		this.portName = device;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void connect() {
		try {
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(baud, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();
			logger.debug("Ekozefir Serial Port message listener started");
		} catch (IOException ex) {
			logger.error(ex.toString());
		} catch (NoSuchPortException ex) {
			logger.error(ex.toString());
		} catch (PortInUseException ex) {
			logger.error(ex.toString());
		} catch (UnsupportedCommOperationException ex) {
			logger.error(ex.toString());
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void disconnect() {
		logger.debug("Disconnecting Ekozefir driver");
		closeAndIgnoreException(in);
		closeAndIgnoreException(out);
		close(serialPort);
		serialPort = null;
		in = null;
		out = null;
		logger.debug("Closing port");

	}

	private void close(SerialPort portToClose) {
		if (portToClose != null) {
			portToClose.close();
		}
	}

	private void closeAndIgnoreException(Closeable toClose) {
		if (toClose != null) {
			try {
				toClose.close();
			} catch (IOException e) {
				logger.error(e.toString());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public byte[] receiveBytes(int numberBytes) {
		logger.debug("Reciving bytes from Ekozefir driver");
		for (int tries = 0; tries < maxNumberTries; tries++) {
			try {
				Thread.sleep(waitingTime);
				if (in.available() >= numberBytes) {
					byte incomingBytes[] = new byte[numberBytes];
					in.read(incomingBytes);
					logger.debug("Receive bytes: " + logBytes(incomingBytes));
					return incomingBytes;
				}
			} catch (IOException ex) {
				logger.error("Error during reading data from ekozefir driver", ex);
			} catch (InterruptedException ex) {
				logger.error("Error during reading data from ekozefir driver", ex);
			}
		}
		throw new IllegalStateException("Could not read bytes from ekozefir driver for " + maxNumberTries + " times");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sendBytes(byte[] bytes) {
		Objects.requireNonNull(bytes);
		logger.debug("Sending bytes to Ekozefir driver: " + logBytes(bytes));
		try {
			out.write(bytes);
		} catch (IOException ex) {
			logger.error(ex.toString());
		}
	}

	private String logBytes(byte[] bytes) {
		StringBuilder builder = new StringBuilder();
		for (byte byteValue : bytes) {
			builder.append(byteValue + " ");
		}
		return builder.toString();
	}
}
