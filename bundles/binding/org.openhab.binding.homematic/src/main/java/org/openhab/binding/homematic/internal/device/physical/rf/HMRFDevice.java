/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.physical.rf;

import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.device.physical.AbstractHMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;

/**
 * A HMRFDevices is a homematic wireless devices.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public abstract class HMRFDevice extends AbstractHMPhysicalDevice<CCURF> {

    public HMRFDevice(DeviceDescription deviceDescription, CCURF ccu) {
        super(deviceDescription, ccu);
    }

}
