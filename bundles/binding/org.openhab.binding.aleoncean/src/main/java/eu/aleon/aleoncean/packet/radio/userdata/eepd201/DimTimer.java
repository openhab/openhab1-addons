/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet.radio.userdata.eepd201;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum DimTimer {

    NOT_USED((byte) 0x0),
    TIMER_0_5_SEC((byte) 0x01),
    TIMER_1_0_SEC((byte) 0x02),
    TIMER_1_5_SEC((byte) 0x03),
    TIMER_2_0_SEC((byte) 0x04),
    TIMER_2_5_SEC((byte) 0x05),
    TIMER_3_0_SEC((byte) 0x06),
    TIMER_3_5_SEC((byte) 0x07),
    TIMER_4_0_SEC((byte) 0x08),
    TIMER_4_5_SEC((byte) 0x09),
    TIMER_5_0_SEC((byte) 0x0A),
    TIMER_5_5_SEC((byte) 0x0B),
    TIMER_6_0_SEC((byte) 0x0C),
    TIMER_6_5_SEC((byte) 0x0D),
    TIMER_7_0_SEC((byte) 0x0E),
    TIMER_7_5_SEC((byte) 0x0F);

    private final byte value;

    public static DimTimer fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return DimTimer.NOT_USED;
            case 0x01:
                return DimTimer.TIMER_0_5_SEC;
            case 0x02:
                return DimTimer.TIMER_1_0_SEC;
            case 0x03:
                return DimTimer.TIMER_1_5_SEC;
            case 0x04:
                return DimTimer.TIMER_2_0_SEC;
            case 0x05:
                return DimTimer.TIMER_2_5_SEC;
            case 0x06:
                return DimTimer.TIMER_3_0_SEC;
            case 0x07:
                return DimTimer.TIMER_3_5_SEC;
            case 0x08:
                return DimTimer.TIMER_4_0_SEC;
            case 0x09:
                return DimTimer.TIMER_4_5_SEC;
            case 0x0A:
                return DimTimer.TIMER_5_0_SEC;
            case 0x0B:
                return DimTimer.TIMER_5_5_SEC;
            case 0x0C:
                return DimTimer.TIMER_6_0_SEC;
            case 0x0D:
                return DimTimer.TIMER_6_5_SEC;
            case 0x0E:
                return DimTimer.TIMER_7_0_SEC;
            case 0x0F:
                return DimTimer.TIMER_7_5_SEC;
            default:
                return DimTimer.NOT_USED;
        }
    }

    private DimTimer(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }
}
