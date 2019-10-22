/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.myq.internal;

import java.util.HashMap;

import org.codehaus.jackson.JsonNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class holds the MyQ Device data.
 * <ul>
 * <li>DeviceId: DeviceId from API, need for http Posts</li>
 * <li>DeviceType: MYQ Device Type. GarageDoorOpener, LampModule or Gateway</li>
 * <li>deviceName: MYQ Device Name.</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.9.0
 */
public class MyqDevice {
    protected String deviceId;
    protected String deviceType;
	protected String deviceName;

    static final Logger logger = LoggerFactory.getLogger(MyqDevice.class);

    public MyqDevice(String deviceId, String deviceType, String deviceName, JsonNode deviceJson) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceName = deviceName;

    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public String getDeviceName() {
        return this.deviceName;
    }

    public String toString() {
        return this.deviceId;
    }
}
