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
package org.openhab.binding.powermax.internal.state;

/**
 * A class to store the settings of a zone
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxZoneSettings {

    private static final String[] zoneTypes = { "Non-Alarm", "Emergency", "Flood", "Gas", "Delay 1", "Delay 2",
            "Interior-Follow", "Perimeter", "Perimeter-Follow", "24 Hours Silent", "24 Hours Audible", "Fire",
            "Interior", "Home Delay", "Temperature", "Outdoor" };

    private static final String[] zoneChimes = { "Off", "Melody", "Zone" };

    private String name;
    private String type;
    private String chime;
    private String sensorType;
    private boolean[] partitions;
    private boolean alwaysInAlarm;

    public PowerMaxZoneSettings(String name, byte type, byte chime, String sensorType, boolean[] partitions) {
        this.name = name;
        this.type = ((type & 0x000000FF) < zoneTypes.length) ? zoneTypes[type & 0x000000FF] : null;
        this.chime = ((chime & 0x000000FF) < zoneChimes.length) ? zoneChimes[chime & 0x000000FF] : null;
        this.sensorType = sensorType;
        this.partitions = partitions;
        this.alwaysInAlarm = ((type == 2) || (type == 3) || (type == 9) || (type == 10) || (type == 11)
                || (type == 14));
    }

    /**
     * @return the zone name
     */
    public String getName() {
        return (name == null) ? "Unknown" : name;
    }

    /**
     * Set the zone name
     *
     * @param name
     *            the zone name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the zone type
     */
    public String getType() {
        return (type == null) ? "Unknown" : type;
    }

    /**
     * Set the zone type
     *
     * @param type
     *            the zone type as an internal code
     */
    public void setType(byte type) {
        this.type = ((type & 0x000000FF) < zoneTypes.length) ? zoneTypes[type & 0x000000FF] : null;
        this.alwaysInAlarm = ((type == 2) || (type == 3) || (type == 9) || (type == 10) || (type == 11)
                || (type == 14));
    }

    public String getChime() {
        return (chime == null) ? "Unknown" : chime;
    }

    /**
     * @return the sensor type of this zone
     */
    public String getSensorType() {
        return (sensorType == null) ? "Unknown" : sensorType;
    }

    /**
     * Set the sensor type of this zone
     *
     * @param sensorType
     *            the sensor type
     */
    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    /**
     * @param number
     *            the partition number (first partition is number 1)
     *
     * @return true if the zone is attached to this partition; false if not
     */
    public boolean isInPartition(int number) {
        return ((number <= 0) || (number > partitions.length)) ? false : partitions[number - 1];
    }

    /**
     * @return true if the zone type is always in alarm; false if not
     */
    public boolean isAlwaysInAlarm() {
        return alwaysInAlarm;
    }

}
