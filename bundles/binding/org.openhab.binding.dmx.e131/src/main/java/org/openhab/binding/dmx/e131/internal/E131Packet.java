/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.e131.internal;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * E1.31 packet class
 *
 * E1.31 packet with header and payload
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */

public class E131Packet {

    private static final Logger logger = LoggerFactory.getLogger(E131Packet.class);

    public static final int E131_MAX_PACKET_LEN = 638;
    public static final int E131_MAX_PAYLOAD_SIZE = 512;

    private int universe;
    private int payloadSize = E131_MAX_PAYLOAD_SIZE;
    private byte[] rawPacket = new byte[E131_MAX_PACKET_LEN];
    private E131Node broadcastNode;

    /**
     * default constructor, creates a packet
     *
     * @param uuid
     *            UUID is mandatory
     */

    public E131Packet(UUID uuid) {
        /* init E1.31 root layer, total length 38 bytes */
        rawPacket[0] = 0x00; // preamble size, 2 bytes
        rawPacket[1] = 0x10;
        rawPacket[2] = 0x00; // postamble size, 2 bytes
        rawPacket[3] = 0x00;
        rawPacket[4] = 0x41; // packet identifier, 12 bytes
        rawPacket[5] = 0x53;
        rawPacket[6] = 0x43;
        rawPacket[7] = 0x2d;
        rawPacket[8] = 0x45;
        rawPacket[9] = 0x31;
        rawPacket[10] = 0x2e;
        rawPacket[11] = 0x31;
        rawPacket[12] = 0x37;
        rawPacket[13] = 0x00;
        rawPacket[14] = 0x00;
        rawPacket[15] = 0x00;
        rawPacket[16] = 0x72; // flags & length, 2 bytes
        rawPacket[17] = 0x6e;
        rawPacket[18] = 0x00; // vector, 4 bytes;
        rawPacket[19] = 0x00;
        rawPacket[20] = 0x00;
        rawPacket[21] = 0x04;

        // UUID 16 bytes
        ByteBuffer uuidBytes = ByteBuffer.wrap(new byte[16]);
        uuidBytes.putLong(uuid.getMostSignificantBits());
        uuidBytes.putLong(uuid.getLeastSignificantBits());
        System.arraycopy(uuidBytes.array(), 0, rawPacket, 22, 16);

        /* init E1.31 framing layer, total length 77 bytes */
        rawPacket[38] = 0x72; // flags & length, 2 bytes
        rawPacket[39] = 0x58;
        rawPacket[40] = 0x00; // vector, 4 bytes;
        rawPacket[41] = 0x00;
        rawPacket[42] = 0x00;
        rawPacket[43] = 0x02;
        for (int i = 44; i < 108; i++) { // senderName, 64 bytes
            rawPacket[i] = 0x00;
        }
        rawPacket[108] = 0x64; // priority (default 100), 1 byte
        rawPacket[109] = 0x00; // reserved, 2 bytes
        rawPacket[110] = 0x00;
        rawPacket[111] = 0x00; // sequence number, 1 byte
        rawPacket[112] = 0x00; // options, 1 byte
        rawPacket[113] = 0x00; // universe, 2 bytes
        rawPacket[114] = 0x00;

        /* E1.31 DMP layer, total length 11 + channel count */
        rawPacket[115] = 0x72; // flags & length, 2 bytes
        rawPacket[116] = 0x0b;
        rawPacket[117] = 0x02; // vector, 1 byte
        rawPacket[118] = (byte) 0xa1; // address type, 1 byte
        rawPacket[119] = 0x00; // start address, 2 bytes
        rawPacket[120] = 0x00;
        rawPacket[121] = 0x00; // address increment, 2 bytes
        rawPacket[122] = 0x01;
        rawPacket[123] = 0x02; // payload size, 2 bytes (including start code)
        rawPacket[123] = 0x01;
        rawPacket[125] = 0x00; // DMX start code, 1 byte
    }

    /**
     * set payload size
     *
     * @param payloadSize
     *            payload size (number of DMX channels in this packet)
     */
    public void setPayloadSize(int payloadSize) {
        if (payloadSize < 1) {
            payloadSize = 1;
            logger.error("payload minimum is one slot");
        } else if (payloadSize > 512) {
            payloadSize = 512;
            logger.warn("coercing payload size to allowed maximum value of 512");
        }

        /* root Layer */
        rawPacket[16] = (byte) ((28672 + 110 + payloadSize) / 256);
        rawPacket[17] = (byte) ((28672 + 110 + payloadSize) % 256);

        /* framing layer */
        rawPacket[38] = (byte) ((28672 + 88 + payloadSize) / 256);
        rawPacket[39] = (byte) ((28672 + 88 + payloadSize) % 256);

        /* DMP layer */
        rawPacket[115] = (byte) ((28672 + 11 + payloadSize) / 256);
        rawPacket[116] = (byte) ((28672 + 11 + payloadSize) % 256);
        rawPacket[123] = (byte) ((payloadSize + 1) / 256);
        rawPacket[124] = (byte) ((payloadSize + 1) % 256);

        this.payloadSize = payloadSize;
    }

    /**
     * sets universe, calculates sender name and broadcast-address
     *
     * @param universe
     */
    public void setUniverse(int universe) {
        /* observe limits from E1.31 standard (coerce to range) */
        if (universe < 1) {
            this.universe = 1;
            logger.warn("coercing universe number to allowed minimum of 1");
        } else if (universe > 63999) {
            this.universe = 63999;
            logger.warn("coercing universe number to allowed maximum of 63999");
        } else {
            this.universe = universe;
        }

        /* set universe in packet */
        rawPacket[113] = (byte) (this.universe / 256);
        rawPacket[114] = (byte) (this.universe % 256);

        /* set sender name in packet */
        String senderName = new String("openHAB DMX <" + String.format("%05d", this.universe) + ">");
        byte[] senderNameBytes = senderName.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < senderName.length(); i++) {
            rawPacket[44 + i] = senderNameBytes[i];
        }

        /* calculate broadcast node address */
        broadcastNode = new E131Node(new String(
                "239.255." + String.format("%d", rawPacket[113]) + "." + String.format("%d", rawPacket[114])));

        logger.debug("set packet universe to {}", this.universe);
    }

    /**
     * set sequence number
     *
     * @param sequenceNo
     *            sequence number (0-255)
     */
    public void setSequence(int sequenceNo) {
        rawPacket[111] = (byte) (sequenceNo % 256);
    }

    /**
     * set priority
     *
     * @param priority
     *            data priority (for multiple senders), allowed values are 0-200, default 100
     */
    public void setPriority(int priority) {
        /* observe limits (coerce to range) */
        if (priority < 0) {
            rawPacket[108] = (byte) 0;
            logger.warn("coercing packet priority to allowed minimum of 0");
        } else if (priority > 200) {
            rawPacket[108] = (byte) 200;
            logger.warn("coercing packet priority to allowed minimum of 200");
        } else {
            rawPacket[108] = (byte) (priority);
            logger.debug("set packet priority to {}", priority);
        }
    }

    /**
     * set DMX payload data
     *
     * @param payload
     *            byte array containing DMX channel data
     */
    public void setPayload(byte[] payload) {
        System.arraycopy(payload, 0, rawPacket, 126, payloadSize);
    }

    /**
     * set payload data
     *
     * @param payload
     *            byte array containing DMX channel data
     * @param payloadSize
     *            length of data (no. of channels)
     */
    public void setPayload(byte[] payload, int payloadSize) {
        if (payloadSize != this.payloadSize) {
            setPayloadSize(payloadSize);
        }
        setPayload(payload);
    }

    /**
     * get packet for transmission
     *
     * @return
     *         byte array with raw packet data
     */
    public byte[] getRawPacket() {
        return rawPacket;
    }

    /**
     * get packet length
     *
     * @return
     *         full packet length
     */
    public int getPacketLength() {
        return (126 + this.payloadSize);
    }

    /**
     * get universe of this packet
     *
     * @return
     *         universe number (1..63999)
     *
     */
    public int getUniverse() {
        return this.universe;
    }

    /**
     * get payload size
     *
     * @return
     *         number of DMX channels in this packet
     */
    public int getPayloadSize() {
        return this.payloadSize;
    }

    /**
     * get this packets broadcast node
     *
     * @return
     *         E1.31 node with broadcast address
     *
     */
    public E131Node getBroadcastNode() {
        return this.broadcastNode;
    }

}
