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
package org.openhab.binding.nest.internal.messages;

import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonValue;

/**
 * Possible values for *_alarm_state:
 *
 * <dl>
 * <dt>ok</dt>
 * <dd>OK</dd>
 * <dt>warning</dt>
 * <dd>Warning - Smoke or CO Detected</dd>
 * <dt>emergency</dt>
 * <dd>Emergency - * Detected - move to fresh air</dd>
 * </dl>
 *
 * @author John Cocula
 * @since 1.9.0
 */
public enum AlarmState {
    OK("ok"),
    WARNING("warning"),
    EMERGENCY("emergency");

    private final String state;

    private AlarmState(String state) {
        this.state = state;
    }

    @JsonValue
    public String value() {
        return state;
    }

    @JsonCreator
    public static AlarmState forValue(String v) {
        for (AlarmState as : AlarmState.values()) {
            if (as.state.equals(v)) {
                return as;
            }
        }
        throw new IllegalArgumentException("Invalid alarm_state: " + v);
    }

    @Override
    public String toString() {
        return this.state;
    }
}
