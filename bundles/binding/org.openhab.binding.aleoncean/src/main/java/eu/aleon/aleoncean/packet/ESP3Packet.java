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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.aleon.aleoncean.packet.response.Response;
import eu.aleon.aleoncean.packet.response.UnknownResponseException;
import eu.aleon.aleoncean.util.CRC8;

/**
 * Representation of a ESP3 packet.
 *
 * We save only the real data of a ESP3 packet.
 * The necessary raw data is created if it is necessary (CRC8, ...).
 *
 * ESP3 package structure:
 *
 * 0; 1 byte: sync byte
 * 1; 2 byte: header: data length
 * 3; 1 byte: header: optional length
 * 4; 1 byte: header: package type
 * 5; 1 byte: CRC8 header
 * 6; x byte: data
 * 6 + x; y byte: optional data
 * 6 + x + y; 1 byte: CRC8 data (+ optional data)
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ESP3Packet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ESP3Packet.class);

    public static final int MAX_RAW_SIZE = 1 /* sync byte */
                                           + 2 + 2 + 1 /* header */
                                           + 1 /* CRC8 header */
                                           + 255 * 255 /* max data length */
                                           + 255 /* max optional length */
                                           + 1 /* CRC8 data ( + optional data) */;

    public static final byte SYNC_BYTE = (byte) 0x55;

    public static final int HEADER_START = 1;
    public static final int HEADER_SIZE = 4;
    public static final int POS_SYNC_BYTE = 0;
    public static final int POS_DATA_LENGTH_HIGH = 1;
    public static final int POS_DATA_LENGTH_LOW = 2;
    public static final int POS_OPTIONAL_LENGTH = 3;
    public static final int POS_PACKET_TYPE = 4;
    public static final int POS_CRC8_HEADER = 5;
    public static final int DATA_START = 6;

    private byte packetType = PacketType.UNSET;

    private byte[] data = null;

    private byte[] optionalData = null;

    /**
     * Create a new empty ESP3 packet.
     *
     */
    public ESP3Packet() {
    }

    public ESP3Packet(final byte packetType) {
        setPacketType(packetType);
    }

    /**
     * Fill the ESP3 packet by using the network raw data.
     *
     * The raw data must begin with the sync byte and end with the data CRC8.
     * The raw data must also represents a valid ESP3 packet data.
     *
     * @param raw The raw data representing a valid ESP3 packet.
     */
    public void fillFromRaw(final byte[] raw) {
        /*
         * We save only data, that could not be restored.
         * We have to call setData(...), setOptionalData(...), etc. so a
         * derived class could extract the data, that is necessary to generate
         * a byte stream later.
         */

        setPacketType(getPacketType(raw));

        final int dataStart = DATA_START;
        final int dataLen = getLength(raw[POS_DATA_LENGTH_HIGH], raw[POS_DATA_LENGTH_LOW]);
        final int optionalStart = dataStart + dataLen;
        final int optionalLen = getLength(raw[POS_OPTIONAL_LENGTH]);
        byte[] tmp;

        tmp = new byte[dataLen];
        System.arraycopy(raw, dataStart, tmp, 0, dataLen);
        setData(tmp);

        tmp = new byte[optionalLen];
        System.arraycopy(raw, optionalStart, tmp, 0, optionalLen);
        setOptionalData(tmp);
    }

    public byte[] generateRaw() {
        final byte[] rawData = getData();
        byte[] rawOptionalData = getOptionalData();
        if (rawOptionalData == null) {
            rawOptionalData = new byte[0];
        }

        final int rawLength = 1 /* sync. byte */
                              + 2 /* header: data length */
                              + 1 /* header: optional length */
                              + 1 /* header: package type */
                              + 1 /* CRC8 header */
                              + rawData.length /* the data (variable length) */
                              + rawOptionalData.length /* the optional data (variable length) */
                              + 1 /* CRC8 data (+ optional data) */;

        final CRC8 crc = new CRC8();

        final byte[] raw = new byte[rawLength];
        final ByteBuffer bb = ByteBuffer.wrap(raw);
        bb.order(ByteOrder.BIG_ENDIAN);

        bb.put(SYNC_BYTE);

        bb.putShort((short) rawData.length);
        bb.put((byte) rawOptionalData.length);
        bb.put(getPacketType());

        crc.update(raw, HEADER_START, HEADER_SIZE);
        bb.put(crc.getValue());
        crc.reset();

        bb.put(rawData);
        crc.update(rawData);

        bb.put(rawOptionalData);
        crc.update(rawOptionalData);

        bb.put(crc.getValue());

        return raw;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(final byte[] data) {
        this.data = data;
    }

    public byte[] getOptionalData() {
        return optionalData;
    }

    public void setOptionalData(final byte[] optionalData) {
        this.optionalData = optionalData;
    }

    public Response inspectResponsePacket(final ResponsePacket packet) throws UnknownResponseException {
        throw new UnknownResponseException();
    }

    public byte getPacketType() {
        return packetType;
    }

    public final void setPacketType(final byte packetType) {
        this.packetType = packetType;
    }

    private static int getLength(final byte high, final byte low) {
        return ((((int) high) & 0xFF) << 8) + (((int) low) & 0xFF);
    }

    private static int getLength(final byte b) {
        return ((int) b) & 0xFF;
    }

    public static byte getPacketType(final byte[] raw) {
        return raw[POS_PACKET_TYPE];
    }

    public static boolean checkRawData(final byte[] raw) {
        if (raw[POS_SYNC_BYTE] != SYNC_BYTE) {
            LOGGER.debug("Sync byte failure.");
            return false;
        }

        final CRC8 crcHeader = new CRC8();
        final CRC8 crcData = new CRC8();
        final int lenData = getLength(raw[POS_DATA_LENGTH_HIGH], raw[POS_DATA_LENGTH_LOW]);
        final int lenOptional = getLength(raw[POS_OPTIONAL_LENGTH]);

        crcHeader.update(raw, HEADER_START, HEADER_SIZE);
        if (crcHeader.getValue() != raw[POS_CRC8_HEADER]) {
            LOGGER.debug("CRC8 header differs.");
            return false;
        }

        crcData.update(raw, DATA_START, lenData + lenOptional);
        if (crcData.getValue() != raw[DATA_START + lenData + lenOptional]) {
            LOGGER.debug("CRC8 data differs.");
            return false;
        }

        return true;
    }

    @Override
    public String toString() {
        byte[] rawData = getData();
        byte[] rawOptionalData = getOptionalData();
        if (rawData == null) {
            rawData = new byte[0];
        }
        if (rawOptionalData == null) {
            rawOptionalData = new byte[0];
        }

        return String.format(
                "ESP3Packet{packetType=0x%02X, dataLen=%d, optionalDataLen=%d, data=%s, optionalData=%s}",
                getPacketType(), rawData.length, rawOptionalData.length, Arrays.toString(rawData), Arrays.toString(rawOptionalData)
        );
    }

}
