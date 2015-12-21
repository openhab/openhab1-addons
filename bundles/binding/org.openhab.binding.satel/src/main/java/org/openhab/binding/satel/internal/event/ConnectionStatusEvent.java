/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.event;

/**
 * Event class describing connection status to Satel module.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class ConnectionStatusEvent implements SatelEvent {

	private boolean connected;

	/**
	 * Constructs event class with given connection status.
	 * 
	 * @param connected value describing connection status
	 */
	public ConnectionStatusEvent(boolean connected) {
		this.connected = connected;
	}

	/**
	 * Returns status of connection.
	 * 
	 * @return a boolean value describing connection status
	 */
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s: connected = %b", this.getClass().getName(), this.connected);
	}
}
