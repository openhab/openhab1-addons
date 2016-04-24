/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.connector;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerException;
import org.openhab.binding.oppoblurayplayer.internal.core.OppoBlurayPlayerCommandReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for serial port communication.
 * 
 * @author netwolfuk (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerSerialConnector implements OppoBlurayPlayerConnector, SerialPortEventListener {

	private static final Logger logger = 
		LoggerFactory.getLogger(OppoBlurayPlayerSerialConnector.class);

	String serialPortName = null;
	InputStream in = null;
	OutputStream out = null;
	SerialPort serialPort = null;

	private OppoBlurayPlayerCommandReceiver commandReceiver = null;


	public OppoBlurayPlayerSerialConnector(String serialPort) {
		serialPortName = serialPort;
	}

	/**
	 * {@inheritDoc}
	 */
	public void connect() throws OppoBlurayPlayerException {

		try {
			logger.debug("Open connection to serial port '{}'", serialPortName);
			CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);

			CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);

			serialPort = (SerialPort) commPort;
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			serialPort.enableReceiveThreshold(1);
			serialPort.disableReceiveTimeout();

			in = serialPort.getInputStream();
			out = serialPort.getOutputStream();
			serialPort.addEventListener(this);
			// activate the DATA_AVAILABLE notifier
			serialPort.notifyOnDataAvailable(true);

			out.flush();
			if (in.markSupported()) {
				in.reset();
			}
			
			
		} catch (Exception e) {
			throw new OppoBlurayPlayerException(e);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	public void disconnect() throws OppoBlurayPlayerException {
		
		serialPort.removeEventListener();
		
		if (out != null) {
			logger.debug("Close serial out stream");
			IOUtils.closeQuietly(out);
		}
		if (in != null) {
			logger.debug("Close serial in stream");
			IOUtils.closeQuietly(in);
		}
		if (serialPort != null) {
			logger.debug("Close serial port");
			serialPort.close();
		}
		
		serialPort = null;
		out = null;
		in = null;

		logger.debug("Closed");
	}

	/**
	 * {@inheritDoc}
	 */
	public void sendMessage(String data, int timeout) throws OppoBlurayPlayerException {
		try {
			out.write(data.getBytes());
			out.write("\r\n".getBytes());
			out.flush();

		} catch (Exception e) {
			throw new OppoBlurayPlayerException(e);
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void messageReceived(String message) throws OppoBlurayPlayerException {
		if (commandReceiver  != null){
			commandReceiver.receivedCommand(message);
		} else {
			logger.debug("No receiver registered.");
		}
		
	}

	@Override
	public void registerCommandReceiver(OppoBlurayPlayerCommandReceiver receiver) {
		this.commandReceiver = receiver;
		logger.debug("Command receiver registered. {}", receiver);
	}

	@Override
	public void unregisterCommandReceiver(OppoBlurayPlayerCommandReceiver receiver) {
		this.commandReceiver = null;
		logger.debug("Command receiver unregistered.");
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		logger.debug("SerialPortEvent received. {}: {}", new Object[] { serialPort, event.getEventType() });
		switch (event.getEventType()) {
		case SerialPortEvent.BI:
		case SerialPortEvent.OE:
		case SerialPortEvent.FE:
		case SerialPortEvent.PE:
		case SerialPortEvent.CD:
		case SerialPortEvent.CTS:
		case SerialPortEvent.DSR:
		case SerialPortEvent.RI:
		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;
		case SerialPortEvent.DATA_AVAILABLE:
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(in), 32*1024*1024);
				if(br.ready()) {
					String line = br.readLine();
					logger.debug("SerialPortEvent line received. {}", line);
					line = StringUtils.chomp(line);
					line = line.replace(",",".");
					messageReceived(line.trim());
				}
			} catch (IOException e) {
				logger.debug("Error receiving data on serial port {}: {}", new Object[] { serialPort, e.getMessage() });
			} catch (OppoBlurayPlayerException e) {
				logger.debug("Error receiving data on serial port {}: {}", new Object[] { serialPort, e.getMessage() });
			}
			break;
		}
		
	}

}
