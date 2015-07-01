
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.DATASIZE_5;
import static org.openhab.io.transport.serial.telnet.RFC2217.DATASIZE_6;
import static org.openhab.io.transport.serial.telnet.RFC2217.DATASIZE_7;
import static org.openhab.io.transport.serial.telnet.RFC2217.DATASIZE_8;
import static org.openhab.io.transport.serial.telnet.RFC2217.DATASIZE_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;
import static org.openhab.io.transport.serial.telnet.RFC2217.SET_DATASIZE;

/**
 * RFC 2217 {@code SET-DATASIZE} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class DataSizeCommand extends ComPortCommand {

    private int dataSize;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length != 3
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#SET_DATASIZE} (client or server)
     * @throws IllegalArgumentException if {@code bytes[2]} is not a valid RFC 2217 data size value
     */
    public DataSizeCommand(int[] bytes) {
        super("SET-DATASIZE", SET_DATASIZE, bytes);
        this.dataSize = bytes[2];
        switch (this.dataSize) {
        case DATASIZE_REQUEST:
        case DATASIZE_5:
        case DATASIZE_6:
        case DATASIZE_7:
        case DATASIZE_8:
            break;
        default:
            throw new IllegalArgumentException("invalid data size value " + this.dataSize);
        }
    }

    /**
     * Encoding constructor.
     *
     * @param dataSize data size value
     * @param client true for the client-to-server command, false for the server-to-client command
     * @throws IllegalArgumentException if {@code dataSize} is not a valid RFC 2217 data size value
     */
    public DataSizeCommand(boolean client, int dataSize) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? SET_DATASIZE : SET_DATASIZE + SERVER_OFFSET,
            dataSize
        });
    }

    @Override
    public String toString() {
        String desc;
        switch (this.dataSize) {
        case DATASIZE_REQUEST:
            desc = "REQUEST";
            break;
        case DATASIZE_5:
            desc = "5";
            break;
        case DATASIZE_6:
            desc = "6";
            break;
        case DATASIZE_7:
            desc = "7";
            break;
        case DATASIZE_8:
            desc = "8";
            break;
        default:
            desc = "?";
            break;
        }
        return this.getName() + " " + desc;
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.caseDataSize(this);
    }

    public int getDataSize() {
        return this.dataSize;
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

