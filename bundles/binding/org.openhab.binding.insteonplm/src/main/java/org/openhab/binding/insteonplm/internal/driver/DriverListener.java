/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonplm.internal.driver;

import java.util.HashMap;

import org.openhab.binding.insteonplm.internal.device.InsteonAddress;
import org.openhab.binding.insteonplm.internal.device.InsteonDevice;

/**
 * Interface for classes that want to listen to notifications from
 * the driver.
 * 
 * @author Bernd Pfrommer
 * @since 1.5.0
 */

public interface DriverListener {
	abstract HashMap<InsteonAddress, InsteonDevice> getDeviceList();
	/**
	 * Notification that querying of the modems on all ports has successfully completed.
	 */
	public abstract void driverCompletelyInitialized();
}
