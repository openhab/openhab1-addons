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

import java.io.IOException;
import java.util.LinkedList;
import java.util.regex.Pattern;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class parses the JSON data and creates a LinkedList of MyQ
 * Devices
 * <ul>
 * <li>devices: LinkedList of Devices</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.9.0
 */
public class MyqDeviceData {
    static final Logger logger = LoggerFactory.getLogger(MyqDeviceData.class);

    LinkedList<MyqDevice> devices = new LinkedList<MyqDevice>();

    /**
     * Constructor of the MyqDeviceData.
     * 
     * @param rootNode
     *            The Json node returned from the myq website.
     */
    public MyqDeviceData(JsonNode rootNode) throws IOException {
        if (rootNode.has("items")) {
            JsonNode node = rootNode.get("items");
            if (node.isArray()) {
                logger.debug("Chamberlain MyQ Devices:");
                int arraysize = node.size();
                for (int i = 0; i < arraysize; i++) {
                    String deviceId = node.get(i).get("serial_number").asText();
                    String deviceName = node.get(i).get("name").asText();
                    String deviceType = node.get(i).get("device_type").asText();
                    if (deviceType.compareTo("garagedooropener") == 0) {
                        devices.add(new GarageDoorDevice(deviceId, deviceType, deviceName, node.get(i)));
                    } else if (deviceType.compareTo("lamp") == 0) {
                        devices.add(new LampDevice(deviceId, deviceType, deviceName, node.get(i)));
                    }
                }
            }
        }
    }

    public MyqDevice getDevice(String deviceID) {
        for (MyqDevice device : devices) {
             if(device.getDeviceId().compareTo(deviceID) == 0 ||
             device.getDeviceName().compareTo(deviceID) == 0)
                 return device;
        }
        return null;
    }
}
