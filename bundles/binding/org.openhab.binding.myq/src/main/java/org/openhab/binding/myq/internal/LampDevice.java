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

import org.codehaus.jackson.JsonNode;
import org.openhab.core.library.types.OnOffType;

/**
 * This Class holds the Lamp Device data and extends MyqDevice.
 * <ul>
 * <li>state: Lamp Module "lightstate" Attribute</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @since 1.9.0
 */
public class LampDevice extends MyqDevice {
    /**
     * The OnOffType state of the Lamp Module
     */
    private OnOffType state;

    public LampDevice(int deviceId, String deviceType, int deviceTypeID, JsonNode deviceJson) {
        super(deviceId, deviceType, deviceTypeID, deviceJson);
        JsonNode attributes = deviceJson.get("Attributes");
        if (attributes.isArray()) {
            int attributesSize = attributes.size();
            for (int j = 0; j < attributesSize; j++) {
                String attributeName = attributes.get(j).get("AttributeDisplayName").asText();
                if (attributeName.contains("lightstate")) {
                    int lightstate = attributes.get(j).get("Value").asInt();
                    deviceAttributes.put("UpdatedDate", attributes.get(j).get("UpdatedDate").asText());
                    this.state = getLampState(lightstate);
                    logger.debug("Lamp DeviceID: {} DeviceType: {} Lightstate : {}", deviceId, deviceType, lightstate);
                    break;
                }
            }
        }
    }

    /**
     * Internal Method to convert API value to ON/OFF state
     */
    private OnOffType getLampState(int value) {
        if (value == 1) {
            return OnOffType.ON;
        }
        return OnOffType.OFF;
    }

    /**
     * OnOffType value of the light state
     * 
     * @return OnOffType value of the light state
     */
    public OnOffType getState() {
        return this.state;
    }
}
