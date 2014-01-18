/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device;

import org.openhab.binding.homematic.internal.ccu.CCURF;
import org.openhab.binding.homematic.internal.device.physical.rf.DefaultHMRFDevice;
import org.openhab.binding.homematic.internal.device.physical.rf.HMRFDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides means of creating devices from DeviceDescription objects.
 * 
 * @author Mathias Ewald
 * @since 1.2.0
 */
public class HMDeviceFactory {

    private static final Logger logger = LoggerFactory.getLogger(HMDeviceFactory.class);

    public static HMRFDevice createRFDevice(CCURF ccu, String address) {
        if (ccu == null) {
            throw new IllegalArgumentException("ccu must no be null");
        }
        if (address == null) {
            throw new IllegalArgumentException("address must no be null");
        }
        try {
            return new DefaultHMRFDevice(ccu, address);
        } catch (RuntimeException e) {
            logger.error("Could not create Device with address " + address, e);
            return null;
        }
    }

}
