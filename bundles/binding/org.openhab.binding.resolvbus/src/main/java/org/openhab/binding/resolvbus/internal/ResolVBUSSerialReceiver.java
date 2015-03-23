/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.internal;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


import org.openhab.binding.resolvbus.model.ResolVBUSConfig;
import org.openhab.binding.resolvbus.model.ResolVBUSDevice;
import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Michael Heckmann
 * @since 1.7.0
 */

public class ResolVBUSSerialReceiver implements ResolVBUSReceiver, Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(ResolVBUSSerialReceiver.class);

	private ResolVBUSListener listener;
		private InputStream inStream;
	private boolean running = false;
	private ResolVBUSConfig config;
	private ResolVBUSInputStream resolStream;
	private List<Byte> resolStreamRAW;
	private CommPortIdentifier serialPortId;
	private Enumeration<CommPortIdentifier> enumComm;
	private SerialPort serialPort;
	private int baudrate;
	private int dataBits;
	private int stopBits;
	private int parity;
	private String portName;
	private String password;

	public ResolVBUSSerialReceiver(ResolVBUSListener listener) {
		this.listener = listener;
	}

		
	/**
	 * Open Socket to the SERIAL/USB-Adapter
	 */
	public void initializeReceiver(String serialPort, String password, ResolVBUSConfig config) {

		try {
			this.config = config;
			this.password = password;
			openSerialPort();
			logger.debug("Connected to: " + serialPort);
			resolStreamRAW = new ArrayList<Byte>();
		} catch (Exception e) {
			logger.debug(e.getMessage());
			e.printStackTrace();
		}
	}


	/**
	 * Stop the thread
	 */
	public void stopListener() {
		running = false;
		if (serialPort != null)
			serialPort.close();
		serialPort = null;
	}

	public void run() {
		if (serialPort == null)
			throw new IllegalStateException(
					"Cannot access socket. You must call"
							+ " call initializeListener(..) first!");

		if (initDevice())
			running = true; // start loop
		try {
			byte [] bBuffer = new byte[1];
			
			//Waiting for input which is sent periodically
			while (running) {
				
				do {
					inStream.read(bBuffer);
					resolStreamRAW.add(bBuffer[0]);

				} while (bBuffer[0] != (byte) 0xAA);
		
				resolStreamRAW.add(0, (byte) 0xAA);

				resolStream = new ResolVBUSInputStream(resolStreamRAW);
				

				if (!resolStream.isErrorFree()) {
					logger.debug("Error in received stream...trying next stream");
					resolStreamRAW.clear();
					continue;							
				}
				
				listener.processInputStream(resolStream);
				
				resolStreamRAW.clear();
				while (bBuffer[0] != (byte) 0xAA) {
					inStream.read(bBuffer);
				}
				Thread.sleep(5000);
			}
			
			inStream.close();
		
			
		} catch (IOException e) {
			logger.debug(e.getMessage());
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}
	}


	private boolean initDevice() {
		String inputString;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					serialPort.getInputStream()));
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
					serialPort.getOutputStream()));

			inputString = reader.readLine();
			logger.debug("Received input: "+inputString);
			if (inputString.startsWith("+HELLO")) {
				logger.debug("Welcome message...sending password");
				writer.write("PASS "+password);
				writer.flush();
			} else {
				logger.debug("No welcome Message...Exiting");
				writer.close();
				reader.close();
				return false;
			}

			inputString = reader.readLine();
			logger.debug("Received input: "+inputString);
			if (inputString.startsWith("+OK:")) {
				logger.debug("Password accepted..");
				writer.write("DATA");
				writer.flush();
			} else {
				logger.debug("Password not accepted...Exiting");
				writer.close();
				reader.close();
				return false;
			}

			inputString = reader.readLine();
			logger.debug("Waiting for SYNC byte...");
			int syncByte = -1;
			while (syncByte != 127) {
				syncByte = reader.read();
			}
			logger.debug("SYNC byte received...Data processing..");
		} catch (IOException e) {
			logger.debug("Error while initializing LAN interface)");
			logger.debug(e.getMessage());
			return false;
		}
		return true;
	}
	
	private void openSerialPort() {
		// OpenSerialPort
		Boolean foundPort = false;

		System.out.println("Opening Serialport");
		enumComm = CommPortIdentifier.getPortIdentifiers();
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (portName.contentEquals(serialPortId.getName())) {
				foundPort = true;
				break;
			}

		}
		if (foundPort != true) {
			logger.debug("Serialport not found: " + portName);
			return;
		}
		try {
			serialPort = (SerialPort) serialPortId
					.open("Open and Sending", 500);
		} catch (PortInUseException e) {
			logger.debug("Port in use");
		}

		try {
			serialPort
					.setSerialPortParams(baudrate, dataBits, stopBits, parity);
		} catch (UnsupportedCommOperationException e) {
			logger.debug("Setting SerialPort parameters not possible");
		}
		System.out.println("Serialport open");
	}


	@Override
	public void initializeReceiver(String host, int port, String password,
			ResolVBUSConfig config) {
		logger.debug("This is the SerialReceiver. No LAN defintion necessary");
		
	}	

}
