/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
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
 * <li>deviceTypeID: MYQ Device TypeID.</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.9.0
 */
public class MyqDevice {
    protected int deviceId;
    protected String deviceType;
    protected int deviceTypeID;
    protected HashMap<String, String> deviceAttributes = new HashMap<String, String>();

    static final Logger logger = LoggerFactory.getLogger(MyqDevice.class);

    public MyqDevice(int deviceId, String deviceType, int deviceTypeID, JsonNode deviceJson) {
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.deviceTypeID = deviceTypeID;

        deviceAttributes.put("MyQDeviceId", Integer.toString(deviceId));
        deviceAttributes.put("MyQDeviceTypeName", deviceType);
        deviceAttributes.put("MyQDeviceTypeId", Integer.toString(deviceTypeID));
        deviceAttributes.put("SerialNumber", deviceJson.get("SerialNumber").asText());

        JsonNode otherAttributes = deviceJson.get("Attributes");
        if (otherAttributes.isArray()) {
            int attributesSize = otherAttributes.size();
            for (int j = 0; j < attributesSize; j++) {
                String attributeName = otherAttributes.get(j).get("AttributeDisplayName").asText();
                String attributeValue = otherAttributes.get(j).get("Value").asText();
                logger.trace("AttributeName: {} AttributeValue: {}", attributeName, attributeValue);
                deviceAttributes.put(attributeName, attributeValue);
            }
        }
    }

    public int getDeviceId() {
        return this.deviceId;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public int getDeviceTypeID() {
        return this.deviceTypeID;
    }

    public boolean hasAttribute(String AttributeName) {
        return this.deviceAttributes.containsKey(AttributeName);
    }

    public String getAttribute(String AttributeName) {
        return this.deviceAttributes.get(AttributeName);
    }

    public String toString() {
        return this.deviceType;
    }
}
