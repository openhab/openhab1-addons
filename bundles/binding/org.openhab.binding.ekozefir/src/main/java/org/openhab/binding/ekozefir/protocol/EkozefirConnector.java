/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.protocol;

/**
 * Interface of serial connection to ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public interface EkozefirConnector {
	/**
	 * Connecting to Ekozefir driver.
	 */
	void connect();

	/**
	 * Disconnecting from Ekozefir driver.
	 */
	void disconnect();

	/**
	 * Receive all bytes from buffer.
	 * 
	 * @return bytes of message
	 */
	byte[] receiveBytes(int number);

	/**
	 * Send byte message to driver.
	 * 
	 * @param bytes
	 *            bytes to send
	 */
	void sendBytes(byte[] bytes);
}
