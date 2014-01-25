/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal;

import java.util.EventObject;

import org.openhab.binding.onkyo.internal.eiscp.Eiscp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class open a TCP/IP connection to the Onkyo device and send a command.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class OnkyoConnection implements OnkyoEventListener {

	private static Logger logger = 
		LoggerFactory.getLogger(OnkyoConnection.class);

	private String ip;
	private int port;
	private Eiscp connection = null;
	
	public OnkyoConnection(String ip, int port) {
		this.ip = ip;
		this.port = port;
		connection = new Eiscp(ip, port);
	}

	public void openConnection() {
		connection.connectSocket();
	}

	public void closeConnection() {
		connection.closeSocket();
	}

	public void addEventListener(OnkyoEventListener listener) {
		connection.addEventListener(listener);
	}
	
	public void removeEventListener(OnkyoEventListener listener) {
		connection.removeEventListener(listener);
	}
	
	/**
	 * Sends a command to Onkyo device.
	 * 
	 * @param cmd eISCP command to send
	 */
	public void send(final String cmd) {

		try {
			connection.sendCommand(cmd);
		} catch (Exception e) {
			logger.error("Could not send command to device on {}: {}", ip + ":" + port, e);
		}

	}

	@Override
	public void statusUpdateReceived(EventObject event, String ip, String data) {
		
	}

}