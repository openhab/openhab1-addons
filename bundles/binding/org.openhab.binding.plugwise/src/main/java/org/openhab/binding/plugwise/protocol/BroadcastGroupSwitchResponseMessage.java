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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Broadcast group switch response
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class BroadcastGroupSwitchResponseMessage extends Message {

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(\\w{16})(\\w{2})(\\w{2})");

    private int portMask;
    private boolean powerState;
    private Calendar dateTimeReceived = Calendar.getInstance();

    public BroadcastGroupSwitchResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.BROADCAST_GROUP_SWITCH_RESPONSE;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    public int getPortMask() {
        return portMask;
    }

    public boolean getPowerState() {
        return powerState;
    }

    public Calendar getDateTimeReceived() {
        return dateTimeReceived;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = REQUEST_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            portMask = Integer.parseInt(matcher.group(2));
            powerState = (matcher.group(3).equals("01"));
        } else {
            logger.debug("Plugwise protocol BroadcastGroupSwitchRequestMessage error: {} does not match", payLoad);
        }
    }

}
