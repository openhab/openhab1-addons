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
package org.openhab.binding.dmx.lib485;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.dmx.DmxConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Connection Implementation using lib485 as the DMX target.
 */
public class Lib485Connection implements DmxConnection {

	private static final Logger logger = 
		LoggerFactory.getLogger(Lib485Connection.class);

	private Socket connection;
	
	private final static String DEFAULT_HOST = "localhost";
	
	private final static int DEFAULT_PORT = 9020;
	

	@Override
	public void open(String connectionString) throws Exception {
		String host = DEFAULT_HOST;
		int port = DEFAULT_PORT;
		
		if (StringUtils.isNotBlank(connectionString)) {
			String[] connectionStringElements = connectionString.split(":");
			if (connectionStringElements.length == 1) {
				host = connectionStringElements[0];
			} else if (connectionStringElements.length == 2) {
				host = connectionStringElements[0];
				port = Integer.valueOf(connectionStringElements[1]).intValue();
			}
		}
			
		connection = new Socket(host, port);
		if (connection.isConnected()) {
			logger.debug("Connected to Lib485 DMX service");
		}
	}

	@Override
	public void close() {
		if (connection != null && !connection.isClosed()) {
			try {
				connection.close();
			} catch (IOException e) {
				logger.warn("Could not close socket.", e);
			}
		}
		connection = null;
	}
	
	@Override
	public boolean isClosed() {
		return connection.isClosed();
	}
	
	@Override
	public void sendDmx(byte[] buffer) throws Exception {
		logger.debug("Sending Data to DMX");
		connection.getOutputStream().write(buffer);
	}
	

}
