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
package org.openhab.binding.upb.internal;

/**
 * Model for the first two bytes of UPB messages.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class ControlWord {

    private static final int TRANSMIT_COUNT_SHIFT = 2;
    private static final int TRANSMIT_COUNT_MASK = 0b00001100;
    private static final int TRANSMIT_SEQUENCE_MASK = 0b00000011;
    private static final int ACK_PULSE_MASK = 0b00010000;
    private static final int ID_PULSE_MASK = 0b00100000;
    private static final int ACK_MESSAGE_MASK = 0b01000000;
    private static final int REPEATER_COUNT_SHIFT = 5;
    private static final int REPEATER_COUNT_MASK = 0b01100000;
    private static final int PACKET_LENGTH_MASK = 0b00011111;
    private static final int LINK_MASK = 0b10000000;

    private byte one = 0;
    private byte two = 0;

    /**
     * Sets the two bytes of the control word.
     *
     * @param one
     *            the first byte.
     * @param two
     *            the second byte.
     */
    public void setBytes(byte one, byte two) {
        this.one = one;
        this.two = two;
    }

    /**
     * @return the two bytes of the control word.
     */
    public byte[] getBytes() {
        return new byte[] { two, one };
    }

    /**
     * @return the link
     */
    public boolean isLink() {
        return (two & LINK_MASK) > 0;
    }

    /**
     * @param link
     *            the link to set
     */
    public void setLink(boolean link) {
        two = (byte) (link ? two | LINK_MASK : two & ~LINK_MASK);
    }

    /**
     * @return the repeaterCount
     */
    public int getRepeaterCount() {
        return (two & REPEATER_COUNT_MASK) >> REPEATER_COUNT_SHIFT;
    }

    /**
     * @param repeaterCount
     *            the repeaterCount to set
     */
    public void setRepeaterCount(int repeaterCount) {
        two = (byte) (two | (repeaterCount << REPEATER_COUNT_SHIFT));
    }

    /**
     * @return the packetLength
     */
    public int getPacketLength() {
        return two & PACKET_LENGTH_MASK;
    }

    /**
     * @param packetLength
     *            the packetLength to set
     */
    public void setPacketLength(int packetLength) {
        two = (byte) (two | packetLength);
    }

    /**
     * @return the transmitCount
     */
    public int getTransmitCount() {
        return (one & TRANSMIT_COUNT_MASK) >> TRANSMIT_COUNT_SHIFT;
    }

    /**
     * @param transmitCount
     *            the transmitCount to set
     */
    public void setTransmitCount(int transmitCount) {
        one = (byte) (one | (transmitCount << TRANSMIT_COUNT_SHIFT));
    }

    /**
     * @return the transmitSequence
     */
    public int getTransmitSequence() {
        return one & TRANSMIT_SEQUENCE_MASK;
    }

    /**
     * @param transmitSequence
     *            the transmitSequence to set
     */
    public void setTransmitSequence(int transmitSequence) {
        one = (byte) (one | transmitSequence);
    }

    /**
     * @return the ackPulse
     */
    public boolean isAckPulse() {
        return (one & ACK_PULSE_MASK) > 0;
    }

    /**
     * @param ackPulse
     *            the ackPulse to set
     */
    public void setAckPulse(boolean ackPulse) {
        one = (byte) (ackPulse ? one | ACK_PULSE_MASK : one & ~ACK_PULSE_MASK);
    }

    /**
     * @return the idPulse
     */
    public boolean isIdPulse() {
        return (one & ID_PULSE_MASK) > 0;
    }

    /**
     * @param idPulse
     *            the idPulse to set
     */
    public void setIdPulse(boolean idPulse) {
        one = (byte) (idPulse ? one | ID_PULSE_MASK : one & ~ID_PULSE_MASK);
    }

    /**
     * @return the ackMessage
     */
    public boolean isAckMessage() {
        return (one & ACK_MESSAGE_MASK) > 0;
    }

    /**
     * @param ackMessage
     *            the ackMessage to set
     */
    public void setAckMessage(boolean ackMessage) {
        one = (byte) (ackMessage ? one | ACK_MESSAGE_MASK : one & ~ACK_MESSAGE_MASK);
    }
}
