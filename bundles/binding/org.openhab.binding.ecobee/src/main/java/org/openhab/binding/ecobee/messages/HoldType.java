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
 * Hold types ease the calculation of end times for holds. You are always free to provide the <code>startDate</code> and
 * <code>startTime</code> for the hold and it will be honoured, except where documented. The <code>endDate</code> and
 * <code>endTime</code> depend on the hold type chosen. Default is <code>indefinite</code>. Valid values:
 * <code>dateTime</code>, <code>nextTransition</code>, <code>indefinite</code>, <code>holdHours</code>.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SetHold.shtml">SetHold</a>
 * @author John Cocula
 * @since 1.7.0
 */
public enum HoldType {

    /**
     * Use the provided <code>startDate</code>, <code>startTime</code>, <code>endDate</code> and <code>endTime</code>
     * for the event. If start date/time is not provided, it will be assumed to be right now. End date/time is required.
     */
    DATE_TIME("dateTime"),

    /**
     * The end date/time will be set to the next climate transition in the program.
     */
    NEXT_TRANSITION("nextTransition"),

    /**
     * The hold will not end and require to be cancelled explicitly.
     */
    INDEFINITE("indefinite"),

    /**
     * Use the value in the <code>holdHours</code> parameter to set the end date/time for the event.
     */
    HOLD_HOURS("holdHours");

    private final String type;

    private HoldType(final String type) {
        this.type = type;
    }

    @JsonCreator
    public static HoldType forValue(String v) {
        for (HoldType ht : HoldType.values()) {
            if (ht.type.equals(v)) {
                return ht;
            }
        }
        throw new IllegalArgumentException("Invalid hold type: " + v);
    }

    @Override
    @JsonValue
    public String toString() {
        return this.type;
    }
}
