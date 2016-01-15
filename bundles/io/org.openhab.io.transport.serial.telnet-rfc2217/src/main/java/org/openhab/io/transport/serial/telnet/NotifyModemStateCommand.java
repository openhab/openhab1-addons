
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.NOTIFY_MODEMSTATE;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;

/**
 * RFC 2217 {@code NOTIFY-MODEMSTATE} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class NotifyModemStateCommand extends ComPortCommand {

    private int modemState;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length != 3
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#NOTIFY_MODEMSTATE} (client or server)
     */
    public NotifyModemStateCommand(int[] bytes) {
        super("NOTIFY-MODEMSTATE", NOTIFY_MODEMSTATE, bytes);
        this.modemState = bytes[2];
    }

    /**
     * Encoding constructor.
     *
     * @param modemState modem state value
     * @param client true for the client-to-server command, false for the server-to-client command
     */
    public NotifyModemStateCommand(boolean client, int modemState) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? NOTIFY_MODEMSTATE : NOTIFY_MODEMSTATE + SERVER_OFFSET,
            modemState
        });
    }

    @Override
    public String toString() {
        return this.getName() + " " + Util.decodeBits(this.modemState, Util.MODEM_STATE_BITS);
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.caseNotifyModemState(this);
    }

    public int getModemState() {
        return this.modemState;
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

