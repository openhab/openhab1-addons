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