/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class PacketType {

    /**
     * This is a not defined, but reserved package type.
     */
    public static final byte UNSET = (byte) 0x00;

    /**
     * Radio telegram
     */
    public static final byte RADIO = (byte) 0x01;

    /**
     * Response to any packet
     */
    public static final byte RESPONSE = (byte) 0x02;

    /**
     * Radio subtelegram
     */
    public static final byte RADIO_SUB_TEL = (byte) 0x03;

    /**
     * Event message
     */
    public static final byte EVENT = (byte) 0x04;

    /**
     * Common command
     */
    public static final byte COMMON_COMMAND = (byte) 0x05;

    /**
     * Smart Ack command
     */
    public static final byte SMART_ACK_COMMAND = (byte) 0x06;

    /**
     * Remote management command
     */
    public static final byte REMOTE_MAN_COMMAND = (byte) 0x07;

    /**
     * Radio message
     */
    public static final byte RADIO_MESSAGE = (byte) 0x09;

    /**
     * Advances protocol radio telegram
     */
    public static final byte RADIO_ADVANCED = (byte) 0x0A;

    private PacketType() {
    }

}
