/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
