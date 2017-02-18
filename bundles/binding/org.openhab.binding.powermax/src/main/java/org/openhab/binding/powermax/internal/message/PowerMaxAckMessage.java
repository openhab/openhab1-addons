/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
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
