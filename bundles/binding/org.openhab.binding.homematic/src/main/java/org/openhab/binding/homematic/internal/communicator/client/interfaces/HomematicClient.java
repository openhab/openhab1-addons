/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.client.interfaces;

import org.openhab.binding.homematic.internal.communicator.client.BaseHomematicClient.HmValueItemIteratorCallback;
import org.openhab.binding.homematic.internal.communicator.client.HomematicClientException;
import org.openhab.binding.homematic.internal.model.HmDatapoint;
import org.openhab.binding.homematic.internal.model.HmInterface;
import org.openhab.binding.homematic.internal.model.HmValueItem;

/**
 * The interface for all Homematic server clients.
 * 
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public interface HomematicClient {

	/**
	 * Starts the client.
	 */
	public void start() throws HomematicClientException;

	/**
	 * Stops the client.
	 */
	public void shutdown() throws HomematicClientException;

	/**
	 * Returns true, if the client has been started.
	 */
	public boolean isStarted();

	/**
	 * Set the value of a datapoint.
	 */
	public void setDatapointValue(HmDatapoint dp, String datapointName, Object value) throws HomematicClientException;

	/**
	 * Register a callback where the Homematic server can send its events.
	 */
	public void registerCallback() throws HomematicClientException;

	/**
	 * Release a callback.
	 */
	public void releaseCallback() throws HomematicClientException;

	/**
	 * Retrieves all variables from the Homematic server.
	 */
	public void iterateAllVariables(HmValueItemIteratorCallback callback) throws HomematicClientException;

	/**
	 * Retrieves all datapoints from the Homematic server.
	 */
	public void iterateAllDatapoints(HmValueItemIteratorCallback callback) throws HomematicClientException;

	/**
	 * Execute a program/script on the Homematic server.
	 */
	public void executeProgram(String programName) throws HomematicClientException;

	/**
	 * Set a variable on the Homematic server.
	 */
	public void setVariable(HmValueItem hmValueItem, Object value) throws HomematicClientException;

	/**
	 * Sends a message and sets properties for the display of a 19 key Homematic
	 * remote control. Used in the Homematic action.
	 */
	public void setRemoteControlDisplay(String remoteControlAddress, String text, String options)
			throws HomematicClientException;

	/**
	 * Returns true, if the client supports variables.
	 */
	public boolean supportsVariables();

	/**
	 * Returns the default interface to communicate with the Homematic server.
	 */
	public HmInterface getDefaultInterface();

}
