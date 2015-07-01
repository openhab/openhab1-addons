
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_EVEN;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_MARK;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_NONE;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_ODD;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.PARITY_SPACE;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;
import static org.openhab.io.transport.serial.telnet.RFC2217.SET_PARITY;

/**
 * RFC 2217 {@code SET-PARITY} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class ParityCommand extends ComPortCommand {

    private int parity;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length != 3
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#SET_PARITY} (client or server)
     * @throws IllegalArgumentException if {@code bytes[2]} is not a valid RFC 2217 parity value
     */
    public ParityCommand(int[] bytes) {
        super("SET-PARITY", SET_PARITY, bytes);
        this.parity = bytes[2];
        switch (this.parity) {
        case PARITY_REQUEST:
        case PARITY_NONE:
        case PARITY_ODD:
        case PARITY_EVEN:
        case PARITY_MARK:
        case PARITY_SPACE:
            break;
        default:
            throw new IllegalArgumentException("invalid parity value " + this.parity);
        }
    }

    /**
     * Encoding constructor.
     *
     * @param parity parity value
     * @param client true for the client-to-server command, false for the server-to-client command
     * @throws IllegalArgumentException if {@code parity} is not a valid RFC 2217 parity value
     */
    public ParityCommand(boolean client, int parity) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? SET_PARITY : SET_PARITY + SERVER_OFFSET,
            parity
        });
    }

    @Override
    public String toString() {
        String desc;
        switch (this.parity) {
        case PARITY_REQUEST:
            desc = "REQUEST";
            break;
        case PARITY_NONE:
            desc = "NONE";
            break;
        case PARITY_ODD:
            desc = "ODD";
            break;
        case PARITY_EVEN:
            desc = "EVEN";
            break;
        case PARITY_MARK:
            desc = "MARK";
            break;
        case PARITY_SPACE:
            desc = "SPACE";
            break;
        default:
            desc = "?";
            break;
        }
        return this.getName() + " " + desc;
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.caseParity(this);
    }

    public int getParity() {
        return this.parity;
    }

    @Override
    int getMinPayloadLength() {
        return 1;
    }

    @Override
    int getMaxPayloadLength() {
        return 1;
    }
}

