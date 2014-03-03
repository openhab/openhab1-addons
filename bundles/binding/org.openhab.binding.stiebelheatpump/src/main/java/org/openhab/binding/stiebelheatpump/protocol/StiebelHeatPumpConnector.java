/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.IOException;

import org.openhab.binding.stiebelheatpump.internal.StiebelHeatPumpException;

/**
 * Base class for Stiebel  heat pump communication.
 * 
 * @author Peter Kreutzer
 * @since 1.4.0
 */
public abstract class StiebelHeatPumpConnector {

	/**
	 * Procedure for connect to heat pump.
	 * 
	 * @throws StiebelHeatPumpException 
	 */
	public abstract void connect() throws StiebelHeatPumpException;

	/**
	 * Procedure for disconnect from heat pump.
	 * 
	 * @throws StiebelHeatPumpException
	 */
	public abstract void disconnect() throws StiebelHeatPumpException;

	/**
	 * Procedure for read firmware version from heat pump.
	 * 
	 * @throws StiebelHeatPumpException
	 */
	public abstract String getHeatPumpVersion() throws StiebelHeatPumpException;
	
	/**
	 * Procedure for receiving datagram from heat pump.
	 * 
	 * @throws StiebelHeatPumpException
	 */
	public abstract byte[] receiveDatagram() throws StiebelHeatPumpException;

}

