/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.mystromecopower.internal.api;

import java.util.List;

import org.openhab.binding.mystromecopower.internal.api.model.MystromDevice;

/**
 * @author Jordens Christophe
 * @since 1.8.0-SNAPSHOT Interface for mystrom client API.
 */
public interface IMystromClient {
	/**
	 * Do login.
	 * 
	 * @return True if login succeeded else false.
	 */
	public Boolean login();

	/**
	 * Search all devices on the connected account, needs to call login method
	 * before.
	 * 
	 * @return List of devices.
	 */
	public List<MystromDevice> getDevices();

	/**
	 * Returns information about a device by the id.
	 * 
	 * @param deviceId
	 *            The id of the device on the mystrom SRS server.
	 * @return The device information.
	 */
	public MystromDevice getDeviceInfo(String deviceId);

	/**
	 * Change the state of a device on or off.
	 * 
	 * @param deviceId
	 *            The id of the device for which to change state.
	 * @param newStateIsOn
	 *            Indicates if new state must be on or off.
	 * @return True if change is successful else False.
	 */
	public Boolean ChangeState(String deviceId, Boolean newStateIsOn);

	/**
	 * Restart the master device.
	 * 
	 * @param deviceId
	 *            The id of the master device.
	 */
	public void RestartMaster(String deviceId);
}
