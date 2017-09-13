/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.simplebinary.internal;

/**
 *
 * Class holding item data
 *
 * @author Vita Tucek
 * @since 1.9.0
 */
public class SimpleBinaryItemData extends SimpleBinaryMessage {

    protected byte[] itemData;
    protected int resendCounter = 0;

    /**
     * Construct item data instance for unspecified item
     *
     * @param messageId
     *            Message ID
     * @param deviceId
     *            Device ID
     * @param itemData
     *            Raw data
     */
    public SimpleBinaryItemData(byte messageId, int deviceId, byte[] itemData) {
        super(messageId, deviceId, -1);

        this.itemData = itemData;
    }

    /**
     * Construct item data instance
     *
     * @param messageId
     *            Message ID
     * @param deviceId
     *            Device ID
     * @param itemAddress
     *            Item address
     * @param itemData
     *            Raw data
     */
    public SimpleBinaryItemData(byte messageId, int deviceId, int itemAddress, byte[] itemData) {
        super(messageId, deviceId, itemAddress);

        this.itemData = itemData;
    }

    /**
     * Return item raw data
     *
     * @return
     */
    public byte[] getData() {
        return itemData;
    }

    /**
     * Return resend counter value
     *
     * @since 1.9.0
     * @return Resend counter value
     */
    public int getResendCounter() {
        return resendCounter;
    }

    /**
     * Increment resend counter value
     *
     * @since 1.9.0
     * @return Resend counter value
     */
    public int incrementResendCounter() {
        return ++resendCounter;
    }
}
