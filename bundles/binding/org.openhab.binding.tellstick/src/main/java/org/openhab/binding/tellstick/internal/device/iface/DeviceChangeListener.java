/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.tellstick.internal.device.iface;

import java.util.EventListener;

import org.openhab.binding.tellstick.internal.device.TellstickDeviceEvent;

/**
 * A device received on a device change in telldus center.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public interface DeviceChangeListener extends EventListener {

	/**
	 * This event listener must be implemented. This is the method that will get
	 * called if we got requests.
	 */
	void onRequest(TellstickDeviceEvent newDevices);

}
