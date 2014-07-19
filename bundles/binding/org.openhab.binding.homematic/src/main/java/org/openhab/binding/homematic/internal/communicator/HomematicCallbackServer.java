/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

/**
 * The server interface with the common methods for XML-RPC and BIN-RPC
 * communication.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface HomematicCallbackServer {

	/**
	 * Starts the Homematic callback server.
	 */
	public void start() throws Exception;

	/**
	 * Stops the Homematic callback server.
	 */
	public void shutdown();

}
