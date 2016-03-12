/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.powermax.internal.message;

/**
 * Used to map received messages from the Visonic alarm panel to a ENUM value
 *
 * @author lolodomo
 * @since 1.9.0
 */
public enum PowerMaxReceiveType {

    ACK((byte) 0x02, 0, false, PowerMaxAckMessage.class, "ACK"),
    TIMEOUT((byte) 0x06, 0, false, PowerMaxTimeoutMessage.class, "TIMEOUT"),
    DENIED((byte) 0x08, 0, true, PowerMaxDeniedMessage.class, "DENIED"),
    STOP((byte) 0x0B, 0, true, PowerMaxBaseMessage.class, "STOP"),
    DOWNLOAD_RETRY((byte) 0x25, 14, true, PowerMaxDownloadRetryMessage.class, "DOWNLOAD_RETRY"),
    SETTINGS((byte) 0x33, 14, true, PowerMaxSettingsMessage.class, "SETTINGS"),
    INFO((byte) 0x3C, 14, true, PowerMaxInfoMessage.class, "INFO"),
    SETTINGS_ITEM((byte) 0x3F, 0, true, PowerMaxSettingsMessage.class, "SETTINGS_ITEM"),
    EVENT_LOG((byte) 0xA0, 15, true, PowerMaxEventLogMessage.class, "EVENT_LOG"),
    ZONESNAME((byte) 0xA3, 15, true, PowerMaxZonesNameMessage.class, "ZONESNAME"),
    STATUS((byte) 0xA5, 15, true, PowerMaxStatusMessage.class, "STATUS"),
    ZONESTYPE((byte) 0xA6, 15, true, PowerMaxZonesTypeMessage.class, "ZONESTYPE"),
    PANEL((byte) 0xA7, 15, true, PowerMaxPanelMessage.class, "PANEL"),
    POWERLINK((byte) 0xAB, 15, false, PowerMaxPowerlinkMessage.class, "POWERLINK"),
    POWERMASTER((byte) 0xB0, 0, true, PowerMaxPowerMasterMessage.class, "POWERMASTER"),
    F1((byte) 0xF1, 9, false, PowerMaxBaseMessage.class, "F1");

    private byte code;
    private int length;
    private boolean ackRequired;
    private Class<?> handlerClass;
    private String description;

    private PowerMaxReceiveType(byte code, int length, boolean ackRequired, Class<?> handlerClass, String description) {
        this.code = code;
        this.length = length;
        this.ackRequired = ackRequired;
        this.handlerClass = handlerClass;
        this.description = description;
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
     * @return the message description
     */
    public String getDescription() {
        return description;
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
