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
package org.openhab.binding.ecobee.messages;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Possible values for fan mode
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/objects/Event.shtml">Event</a>
 * @author John Cocula
 * @since 1.7.0
 */
public enum FanMode {
    AUTO("auto"),
    ON("on");

    private final String mode;

    private FanMode(final String mode) {
        this.mode = mode;
    }

    @JsonValue
    public String value() {
        return mode;
    }

    @JsonCreator
    public static FanMode forValue(String v) {
        for (FanMode fm : FanMode.values()) {
            if (fm.mode.equals(v)) {
                return fm;
            }
        }
        throw new IllegalArgumentException("Invalid fan mode: " + v);
    }

    @Override
    public String toString() {
        return this.mode;
    }
}
