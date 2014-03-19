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
 * Default Channel implementation.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.2.0
 */
public class DefaultChannel extends AbstractHMChannel {

    public DefaultChannel(HMPhysicalDevice parent, DeviceDescription deviceDescription) {
        super(parent, deviceDescription);
    }

}
