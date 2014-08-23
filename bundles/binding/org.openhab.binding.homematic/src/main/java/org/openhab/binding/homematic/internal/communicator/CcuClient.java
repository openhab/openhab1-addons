/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator;

import org.openhab.binding.homematic.internal.communicator.client.CcuClientException;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmInterface;

/**
 * The client interface with the common methods for XML-RPC and BIN-RPC
 * communication.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public interface CcuClient {

	/**
	 * Register a callback for the specified interface where the CCU can send
	 * their events.
	 */
	public void init(HmInterface hmInterface) throws CcuClientException;

	/**
	 * Release a callback.
	 */
	public void release(HmInterface hmInterface) throws CcuClientException;

	/**
	 * Set the value of a datapoint.
	 */
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws CcuClientException;

}
