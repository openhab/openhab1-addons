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

/**
 * Used to map received messages from the Visonic alarm panel to a ENUM value
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public enum PowerMaxReceiveType {

    ACK((byte) 0x02, 0, false, PowerMaxAckMessage.class),
    TIMEOUT((byte) 0x06, 0, false, PowerMaxTimeoutMessage.class),
    DENIED((byte) 0x08, 0, true, PowerMaxDeniedMessage.class),
    STOP((byte) 0x0B, 0, true, PowerMaxBaseMessage.class),
    DOWNLOAD_RETRY((byte) 0x25, 14, true, PowerMaxDownloadRetryMessage.class),
    SETTINGS((byte) 0x33, 14, true, PowerMaxSettingsMessage.class),
    INFO((byte) 0x3C, 14, true, PowerMaxInfoMessage.class),
    SETTINGS_ITEM((byte) 0x3F, 0, true, PowerMaxSettingsMessage.class),
    EVENT_LOG((byte) 0xA0, 15, true, PowerMaxEventLogMessage.class),
    ZONESNAME((byte) 0xA3, 15, true, PowerMaxZonesNameMessage.class),
    STATUS((byte) 0xA5, 15, true, PowerMaxStatusMessage.class),
    ZONESTYPE((byte) 0xA6, 15, true, PowerMaxZonesTypeMessage.class),
    PANEL((byte) 0xA7, 15, true, PowerMaxPanelMessage.class),
    POWERLINK((byte) 0xAB, 15, false, PowerMaxPowerlinkMessage.class),
    POWERMASTER((byte) 0xB0, 0, true, PowerMaxPowerMasterMessage.class),
    F1((byte) 0xF1, 9, false, PowerMaxBaseMessage.class);

    private byte code;
    private int length;
    private boolean ackRequired;
    private Class<?> handlerClass;

    private PowerMaxReceiveType(byte code, int length, boolean ackRequired, Class<?> handlerClass) {
        this.code = code;
        this.length = length;
        this.ackRequired = ackRequired;
        this.handlerClass = handlerClass;
    }

    /**
     * @return the code identifying the message (second byte in the message)
     */
    public byte getCode() {
        return code;
    }

    /**
     * @return the message expected length
     */
    public int getLength() {
        return length;
    }

    /**
     * @return true if the received message requires the sending of an ACK, false if not
     */
    public boolean isAckRequired() {
        return ackRequired;
    }

    /**
     * @return the class that should handle the message
     */
    public Class<?> getHandlerClass() {
        return handlerClass;
    }

    /**
     * Set the class that should handle the message
     *
     * @param handlerClass
     *            the class that should handle the message
     */
    public void setHandlerClass(Class<?> handlerClass) {
        this.handlerClass = handlerClass;
    }

    /**
     * Get the ENUM value from its identifying code
     *
     * @param code
     *            the identifying code
     *
     * @return the corresponding ENUM value
     *
     * @throws IllegalArgumentException if no ENUM value corresponds to this code
     */
    public static PowerMaxReceiveType fromCode(byte code) {
        for (PowerMaxReceiveType messageType : PowerMaxReceiveType.values()) {
            if (messageType.getCode() == code) {
                return messageType;
            }
        }

        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
