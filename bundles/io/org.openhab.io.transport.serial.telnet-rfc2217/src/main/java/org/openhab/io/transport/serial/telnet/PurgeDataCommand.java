
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.PURGE_DATA;
import static org.openhab.io.transport.serial.telnet.RFC2217.PURGE_DATA_BOTH_DATA_BUFFERS;
import static org.openhab.io.transport.serial.telnet.RFC2217.PURGE_DATA_RECEIVE_DATA_BUFFER;
import static org.openhab.io.transport.serial.telnet.RFC2217.PURGE_DATA_TRANSMIT_DATA_BUFFER;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;

/**
 * RFC 2217 {@code PURGE-DATA} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class PurgeDataCommand extends ComPortCommand {

    private int purgeData;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length != 3
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#PURGE_DATA} (client or server)
     * @throws IllegalArgumentException if {@code bytes[2]} is not a valid RFC 2217 purge data value
     */
    public PurgeDataCommand(int[] bytes) {
        super("PURGE-DATA", PURGE_DATA, bytes);
        this.purgeData = bytes[2];
        switch (this.purgeData) {
        case PURGE_DATA_RECEIVE_DATA_BUFFER:
        case PURGE_DATA_TRANSMIT_DATA_BUFFER:
        case PURGE_DATA_BOTH_DATA_BUFFERS:
            break;
        default:
            throw new IllegalArgumentException("invalid purge data value " + this.purgeData);
        }
    }

    /**
     * Encoding constructor.
     *
     * @param purgeData purge data value
     * @param client true for the client-to-server command, false for the server-to-client command
     * @throws IllegalArgumentException if {@code purgeData} is not a valid RFC 2217 purge data value
     */
    public PurgeDataCommand(boolean client, int purgeData) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? PURGE_DATA : PURGE_DATA + SERVER_OFFSET,
            purgeData
        });
    }

    @Override
    public String toString() {
        String desc;
        switch (this.purgeData) {
        case PURGE_DATA_RECEIVE_DATA_BUFFER:
            desc = "RECEIVE-DATA-BUFFER";
            break;
        case PURGE_DATA_TRANSMIT_DATA_BUFFER:
            desc = "TRANSMIT-DATA-BUFFER";
            break;
        case PURGE_DATA_BOTH_DATA_BUFFERS:
            desc = "BOTH-DATA-BUFFERS";
            break;
        default:
            desc = "?";
            break;
        }
        return this.getName() + " " + desc;
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.casePurgeData(this);
    }

    public boolean isPurgeReceiveDataBuffer() {
        return (this.purgeData & PURGE_DATA_RECEIVE_DATA_BUFFER) != 0;
    }

    public boolean isPurgeTransmitDataBuffer() {
        return (this.purgeData & PURGE_DATA_TRANSMIT_DATA_BUFFER) != 0;
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

