/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal.transport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import gnu.io.NRSerialPort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Implement the serial based transport for communicating with a Benq projector
 * 
 * @author Kristian Larsson (kll)
 * @since 1.8.0
 */
public class BenqProjectorSerialTransport implements BenqProjectorTransport {

	private static final Logger logger = LoggerFactory
			.getLogger(BenqProjectorSerialTransport.class);

	/**
	 * Serial device to use
	 */
	private String serialDevice = "";
	// TODO: what should default be?
	private int serialSpeed = 57600;

	private NRSerialPort serialPort = null;
	private PrintWriter projectorWriter = null;
	private BufferedReader projectorReader = null;
	private boolean retryAttempt = false;

	private boolean serialConnect() {
		logger.debug("Running connection setup");
		try {
			logger.debug("Setting up socket connection to " + this.serialDevice);
			this.serialPort = new NRSerialPort(this.serialDevice, this.serialSpeed);
			this.serialPort.connect();

			this.projectorReader = new BufferedReader(new InputStreamReader(this.serialPort.getInputStream()));
			this.projectorWriter = new PrintWriter(
					this.serialPort.getOutputStream(), true);

			logger.debug("Serial connection setup successfully!");
			return true;
		} catch (Exception e) {
			logger.error("Unable to connect to device: " + this.serialDevice, e);
		}
		return false;
	}

	@Override
	public boolean setupConnection(String connectionParams) {
		boolean setupOK = false;
		if (this.serialPort == null) {
			/* parse connection paramters in the format device:speed */
			String[] deviceIdParts = connectionParams.split(":");
			if (deviceIdParts.length == 1) {
				logger.debug("Using default speed: " + this.serialSpeed);
				this.serialDevice = deviceIdParts[0];
			} else if (deviceIdParts.length == 2) {
				this.serialDevice = deviceIdParts[0];
				this.serialSpeed = Integer.parseInt(deviceIdParts[1]);
			} else {
				return false;
			}

			setupOK = this.serialConnect();
		} else {
			logger.debug("Port is already setup");
		}
		return setupOK;
	}

	@Override
	public void closeConnection() {
		try {
			this.serialPort.disconnect();
		} catch (Exception e) {
			logger.error("Trying to close socket resulted in IO exception: "
					+ e.getMessage());
		}
		this.serialPort = null;
	}

	@Override
	public String sendCommandExpectResponse(String cmd) {
		String respStr = "";
		String tmp;
		if (this.projectorWriter != null) {
			this.projectorWriter.printf("%s", cmd);
			logger.debug("Sent command '" + cmd.replace("\r", "") + "'");
			try {
				tmp = this.projectorReader.readLine();
				while (tmp != null) {
					if (tmp.startsWith("*") == true && tmp.endsWith("#")) {
						/* got response */
						logger.debug("Response: '" + tmp + "'");
						respStr = tmp;
						break;
					}
					tmp = this.projectorReader.readLine();
				}
			} catch (Exception e) {
				logger.error("IO Exception while reading response from projector: "
						+ e.getMessage());
				if (retryAttempt == false) {
					this.closeConnection();
					if (this.serialConnect()) {
						logger.debug("Reconnect successful - retrying transmission");
						/* set flag to stop us ending up here again and so avoid infinite recursion */
						retryAttempt = true;
						sendCommandExpectResponse(cmd);						
					} else {
						logger.error("Attempt to reconnect after IOException failed: "
								+ e.getMessage());
					}
					/* reset flag */
					retryAttempt = false;
				}
			} 
		} else {
			logger.debug("Not sending command to projector as connection is not setup yet.");
		}

		return respStr;
	}

}
