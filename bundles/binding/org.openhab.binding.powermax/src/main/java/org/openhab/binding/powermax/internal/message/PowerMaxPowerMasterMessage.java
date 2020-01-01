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
package org.openhab.binding.powermax.internal.message;

import org.openhab.binding.powermax.internal.state.PowerMaxState;

/**
 * A class for PowerMaster message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxPowerMasterMessage extends PowerMaxBaseMessage {

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxPowerMasterMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        byte[] message = getRawData();
        byte msgType = message[2];
        byte subType = message[3];

        if ((msgType == 0x03) && (subType == 0x39)) {
            PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();
            comm.sendMessage(PowerMaxSendType.POWERMASTER_ZONE_STAT1);
            comm.sendMessage(PowerMaxSendType.POWERMASTER_ZONE_STAT2);
        }

        return null;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        byte msgType = message[2];
        byte subType = message[3];
        byte msgLen = message[4];

        str += "\n - type = " + String.format("%02X", msgType);
        str += "\n - subtype = " + String.format("%02X", subType);
        str += "\n - msgLen = " + String.format("%02X", msgLen);

        return str;
    }

}
