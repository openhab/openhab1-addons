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

/**
 * This Class holds the Garage Door Opener Device data and extends MyqDevice.
 * <ul>
 * <li>Status: Garage Door Opener "doorstate" Attribute</li>
 * </ul>
 * 
 * @author Scott Hanson
 * @author Dan Cunningham
 * @since 1.8.0
 */
class GarageDoorDevice extends MyqDevice {

    private GarageDoorStatus status;

    public GarageDoorDevice(String deviceId, String deviceType, String deviceName, JsonNode deviceJson) {
        super(deviceId, deviceType, deviceName, deviceJson);

        JsonNode state = deviceJson.get("state");
        if (state != null && state.has("door_state")) {
            this.status = GarageDoorStatus.GetDoorStatus(state.get("door_state").asText());
            logger.debug("GarageDoorOpener DeviceID: {} DeviceType: {} DeviceName: {} Doorstate : {}", 
            deviceId, deviceType, deviceName, state.get("door_state").asText());
        }
    }

    public GarageDoorStatus getStatus() {
        return this.status;
    }

    public enum GarageDoorStatus {
        OPEN("Open", "open"),
        CLOSED("Closed", "closed"),
        PARTIAL("Partially Open/Closed", "stopped"),
        OPENING("Opening", "opening"),
        CLOSING("Closing", "closing"),
        MOVING("Moving", "transition"),
        UNKNOWN("Unknown", "unknown");

        /**
         * The label used to display status to a user
         */
        private String label;
        /**
         * The String value returned from the MyQ API
         */
        private String value;

        private GarageDoorStatus(String label, String value) {
            this.label = label;
            this.value = value;
        }

        /**
         * Label for the door status
         * 
         * @return human readable label
         */
        public String getLabel() {
            return label;
        }

        /**
         * Int value of the door status
         * 
         * @return String value of the door status
         */
        public String getValue() {
            return value;
        }

        /**
         * Is the door in a closed or closing state
         * 
         * @return is closed or is closing
         */
        public boolean isClosedOrClosing() {
            return (this == CLOSED || this == CLOSING);
        }

        /**
         * Is the door in a closed state
         * 
         * @return is closed
         */
        public boolean isClosed() {
            return (this == CLOSED);
        }

        /**
         * Is the door in a open or partial open state
         * 
         * @return is open or partial open
         */
        public boolean isOpen() {
            return (this == OPEN || this == PARTIAL);
        }

        /**
         * Is the door in motion
         * 
         * @return door in motion
         */
        public boolean inMotion() {
            return (this == OPENING || this == CLOSING || this == MOVING);
        }

        /**
         * Lookup a door status by its String value
         * 
         * @param value
         * @return a door status enum
         */
        public static GarageDoorStatus GetDoorStatus(String value) {
            for (GarageDoorStatus ds : values()) {
                if (ds.getValue().compareTo(value)==0)
                    return ds;
            }
            return UNKNOWN;
        }
    }
}
