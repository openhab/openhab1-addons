/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the serial implementation of the eBus connector. It only handles
 * serial specific connection/disconnection. All logic is handled by
 * abstract class AbstractEBusConnector.
 * 
* @author Christian Sowada
* @since 1.7.0
*/
public class EBusSerialConnector extends AbstractEBusConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusSerialConnector.class);
	
	/** The serial object */
	private SerialPort serialPort;
	
	/** The serial port name */
	private String port;

	/**
	 * @param port
	 */
	public EBusSerialConnector(String port) {
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#connect()
	 */
	@Override
	public boolean connect() throws IOException {
		try {
			final CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(port);

			if(portIdentifier != null) {
				serialPort = (SerialPort) portIdentifier.open("openhab-ebus", 5000);
				serialPort.setSerialPortParams(2400, SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				// set timeout 10 sec.
				serialPort.enableReceiveTimeout(10000);
				serialPort.disableReceiveThreshold();
				
				outputStream = serialPort.getOutputStream();
				inputStream = serialPort.getInputStream();
				
				return true;
			}
			
		} catch (NoSuchPortException e) {
			logger.error("Unable to connect to serial port {}", port);
			
		} catch (PortInUseException e) {
			logger.error("Serial port {} is already in use", port);
			
		} catch (UnsupportedCommOperationException e) {
			logger.error(e.toString(), e);
			
		}
		
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#disconnect()
	 */
	@Override
	public boolean disconnect() throws IOException  {
		super.disconnect();
		
		if(serialPort != null) {
			serialPort.close();
			serialPort = null;
		}

		return true;
	}
}
