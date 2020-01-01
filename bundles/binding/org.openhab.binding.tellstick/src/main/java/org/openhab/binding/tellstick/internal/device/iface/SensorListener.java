/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
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
