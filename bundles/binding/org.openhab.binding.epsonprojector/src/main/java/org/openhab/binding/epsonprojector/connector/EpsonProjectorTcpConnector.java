/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.connector;

import org.openhab.binding.epsonprojector.internal.EpsonProjectorException;

/**
 * Connector for TCP communication.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public class EpsonProjectorTcpConnector implements EpsonProjectorConnector {

	String ip = null;
	int port = 10000;

	public EpsonProjectorTcpConnector(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * {@inheritDoc}
	 */
	public void connect() throws EpsonProjectorException {
		throw new EpsonProjectorException("The TCP connector is not yet implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	public void disconnect() throws EpsonProjectorException {
		throw new EpsonProjectorException("The TCP connector is not yet implemented");
	}

	/**
	 * {@inheritDoc}
	 */
	public String sendMessage(String data, int timeout) throws EpsonProjectorException {
		throw new EpsonProjectorException("The TCP connector is not yet implemented");
	}

}
