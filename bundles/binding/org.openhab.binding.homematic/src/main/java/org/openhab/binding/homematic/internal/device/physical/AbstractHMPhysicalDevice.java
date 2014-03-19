/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.physical;

import org.openhab.binding.homematic.internal.ccu.CCU;
import org.openhab.binding.homematic.internal.device.AbstractHMDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;
import org.openhab.binding.homematic.internal.xmlrpc.impl.ParamsetDescription;

/**
 * Base class for all physical devices.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 *
 * @param <C>
 */
public abstract class AbstractHMPhysicalDevice<C extends CCU<? extends HMPhysicalDevice>> extends AbstractHMDevice implements
HMPhysicalDevice {

    private C ccu;
    private ParamsetDescription master;

    public AbstractHMPhysicalDevice(DeviceDescription deviceDescription, C ccu) {
        super(deviceDescription);
        this.ccu = ccu;
        master = ccu.getConnection().getParamsetDescription(getAddress(), "MASTER");
    }

    @Override
    public final C getCCU() {
        return ccu;
    }

    @Override
    public final String getFirmware() {
        return getDeviceDescription().getFirmware();
    }

    @Override
    public final Integer getVersion() {
        return getDeviceDescription().getVersion();
    }

    @Override
    public final ParamsetDescription getMaster() {
        return master;
    }

    @Override
    public final void setMaster(ParamsetDescription paramset) {
    }

}
