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
package eu.aleon.aleoncean.packet.radio.userdata.eepd201;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum IOChannel {

    OUTPUT_CHANNEL_00((byte) 0x00),
    OUTPUT_CHANNEL_01((byte) 0x01),
    OUTPUT_CHANNEL_02((byte) 0x02),
    OUTPUT_CHANNEL_03((byte) 0x03),
    OUTPUT_CHANNEL_04((byte) 0x04),
    OUTPUT_CHANNEL_05((byte) 0x05),
    OUTPUT_CHANNEL_06((byte) 0x06),
    OUTPUT_CHANNEL_07((byte) 0x07),
    OUTPUT_CHANNEL_08((byte) 0x08),
    OUTPUT_CHANNEL_09((byte) 0x09),
    OUTPUT_CHANNEL_0A((byte) 0x0A),
    OUTPUT_CHANNEL_0B((byte) 0x0B),
    OUTPUT_CHANNEL_0C((byte) 0x0C),
    OUTPUT_CHANNEL_0D((byte) 0x0D),
    OUTPUT_CHANNEL_0E((byte) 0x0E),
    OUTPUT_CHANNEL_0F((byte) 0x0F),
    OUTPUT_CHANNEL_10((byte) 0x00),
    OUTPUT_CHANNEL_11((byte) 0x01),
    OUTPUT_CHANNEL_12((byte) 0x02),
    OUTPUT_CHANNEL_13((byte) 0x03),
    OUTPUT_CHANNEL_14((byte) 0x04),
    OUTPUT_CHANNEL_15((byte) 0x05),
    OUTPUT_CHANNEL_16((byte) 0x06),
    OUTPUT_CHANNEL_17((byte) 0x07),
    OUTPUT_CHANNEL_18((byte) 0x08),
    OUTPUT_CHANNEL_19((byte) 0x09),
    OUTPUT_CHANNEL_1A((byte) 0x0A),
    OUTPUT_CHANNEL_1B((byte) 0x0B),
    OUTPUT_CHANNEL_1C((byte) 0x0C),
    OUTPUT_CHANNEL_1D((byte) 0x0D),
    ALL_OUTPUT_CHANNELS((byte) 0x1E),
    INPUT_CHANNEL((byte) 0x1F);

    private static final Logger LOGGER = LoggerFactory.getLogger(IOChannel.class);

    public static IOChannel fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return IOChannel.OUTPUT_CHANNEL_00;
            case 0x01:
                return IOChannel.OUTPUT_CHANNEL_01;
            case 0x02:
                return IOChannel.OUTPUT_CHANNEL_02;
            case 0x03:
                return IOChannel.OUTPUT_CHANNEL_03;
            case 0x04:
                return IOChannel.OUTPUT_CHANNEL_04;
            case 0x05:
                return IOChannel.OUTPUT_CHANNEL_05;
            case 0x06:
                return IOChannel.OUTPUT_CHANNEL_06;
            case 0x07:
                return IOChannel.OUTPUT_CHANNEL_07;
            case 0x08:
                return IOChannel.OUTPUT_CHANNEL_08;
            case 0x09:
                return IOChannel.OUTPUT_CHANNEL_09;
            case 0x0A:
                return IOChannel.OUTPUT_CHANNEL_0A;
            case 0x0B:
                return IOChannel.OUTPUT_CHANNEL_0B;
            case 0x0C:
                return IOChannel.OUTPUT_CHANNEL_0C;
            case 0x0D:
                return IOChannel.OUTPUT_CHANNEL_0D;
            case 0x0E:
                return IOChannel.OUTPUT_CHANNEL_0E;
            case 0x0F:
                return IOChannel.OUTPUT_CHANNEL_0F;
            case 0x10:
                return IOChannel.OUTPUT_CHANNEL_10;
            case 0x11:
                return IOChannel.OUTPUT_CHANNEL_11;
            case 0x12:
                return IOChannel.OUTPUT_CHANNEL_12;
            case 0x13:
                return IOChannel.OUTPUT_CHANNEL_13;
            case 0x14:
                return IOChannel.OUTPUT_CHANNEL_14;
            case 0x15:
                return IOChannel.OUTPUT_CHANNEL_15;
            case 0x16:
                return IOChannel.OUTPUT_CHANNEL_16;
            case 0x17:
                return IOChannel.OUTPUT_CHANNEL_17;
            case 0x18:
                return IOChannel.OUTPUT_CHANNEL_18;
            case 0x19:
                return IOChannel.OUTPUT_CHANNEL_19;
            case 0x1A:
                return IOChannel.OUTPUT_CHANNEL_1A;
            case 0x1B:
                return IOChannel.OUTPUT_CHANNEL_1B;
            case 0x1C:
                return IOChannel.OUTPUT_CHANNEL_1C;
            case 0x1D:
                return IOChannel.OUTPUT_CHANNEL_1D;
            case 0x1E:
                return IOChannel.ALL_OUTPUT_CHANNELS;
            case 0x1F:
                return IOChannel.INPUT_CHANNEL;
            default:
                LOGGER.warn("Invalid IOChannel: {}", value);
                return IOChannel.ALL_OUTPUT_CHANNELS;
        }
    }

    final byte value;

    private IOChannel(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }

}
