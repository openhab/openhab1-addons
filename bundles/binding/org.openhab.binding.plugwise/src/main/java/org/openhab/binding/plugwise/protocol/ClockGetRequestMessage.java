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
 * Circle Clock request
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class ClockGetRequestMessage extends Message {

    public ClockGetRequestMessage(String MAC) {
        super(MAC, "");
        type = MessageType.CLOCK_GET_REQUEST;
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
