/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
