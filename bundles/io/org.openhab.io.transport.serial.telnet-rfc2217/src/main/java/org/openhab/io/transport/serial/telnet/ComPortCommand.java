
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import java.util.Arrays;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;

/**
 * Superclass for RFC 2217 commands.
 *
 * <p>
 * Instances of this class (and all subclasses) are immutable.
 * </p>
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public abstract class ComPortCommand {

    final String name;
    final int[] bytes;

    /**
     * Constructor.
     *
     * @param name human readable name of this command
     * @param command required {@code COM-PORT-OPTION} command byte value (must be the client-to-server value)
     * @param bytes encoded command starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length that is too short or too long
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code command} is greater than or equal to {@link RFC2217#SERVER_OFFSET}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@code command}
     *  or {@code command} + {@link RFC2217#SERVER_OFFSET}
     */
    protected ComPortCommand(String name, int command, int[] bytes) {
        this.name = name;
        int minLength = 2 + this.getMinPayloadLength();
        int maxLength = 2 + this.getMaxPayloadLength();
        if (bytes.length < minLength || bytes.length > maxLength) {
            throw new IllegalArgumentException("command " + command + " length = "
              + bytes.length + " is not in the range " + minLength + ".." + maxLength);
        }
        this.bytes = bytes.clone();                 // maintain immutability
        if (this.bytes[0] != COM_PORT_OPTION)
            throw new IllegalArgumentException("not a COM-PORT-OPTION");
        if (command >= SERVER_OFFSET)
            throw new IllegalArgumentException("invalid command " + command);
        if (this.getCommand() != command && this.getCommand() != command + SERVER_OFFSET)
            throw new IllegalArgumentException("not a " + name + " option");
    }

    /**
     * Determine if this command is client-to-server or server-to-client.
     *
     * @return true if this command is sent from the server to the client, false for the opposite
     */
    public final boolean isServerCommand() {
        return this.getCommand() >= SERVER_OFFSET;
    }

    /**
     * Get the encoding of this instance.
     *
     * @return encoding starting with {@code COM-PORT-OPTION}
     */
    public final int[] getBytes() {
        return this.bytes.clone();                  // maintain immutability
    }

    /**
     * Get the command byte.
     *
     * @return RFC 2217-defined byte value for this command
     */
    public final int getCommand() {
        return this.bytes[1] & 0xff;
    }

    /**
     * Get the human-readable name of this option.
     */
    public String getName() {
        return this.name + (this.isServerCommand() ? "[S]" : "[C]");
    }

    /**
     * Get the human-readable description of this option.
     */
    @Override
    public abstract String toString();

    /**
     * Apply visitor pattern.
     *
     * @param sw visitor switch handler
     */
    public abstract void visit(ComPortCommandSwitch sw);

    /**
     * Get the option payload as bytes.
     */
    byte[] getPayload() {
        byte[] buf = new byte[this.bytes.length - 2];
        for (int i = 2; i < bytes.length; i++)
            buf[i - 2] = (byte)this.bytes[i];
        return buf;
    }

    /**
     * Get minimum required length of the payload of this command.
     */
    abstract int getMinPayloadLength();

    /**
     * Get maximum required length of the payload of this command.
     */
    abstract int getMaxPayloadLength();

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass())
            return false;
        ComPortCommand that = (ComPortCommand)obj;
        return Arrays.equals(this.bytes, that.bytes);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.bytes.length; i++)
            hash = hash * 37 + this.bytes[i];
        return hash;
    }
}

