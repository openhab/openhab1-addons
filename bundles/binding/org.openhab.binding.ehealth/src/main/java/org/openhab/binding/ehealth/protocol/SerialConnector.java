/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ehealth.protocol;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.TooManyListenersException;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.ehealth.internal.EHealthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides access to the serial port. Interested parties should
 * add themselves as {@link SerialEventProcessor}.
 * 
 * @author Thomas Eichstaedt-Engelen
 * @since 1.6.0
 */
public class SerialConnector implements SerialPortEventListener {

	private static final Logger logger = 
		LoggerFactory.getLogger(SerialConnector.class);
	
	private String portName;
	private final static int BAUD = 9600;
	
	private CommPortIdentifier portId;
	private SerialPort serialPort;

	private InputStream inputStream;
	private OutputStream outputStream;
	private BufferedReader bReader;
	private BufferedWriter bWriter;

	private List<SerialEventProcessor> processors;
	
	
	public SerialConnector(String portName) {
		this.portName = portName;
		this.processors = new ArrayList<SerialEventProcessor>();
	}
	
	
	public void addSerialEventProcessor(SerialEventProcessor processor) {
		this.processors.add(processor);
	}
	
	public void removeSerialEventProcessor(SerialEventProcessor processor) {
		this.processors.remove(processor);
	}
	
	public void connect() throws EHealthException {
		logger.debug("Goint to open Serial connection on port '{}'", portName);
		
		// parse ports and if the default port is found, initialized the reader
		Enumeration<?> portList = CommPortIdentifier.getPortIdentifiers();
		while (portList.hasMoreElements()) {
			CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
			if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (id.getName().equals(portName)) {
					logger.debug("Serial port '{}' has been found.", portName);
					portId = id;
				}
			}
		}
		
		if (portId != null) {
			// initialize serial port
			try {
				serialPort = (SerialPort) portId.open("openHAB", 2000);
				
				inputStream = serialPort.getInputStream();
				outputStream = serialPort.getOutputStream();
				
				bReader = new BufferedReader(new InputStreamReader(inputStream));
				bWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
				
				serialPort.addEventListener(this);
				serialPort.notifyOnDataAvailable(true);
				serialPort.setSerialPortParams(BAUD, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

			} catch (IOException e) {
				throw new EHealthException(e);
			} catch (PortInUseException e) {
				throw new EHealthException(e);
			} catch (UnsupportedCommOperationException e) {
				throw new EHealthException(e);
			} catch (TooManyListenersException e) {
				throw new EHealthException(e);
			}
			
		} else {
			StringBuilder sb = new StringBuilder();
			portList = CommPortIdentifier.getPortIdentifiers();
			while (portList.hasMoreElements()) {
				CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
				if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
					sb.append(id.getName() + "\n");
				}
			}
			throw new EHealthException("Serial port '" + portName + "' could not be found. Available ports are:\n" + sb.toString());
		}
	}

	public void disconnect() {
		logger.debug("Disconnecting from serial port '{}'", portName);
		
		if (serialPort != null) {
			serialPort.removeEventListener();
		}
		
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(outputStream);
		if (serialPort != null) {
			serialPort.close();
		}
		
		logger.debug("Serial port '{}' closed", portName);
	}

	
	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				String data = bReader.readLine();
				logger.trace("Received raw message to process: '{}'", data);
				notifyEventProcessors(data);
			} catch (IOException e) {
				logger.error("Exception while reading from serial port", e);
			}
		}
	}
	
	
	private void notifyEventProcessors(String data) {
		for (SerialEventProcessor processor : processors) {
			processor.processSerialData(data);
		}
	}


	/**
	 * Writes the given {@code message} to the serial port.
	 * 
	 * @param message the message to write to the serial port.
	 */
	public void writeMessage(String message) {
		logger.debug("Sending raw message to serial port: " + message);
		if (bWriter == null) {
			logger.error("Can't write message, BufferedWriter is NULL");
		}
		
		synchronized (bWriter) {
			try {
				bWriter.write(message);
				bWriter.flush();
			} catch (IOException e) {
				logger.error("Can't write to serial port", e);
			}
		}
	}
	
	
}
