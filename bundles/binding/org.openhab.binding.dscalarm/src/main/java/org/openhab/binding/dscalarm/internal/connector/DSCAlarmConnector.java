/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.openhab.binding.dscalarm.internal.connector;

import org.openhab.binding.dscalarm.internal.DSCAlarmEventListener;

/**
 * Interface for DSC Alarm communication.
 * @author Russell Stephens
 * @since 1.6.0
 */
public interface DSCAlarmConnector {

	/**
	 * Returns Connector Type
	 **/
	DSCAlarmConnectorType getConnectorType();

	/**
	 * Method for opening a connection to DSC Alarm.
	 */
	void open();

	/**
	 * Method for closing a connection to DSC Alarm.
	 */
	void close();

	/**
	 * Method for writing to an open DSC Alarm connection
	 **/
	void write(String writeString);

	/**
	 * Method for reading from an open DSC Alarm connection
	 **/
	String read();

	/**
	 * Returns connection status
	 **/
	boolean isConnected();

	/**
	 * Method for registering an event listener.
	 * @param listener
	 */
	public void addEventListener(DSCAlarmEventListener listener);

	/**
	 * Method for removing an event listener.
	 * @param listener
	 */
	public void removeEventListener(DSCAlarmEventListener listener);
}
