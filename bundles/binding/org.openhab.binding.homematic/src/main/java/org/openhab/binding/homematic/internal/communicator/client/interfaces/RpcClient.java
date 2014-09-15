/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client.interfaces;

import java.util.Map;

import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.communicator.client.ServerId;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmRssiInfo;

/**
 * The interface with the methods for BIN-RPC communication.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface RpcClient {

	/**
	 * Starts the client.
	 */
	public void start() throws HomematicClientException;

	/**
	 * Stops the client.
	 */
	public void shutdown() throws HomematicClientException;

	/**
	 * Set the value of a device specified by address.
	 */
	public void setDatapointValue(HmInterface hmInterface, String address, String datapointName, Object value)
			throws HomematicClientException;

	/**
	 * Register a callback for the specified interface where the Homematic
	 * server can send its events.
	 */
	public void init(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Release a callback for the specified interface.
	 */
	public void release(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Returns all metadata and values from the Homematic server.
	 */
	public Object[] getAllValues(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Returns all metadata and values from the Homematic server.
	 */
	public Map<String, ?> getAllSystemVariables(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Set the value of a system variable.
	 */
	public void setSystemVariable(HmInterface hmInterface, String name, Object value) throws HomematicClientException;

	/**
	 * Executes a program/script on the Homematic server.
	 */
	public void executeProgram(HmInterface hmInterface, String programName) throws HomematicClientException;

	/**
	 * Returns the id of the Homematic server.
	 */
	public ServerId getServerId(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Returns the rssi info of RF devices.
	 */
	public Map<String, HmRssiInfo> getRssiInfo(HmInterface hmInterface) throws HomematicClientException;

	/**
	 * Returns the description of a Homematic device.
	 */
	public Map<String, String> getDeviceDescription(HmInterface hmInterface, String address)
			throws HomematicClientException;
}
