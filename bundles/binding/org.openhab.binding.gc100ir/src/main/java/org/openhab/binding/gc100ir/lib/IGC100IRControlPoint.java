/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.gc100ir.lib;

import java.util.Set;

import org.openhab.binding.gc100ir.internal.response.GC100IRCommand;

/**
 * Manages connections, disconnections and action of the devices attached to the GC-100 devices.
 *
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public interface IGC100IRControlPoint {

    public static final String FLOW_CONTROL_VALUE_FLOW_HARDWARE = "FLOW_HARDWARE";

    /**
     * Call the action on specific GC-100 device using 'ipAddress' and get requested values.
     *
     * @param ipaddress a String value of ipaddress
     * @param module an integer value of module
     * @param connector an integer value of connector
     * @param code a String value of code
     * @return a String value of response
     */
    public GC100IRCommand doAction(String ipaddress, int module, int connector, String code);

    /**
     * Method to connect to the specified target device connected to the specified 'module' and 'connector'.
     *
     * @param ipaddress a String value of ipaddress
     * @param module an integer value of module
     * @param connector an integer value of connector
     * @return a boolean whether device is connected to the specified module or not.
     *
     */
    public boolean connectTarget(String ipaddress, int module, int connector);

    /**
     * Called when any target is disconnected.
     *
     * @param ipaddress a String value of ipAddress
     * @param module an integer value of module
     * @param connector an integer value of connector
     */
    public void disconnectTarget(String ipaddress, int module, int connector);

    /**
     * Get available GC100 Devices.
     *
     * @return an Object of Set&lt;GCDevices&gt; specifies available devices
     */
    public Set<GC100IRDevice> getAvailableDevices();

}
