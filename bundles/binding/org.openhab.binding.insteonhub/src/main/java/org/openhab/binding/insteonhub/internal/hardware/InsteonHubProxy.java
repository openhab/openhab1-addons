/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware;

/**
 * Interface for an INSTEON Hub controller
 * 
 * @author Eric Thill
 * @since 1.4.0
 */
public interface InsteonHubProxy {

	/**
	 * Connection string used for logging
	 * 
	 * @return
	 */
	String getConnectionString();

	/**
	 * Connect and start any internal threads
	 */
	void start();

	/**
	 * Disconnect and stop any internal threads
	 */
	void stop();

	/**
	 * Set device fast on/off
	 * 
	 * @param device
	 *            the device (format: AABBCC)
	 * @param power
	 *            true => on, false => off
	 */
	void setDevicePower(String device, boolean power);

	/**
	 * Set device dimmer level
	 * 
	 * @param device
	 *            the device (format: AABBCC)
	 * @param level
	 *            0-255
	 */
	void setDeviceLevel(String device, int level);

	/**
	 * Start Brightening/Dimming
	 * 
	 * @param device
	 *            the device (format: AABBCC)
	 * @param adjustmentType
	 *            DIM or BRIGHTEN
	 */
	void startDeviceAdjustment(String device,
			InsteonHubAdjustmentType adjustmentType);

	/**
	 * Stop Brightening/Dimming
	 * 
	 * @param device
	 *            the device (format: AABBCC)
	 */
	void stopDeviceAdjustment(String device);

	/**
	 * Request that a device's current level be sent over all registered
	 * {@link InsteonHubProxyListener}'s
	 * 
	 * @param device
	 */
	void requestDeviceLevel(String device);

	/**
	 * Register a listener for callback events
	 * 
	 * @param listener
	 */
	void addListener(InsteonHubProxyListener listener);

	/**
	 * Unregister a listener for callback events
	 * 
	 * @param listener
	 */
	void removeListener(InsteonHubProxyListener listener);
}
