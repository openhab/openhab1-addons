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
package org.openhab.binding.zwave.internal.protocol;

/**
 * enum defining the different ZWave node types
 *
 * @author Chris Jackson
 * @since 1.5
 */
public enum ZWaveDeviceType {
    UNKNOWN("Unknown"),
    SLAVE("Slave"),
    PRIMARY("Primary Controller"),
    SECONDARY("Secondary Controller"),
    SUC("Static Update Controller");

    private ZWaveDeviceType(final String text) {
        this.text = text;
    }

    private final String text;

    public String getLabel() {
        return text;
    }

    public static ZWaveDeviceType fromString(String text) {
        if (text != null) {
            for (ZWaveDeviceType c : ZWaveDeviceType.values()) {
                if (text.equalsIgnoreCase(c.name())) {
                    return c;
                }
            }
        }
        return null;
    }
}
