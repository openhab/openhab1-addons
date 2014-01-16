/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx;

/**
 * DmxConnection. Used for sending DMX values to an (virtual) device. Implement
 * this interface and provide it as an OSGI service if you want to use your own
 * HW interface.
 * 
 * @author Davy Vanherbergen
 * @since 1.2.0
 */
public interface DmxConnection {

	/**
	 * Send the given buffer to the DMX device.
	 * 
	 * @param buffer
	 *            buffer containing max 512 DMX values
	 * @throws Exception
	 */
	public void sendDmx(byte[] buffer) throws Exception;

	/**
	 * @return true if the connection is closed.
	 */
	public boolean isClosed();

	/**
	 * Open the connection. The connection string is taken from the
	 * dmx:connection property in the openhab configuration file.
	 * 
	 * @param connectionString
	 * @throws Exception
	 */
	public void open(String connectionString) throws Exception;

	/**
	 * Close the connection.
	 */
	public void close();

}
