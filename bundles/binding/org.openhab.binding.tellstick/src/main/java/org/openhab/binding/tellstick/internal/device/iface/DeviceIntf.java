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

import org.openhab.binding.tellstick.internal.device.TellstickException;

/**
 * A generic device.
 *
 * @author peec
 * @author jarlebh
 * @since 1.5.0
 *
 */
public interface DeviceIntf {

    /**
     * Turns on the device.
     */
    public void on() throws TellstickException;

    /**
     * Turns off the device.
     */
    public void off() throws TellstickException;

    /**
     * Returns the name of the device.
     */
    public String getType();

}
