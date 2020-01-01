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
 * Devices that can perform the action bell. Does not implement super interface
 * Device since this should only perform the bell action.
 *
 * @author peec
 * @author jarlebh
 * @since 1.5.0
 *
 */
public interface BellDeviceIntf {

    /**
     * Bells the bell, meaning a sound will be played from the belling device.
     * 
     * @throws TellstickException
     */
    public void bell() throws TellstickException;

    /**
     * Returns the name of the device.
     */
    public String getType();

}
