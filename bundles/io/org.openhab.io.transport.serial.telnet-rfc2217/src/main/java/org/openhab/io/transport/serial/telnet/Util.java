
/*
 * Copyright (C) 2010 Archie L. Cobbs. All rights reserved.
 *
 * $Id$
 */

package org.openhab.io.transport.serial.telnet;

import java.util.ArrayList;

/**
 * Utility methods.
 */
final class Util {

    static final String[] LINE_STATE_BITS = new String[] {
        "TIME_OUT",
        "TRANSFER_SHIFT_REGISTER_EMPTY",
        "TRANSFER_HOLDING_REGISTER_EMPTY",
        "BREAK_DETECT",
        "FRAMING_ERROR",
        "PARITY_ERROR",
        "OVERRUN_ERROR",
        "DATA_READY",
    };

    static final String[] MODEM_STATE_BITS = new String[] {
        "CARRIER_DETECT",
        "RING_INDICATOR",
        "DSR",
        "CTS",
        "DELTA_CARRIER_DETECT",
        "TRAILING_EDGE_RING_DETECTOR",
        "DELTA_DSR",
        "DELTA_CTS",
    };

    private Util() {
    }

    static String decodeBits(int value, String[] names) {
        ArrayList<String> list = new ArrayList<String>(8);
        for (int i = 0; i < 8; i++) {
            if ((value & (1 << (7 - i))) != 0)
                list.add(names[i]);
        }
        if (list.isEmpty())
            return "(none)";
        names = list.toArray(new String[list.size()]);
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < names.length; i++) {
            if (i > 0)
                buf.append(' ');
            buf.append(names[i]);
        }
        return buf.toString();
    }

    static String rawBytes(int[] data, int off, int len) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0)
                buf.append(' ');
            buf.append(String.format("0x%02x", data[off + i]));
        }
        return buf.toString();
    }
}

