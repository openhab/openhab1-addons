/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ebus.internal.connection;

import java.io.IOException;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is the TCP/IP implementation of the eBus connector. It only handles
 * TCP/IP specific connection/disconnection. All logic is handled by
 * abstract class AbstractEBusConnector.
 * 
 * @author Christian Sowada
 * @since 1.7.0
 */
public class EBusTCPConnector extends AbstractEBusConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(EBusTCPConnector.class);
	
	/** The tcp socket */
	private Socket socket;

	/** The tcp hostname */
	private String hostname;

	/** The tcp port */
	private int port;

	/**
	 * Constructor
	 * @param hostname
	 * @param port
	 */
	public EBusTCPConnector(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
	}
	
	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#connect()
	 */
	@Override
	public boolean connect() throws IOException  {
		try {
			socket = new Socket(hostname, port);
			socket.setSoTimeout(20000);
			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();

			return true;
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.openhab.binding.ebus.connection.AbstractEBusConnector#disconnect()
	 */
	@Override
	public boolean disconnect() throws IOException  {
		if(socket != null) {
			socket.close();
			socket = null;
		}
		return true;
	}

}
