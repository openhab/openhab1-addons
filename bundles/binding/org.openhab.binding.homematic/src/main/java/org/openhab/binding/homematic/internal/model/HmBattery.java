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
package org.openhab.binding.homematic.internal.model;

/**
 * Object that represents a battery info.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class HmBattery {
    private HmBatteryType type;
    private int count;

    public HmBattery(HmBatteryType type, int count) {
        this.type = type;
        this.count = count;
    }

    /**
     * Returns the type of the battery.
     */
    public HmBatteryType getType() {
        return type;
    }

    /**
     * Returns the number of batteries.
     */
    public int getCount() {
        return count;
    }

    /**
     * Returns a info string containing the battery count and the type.
     */
    public String getInfo() {
        return count + "x " + type.toString();
    }
}
