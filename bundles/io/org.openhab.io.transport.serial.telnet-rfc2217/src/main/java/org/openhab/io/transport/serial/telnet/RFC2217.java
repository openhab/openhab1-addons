
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;



/**
 * RFC 2217 constants and utility methods.
 *
 * @see <a href="http://tools.ietf.org/html/rfc2217">RFC 2217</a>
 */
public final class RFC2217 {

    // COM-PORT-OPTION telnet option
    public static final int COM_PORT_OPTION = 44;

    // COM-PORT-OPTION commands
    public static final int SIGNATURE = 0;
    public static final int SET_BAUDRATE = 1;
    public static final int SET_DATASIZE = 2;
    public static final int SET_PARITY = 3;
    public static final int SET_STOPSIZE = 4;
    public static final int SET_CONTROL = 5;
    public static final int NOTIFY_LINESTATE = 6;
    public static final int NOTIFY_MODEMSTATE = 7;
    public static final int FLOWCONTROL_SUSPEND = 8;
    public static final int FLOWCONTROL_RESUME = 9;
    public static final int SET_LINESTATE_MASK = 10;
    public static final int SET_MODEMSTATE_MASK = 11;
    public static final int PURGE_DATA = 12;
    public static final int SERVER_OFFSET = 100;

    // SET_DATASIZE values
    public static final int DATASIZE_REQUEST = 0;
    public static final int DATASIZE_5 = 5;
    public static final int DATASIZE_6 = 6;
    public static final int DATASIZE_7 = 7;
    public static final int DATASIZE_8 = 8;

    // SET_PARITY values
    public static final int PARITY_REQUEST = 0;
    public static final int PARITY_NONE = 1;
    public static final int PARITY_ODD = 2;
    public static final int PARITY_EVEN = 3;
    public static final int PARITY_MARK = 4;
    public static final int PARITY_SPACE = 5;

    // SET_STOPSIZE values
    public static final int STOPSIZE_REQUEST = 0;
    public static final int STOPSIZE_1 = 1;
    public static final int STOPSIZE_2 = 2;
    public static final int STOPSIZE_1_5 = 3;

    // SET_CONTROL values
    public static final int CONTROL_OUTBOUND_FLOW_REQUEST = 0;
    public static final int CONTROL_OUTBOUND_FLOW_NONE = 1;
    public static final int CONTROL_OUTBOUND_FLOW_XON_XOFF = 2;
    public static final int CONTROL_OUTBOUND_FLOW_HARDWARE = 3;
    public static final int CONTROL_BREAK_REQUEST = 4;
    public static final int CONTROL_BREAK_ON = 5;
    public static final int CONTROL_BREAK_OFF = 6;
    public static final int CONTROL_DTR_REQUEST = 7;
    public static final int CONTROL_DTR_ON = 8;
    public static final int CONTROL_DTR_OFF = 9;
    public static final int CONTROL_RTS_REQUEST = 10;
    public static final int CONTROL_RTS_ON = 11;
    public static final int CONTROL_RTS_OFF = 12;
    public static final int CONTROL_INBOUND_FLOW_REQUEST = 13;
    public static final int CONTROL_INBOUND_FLOW_NONE = 14;
    public static final int CONTROL_INBOUND_FLOW_XON_XOFF = 15;
    public static final int CONTROL_INBOUND_FLOW_HARDWARE = 16;
    public static final int CONTROL_OUTBOUND_FLOW_DCD = 17;
    public static final int CONTROL_INBOUND_FLOW_DTR = 18;
    public static final int CONTROL_OUTBOUND_FLOW_DSR = 19;

    // SET_LINESTATE_MASK bit values
    public static final int LINESTATE_TIME_OUT = 0x80;
    public static final int LINESTATE_TRANSFER_SHIFT_REGISTER_EMPTY = 0x40;
    public static final int LINESTATE_TRANSFER_HOLDING_REGISTER_EMPTY = 0x20;
    public static final int LINESTATE_BREAK_DETECT = 0x10;
    public static final int LINESTATE_FRAMING_ERROR = 0x08;
    public static final int LINESTATE_PARITY_ERROR = 0x04;
    public static final int LINESTATE_OVERRUN_ERROR = 0x02;
    public static final int LINESTATE_DATA_READY = 0x01;

    // SET_MODEMSTATE_MASK bit values
    public static final int MODEMSTATE_CARRIER_DETECT = 0x80;
    public static final int MODEMSTATE_RING_INDICATOR = 0x40;
    public static final int MODEMSTATE_DSR = 0x20;
    public static final int MODEMSTATE_CTS = 0x10;
    public static final int MODEMSTATE_DELTA_CARRIER_DETECT = 0x08;
    public static final int MODEMSTATE_TRAILING_EDGE_RING_DETECTOR = 0x04;
    public static final int MODEMSTATE_DELTA_DSR = 0x02;
    public static final int MODEMSTATE_DELTA_CTS = 0x01;

    // PURGE_DATA values
    public static final int PURGE_DATA_RECEIVE_DATA_BUFFER = 0x01;
    public static final int PURGE_DATA_TRANSMIT_DATA_BUFFER = 0x02;
    public static final int PURGE_DATA_BOTH_DATA_BUFFERS = 0x03;

    private RFC2217() {
    }

    /**
     * Decode an RFC 2217 {@code COM-PORT-OPTION} command.
     *
     * @throws IllegalArgumentException if the bytes are not a valid encoded RFC 2217 {@link #COM_PORT_OPTION}
     */
    public static ComPortCommand decodeComPortCommand(int[] bytes) {
        if (bytes.length < 2)
            throw new IllegalArgumentException("length < 2");
        if (bytes[0] != COM_PORT_OPTION)
            throw new IllegalArgumentException("not a COM-PORT-OPTION (option = " + bytes[0] + ")");
        switch (bytes[1]) {
        case SIGNATURE:
        case SIGNATURE + SERVER_OFFSET:
            return new SignatureCommand(bytes);
        case SET_BAUDRATE:
        case SET_BAUDRATE + SERVER_OFFSET:
            return new BaudRateCommand(bytes);
        case SET_DATASIZE:
        case SET_DATASIZE + SERVER_OFFSET:
            return new DataSizeCommand(bytes);
        case SET_PARITY:
        case SET_PARITY + SERVER_OFFSET:
            return new ParityCommand(bytes);
        case SET_STOPSIZE:
        case SET_STOPSIZE + SERVER_OFFSET:
            return new StopSizeCommand(bytes);
        case SET_CONTROL:
        case SET_CONTROL + SERVER_OFFSET:
            return new ControlCommand(bytes);
        case NOTIFY_LINESTATE:
        case NOTIFY_LINESTATE + SERVER_OFFSET:
            return new NotifyLineStateCommand(bytes);
        case NOTIFY_MODEMSTATE:
        case NOTIFY_MODEMSTATE + SERVER_OFFSET:
            return new NotifyModemStateCommand(bytes);
        case FLOWCONTROL_SUSPEND:
        case FLOWCONTROL_SUSPEND + SERVER_OFFSET:
            return new FlowControlSuspendCommand(bytes);
        case FLOWCONTROL_RESUME:
        case FLOWCONTROL_RESUME + SERVER_OFFSET:
            return new FlowControlResumeCommand(bytes);
        case SET_LINESTATE_MASK:
        case SET_LINESTATE_MASK + SERVER_OFFSET:
            return new LineStateMaskCommand(bytes);
        case SET_MODEMSTATE_MASK:
        case SET_MODEMSTATE_MASK + SERVER_OFFSET:
            return new ModemStateMaskCommand(bytes);
        case PURGE_DATA:
        case PURGE_DATA + SERVER_OFFSET:
            return new PurgeDataCommand(bytes);
        default:
            throw new IllegalArgumentException("unrecognized COM-PORT-OPTION command " + bytes[1]);
        }
    }
}

