/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.samsungtv.internal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.quist.samy.remocon.Key;
import de.quist.samy.remocon.RemoteSession;

/**
 * This class open a TCP/IP connection to the Samsung device, sends commands and wait for a response
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class SamsungTvConnection {

	private static Logger logger = 
		LoggerFactory.getLogger(SamsungTvConnection.class);
	
	private String ip;
	private int port;
	
	
	public SamsungTvConnection(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * Sends a command to Samsung device.
	 * 
	 * @param cmd
	 *            Command to send
	 */
	public void send(final String cmd) {
		
		RemoteSession remoteController = null;
		
		try {
			remoteController = RemoteSession.create("openHAB", "openHAB", ip, port);
			
			Key key = Key.valueOf(cmd);
			logger.debug("Try to send command: {}", cmd);
			
			remoteController.sendKey(key);

		} catch (Exception e) {
			logger.error("Could not send command to device on {}: {}", ip + ":" + port, e);
		} 
		
		remoteController = null;
	}
	

}