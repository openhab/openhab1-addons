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
 * Role call request message, sent by Circle+ in the network in order to "scan" for available Circles
 * Upto 64 devices can be part of a plugwise network
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RoleCallRequestMessage extends Message {

    private int nodeID;

    public RoleCallRequestMessage(String MAC, int nodeID) {
        super(MAC, "");
        this.nodeID = nodeID;
        type = MessageType.DEVICE_ROLECALL_REQUEST;
    }

    @Override
    protected String payLoadToHexString() {
        return String.format("%02X", nodeID);
    }

    @Override
    protected void parsePayLoad() {
    }

    @Override
    public String getPayLoad() {
        return String.format("%02X", nodeID);
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

}
