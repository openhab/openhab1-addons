/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.message;

import org.openhab.binding.powermax.internal.state.PowerMaxState;

/**
 * A class for ACK message handling
 *
 * @author lolodomo
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
