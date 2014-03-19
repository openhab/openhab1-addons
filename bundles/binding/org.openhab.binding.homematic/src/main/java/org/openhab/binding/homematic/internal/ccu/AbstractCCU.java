/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.ccu;

import java.util.HashSet;
import java.util.Set;

import org.openhab.binding.homematic.internal.device.HMDevice;
import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract implementation of the CCU interface implements CCUListener
 * related methods as well as suppor methods for subclasses to fire events.
 * 
 * @author Mathias Ewald
 * 
 * @param <T>
 */
public abstract class AbstractCCU<T extends HMPhysicalDevice> implements CCU<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractCCU.class);

    private Set<CCUListener> listeners;

    public AbstractCCU() {
        listeners = new HashSet<CCUListener>();
    }

    @Override
    public void addCCUListener(CCUListener l) {
        if (listeners.add(l)) {
            logger.debug("CCUListener added");
        }
    }

    @Override
    public void removeCCUListener(CCUListener l) {
        if (listeners.remove(l)) {
            logger.debug("CCUListener added");
        }
    }

    protected void fireDeviceAdded(HMDevice device) {
        logger.debug("firing device added event to " + listeners.size() + " CCUListeners");
        for (CCUListener l : listeners) {
            l.deviceAdded(device);
        }
    }

    protected void fireDeviceRemoved(HMDevice device) {
        logger.debug("firing device removed event to " + listeners.size() + " CCUListeners");
        for (CCUListener l : listeners) {
            l.deviceRemoved(device);
        }
    }

}
