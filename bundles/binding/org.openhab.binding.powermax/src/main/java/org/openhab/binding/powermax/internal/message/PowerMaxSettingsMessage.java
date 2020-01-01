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

import java.util.Arrays;

import org.openhab.binding.powermax.internal.state.PowerMaxState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for SETTINGS and SETTINGS_ITEM messages handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxSettingsMessage extends PowerMaxBaseMessage {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxSettingsMessage.class);

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxSettingsMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = new PowerMaxState();

        byte[] message = getRawData();
        int index = message[2] & 0x000000FF;
        int page = message[3] & 0x000000FF;
        int length = 0;

        if (getReceiveType() == PowerMaxReceiveType.SETTINGS) {
            length = message.length - 6;
            updatedState.setUpdateSettings(Arrays.copyOfRange(message, 2, 2 + 2 + length));
        } else if (getReceiveType() == PowerMaxReceiveType.SETTINGS_ITEM) {
            length = message[4] & 0x000000FF;
            byte[] data = new byte[length + 2];
            int i = 0;
            for (int j = 2; j <= 3; j++) {
                data[i++] = message[j];
            }
            for (int j = 0; j < length; j++) {
                data[i++] = message[j + 5];
            }
            updatedState.setUpdateSettings(data);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Received Powermax setting page {} index {} length {}", String.format("%02X (%d)", page, page),
                    String.format("%02X (%d)", index, index), String.format("%02X (%d)", length, length));
        }

        return updatedState;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        int index = message[2] & 0x000000FF;
        int page = message[3] & 0x000000FF;

        str += "\n - page = " + String.format("%02X (%d)", page, page);
        str += "\n - index = " + String.format("%02X (%d)", index, index);

        return str;
    }

}
