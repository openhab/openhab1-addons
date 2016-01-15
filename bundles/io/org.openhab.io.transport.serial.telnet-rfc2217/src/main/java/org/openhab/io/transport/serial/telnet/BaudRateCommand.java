
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;
import static org.openhab.io.transport.serial.telnet.RFC2217.SET_BAUDRATE;

/**
 * RFC 2217 {@code SET-BAUDRATE} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class BaudRateCommand extends ComPortCommand {

    private int baudRate;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length that is too short or too long
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#SET_BAUDRATE} (client or server)
     */
    public BaudRateCommand(int[] bytes) {
        super("SET-BAUDRATE", SET_BAUDRATE, bytes);
        this.baudRate = ((bytes[2] & 0xff) << 24) | ((bytes[3] & 0xff) << 16) | ((bytes[4] & 0xff) << 8) | (bytes[5] & 0xff);
    }

    /**
     * Encoding constructor.
     *
     * @param baudRate baud rate
     * @param client true for the client-to-server command, false for the server-to-client command
     */
    public BaudRateCommand(boolean client, int baudRate) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? SET_BAUDRATE : SET_BAUDRATE + SERVER_OFFSET,
            (baudRate >> 24) & 0xff,
            (baudRate >> 16) & 0xff,
            (baudRate >> 8) & 0xff,
            baudRate & 0xff,
        });
    }

    @Override
    public String toString() {
        return this.getName() + " " + this.baudRate;
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.caseBaudRate(this);
    }

    public int getBaudRate() {
        return this.baudRate;
    }

    @Override
    int getMinPayloadLength() {
        return 4;
    }

    @Override
    int getMaxPayloadLength() {
        return 4;
    }
}

