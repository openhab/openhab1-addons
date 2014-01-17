/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.pioneeravr.internal;

import java.util.EventObject;

import org.openhab.binding.pioneeravr.internal.ipcontrolprotocol.IpControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class opens a TCP/IP connection to the Pioneer AV receiver device and sends a command.
 * 
 * @author Rainer Ostendorf
 * @author based on the Onkyo binding by Pauli Anttila and others
 * @since 1.4.0
 */
public class PioneerAvrConnection implements PioneerAvrEventListener {

	private static Logger logger = 
		LoggerFactory.getLogger(PioneerAvrConnection.class);

	private String ip;
	private int port;
	private IpControl connection = null;
	
	public PioneerAvrConnection(String ip, int port, Boolean doConnectionCheck) {
		this.ip = ip;
		this.port = port;
		connection = new IpControl(ip, port, doConnectionCheck);
	}

	public void openConnection() {
		connection.connectSocket();
	}

	public void closeConnection() {
		connection.closeSocket();
	}

	public void addEventListener(PioneerAvrEventListener listener) {
		connection.addEventListener(listener);
	}
	
	public void removeEventListener(PioneerAvrEventListener listener) {
		connection.removeEventListener(listener);
	}
	
	/**
	 * Sends a command to Pioneer AV device.
	 * 
	 * @param cmd Pioneer AV command to send
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
