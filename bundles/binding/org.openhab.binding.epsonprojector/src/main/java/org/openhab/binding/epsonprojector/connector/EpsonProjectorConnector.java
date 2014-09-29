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
 * Base class for Epson projector communication.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public interface EpsonProjectorConnector {

	/**
	 * Procedure for connecting to projector.
	 * 
	 * @throws EpsonProjectorException
	 */
	void connect() throws EpsonProjectorException;

	/**
	 * Procedure for disconnecting to projector controller.
	 * 
	 * @throws EpsonProjectorException
	 */
	void disconnect() throws EpsonProjectorException;

	/**
	 * Procedure for send raw data to projector.
	 * 
	 * @param data
	 *            Message to send.
	 * 
	 * @param timeout
	 *            timeout to wait response in milliseconds.
	 * 
	 * @throws EpsonProjectorException
	 */
	String sendMessage(String data, int timeout) throws EpsonProjectorException;

}
