/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Serial Interface for connecting to the Nikobus system. This interface has
 * been designed to work with the Nikobus PC-Link (05-200) module.
 * 
 * To view default serial ports on linux use lsusb.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusInterface implements SerialPortEventListener {

	private static Logger log = LoggerFactory.getLogger(NikobusInterface.class);

	private static final String RS232_SET_CMD_MODE = "++++";
	private static final String RS232_DISCONNECT = "ATH0";
	private static final String RS232_RESET_DEVICE = "ATZ";
	private static final String PC_LINK_IDENTIFIER = "$10110000B8CF9D";
	private String serialPort;
	private static final int CONNECT_TIMEOUT = 2000;
	private static final String SERIAL_PORT_PROPERTY_NAME = "gnu.io.rxtx.SerialPorts";
	private SerialPort port;
	private LinkedBlockingQueue<byte[]> bufferQueue = new LinkedBlockingQueue<byte[]>();
	private volatile long lastEventTimestamp;

	/**
	 * @return true if any data was received so far.
	 */
	private boolean hasReceivedData() {
		return lastEventTimestamp > 0;
	}

	/**
	 * Check if we have an active connection.
	 * 
	 * @return true if there is an active serial connection.
	 */
	public boolean isConnected() {
		if (port == null) {
			return false;
		}
		try {
			if (port.getInputStream() == null) {
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return hasReceivedData();
	}

	/**
	 * Initialize a serial connection to the Nikobus module.
	 * 
	 * @throws Exception
	 */
	public void connect() throws Exception {

		if (isConnected()) {
			return;
		}

		// reset flag
		lastEventTimestamp = 0;

		if (serialPort == null) {
			throw new Exception(
					"No serial port defined. Check your configuration.");
		}

		openCommPort(serialPort);

		// send connection sequence
		write(RS232_SET_CMD_MODE);
		write(RS232_DISCONNECT);
		write(RS232_RESET_DEVICE);
		write(PC_LINK_IDENTIFIER);
		write("#L0");
		write("#E0");
		write("#L0");
		write("#E1");

		// wait until we receive some acknowledgment from the nikobus
		// if we don't received it within 2 seconds, we shut it down..
		long start = System.currentTimeMillis();
		while (start - System.currentTimeMillis() < 2000) {
			if (!hasReceivedData()) {
				// give the pc-link some time to respond..
				Thread.sleep(200);
			} else {
				break;
			}
		}

		if (isConnected()) {
			log.info("Connected to Nikobus :-)");
		} else {
			log.error("Could not connect to Nikobus.");
			if (port != null) {
				try {
					port.removeEventListener();
					port.getInputStream().close();
					port.getOutputStream().close();
				} catch (IOException e) {
					// noop
				}
				port.close();
			}
		}
	}

	/**
	 * Locate serial port and open connection.
	 * 
	 * @param portName
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void openCommPort(String portName) throws Exception {

		if (log.isDebugEnabled()) {
			Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
			while (portIdentifiers.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portIdentifiers.nextElement();
				log.debug("Found port: {} x {}", id.getName(), id.getCurrentOwner());
			}
		}

		CommPortIdentifier portIdentifier = null;

		try {
			portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		} catch (NoSuchPortException e) {
			log.debug("Port not found during first attempt : {}", e.getMessage());
		}

		if (portIdentifier == null) {
			try {
				System.setProperty(SERIAL_PORT_PROPERTY_NAME, portName);
				portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
			} catch (Exception e) {
				log.debug("Port not found during second attempt : {}", e.getMessage());
				System.clearProperty(SERIAL_PORT_PROPERTY_NAME);
			}
		}

		if (portIdentifier == null) {
			throw new Exception("Serial port '" + portName + "' not found.");
		}
		if (portIdentifier.isCurrentlyOwned()) {
			throw new Exception("Serial port '" + portName + "' is in use.");
		}

		// open port
		port = (SerialPort) portIdentifier.open(this.getClass().getSimpleName(), CONNECT_TIMEOUT);
		log.info("Connected to serial port '{}'", portIdentifier.getName());

		// configure
		port.setSerialPortParams(9600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		port.notifyOnDataAvailable(true);
		port.addEventListener(this);
	}

	/**
	 * Disconnect the serial port connection to the Nikobus module.
	 */
	public void disconnect() {

		log.info("Closing Nikobus connection.");

		if (!isConnected()) {
			return;
		}

		lastEventTimestamp = 0;

		port.removeEventListener();
		try {
			write(RS232_DISCONNECT);
		} catch (Exception e) {
			log.error("Could not send disconnect command", e);
		}

		if (port != null) {
			try {
				port.getInputStream().close();
				port.getOutputStream().close();
			} catch (IOException e) {
				log.error("Error closing port: {}", e.getMessage());
			}

			port.close();
		}
	}

	/**
	 * Write a message to the serial port. The message will be terminated with a
	 * CR.
	 * 
	 * @param message
	 * @throws Exception
	 */
	private void write(String message) throws Exception {

		if (hasReceivedData() && !isConnected()) {
			log.warn("Nikobus interface connected lost. Reconnecting...");
			connect();
		}

		log.debug("Sending : {}", message);

		port.getOutputStream().write(message.getBytes());
		port.getOutputStream().write("\r".getBytes());
		port.getOutputStream().flush();

	}

	/**
	 * Write a message to the serial port. The message will be terminated with a
	 * CR.
	 * 
	 * @param message
	 * @throws Exception
	 */
	public void writeMessage(String message) throws Exception {

		if (!hasReceivedData()) {
			return;
		}
		write(message);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Any data received from a serial event, is immediately passed on to the
	 * command receiver thread.
	 */
	@Override
	public void serialEvent(SerialPortEvent event) {

		if (event.getEventType() != SerialPortEvent.DATA_AVAILABLE) {
			return;
		}

		InputStream is = null;

		try {

			lastEventTimestamp = System.currentTimeMillis();
			is = port.getInputStream();

			int available = is.available();
			if (available == 0) {
				log.warn("Received data available event, but no data was found!");
				return;
			}

			byte[] buffer = new byte[available];
			is.read(buffer, 0, available);
			bufferQueue.add(buffer);
			if (available != 1 || buffer[0] != 13) {
				// don't print single CR's in log..
				log.trace("Received: {}", new String(buffer));
			}
		} catch (IOException e) {
			log.error("Error receiving data on serial port {}: {}",
					port.getName(), e.getMessage());
		}

	}

	/**
	 * @return queue containing all received characters.
	 */
	public LinkedBlockingQueue<byte[]> getBufferQueue() {
		return bufferQueue;
	}

	/**
	 * Set the name of the serial port to use, e.g. /dev/ttyUSB0
	 * 
	 * @param configuredSerialPort
	 */
	public void setPort(String configuredSerialPort) {
		serialPort = configuredSerialPort;
	}

	/**
	 * @return name of serial port.
	 */
	public String getPort() {
		return serialPort;
	}

	/**
	 * @return time when last serial event was received
	 */
	public long getLastEventTimestamp() {
		return lastEventTimestamp;
	}

}