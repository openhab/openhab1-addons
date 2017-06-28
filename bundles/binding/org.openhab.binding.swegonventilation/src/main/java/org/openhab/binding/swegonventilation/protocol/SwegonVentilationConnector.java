/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;

/**
 * Base class for Swegon ventilation system communication.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public abstract class SwegonVentilationConnector {

    /**
     * Procedure for connect to Swegon ventilation system.
     *
     * @throws SwegonVentilationException
     */
    public abstract void connect() throws SwegonVentilationException;

    /**
     * Procedure for disconnect from Swegon ventilation system.
     *
     * @throws SwegonVentilationException
     */
    public abstract void disconnect() throws SwegonVentilationException;

    /**
     * Procedure for receiving datagram from Swegon ventilation system.
     *
     * @throws SwegonVentilationException
     */
    public abstract byte[] receiveDatagram() throws SwegonVentilationException;

    public int calculateCRC(byte[] bytes, int len) {
        return calculateCRC(bytes, 0, len);
    }

    public static int calculateCRC(byte[] bytes, int start, int end) {
        int crc = 0xFFFF; // initial value
        int polynomial = 0x1021; // 0001 0000 0010 0001 (0, 5, 12)

        for (int index = start; index < end; index++) {
            byte b = bytes[index];
            for (int i = 0; i < 8; i++) {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit) {
                    crc ^= polynomial;
                }
            }
        }

        crc &= 0xffff;

        return crc;
    }

    public int toInt(byte hb, byte lb) {
        return ((hb << 8) & 0xFF00) | (lb & 0xFF);
    }

}
