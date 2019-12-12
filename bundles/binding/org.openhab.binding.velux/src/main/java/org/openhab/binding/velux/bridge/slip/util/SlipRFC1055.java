/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge.slip.util;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transport layer supported by the Velux bridge:
 * SLIP wrapping supported by the Velux bridge.
 * <P>
 * Methods available:
 * <UL>
 * <LI>{@link #encode(byte[] payload) } converts a given payload into transfer byte encoding.</LI>
 * <LI>{@link #decode(byte[] packet) } converts a given transfer byte encoding into a payload.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SlipRFC1055 {

    private final Logger logger = LoggerFactory.getLogger(SlipRFC1055.class);

    private static final byte SLIP_BYTE_END = (byte) 0xC0;
    private static final byte SLIP_BYTE_ESC = (byte) 0xDB;
    private static final byte SLIP_BYTE_ESC_END = (byte) 0xDC;
    private static final byte SLIP_BYTE_ESC_ESC = (byte) 0xDD;

    /**
     * Converts a given payload into transfer byte encoding.
     *
     * @param payload Array of bytes to be transmitted.
     * @return <b>packet</b>
     *         of type Array-of-byte as encoded payload.
     */
    public byte[] encode(byte[] payload) {
        logger.trace("encode() for data size {} called.", payload.length);

        int additional = 2;
        for (byte b : payload) {
            if ((b == SLIP_BYTE_ESC) || (b == SLIP_BYTE_END)) {
                additional++;
            }
        }
        byte[] packet = new byte[payload.length + additional];
        int packetIndex = 0;
        packet[packetIndex++] = SLIP_BYTE_END;

        for (byte b : payload) {
            if (b == SLIP_BYTE_ESC) {
                packet[packetIndex++] = SLIP_BYTE_ESC;
                packet[packetIndex++] = SLIP_BYTE_ESC_ESC;
            } else if (b == SLIP_BYTE_END) {
                packet[packetIndex++] = SLIP_BYTE_ESC;
                packet[packetIndex++] = SLIP_BYTE_ESC_END;
            } else {
                packet[packetIndex++] = b;
            }
        }
        packet[packetIndex++] = SLIP_BYTE_END;
        assert (packetIndex == packet.length);
        logger.trace("encode() provides transfer encoding: {}.", new Packet(packet).toString());
        return packet;
    }

    /**
     * Converts a given transfer byte encoding into a payload.
     *
     * @param packet Array of bytes as being received.
     * @return <b>payload</b>
     *         of type Array-of-byte as decoded payload.
     * @throws ParseException in case of decoding errors.
     */
    public byte[] decode(byte[] packet) throws ParseException {
        logger.trace("decode() for packet size {} called.", packet.length);
        if (packet.length < 3) {
            throw new ParseException("Packet too short", 0);
        }
        if (packet[0] != SLIP_BYTE_END) {
            throw new ParseException("Unexpected byte at 1st position", 0);
        }
        ;
        if (packet[packet.length - 1] != SLIP_BYTE_END) {
            throw new ParseException("Unexpected byte at last position", 0);
        }
        ;
        int additional = -2;
        for (int i = 0; i < packet.length; i++) {
            if (packet[i] == SLIP_BYTE_ESC) {
                additional--;
            }
        }
        byte[] payload = new byte[packet.length + additional];

        int packetIndex = 0;
        for (int i = 0; i < packet.length; i++) {
            if ((i == 0) || (i == packet.length - 1)) {
                continue;
            }
            if ((packet[i] == SLIP_BYTE_ESC) && (packet[i + 1] == SLIP_BYTE_ESC_ESC)) {
                payload[packetIndex++] = SLIP_BYTE_ESC;
                i++;
            } else if ((packet[i] == SLIP_BYTE_ESC) && (packet[i + 1] == SLIP_BYTE_ESC_END)) {
                payload[packetIndex++] = SLIP_BYTE_END;
                i++;
            } else {
                payload[packetIndex++] = packet[i];
            }
        }
        logger.trace("decode() provides payload: {}.", new Packet(payload).toString());
        return payload;
    }

}
