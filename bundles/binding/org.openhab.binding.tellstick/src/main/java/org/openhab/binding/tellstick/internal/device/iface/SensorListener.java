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

import org.openhab.binding.tellstick.internal.device.TellstickSensorEvent;

/**
 * A listener for sensor events.
 * 
 * @author jarlebh
 * @since 1.5.0
 */
public interface SensorListener extends EventListener {
	void onRequest(TellstickSensorEvent newDevices);
}
