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
 * A class for DOWNLOAD RETRY message handling
 *
 * @author lolodomo
 * @since 1.9.0
 */
public class PowerMaxDownloadRetryMessage extends PowerMaxBaseMessage {

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxDownloadRetryMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        byte[] message = getRawData();
        int waitTime = message[4] & 0x000000FF;

        PowerMaxCommDriver.getTheCommDriver().sendMessageLater(PowerMaxSendType.DOWNLOAD, waitTime);

        return null;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        int waitTime = message[4] & 0x000000FF;

        str += "\n - wait time = " + waitTime + " seconds";

        return str;
    }

}
