/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal;

import org.apache.commons.lang.StringUtils;

/**
 * Used to map selector types from the binding string to a ENUM value
 *
 * @author lolodomo
 * @since 1.9.0
 */
public enum PowerMaxSelectorType {

    PANEL_MODE("panel_mode"),
    PANEL_TYPE("panel_type"),
    PANEL_SERIAL("panel_serial"),
    PANEL_EPROM("panel_eprom"),
    PANEL_SOFTWARE("panel_software"),
    PANEL_TROUBLE("panel_trouble"),
    PANEL_ALERT_IN_MEMORY("panel_alert_in_memory"),

    PARTITION_STATUS("partition_status"),
    PARTITION_READY("partition_ready"),
    PARTITION_BYPASS("partition_bypass"),
    PARTITION_ALARM("partition_alarm"),
    PARTITION_ARMED("partition_armed"),
    PARTITION_ARM_MODE("partition_arm_mode"),

    ZONE_STATUS("zone_status"),
    ZONE_LAST_TRIP("zone_last_trip"),
    ZONE_BYPASSED("zone_bypassed"),
    ZONE_ARMED("zone_armed"),
    ZONE_LOW_BATTERY("zone_low_battery"),

    COMMAND("command"),

    EVENT_LOG("event_log"),

    PGM_STATUS("PGM_status"),
    X10_STATUS("X10_status");

    private String selector;

    private PowerMaxSelectorType(String selector) {
        this.selector = selector;
    }

    public String getSelector() {
        return selector;
    }

    /**
     * Get the ENUM value from its string selector
     *
     * @param selector
     *            the ENUM label as a string
     *
     * @return the corresponding ENUM value
     *
     * @throws IllegalArgumentException
     *             if no ENUM value corresponds to this string
     */
    public static PowerMaxSelectorType fromString(String selector) {
        if (!StringUtils.isEmpty(selector)) {
            for (PowerMaxSelectorType selectorType : PowerMaxSelectorType.values()) {
                if (selectorType.getSelector().equals(selector)) {
                    return selectorType;
                }
            }
        }

        throw new IllegalArgumentException("Invalid label: " + selector);
    }
}
