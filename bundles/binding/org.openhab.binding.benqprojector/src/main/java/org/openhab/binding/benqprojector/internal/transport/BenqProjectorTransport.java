/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.benqprojector.internal.transport;

/**
 * Interface for BenQ projector transport classes.
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public interface BenqProjectorTransport {
	
	/**
	 * Setup the transport connection
	 * @param connectionParams String containing all the information to setup the connection
	 * @return true if connection setup successfully
	 */
	public boolean setupConnection(String connectionParams);
	
	/**
	 * Close down the connection
	 */
	public void closeConnection();
	
	/**
	 * Send command to projector via transport and get a response 
	 * @param cmd command to send
	 * @return response to command
	 */
	public String sendCommandExpectResponse(String cmd);
	
}
