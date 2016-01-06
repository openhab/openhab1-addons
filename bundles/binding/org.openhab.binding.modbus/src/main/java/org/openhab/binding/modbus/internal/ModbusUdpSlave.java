/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.net.InetAddress;
import java.net.UnknownHostException;
import net.wimpi.modbus.io.ModbusUDPTransaction;
import net.wimpi.modbus.net.UDPMasterConnection;
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
public class ModbusUdpSlave extends ModbusIPSlave {

	private static final Logger logger = LoggerFactory.getLogger(ModbusUdpSlave.class);

	private UDPMasterConnection connection = null;

	public ModbusUdpSlave(String slave) {
		super(slave);
		transaction = new ModbusUDPTransaction();
	}

	public boolean isConnected() {
		return true;
	}

	/**
	 * Establishes connection to the device
	 */
	public boolean connect() {
		try {
			if (connection == null)
				connection = new UDPMasterConnection(InetAddress.getByName(getHost()));
		} catch (UnknownHostException e) {
			logger.debug("ModbusSlave: Error connecting to master: {}", e.getMessage());
			resetConnection();
			return false;
		}
		if (!connection.isConnected())
			try {
				connection.setPort(getPort());
				connection.connect();
                                ((ModbusUDPTransaction)transaction).setTerminal(connection.getTerminal());
			} catch (Exception e) {
				logger.debug("ModbusSlave: Error connecting to master: {}", e.getMessage());
				return false;
			}
		return true;
	}
	
	public void resetConnection() {
		if (connection != null) {
			connection.close();
		}
		connection = null;
	}
}
