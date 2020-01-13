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
package org.openhab.binding.zwave.internal.protocol.commandclass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.openhab.binding.zwave.internal.protocol.SerialMessage;
import org.openhab.binding.zwave.internal.protocol.ZWaveNode;
import org.openhab.binding.zwave.internal.protocol.commandclass.ZWaveCommandClass.CommandClass;

/**
 * Used only by {@link ZWaveSecurityCommandClass}
 *
 * ZWave security protocol that certain messages be "security encapsulated"
 * (that is, encrypted and signed). The first step to send a {@link SerialMessage}
 * securely is break down the payload into one or more security payload frames
 * that will then be queued up in {@link ZWaveSecurityCommandClass} to await
 * {@link ZWaveSecurityCommandClass#SECURITY_NONCE_REPORT} messages from the
 * device.
 *
 * @see {@link ZWaveSecurityCommandClass#queueMessageForEncapsulation}
 * @author Dave Badia
 * @since TODO
 */
public class ZWaveSecurityPayloadFrame {
    // TODO: DB instead of timeout here, tie this back to the sending of the nonce request?
    private static final long MESSAGE_EXPIRATION_MS = TimeUnit.MINUTES.toMillis(2);
    /**
     * The largest amount of payload we can fit into a single
     * {@link ZWaveSecurityPayloadFrame}. {@link SerialMessage} contents larger than this
     * must be split into multiple {@link ZWaveSecurityPayloadFrame}
     */
    private static final int SECURITY_PAYLOAD_ONE_PART_SIZE = 28;

    /**
     * Sequence byte is zero for messages that fit in a single frame
     */
    static final byte SEQUENCE_BYTE_FOR_SINGLE_FRAME_MESSAGE = 0;

    /**
     * Every <b>set</b> of multi frame messages must have unique sequence number.
     */
    private static final AtomicInteger sequenceCounter = new AtomicInteger(0);

    // metadata fields
    /**
     * The time at which this message should be discarded from the encapsulation
     * queue if no nonce reply has been received
     */
    private final long expirationTime;

    // data fields
    private final String logMessage;
    private final int partNumber;
    private final int totalParts;
    private final byte sequenceByte;
    private final byte[] partBytes;
    private final SerialMessage originalMessage;

    public static List<ZWaveSecurityPayloadFrame> convertToSecurityPayload(ZWaveNode node,
            SerialMessage messageToEncapsulate) {
        // We need to start with command class byte, so strip off node ID and length from beginning
        int copyLength = messageToEncapsulate.getMessagePayload().length - 2;
        byte[] payloadBuffer = new byte[copyLength];
        System.arraycopy(messageToEncapsulate.getMessagePayload(), 2, payloadBuffer, 0, copyLength);

        List<ZWaveSecurityPayloadFrame> list = new ArrayList<ZWaveSecurityPayloadFrame>();
        /*
         * The sequence data in a single byte. The entire byte is zero if the whole
         * message fit into one frame. If multiple frames are required:
         * 1st 2 bits: reserved, always 0
         * 3rd bit: second frame: 0 for 1st frame, 1 for second frame
         * 4th bit: sequenced: 0 if the entire message fits in one frame; 1 if more than 1 are required
         * last 4 bits: sequence counter - used to tell groups of sequenced messages apart.
         * Must be the same for part 1 and part 2 of a sequenced message
         */
        if (payloadBuffer.length > SECURITY_PAYLOAD_ONE_PART_SIZE) {
            // Use this byte for both parts, but OR it for each frame
            byte messageSequnceByte = (byte) sequenceCounter.getAndIncrement();
            // Message must be split into two parts
            byte[] partOneBuffer = new byte[SECURITY_PAYLOAD_ONE_PART_SIZE];
            System.arraycopy(payloadBuffer, 0, partOneBuffer, 0, SECURITY_PAYLOAD_ONE_PART_SIZE);
            byte partOneSequenceByte = (byte) (messageSequnceByte | 0x10); // Sequenced, first frame
            list.add(new ZWaveSecurityPayloadFrame(node, 1, 2, partOneBuffer, partOneSequenceByte,
                    messageToEncapsulate));

            byte partTwoSequenceByte = (byte) (messageSequnceByte | 0x30); // Sequenced, second frame
            int part2Length = payloadBuffer.length - SECURITY_PAYLOAD_ONE_PART_SIZE;
            byte[] partTwoBuffer = new byte[part2Length];
            System.arraycopy(payloadBuffer, SECURITY_PAYLOAD_ONE_PART_SIZE, partTwoBuffer, 0, part2Length);
            list.add(new ZWaveSecurityPayloadFrame(node, 2, 2, partTwoBuffer, partTwoSequenceByte,
                    messageToEncapsulate));
        } else {
            // The entire message can be encapsulated as one
            list.add(new ZWaveSecurityPayloadFrame(node, payloadBuffer, messageToEncapsulate));
        }
        return list;
    }

    private ZWaveSecurityPayloadFrame(ZWaveNode node, int partNumber, int totalParts, byte[] partBuffer,
            byte sequenceByte, SerialMessage originalMessage) {
        this.originalMessage = originalMessage;
        this.partNumber = partNumber;
        this.partBytes = partBuffer;
        this.totalParts = totalParts;
        this.sequenceByte = sequenceByte;
        this.expirationTime = System.currentTimeMillis() + MESSAGE_EXPIRATION_MS;
        // Replace the original payload bytes with ours
        String ourSerialMessageString = originalMessage.toString();
        int index = ourSerialMessageString.indexOf("payload");
        if (index > 0) {
            ourSerialMessageString = ourSerialMessageString.substring(0, index);
            ourSerialMessageString = new StringBuilder(ourSerialMessageString).append("payload = ")
                    .append(SerialMessage.bb2hex(partBytes)).toString();
        }
        this.logMessage = String.format("NODE %s: SecurityPayload (part %d of %d) for %s : %s", node.getNodeId(),
                partNumber, totalParts, CommandClass.getCommandClass(originalMessage.getMessageBuffer()[6] & 0xff),
                ourSerialMessageString);
    }

    private ZWaveSecurityPayloadFrame(ZWaveNode node, byte[] messageBuffer, SerialMessage originalMessage) {
        this(node, 1, 1, messageBuffer, SEQUENCE_BYTE_FOR_SINGLE_FRAME_MESSAGE, originalMessage);
    }

    protected int getTotalParts() {
        return totalParts;
    }

    protected String getLogMessage() {
        return logMessage;
    }

    protected byte[] getMessageBytes() {
        return partBytes;
    }

    protected int getPart() {
        return partNumber;
    }

    protected byte getSequenceByte() {
        return sequenceByte;
    }

    protected int getLength() {
        return partBytes.length;
    }

    @Override
    public String toString() {
        return logMessage;
    }

    protected long getExpirationTime() {
        return expirationTime;
    }

    protected SerialMessage getOriginalMessage() {
        return originalMessage;
    }
}
