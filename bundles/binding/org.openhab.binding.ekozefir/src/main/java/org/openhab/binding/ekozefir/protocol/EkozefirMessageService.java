/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.protocol;

import org.openhab.binding.ekozefir.ahucommand.AhuCommand;
import org.openhab.binding.ekozefir.response.Response;

/**
 * Interface of message connection to ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 * 
 */
public interface EkozefirMessageService {
	/**
	 * Response from ahu.
	 * 
	 * @return response
	 */
	Response getResponse();

	/**
	 * Get ahu name.
	 * 
	 * @return ahu name
	 */
	Character getAhuName();

	/**
	 * Send command to ahu.
	 * 
	 * @param command message
	 */
	void sendMessage(AhuCommand command);

	/**
	 * Connect to ahu driver.
	 */
	void connect();

	/**
	 * Disconnect from ahu driver.
	 */
	void disconnect();
}
