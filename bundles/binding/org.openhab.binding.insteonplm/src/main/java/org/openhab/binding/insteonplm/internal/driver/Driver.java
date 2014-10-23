/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;
import java.io.IOException;
import java.util.HashMap;

import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;
import org.openhab.binding.insteonplm.internal.message.Msg;
import org.openhab.binding.insteonplm.internal.message.MsgListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * The driver class manages the modem ports.
 * XXX: at this time, only a single modem has ever been used. Expect
 * the worst if you connect multiple modems. When multiple modems
 * are required, this code needs to be tested and fixed.
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public class Driver {
	private static final Logger logger = LoggerFactory.getLogger(Driver.class);

	// maps device name to serial port, i.e /dev/insteon -> Port object
	private HashMap<String, Port> m_ports = new HashMap<String, Port>();
	private DriverListener m_listener = null; // single listener for notifications
	
	public HashMap<InsteonAddress, InsteonDevice> getDeviceList() {
		return m_listener.getDeviceList();
	}
	
	public void setDriverListener(DriverListener listener) {
		m_listener = listener;
	}
	public boolean isReady() {
		for (Port p : m_ports.values()) {
			if (!p.isRunning()) return false;
		}
		return true;
	}
	
	/**
	 * Add new port (modem) to the driver
	 * @param name the name of the port (from the config file, e.g. port_0, port_1, etc
	 * @param port the device name, e.g. /dev/insteon, /dev/ttyUSB0 etc
	 */
	public void addPort(String name, String port) {
		if (m_ports.keySet().contains(port)) {
			logger.warn("ignored attempt to add duplicate port: {} {}", name, port);
		} else {
			m_ports.put(port, new Port(port, this));
			logger.debug("added new port: {} {}", name, port);
		}
	}
	/**
	 * Register a message listener with a port
	 * @param listener the listener who wants to listen to port messages
	 * @param port the port (e.g. /dev/ttyUSB0) to which the listener listens
	 */
	public void addMsgListener(MsgListener listener, String port) {
		if (m_ports.keySet().contains(port)) {
			m_ports.get(port).addListener(listener);
		} else {
			logger.error("referencing unknown port {}!", port);
		}
	}
	
	public void startAllPorts() {
		for (Port p : m_ports.values()) { p.start(); }
	}
	
	public void stopAllPorts() {
		for (Port p : m_ports.values()) { p.stop(); }
	}

	/**
	 * Write message to a port
	 * @param port name of the port to write to (e.g. '/dev/ttyUSB0')
	 * @param m the message to write
	 * @throws IOException
	 */
	public void writeMessage(String port, Msg m) throws IOException {
		Port p = getPort(port);
		if (p == null) {
			logger.error("cannot write to unknown port {}", port);
			throw new IOException();
		}
		p.writeMessage(m);
	}
	
	public String getDefaultPort() {
		return (m_ports.isEmpty() ? null : m_ports.keySet().iterator().next());
	}
	
	public int getNumberOfPorts() {
		int n = 0;
		for (Port p : m_ports.values()) {
			if (p.isRunning()) n++;
		}
		return n;
	}
	
	public boolean isMsgForUs(InsteonAddress toAddr) {
		if (toAddr == null) return false;
		for (Port p : m_ports.values()) {
			if (p.getAddress().equals(toAddr)) return true;
		}
		return false;
	}
	/**
	 * Get port object corresponding to device
	 * @param port device name of port (e.g. /dev/ttyUSB0)
	 * @return corresponding Port object or null if not found 
	 */
	public Port getPort(String port) {
		if (port.equalsIgnoreCase("DEFAULT")) {
			if (m_ports.isEmpty()) {
				logger.error("no default port found!");
				return null;
			}
			return m_ports.values().iterator().next();
		}
		if (!m_ports.containsKey(port)) {
			logger.error("no port of name {} found!", port);
			return null;
		}
		return m_ports.get(port);
	}
	
	public void deviceListComplete(Port port) {
		// check if all ports have a complete device list
		if (!isDeviceListComplete()) return;
		// if yes, notify listener
		m_listener.driverCompletelyInitialized();
	}

	public boolean isDeviceListComplete() {
		// check if all ports have a complete device list
		for (Port p : m_ports.values()) {
			if (!p.isDeviceListComplete()) {
				return false;
			}
		}
		return true;
	}
}
