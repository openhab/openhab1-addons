/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.net.actions;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;


/**
 * This Action checks the vitality of the given host.
 * 
 * @author Thomas.Eichstaedt-Engelen
 * @since 1.0.0
 */
public class Ping {
	
	/**
	 * Checks the vitality of <code>host</code>. If <code>port</code> '0'
	 * is specified (which is the default when configuring just the host), a
	 * regular ping is issued. If other ports are specified we try open a new
	 * Socket with the given <code>timeout</code>.
	 * 
	 * @param host
	 * @param port
	 * @param timeout
	 * 
	 * @return <code>true</code> when <code>host</code> is reachable on <code>port</code>
	 * within the given <code>timeout</code> and <code>false</code> in all other
	 * cases.
	 * 
	 * @throws IOException
	 * @throws SocketTimeoutException
	 */
	public static boolean checkVitality(String host, int port, int timeout) throws IOException, SocketTimeoutException {
		boolean success = false;
		
		if (host != null && timeout > 0) {
			if (port == 0) {
				success = InetAddress.getByName(host).isReachable(timeout);
			}
			else {
				SocketAddress socketAddress = new InetSocketAddress(host, port);
				
				Socket socket = new Socket();
				socket.connect(socketAddress, timeout);
				success = true;
			}
		}

		return success;
	}
	
	
}
