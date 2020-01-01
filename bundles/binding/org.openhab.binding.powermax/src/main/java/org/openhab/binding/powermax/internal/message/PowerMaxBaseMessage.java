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

import java.lang.reflect.InvocationTargetException;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.powermax.internal.state.PowerMaxState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A base class for handling a message with the Visonic alarm system
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxBaseMessage {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxBaseMessage.class);

    private byte[] rawData;
    private int code;
    private PowerMaxSendType sendType;
    private PowerMaxReceiveType receiveType;

    /**
     * Constructor.
     *
     * @param message
     *            the message as a buffer of bytes
     */
    public PowerMaxBaseMessage(byte[] message) {
        this.sendType = null;
        decodeMessage(message);
    }

    /**
     * Constructor.
     *
     * @param sendType
     *            the type of a message to be sent
     */
    public PowerMaxBaseMessage(PowerMaxSendType sendType) {
        this(sendType, null);
    }

    /**
     * Constructor.
     *
     * @param sendType
     *            the type of a message to be sent
     * @param param
     *            the dynamic part of a message to be sent; null if no dynamic part
     */
    public PowerMaxBaseMessage(PowerMaxSendType sendType, byte[] param) {
        this.sendType = sendType;
        byte[] message = new byte[sendType.getMessage().length + 3];
        int index = 0;
        message[index++] = 0x0D;
        for (int i = 0; i < sendType.getMessage().length; i++) {
            if ((param != null) && (sendType.getParamPosition() != null) && (i >= sendType.getParamPosition())
                    && (i < (sendType.getParamPosition() + param.length))) {
                message[index++] = param[i - sendType.getParamPosition()];
            } else {
                message[index++] = sendType.getMessage()[i];
            }
        }
        message[index++] = 0x00;
        message[index++] = 0x0A;
        decodeMessage(message);
    }

    /**
     * Extract information from the buffer of bytes and set class attributes
     *
     * @param data
     *            the message as a buffer of bytes
     */
    private void decodeMessage(byte[] data) {
        rawData = data;
        code = rawData[1] & 0x000000FF;
        try {
            receiveType = PowerMaxReceiveType.fromCode((byte) code);
        } catch (IllegalArgumentException arg0) {
            receiveType = null;
        }
    }

    /**
     * Work to be done when receiving a message from the Visonic alarm system
     *
     * @return a new state containing all changes driven by the message
     */
    public PowerMaxState handleMessage() {
        // Send an ACK if needed
        if (isAckRequired()) {
            PowerMaxCommDriver.getTheCommDriver().sendAck(this, (byte) 0x02);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("{}message handled by class {}: {}", (receiveType == null) ? "Unsupported " : "",
                    this.getClass().getSimpleName(), this.toString());
        }

        return null;
    }

    /**
     * @return the raw data of the message (buffer of bytes)
     */
    public byte[] getRawData() {
        return rawData;
    }

    /**
     * @return the identifying code of the message (second byte in the buffer)
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the type of the message to be sent
     */
    public PowerMaxSendType getSendType() {
        return sendType;
    }

    /**
     * @return the type of the received message
     */
    public PowerMaxReceiveType getReceiveType() {
        return receiveType;
    }

    /**
     * @return true if the received message requires the sending of an ACK
     */
    public boolean isAckRequired() {
        return receiveType == null || receiveType.isAckRequired();
    }

    @Override
    public String toString() {
        String str = "\n - Raw data = " + DatatypeConverter.printHexBinary(rawData);
        str += "\n - type = " + String.format("%02X", code);
        if (sendType != null) {
            str += " ( " + sendType.toString() + " )";
        } else if (receiveType != null) {
            str += " ( " + receiveType.toString() + " )";
        }

        return str;
    }

    /**
     * Instantiate a class for handling a received message The class depends on the message.
     *
     * @param message
     *            the received message as a buffer of bytes
     *
     * @return a new class instance
     */
    public static PowerMaxBaseMessage getMessageObject(byte[] message) {

        Class<?> cl;
        try {
            cl = PowerMaxReceiveType.fromCode(message[1]).getHandlerClass();
        } catch (IllegalArgumentException e) {
            cl = PowerMaxBaseMessage.class;
        }
        try {
            return (PowerMaxBaseMessage) cl.getConstructor(byte[].class).newInstance(message);
        } catch (InstantiationException e) {
            return new PowerMaxBaseMessage(message);
        } catch (IllegalAccessException e) {
            return new PowerMaxBaseMessage(message);
        } catch (NoSuchMethodException e) {
            return new PowerMaxBaseMessage(message);
        } catch (InvocationTargetException e) {
            return new PowerMaxBaseMessage(message);
        }
    }

}
