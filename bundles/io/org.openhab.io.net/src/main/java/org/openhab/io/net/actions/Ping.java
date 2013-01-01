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
