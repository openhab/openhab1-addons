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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public enum DimValue {

    SWITCH_TO_NEW_OUT_VALUE((byte) 0x00),
    DIM_TO_NEW_OUT_VALUE_TIMER1((byte) 0x01),
    DIM_TO_NEW_OUT_VALUE_TIMER2((byte) 0x02),
    DIM_TO_NEW_OUT_VALUE_TIMER3((byte) 0x03),
    STOP_DIMMING((byte) 0x04),
    UNDEF((byte) 0x07);

    private static final Logger LOGGER = LoggerFactory.getLogger(DimValue.class);

    public static DimValue fromByte(final byte value) {
        switch (value) {
            case 0x00:
                return DimValue.SWITCH_TO_NEW_OUT_VALUE;
            case 0x01:
                return DimValue.DIM_TO_NEW_OUT_VALUE_TIMER1;
            case 0x02:
                return DimValue.DIM_TO_NEW_OUT_VALUE_TIMER2;
            case 0x03:
                return DimValue.DIM_TO_NEW_OUT_VALUE_TIMER3;
            case 0x04:
                return DimValue.STOP_DIMMING;
            default:
                LOGGER.warn("Invalid DimValue: {}", value);
                return DimValue.UNDEF;
        }
    }

    final byte value;

    private DimValue(final byte value) {
        this.value = value;
    }

    public byte toByte() {
        return value;
    }
}
