/**
 * Copyright (c) 2010-2017, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

import java.util.LinkedList;

import org.openhab.binding.simplebinary.internal.SimpleBinaryGenericBindingProvider.DeviceConfig;

/**
 * Device configs holder
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryDeviceConfigCollection extends LinkedList<DeviceConfig> {
    private static final long serialVersionUID = -6827894112097302707L;

    @Override
    public boolean add(DeviceConfig e) {
        if (!this.contains(e)) {
            return super.add(e);
        }
        return false;
    }

    @Override
    public boolean contains(Object o) {
        if (!(o instanceof DeviceConfig)) {
            return false;
        }

        for (DeviceConfig d : this) {
            if (d.deviceName.equals(((DeviceConfig) o).deviceName)
                    && d.deviceAddress == ((DeviceConfig) o).deviceAddress
                    && d.itemAddress == ((DeviceConfig) o).itemAddress) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(String deviceName) {
        for (DeviceConfig d : this) {
            if (d.deviceName.equals(deviceName)) {
                return true;
            }
        }

        return false;
    }

    public DeviceConfig get(String deviceName) {
        for (DeviceConfig d : this) {
            if (d.deviceName.equals(deviceName)) {
                return d;
            }
        }
        return null;
    }
}
