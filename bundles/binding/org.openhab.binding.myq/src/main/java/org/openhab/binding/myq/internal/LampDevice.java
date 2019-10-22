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

    public LampDevice(String deviceId, String deviceType, String deviceName, JsonNode deviceJson) {
        super(deviceId, deviceType, deviceName, deviceJson);

        JsonNode state = deviceJson.get("state");
        if (state != null && state.has("lamp_state")) {
            this.state = getLampState(state.get("lamp_state").asText());
            logger.debug("Lamp DeviceID: {} DeviceType: {} DeviceName: {} Lightstate : {}", 
            deviceId, deviceType, deviceName, state.get("lamp_state").asText());
        }

    }

    /**
     * Internal Method to convert API value to ON/OFF state
     */
    private OnOffType getLampState(String value) {
        if (value.compareTo("on") == 0) {
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
