/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.sapp.internal.model;

/**
 * Class which represents a Pnmas controller
 * 
 * @author Paolo Denti
 * @since 1.8.0
 */
public class SappPnmas {

	private String ip;
	private int port;

	/**
	 * Constructor
	 */
	public SappPnmas(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

	/**
	 * ip getter
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * port getter
	 */
	public int getPort() {
		return port;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("[ ip: %s - port : %d]", ip, port);
	}
}
