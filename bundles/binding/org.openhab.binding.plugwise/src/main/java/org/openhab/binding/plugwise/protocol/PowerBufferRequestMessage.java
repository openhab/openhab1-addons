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
 * Request historical energy consumption data
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class PowerBufferRequestMessage extends Message {

    private int logAddress;

    public PowerBufferRequestMessage(String MAC, int logAddress) {
        super(MAC, "");
        type = MessageType.POWER_BUFFER_REQUEST;
        this.logAddress = logAddress;
    }

    @Override
    protected String payLoadToHexString() {
        return String.format("%08X", (logAddress * 32 + 278528));
    }

    @Override
    protected void parsePayLoad() {
    }

    @Override
    protected String sequenceNumberToHexString() {
        return "";
    }

}
