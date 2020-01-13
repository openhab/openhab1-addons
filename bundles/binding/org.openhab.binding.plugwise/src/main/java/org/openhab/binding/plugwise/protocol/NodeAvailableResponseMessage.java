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

/**
 * Message to be sent to a Circle when its Node Available message is "accepted"
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class NodeAvailableResponseMessage extends Message {

    boolean acceptanceCode;
    String destinationMAC;

    public NodeAvailableResponseMessage(boolean code, String destination) {
        super("", "");
        acceptanceCode = code;
        destinationMAC = destination;
        MAC = "";
        type = MessageType.NODE_AVAILABLE_RESPONSE;

    }

    @Override
    protected String payLoadToHexString() {
        return String.format("%02X", acceptanceCode ? 1 : 0) + destinationMAC;
    }

    @Override
    protected void parsePayLoad() {
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

}
