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
 * Node Available messages are broadcasted by Circles that are not yet part of a network
 * This is has to be tested because typically the network is "set up" using the Plugwise Source software, and never
 * changed after
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class NodeAvailableMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})");

    public NodeAvailableMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.NODE_AVAILABLE;
    }

    @Override
    protected String payLoadToHexString() {
        return "";
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
        } else {
            logger.debug("Plugwise protocol NodeAvailableMessage error: {} does not match", payLoad);
        }

    }

}
