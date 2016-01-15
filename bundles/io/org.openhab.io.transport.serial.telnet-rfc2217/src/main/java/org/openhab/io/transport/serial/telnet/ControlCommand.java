
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import static org.openhab.io.transport.serial.telnet.RFC2217.COM_PORT_OPTION;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_BREAK_OFF;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_BREAK_ON;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_BREAK_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_DTR_OFF;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_DTR_ON;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_DTR_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_INBOUND_FLOW_DTR;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_INBOUND_FLOW_HARDWARE;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_INBOUND_FLOW_NONE;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_INBOUND_FLOW_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_INBOUND_FLOW_XON_XOFF;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_DCD;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_DSR;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_HARDWARE;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_NONE;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_OUTBOUND_FLOW_XON_XOFF;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_RTS_OFF;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_RTS_ON;
import static org.openhab.io.transport.serial.telnet.RFC2217.CONTROL_RTS_REQUEST;
import static org.openhab.io.transport.serial.telnet.RFC2217.SERVER_OFFSET;
import static org.openhab.io.transport.serial.telnet.RFC2217.SET_CONTROL;

/**
 * RFC 2217 {@code SET-CONTROL} command.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public class ControlCommand extends ComPortCommand {

    private int control;

    /**
     * Decoding constructor.
     *
     * @param bytes encoded option starting with the {@code COM-PORT-OPTION} byte
     * @throws NullPointerException if {@code bytes} is null
     * @throws IllegalArgumentException if {@code bytes} has length != 3
     * @throws IllegalArgumentException if {@code bytes[0]} is not {@link RFC2217#COM_PORT_OPTION}
     * @throws IllegalArgumentException if {@code bytes[1]} is not {@link RFC2217#SET_CONTROL} (client or server)
     * @throws IllegalArgumentException if {@code bytes[2]} is not a valid RFC 2217 control value
     */
    public ControlCommand(int[] bytes) {
        super("SET-CONTROL", SET_CONTROL, bytes);
        this.control = bytes[2];
        switch (this.control) {
        case CONTROL_OUTBOUND_FLOW_REQUEST:
        case CONTROL_OUTBOUND_FLOW_NONE:
        case CONTROL_OUTBOUND_FLOW_XON_XOFF:
        case CONTROL_OUTBOUND_FLOW_HARDWARE:
        case CONTROL_BREAK_REQUEST:
        case CONTROL_BREAK_ON:
        case CONTROL_BREAK_OFF:
        case CONTROL_DTR_REQUEST:
        case CONTROL_DTR_ON:
        case CONTROL_DTR_OFF:
        case CONTROL_RTS_REQUEST:
        case CONTROL_RTS_ON:
        case CONTROL_RTS_OFF:
        case CONTROL_INBOUND_FLOW_REQUEST:
        case CONTROL_INBOUND_FLOW_NONE:
        case CONTROL_INBOUND_FLOW_XON_XOFF:
        case CONTROL_INBOUND_FLOW_HARDWARE:
        case CONTROL_OUTBOUND_FLOW_DCD:
        case CONTROL_INBOUND_FLOW_DTR:
        case CONTROL_OUTBOUND_FLOW_DSR:
            break;
        default:
            throw new IllegalArgumentException("invalid control value " + this.control);
        }
    }

    /**
     * Encoding constructor.
     *
     * @param command control command
     * @param client true for the client-to-server command, false for the server-to-client command
     * @throws IllegalArgumentException if {@code command} is not a valid RFC 2217 control value
     */
    public ControlCommand(boolean client, int command) {
        this(new int[] {
            COM_PORT_OPTION,
            client ? SET_CONTROL : SET_CONTROL + SERVER_OFFSET,
            command
        });
    }

    @Override
    public String toString() {
        String desc;
        switch (this.control) {
        case CONTROL_OUTBOUND_FLOW_REQUEST:
            desc = "OUTBOUND-FLOW-REQUEST";
            break;
        case CONTROL_OUTBOUND_FLOW_NONE:
            desc = "OUTBOUND-FLOW-NONE";
            break;
        case CONTROL_OUTBOUND_FLOW_XON_XOFF:
            desc = "OUTBOUND-FLOW-XON-XOFF";
            break;
        case CONTROL_OUTBOUND_FLOW_HARDWARE:
            desc = "OUTBOUND-FLOW-HARDWARE";
            break;
        case CONTROL_BREAK_REQUEST:
            desc = "OUTBOUND-BREAK-REQUEST";
            break;
        case CONTROL_BREAK_ON:
            desc = "OUTBOUND-BREAK-ON";
            break;
        case CONTROL_BREAK_OFF:
            desc = "OUTBOUND-BREAK-OFF";
            break;
        case CONTROL_DTR_REQUEST:
            desc = "OUTBOUND-DTR-REQUEST";
            break;
        case CONTROL_DTR_ON:
            desc = "OUTBOUND-DTR-ON";
            break;
        case CONTROL_DTR_OFF:
            desc = "OUTBOUND-DTR-OFF";
            break;
        case CONTROL_RTS_REQUEST:
            desc = "OUTBOUND-RTS-REQUEST";
            break;
        case CONTROL_RTS_ON:
            desc = "OUTBOUND-RTS-ON";
            break;
        case CONTROL_RTS_OFF:
            desc = "OUTBOUND-RTS-OFF";
            break;
        case CONTROL_INBOUND_FLOW_REQUEST:
            desc = "INBOUND-FLOW-REQUEST";
            break;
        case CONTROL_INBOUND_FLOW_NONE:
            desc = "INBOUND-FLOW-NONE";
            break;
        case CONTROL_INBOUND_FLOW_XON_XOFF:
            desc = "INBOUND-FLOW-XON-OFF";
            break;
        case CONTROL_INBOUND_FLOW_HARDWARE:
            desc = "INBOUND-FLOW-HARDWARE";
            break;
        case CONTROL_OUTBOUND_FLOW_DCD:
            desc = "OUTBOUND-FLOW-DCD";
            break;
        case CONTROL_INBOUND_FLOW_DTR:
            desc = "INBOUND-FLOW-DTR";
            break;
        case CONTROL_OUTBOUND_FLOW_DSR:
            desc = "OUTBOUND-FLOW-DSR";
            break;
        default:
            desc = "?";
            break;
        }
        return this.getName() + " " + desc;
    }

    @Override
    public void visit(ComPortCommandSwitch sw) {
        sw.caseControl(this);
    }

    public int getControl() {
        return this.control;
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

