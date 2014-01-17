/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device;

import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;

/**
 * Base class for all homematic devices.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public abstract class AbstractHMDevice implements HMDevice {

    private DeviceDescription deviceDescription;

    public AbstractHMDevice(DeviceDescription deviceDescription) {
        if (deviceDescription == null) {
            throw new IllegalArgumentException("deviceDescription may not be null");
        }
        this.deviceDescription = deviceDescription;
    }

    @Override
    public final String getAddress() {
        return deviceDescription.getAddress();
    }

    @Override
    public final DeviceDescription getDeviceDescription() {
        return deviceDescription;
    }

}
