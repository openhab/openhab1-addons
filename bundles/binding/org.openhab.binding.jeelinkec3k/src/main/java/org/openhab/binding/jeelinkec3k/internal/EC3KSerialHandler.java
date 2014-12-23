/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.jeelinkec3k.internal;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EC3KSerialHandler implements SerialPortEventListener {

	final static Logger log = LoggerFactory
			.getLogger(EC3KSerialHandler.class);
	private String deviceName;
	private SerialPort serialPort;
	private InputStream is;
	private OutputStream os;
	protected BufferedReader br;
	protected BufferedWriter bw;
	private JeeLinkEC3KBinding binding;
	private boolean isConnected;

	public EC3KSerialHandler(String device, JeeLinkEC3KBinding bind)
			throws Exception {
		deviceName = device;
		binding = bind;
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
			try {
				processNextLine();
			} catch (Exception e) {
				log.error("Serial connection read failed for " + deviceName);
			}
		}
	}

	public void openHardware() throws Exception {
		log.debug("Opening serial connection for " + deviceName);

		CommPortIdentifier portIdentifier = CommPortIdentifier
				.getPortIdentifier(deviceName);
		CommPort port = portIdentifier.open(this.getClass().getName(), 2000);
		serialPort = (SerialPort) port;
		serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,
				SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		is = serialPort.getInputStream();
		os = serialPort.getOutputStream();
		br = new BufferedReader(new InputStreamReader(is));
		bw = new BufferedWriter(new OutputStreamWriter(os));

		serialPort.notifyOnDataAvailable(true);
		log.debug("Adding serial port event listener");
		serialPort.addEventListener(this);
		this.isConnected = true;

	}

	protected void closeHardware() {
		log.debug("Closing serial device " + deviceName);
		if (serialPort != null) {
			serialPort.removeEventListener();
		}
		try {
			if (br != null) {
				br.close();
			}
			if (bw != null) {
				bw.close();
			}
		} catch (IOException e) {
			log.error("Can't close the input and output streams propberly", e);
		} finally {
			if (serialPort != null) {
				serialPort.close();
			}
			this.isConnected = false;
		}

	}

	/**
	 * read and process next line from underlying transport.
	 * 
	 * @throws CULCommunicationException
	 *             if
	 */
	protected void processNextLine() throws Exception {
		try {
			String data = br.readLine();
			if (data == null) {
				String msg = "EOF encountered for " + deviceName;
				log.error(msg);
				throw new Exception(msg);
			}

			log.debug("Received raw message from CUL: " + data);
			if ("EOB".equals(data)) {
				log.warn("(EOB) End of Buffer. Last message lost. Try sending less messages per time slot to the CUL");
				return;
			} else if ("LOVF".equals(data)) {
				log.warn("(LOVF) Limit Overflow: Last message lost. You are using more than 1% transmitting time. Reduce the number of rf messages");
				return;
			}
			binding.dataReceived(data);
			// requestCreditReport();

		} catch (IOException e) {
			log.error("Exception while reading from CUL port " + deviceName, e);
			throw new Exception(e);
		}
	}

	public boolean isConnected() {
		return isConnected;
	}

}
