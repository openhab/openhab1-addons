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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for POWERLINK message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxPowerlinkMessage extends PowerMaxBaseMessage {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxPowerlinkMessage.class);

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxPowerlinkMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = null;

        byte[] message = getRawData();
        byte subType = message[2];

        PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();
        if (subType == 0x03) {
            // keep alive message
            comm.sendAck(this, (byte) 0x02);
            updatedState = new PowerMaxState();
            updatedState.setLastKeepAlive(System.currentTimeMillis());
        } else if (subType == 0x0A && message[4] == 0x01) {
            logger.info("PowerMax alarm binding: Enrolling Powerlink");
            comm.enrollPowerlink();
            updatedState = new PowerMaxState();
            updatedState.setDownloadSetupRequired(true);
        } else {
            comm.sendAck(this, (byte) 0x02);
        }

        return updatedState;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        byte subType = message[2];

        if (subType == 0x03) {
            str += "\n - sub type = keep alive";
        } else if (subType == 0x0A) {
            str += "\n - sub type = enroll";
            str += "\n - enroll = " + String.format("%02X", message[4]);
        } else {
            str += "\n - sub type = " + String.format("%02X", subType);
        }

        return str;
    }

}
