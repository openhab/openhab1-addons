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
