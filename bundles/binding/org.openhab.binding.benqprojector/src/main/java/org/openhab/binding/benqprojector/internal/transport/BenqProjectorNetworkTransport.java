/**
 * Copyright (c) 2010-2014, openHAB.org and others.
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
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implement the network based transport for simple TCP/IP to RS232/Serial port
 * adapters
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class BenqProjectorNetworkTransport implements BenqProjectorTransport {

	private static final Logger logger = LoggerFactory
			.getLogger(BenqProjectorNetworkTransport.class);

	/**
	 * Network host to use
	 */
	private String networkHost = "";

	/**
	 * Network port to use
	 */
	private int networkPort = 0;

	private Socket projectorSocket = null;
	private PrintWriter projectorWriter = null;
	private BufferedReader projectorReader = null;
	private boolean retryAttempt = false;

	/**
	 * Set socket timeout time in milliseconds
	 */
	private final int SOCKET_TIMEOUT_MS = 5000;

	private boolean networkConnect() {
		logger.debug("Running connection setup");
		try {
			logger.debug("Setting up socket connection to " + this.networkHost
					+ ":" + this.networkPort);
			this.projectorSocket = new Socket(this.networkHost,
					this.networkPort);
			this.projectorSocket.setSoTimeout(SOCKET_TIMEOUT_MS);
			logger.debug("Setup reader/writer");
			this.projectorReader = new BufferedReader(new InputStreamReader(
					this.projectorSocket.getInputStream()));
			this.projectorWriter = new PrintWriter(
					this.projectorSocket.getOutputStream(), true);
			logger.debug("Network connection setup successfully!");
			return true;
		} catch (UnknownHostException e) {
			logger.error("Unable to find host: " + this.networkHost);
		} catch (IOException e) {
			logger.error("IO Exception: " + e.getMessage());
		}
		return false;
	}

	@Override
	public boolean setupConnection(String connectionParams) {
		boolean setupOK = false;
		if (this.projectorSocket == null) {
			/* parse connection paramters in the format host:port */
			String[] deviceIdParts = connectionParams.split(":");
			if (deviceIdParts.length == 2) {
				this.networkHost = deviceIdParts[0];
				this.networkPort = Integer.parseInt(deviceIdParts[1]);
			} else {
				return false;
			}

			setupOK = this.networkConnect();
		} else {
			logger.debug("Socket is already setup");
		}
		return setupOK;
	}

	@Override
	public void closeConnection() {
		try {
			this.projectorReader.close();
			this.projectorReader = null;
			this.projectorWriter.close();
			this.projectorReader = null;
			this.projectorSocket.close();
		} catch (IOException e) {
			logger.error("Trying close socket, reader or writer resulted in IO exception: "
					+ e.getMessage());
		}
		this.projectorSocket = null;
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
				if (e instanceof SocketTimeoutException)					
					logger.warn("Timed out reading response from projector");
				else if (e instanceof IOException)
					logger.error("IO Exception while reading response from projector: "
							+ e.getMessage());
				if (retryAttempt == false) {
					this.closeConnection();
					if (this.networkConnect()) {
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
