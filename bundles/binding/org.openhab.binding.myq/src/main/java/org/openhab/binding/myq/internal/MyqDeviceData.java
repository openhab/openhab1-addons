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
        if (rootNode.has("Devices")) {
            JsonNode node = rootNode.get("Devices");
            if (node.isArray()) {
                logger.debug("Chamberlain MyQ Devices:");
                int arraysize = node.size();
                for (int i = 0; i < arraysize; i++) {
                    int deviceId = node.get(i).get("MyQDeviceId").asInt();
                    String deviceType = node.get(i).get("MyQDeviceTypeName").asText();
                    int deviceTypeId = node.get(i).get("MyQDeviceTypeId").asInt();

                    // GarageDoorOpener have MyQDeviceTypeId of 2,5,7,17
                    if (deviceTypeId == 2 || deviceTypeId == 5 || deviceTypeId == 7 || deviceTypeId == 17) {
                        devices.add(new GarageDoorDevice(deviceId, deviceType, deviceTypeId, node.get(i)));
                    } else if (deviceTypeId == 3) { // Light have MyQDeviceTypeId of 3
                        devices.add(new LampDevice(deviceId, deviceType, deviceTypeId, node.get(i)));
                    }
                }
            }
        }
    }

    public MyqDevice getDevice(int index) {
        return index >= devices.size() ? null : devices.get(index);
    }
}
