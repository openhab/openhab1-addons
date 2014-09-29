/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.net.TCPMasterConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ModbusSlave class instantiates physical Modbus slave. 
 * It is responsible for polling data from physical device using TCPConnection.
 * It is also responsible for updating physical devices according to OpenHAB commands  
 *
 * @author Dmitry Krasnov
 * @since 1.1.0
 */
public class ModbusTcpSlave extends ModbusSlave {

	private static final Logger logger = LoggerFactory.getLogger(ModbusTcpSlave.class);

	/** host address */
	private String host;

	/** connection port. Default 502 */
	private int port = Modbus.DEFAULT_PORT;

	private TCPMasterConnection connection = null;

	public ModbusTcpSlave(String slave) {
		super(slave);
		transaction = new ModbusTCPTransaction();
	}

	/**
	 * Performs physical write to device when slave type is "holding" using Modbus FC06 function
	 * @param command command received from OpenHAB
	 * @param readRegister reference to the register that stores current value
	 * @param writeRegister register reference to write data to
	 */

	public boolean isConnected() {
		return connection != null;
	}

	/**
	 * Establishes connection to the device
	 */
	public boolean connect() {
		try {
			if (connection == null)
				connection = new TCPMasterConnection(InetAddress.getByName(getHost()));
		} catch (UnknownHostException e) {
			logger.debug("ModbusSlave: Error connecting to master: " + e.getMessage());				
			connection = null;
			return false;
		}
		if (!connection.isConnected())
			try {
				connection.setPort(getPort());
				connection.connect();
				((ModbusTCPTransaction)transaction).setConnection(connection);
				((ModbusTCPTransaction)transaction).setReconnecting(false);
			} catch (Exception e) {
				logger.debug("ModbusSlave: Error connecting to master: " + e.getMessage());				
				return false;
			}
		return true;
	}
	
	public void resetConnection() {
		connection = null;
	}

	String getHost() {
		return host;
	}

	void setHost(String host) {
		this.host = host;
	}

	int getPort() {
		return port;
	}

	void setPort(int port) {
		this.port = port;
	}

}
