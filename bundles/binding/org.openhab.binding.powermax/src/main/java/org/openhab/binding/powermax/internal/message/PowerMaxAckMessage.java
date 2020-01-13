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
 * A class for ACK message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxAckMessage extends PowerMaxBaseMessage {

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxAckMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = null;

        if (PowerMaxCommDriver.getTheCommDriver().getLastSendMsg().getSendType() == PowerMaxSendType.EXIT) {
            updatedState = new PowerMaxState();
            updatedState.setPowerlinkMode(true);
            updatedState.setDownloadMode(false);
        }

        return updatedState;
    }

}
