/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.device.channel;

import org.openhab.binding.homematic.internal.device.physical.HMPhysicalDevice;
import org.openhab.binding.homematic.internal.xmlrpc.impl.DeviceDescription;

/**
 * Maintenance Channel (normally "#0") implementation.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class HMMaintenanceChannel extends AbstractHMChannel {

    public HMMaintenanceChannel(HMPhysicalDevice parent, DeviceDescription deviceDescription) {
        super(parent, deviceDescription);
    }

    public Boolean getLowBat() {
        return (Boolean) getValues().getValue("LOWBAT");
    }

    public Boolean getUnreach() {
        return (Boolean) getValues().getValue("UNREACH");
    }

    public Boolean getConfigPending() {
        return (Boolean) getValues().getValue("CONFIG_PENDING");
    }

    public Boolean getStickyUnreach() {
        return (Boolean) getValues().getValue("STICKY_UNREACH");
    }

}
