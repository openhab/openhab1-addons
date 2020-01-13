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
package org.openhab.binding.plugwise.protocol;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Announce awake request message
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class AnnounceAwakeRequestMessage extends Message {

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(\\w{16})(\\w{2})");

    public enum AwakeReason {

        /** The SED joins the network for maintenance */
        Maintenance(0),

        /** The SED joins a network for the first time */
        JoinNetwork(1),

        /** The SED joins a network it has already joined, e.g. after reinserting a battery */
        RejoinNetwork(2),

        /** When a SED switches a device group or when reporting values such as temperature/humidity */
        Normal(3),

        /** A human pressed the button on a SED to wake it up */
        WakeupButton(5);

        private int id;

        AwakeReason(int id) {
            this.id = id;
        }

        private static final Map<Integer, AwakeReason> reasonsByValue = new HashMap<Integer, AwakeReason>();

        static {
            for (AwakeReason type : AwakeReason.values()) {
                reasonsByValue.put(type.id, type);
            }
        }

        public static AwakeReason forValue(int value) {
            return reasonsByValue.get(value);
        }

    };

    private AwakeReason awakeReason;

    private Calendar dateTimeReceived = Calendar.getInstance();

    public AnnounceAwakeRequestMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.ANNOUNCE_AWAKE_REQUEST;
    }

    public AwakeReason getAwakeReason() {
        return awakeReason;
    }

    public Calendar getDateTimeReceived() {
        return dateTimeReceived;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = REQUEST_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            awakeReason = AwakeReason.forValue(Integer.parseInt(matcher.group(2)));
        } else {
            logger.debug("Plugwise protocol AnnounceAwakeRequestMessage error: {} does not match", payLoad);
        }
    }
}
