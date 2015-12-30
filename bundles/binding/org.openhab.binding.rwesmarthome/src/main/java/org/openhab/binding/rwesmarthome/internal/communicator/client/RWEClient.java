/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rwesmarthome.internal.communicator.client;

import java.io.IOException;

import org.openhab.binding.rwesmarthome.internal.communicator.exceptions.RWESmarthomeSessionExpiredException;

/**
 * Interface for the communication with RWE Smarthome.
 * 
 * @author ollie-dev
 *
 */
public interface RWEClient {

	/**
	 * Executes a request
	 * 
	 * @param hostname
	 * @param clientId
	 * @param request
	 * @param command
	 * @return
	 * @throws IOException 
	 * @throws RWESmarthomeSessionExpiredException 
	 */
	public String execute(String hostname, String clientId, String request,
			String command) throws IOException, RWESmarthomeSessionExpiredException;
}
