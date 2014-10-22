/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.lib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages the connection with the TCP/IP socket for the specified device
 * attached to the GC-100.
 * 
 * @author Parikshit Thakur & Team
 * @since 1.6.0
 */

class TCPIPSocket {

	private InetAddress inetAddress;
	private String ipAddressString;
	private int port;
	private String deviceType;
	private Socket socket;
	private int module;
	private int connector;
	private static Logger logger = LoggerFactory.getLogger(TCPIPSocket.class);
	private boolean connected;

	/**
	 * Constructor. Initializes the inetAddress, port, module, connector and
	 * deviceType to local variables.
	 * 
	 * @param inetAddress
	 *            an Object of InetAddress
	 * @param ipAddressString
	 *            an String of InetAddress
	 * @param port
	 *            an integer value of port
	 * @param module
	 *            an integer value of module
	 * @param connector
	 *            an integer value of connector
	 * @param deviceType
	 *            a String value of deviceType
	 * @throws IOException
	 */
	TCPIPSocket(InetAddress ipAddress, String ipAddressString, int port,
			int module, int connector, String deviceType) throws IOException {

		this.inetAddress = ipAddress;
		this.ipAddressString = ipAddressString;
		this.port = port;
		this.module = module;
		this.connector = connector;
		this.deviceType = deviceType;

	}

	/**
	 * Constructor. Initializes the inetAddress, port, module, connector and
	 * deviceType to local variables.
	 * 
	 * @param inetAddress
	 *            an Object of InetAddress
	 * @param port
	 *            an integer value of port
	 * @param module
	 *            an integer value of module
	 * @param connector
	 *            an integer value of connector
	 * @param deviceType
	 *            a String value of deviceType
	 * @param socket
	 *            an Object of Socket
	 * @throws IOException
	 */
	TCPIPSocket(InetAddress ipAddress, String ipAddressString, int port,
			int module, int connector, String deviceType, Socket socket)
			throws IOException {

		this.inetAddress = ipAddress;
		this.ipAddressString = ipAddressString;
		this.port = port;
		this.module = module;
		this.connector = connector;
		this.deviceType = deviceType;
		this.socket = socket;
		this.connected = true;
	}

	/**
	 * Checks if TCPIPSocket is connected.
	 * @return true if connected false otherwise.
	 */
	boolean isConnected() {
		return connected;
	}

	/**
	 * Returns IP address in string format.
	 * @return an IP address string
	 */
	String getIPAddressString() {
		return ipAddressString;
	}

	/**
	 * Connect to the TCP/IP socket.
	 * 
	 * @return a boolean whether connection is successful or not.
	 * @throws IOException
	 */
	boolean connect() throws IOException {

		if (connected)
			return true;

		try {
			socket = new Socket(inetAddress, port);

			if (socket.isConnected()) {

				connected = true;
				return true;
			}

		} catch (Exception e) {
			logger.error("Bind Exception Already Connected " + e.getMessage());
		}
		return false;
	}

	/**
	 * Get IPAddress
	 * 
	 * @return an Object of InetAddress
	 */
	InetAddress getInetAddress() {
		return inetAddress;
	}

	/**
	 * Get Port no.
	 * 
	 * @return an integer value for portNo.
	 */
	int getPort() {
		return port;
	}

	/**
	 * Get Socket.
	 * 
	 * @return an Object of Socket
	 */
	Socket getSocket() {
		return socket;
	}

	/**
	 * Get Module.
	 * 
	 * @return an integer value of module
	 */
	int getModule() {
		return module;
	}

	/**
	 * Get connector.
	 * 
	 * @return an integer value of connector
	 */
	int getConnector() {
		return connector;
	}

	/**
	 * Get the device type
	 * 
	 * @return a String value of device type
	 */
	String getDeviceType() {
		return deviceType;
	}
}
