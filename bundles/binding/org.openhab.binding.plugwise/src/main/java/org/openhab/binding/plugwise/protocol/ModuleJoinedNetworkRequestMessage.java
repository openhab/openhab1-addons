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
 * Module joined network request. Sent when a SED (re)joins the network.
 * E.g. when you reinsert the battery of a Scan.
 *
 * @author Wouter Born
 * @since 1.9.0
 */
public class ModuleJoinedNetworkRequestMessage extends Message {

    private static final Pattern REQUEST_PATTERN = Pattern.compile("(\\w{16})");

    private Calendar dateTimeReceived = Calendar.getInstance();

    public ModuleJoinedNetworkRequestMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.MODULE_JOINED_NETWORK_REQUEST;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    public Calendar getDateTimeReceived() {
        return dateTimeReceived;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = REQUEST_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
        } else {
            logger.debug("Plugwise protocol ModuleJoinedNetworkRequestMessage error: {} does not match", payLoad);
        }
    }

}
