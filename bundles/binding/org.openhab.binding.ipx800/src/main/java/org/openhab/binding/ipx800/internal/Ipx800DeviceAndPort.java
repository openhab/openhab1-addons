/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ipx800.internal;

import org.openhab.binding.ipx800.internal.command.Ipx800Port;

/**
 * Device and port bean
 *
 * @author Seebag
 * @since 1.8.0
 */
public class Ipx800DeviceAndPort {
    private final Ipx800DeviceConnector device;
    private final Ipx800Port port;

    public Ipx800DeviceAndPort(Ipx800DeviceConnector device, Ipx800Port port) {
        this.device = device;
        this.port = port;
    }

    public Ipx800DeviceConnector getDevice() {
        return device;
    }

    public Ipx800Port getPort() {
        return port;
    }
}
