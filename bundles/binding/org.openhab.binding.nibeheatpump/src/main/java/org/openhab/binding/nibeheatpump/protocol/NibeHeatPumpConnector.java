/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nibeheatpump.protocol;

import org.openhab.binding.nibeheatpump.internal.NibeHeatPumpException;

/**
 * Base class for Nibe heat pump communication.
 * 
 * @author Pauli Anttila
 * @since 1.3.0
 */
public abstract class NibeHeatPumpConnector {

	/**
	 * Procedure for connect to heat pump.
	 * 
	 * @throws NibeHeatPumpException
	 */
	public abstract void connect() throws NibeHeatPumpException;

	/**
	 * Procedure for disconnect from heat pump.
	 * 
	 * @throws NibeHeatPumpException
	 */
	public abstract void disconnect() throws NibeHeatPumpException;

	/**
	 * Procedure for receiving datagram from heat pump.
	 * 
	 * @throws NibeHeatPumpException
	 */
	public abstract byte[] receiveDatagram() throws NibeHeatPumpException;

}
