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
 * A class for DENIED message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxDeniedMessage extends PowerMaxBaseMessage {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxDeniedMessage.class);

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxDeniedMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = null;

        PowerMaxSendType lastSendType = PowerMaxCommDriver.getTheCommDriver().getLastSendMsg().getSendType();
        if (lastSendType == PowerMaxSendType.EVENTLOG || lastSendType == PowerMaxSendType.ARM
                || lastSendType == PowerMaxSendType.BYPASS) {
            logger.warn("PowerMax alarm binding: invalid PIN code");
        } else if (lastSendType == PowerMaxSendType.DOWNLOAD) {
            logger.warn("PowerMax alarm binding: openHAB Powerlink not enrolled");
            updatedState = new PowerMaxState();
            updatedState.setPowerlinkMode(false);
        }

        return updatedState;
    }

}
