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
 * Real time clock request message. The Circle+ is the only device to hold a real real-time clock
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RealTimeClockGetRequestMessage extends Message {

    public RealTimeClockGetRequestMessage(String MAC) {
        super(MAC, "");
        type = MessageType.REALTIMECLOCK_GET_REQUEST;
    }

    @Override
    protected String payLoadToHexString() {
        return "";
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

    @Override
    protected void parsePayLoad() {
    }

}
