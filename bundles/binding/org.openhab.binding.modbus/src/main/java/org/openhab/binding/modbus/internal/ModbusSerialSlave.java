/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.modbus.internal;

import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.util.SerialParameters;

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
public class ModbusSerialSlave extends ModbusSlave {

	private static final Logger logger = LoggerFactory.getLogger(ModbusSerialSlave.class);

	private static String port = null;
	private static int baud = 9600;
	public void setPort(String port) {
		ModbusSerialSlave.port = port;
	}

	public void setBaud(int baud) {
		ModbusSerialSlave.baud = baud;
	}

	//	String port = null;

	private static SerialConnection connection = null;

	public ModbusSerialSlave(String slave) {
		super(slave);
		transaction = new ModbusSerialTransaction();
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
			//			Enumeration<CommPortIdentifier> portlist =  CommPortIdentifier.getPortIdentifiers();
			//			while (portlist.hasMoreElements()) {
			//				logger.debug(portlist.nextElement().toString());
			//			}

			if (connection == null) {
				SerialParameters params = new SerialParameters();
				params.setPortName(port);
				params.setBaudRate(baud);
				params.setDatabits(8);
				params.setParity("None");
				params.setStopbits(1);
				params.setEncoding(Modbus.SERIAL_ENCODING_RTU);
				params.setEcho(false);
				connection = new SerialConnection(params);
				connection.open();
			}
			if (!connection.isOpen()) {
				connection.open();
			}
			((ModbusSerialTransaction)transaction).setSerialConnection(connection);
		} catch (Exception e) {
			logger.debug("ModbusSlave: Error connecting to master: " + e.getMessage());				
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
