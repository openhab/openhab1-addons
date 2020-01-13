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
 * The acknowledge function allows an alert to be acknowledged.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/Acknowledge.shtml">Acknowledge</a>
 * @author John Cocula
 * @since 1.7.0
 */
public final class AcknowledgeFunction extends AbstractFunction {

    /**
     * The type of acknowledgment.
     */
    public static enum AckType {
        ACCEPT("accept"),
        DECLINE("decline"),
        DEFER("defer"),
        UNACKNOWLEDGED("unacknowledged");

        private final String type;

        private AckType(String type) {
            this.type = type;
        }

        @JsonCreator
        public static AckType forValue(String v) {
            for (AckType at : AckType.values()) {
                if (at.type.equals(v)) {
                    return at;
                }
            }
            throw new IllegalArgumentException("Invalid ack type: " + v);
        }

        @Override
        @JsonValue
        public String toString() {
            return this.type;
        }
    }

    public AcknowledgeFunction(String thermostatIdentifier, String ackRef, AckType ackType, Boolean remindMeLater) {
        super("acknowledge");

        if (thermostatIdentifier == null || ackRef == null || ackType == null) {
            throw new IllegalArgumentException("thermostatIdentifier, ackRef and ackType are required.");
        }

        makeParams().put("thermostatIdentifier", thermostatIdentifier);
        makeParams().put("ackRef", ackRef);
        makeParams().put("ackType", ackType);
        if (remindMeLater != null) {
            makeParams().put("remindMeLater", remindMeLater);
        }
    }
}
