/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Implements IOStream for serial devices.
* 
* @author Bernd Pfrommer
* @author Daniel Pfrommer
* @since 1.7.0
*/
public class SerialIOStream extends IOStream {
	private static final Logger logger = LoggerFactory.getLogger(SerialIOStream.class);
	private	SerialPort		m_port		= null;
	private	final String	m_appName	= "PLM";
	private final int		m_speed		= 19200; // baud rate
	private String			m_devName 	= null;
	
	public SerialIOStream(String devName) {
		m_devName = devName;
	}
	
	@Override
	public boolean open() {
		try {
			/* by default, RXTX searches only devices /dev/ttyS* and
			 * /dev/ttyUSB*, and will so not find symlinks. The
			 *  setProperty() call below helps 
			 */
			String ports = System.getProperty("gnu.io.rxtx.SerialPorts");
			String sp = ((ports == null) ? "" : (ports + ":")) + m_devName;
			// note: calling setProperty() is not threadsafe, in particular if
			// multiple bindings use the serial port
			System.setProperty("gnu.io.rxtx.SerialPorts", sp);
			CommPortIdentifier ci =
					CommPortIdentifier.getPortIdentifier(m_devName);
			CommPort cp = ci.open(m_appName, 1000);
			if (cp instanceof SerialPort) {
				m_port = (SerialPort)cp;
			} else {
				throw new IllegalStateException("unknown port type");
			}
			m_port.setSerialPortParams(m_speed, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
			m_port.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
			logger.debug("setting port speed to {}", m_speed);
			m_port.disableReceiveFraming();
			m_port.enableReceiveThreshold(1);
			m_port.disableReceiveTimeout();
			m_in	= m_port.getInputStream();
			m_out	= m_port.getOutputStream();
			logger.info("successfully opened port {}", m_devName);
			return true;
		} catch (IOException e) {
			logger.error("cannot open port: {}, got IOException ", m_devName, e);
		} catch (PortInUseException e) {
			logger.error("cannot open port: {}, it is in use!", m_devName);
		} catch (UnsupportedCommOperationException e) {
			logger.error("got unsupported operation {} on port {}",
					e.getMessage(), m_devName);
		} catch (NoSuchPortException e) {
			logger.error("got no such port for {}", m_devName);
		} catch (IllegalStateException e) {
			logger.error("got unknown port type for {}", m_devName);
		}
		return false;
	}

	@Override
	public void close() {
		if (m_port != null) {
			m_port.close();
		}
		m_port = null;
	}
	

}
