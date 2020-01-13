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

import javax.xml.bind.DatatypeConverter;

/**
 * Builder class for building UPB messages.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public final class MessageBuilder {

    private byte network;
    private byte source = -1;
    private byte destination;
    private byte[] commands;
    private boolean link = false;

    /**
     * Gets a new {@link MessageBuilder} instance.
     *
     * @return a new MessageBuilder.
     */
    public static MessageBuilder create() {
        return new MessageBuilder();
    }

    private MessageBuilder() {

    }

    /**
     * Sets where this message is for a device or a link.
     *
     * @param link
     *            set to true if this message is for a link.
     * @return the same MessageBuilder instance.
     */
    public MessageBuilder link(boolean link) {
        this.link = link;
        return this;
    }

    /**
     * Sets the UPB network of the message.
     *
     * @param network
     *            the network of the message.
     * @return the same MessageBuilder instance.
     */
    public MessageBuilder network(byte network) {
        this.network = network;
        return this;
    }

    /**
     * Sets the source id of the message (defaults to 0xFF).
     *
     * @param source
     *            the source if of the message.
     * @return the same MessageBuilder instance.
     */
    public MessageBuilder source(byte source) {
        this.source = source;
        return this;
    }

    /**
     * Sets the destination id of the message.
     *
     * @param destination
     *            the destination id.
     * @return the same MessageBuilder instance.
     */
    public MessageBuilder destination(byte destination) {
        this.destination = destination;
        return this;
    }

    /**
     * Sets the command and any arguments of the message.
     *
     * @param commands
     *            the command followed by any arguments.
     * @return the same MessageBuilder instance.
     */
    public MessageBuilder command(byte... commands) {
        this.commands = commands;
        return this;
    }

    /**
     * Builds the message as a HEX string.
     *
     * @return a HEX string of the message.
     */
    public String build() {
        ControlWord controlWord = new ControlWord();

        int packetLength = 6 + commands.length;

        controlWord.setPacketLength(packetLength);
        controlWord.setAckPulse(true);
        controlWord.setLink(link);

        int index = 2;
        byte[] bytes = new byte[packetLength];
        bytes[index++] = network;
        bytes[index++] = destination;
        bytes[index++] = source;

        // Copy in the header
        System.arraycopy(controlWord.getBytes(), 0, bytes, 0, 2);

        // Copy in the actual command and arguments being sent.
        System.arraycopy(commands, 0, bytes, index, commands.length);

        // Calculate the checksum
        // The checksum is the 2's complement of the sum.
        int sum = 0;
        for (byte b : bytes) {
            sum += b;
        }

        bytes[bytes.length - 1] = new Integer(-sum >>> 0).byteValue();

        return DatatypeConverter.printHexBinary(bytes);
    }
}
