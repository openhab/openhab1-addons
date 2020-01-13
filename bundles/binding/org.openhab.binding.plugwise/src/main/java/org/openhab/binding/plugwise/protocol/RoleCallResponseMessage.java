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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Role Call response message
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RoleCallResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{16})(\\w{2})");

    private String nodeMAC;
    private int nodeID;

    public RoleCallResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.DEVICE_ROLECALL_RESPONSE;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            nodeMAC = (matcher.group(2));
            nodeID = (Integer.parseInt(matcher.group(3), 16));
        } else {
            logger.debug("Plugwise protocol RoleCallResponseMessage error: {} does not match", payLoad);
        }

    }

    public String getNodeMAC() {
        return nodeMAC;
    }

    public int getNodeID() {
        return nodeID;
    }

}
