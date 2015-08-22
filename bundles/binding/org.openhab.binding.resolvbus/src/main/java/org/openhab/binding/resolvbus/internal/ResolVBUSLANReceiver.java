/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.resolvbus.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openhab.binding.resolvbus.model.ResolVBUSInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the LAN Receiver where the data is captured via the LAN
 * interface
 * 
 * @author Michael Heckmann
 * @since 1.8.0
 */

public class ResolVBUSLANReceiver implements ResolVBUSReceiver, Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(ResolVBUSLANReceiver.class);

	private ResolVBUSListener listener;
	private Socket vBusSocket;
	private InputStream inStream;
	private boolean running = false;
	private ResolVBUSInputStream resolStream;
	private List<Byte> resolStreamRAW;
	private String password;
	private long updateInterval;
	private Date startTime;
	private boolean keepConnectionAlive;
	private String host;
	private int port;
	private boolean valuePublished;

	public ResolVBUSLANReceiver(ResolVBUSListener listener) {
		this.listener = listener;
	}

	/**
	 * Open Socket to the LAN-Adapter
	 */
	public void initializeReceiver(String host, int port, String password,
			long updateInterval, boolean keepAlive) {

		this.password = password;
		this.updateInterval = updateInterval;
		this.host = host;
		this.port = port;
		this.keepConnectionAlive = keepAlive;
		resolStreamRAW = new ArrayList<Byte>();
		valuePublished = false;
		startTime = null;
		
		openConnection();

		if (vBusSocket == null)
			throw new IllegalStateException(
					"Cannot access socket. You must call"
							+ " call initializeListener(..) first!");

		if (initDevice())
			running = true; // start loop
		else
			logger.debug("Initialization of device was not successful");

	}

	private void openConnection() {

		try {
			vBusSocket = new Socket(host, port);
			vBusSocket.setSoTimeout(10000);
			inStream = vBusSocket.getInputStream();
			logger.debug("Connected to {}:{}", host, port);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void closeConnection() throws IOException {

		inStream.close();
		logger.debug("Closing socket..");
		vBusSocket.close();
		logger.debug("Socket to {}:{} closed", host, port);
	}

	/**
	 * Stop the thread
	 */
	public void stopListener() {
		running = false;
		if (vBusSocket != null)
			try {
				vBusSocket.close();
			} catch (IOException e) {
				logger.debug(e.getMessage());
				e.printStackTrace();
			}
		vBusSocket = null;
	}

	public void run() {
		
		if (!running) {
			logger.debug("Device not initilized...data capturing not running");
			return;
		}

		try {
			byte[] byteBuffer = new byte[1];

			// Waiting for input which is sent periodically
			do {
				receiveAndUpdate(byteBuffer);
				if (valuePublished && !keepConnectionAlive)
					running = false;
			} while (running);

			closeConnection();

		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
	}

	private boolean checkUpdateInterval() {

		if (startTime == null) {
			startTime = new Date();
			return true;
		}

		Date now = new Date();
		if (now.getTime() - startTime.getTime() < updateInterval * 1000) {
			logger.debug("Skipping data...Update Interval not reached");
			return false;
		} else {
			startTime = now;
			return true;
		}

	}

	private boolean initDevice() {
		String inputString;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					vBusSocket.getInputStream()));
			PrintWriter writer = new PrintWriter(vBusSocket.getOutputStream());

			inputString = reader.readLine();
			logger.debug("Received input: {} ", inputString);
			if (inputString.startsWith("+HELLO")) {
				logger.debug("Welcome message...sending password ");
				writer.write("PASS " + password);
				writer.flush();
			} else {
				logger.debug("No welcome Message...Exiting");
				writer.close();
				reader.close();
				return false;
			}

			inputString = reader.readLine();
			logger.debug("Received input: {} ", inputString);
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
			
			if (listener.getDeviceID() == null) {
				identifyDevice();
			}

		} catch (IOException e) {
			logger.debug("Error while initializing LAN interface)");
			logger.debug(e.getMessage());
			return false;
		}
		return true;
	}

	private void identifyDevice() throws IOException {

		logger.debug("Trying to identify Device...");

		byte[] bBuffer = new byte[1];
		boolean deviceIdentified = false;
		List<Byte> streamRAW = new ArrayList<Byte>();

		while (!deviceIdentified) {

			do {
				inStream.read(bBuffer);
				streamRAW.add(bBuffer[0]);

			} while (bBuffer[0] != (byte) 0xAA);

			streamRAW.add(0, (byte) 0xAA);

			resolStream = new ResolVBUSInputStream(streamRAW);

			if (resolStream.isErrorFree()) {
				logger.debug("Received a clean stream...processing..");
				
				String deviceID=resolStream.getSourceAddress();
				
				// Device id = 10 is not a device
				if (deviceID.equals("0010")) {
					continue;
				}

				logger.debug("Device identified: " + deviceID);
				listener.setDeviceID(deviceID);

				streamRAW.clear();
				while (bBuffer[0] != (byte) 0xAA) {
					inStream.read(bBuffer);
				}
				deviceIdentified = true;
			} else {
				logger.debug("Warning: Error in received stream...trying next stream. Can be ignored if everything else is working fine.");
				streamRAW.clear();
				resolStream = null;
			}
		}

	}

	private void receiveAndUpdate(byte[] bBuffer) throws IOException {

		do {
			inStream.read(bBuffer);
			resolStreamRAW.add(bBuffer[0]);

		} while (bBuffer[0] != (byte) 0xAA);

		resolStreamRAW.add(0, (byte) 0xAA);

		resolStream = new ResolVBUSInputStream(resolStreamRAW);

		if (resolStream.isErrorFree()) {
			logger.debug("Received a clean stream...processing..");
			if (checkUpdateInterval())
				listener.processInputStream(resolStream);

			resolStreamRAW.clear();
			while (bBuffer[0] != (byte) 0xAA) {
				inStream.read(bBuffer);
			}
			valuePublished = true;
		} else {
			logger.debug("Warning: Error in received stream...trying next stream. Can be ignored if everything else is working fine.");
			resolStreamRAW.clear();
			resolStream = null;
			return;
		}
	}

	public void initializeReceiver(String serialPort, String password,
			long updateInterval, boolean keepConnectionAlive) {
		logger.error("This is the LAN Receiver. No Serial/USB defintion necessary");

	}

}
