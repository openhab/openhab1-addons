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

import eu.aleon.aleoncean.util.Bits;
import eu.aleon.aleoncean.util.CalculationUtil;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201CMD07 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_MEASUREMENT_RESPONSE;

    private static final int DATA_LENGTH = 6;

    // Unit
    private static final int OFFSET_UN = 8;
    private static final int LENGTH_UN = 3;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    // Measurement value
    private static final int OFFSET_MV = 16;
    private static final int LENGTH_MV = 32;
    public static final long MV_MIN = 0L;
    public static final long MV_MAX = 4294967295L;

    public UserDataEEPD201CMD07() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD07(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public Unit getUnit() {
        return Unit.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_UN, LENGTH_UN));
    }

    public void setUnit(final Unit value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_UN, LENGTH_UN);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
    }

    public long getMeasurementValue() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_MV, LENGTH_MV);
        return raw;
    }

    public void setMeasurementValue(final long value) {
        final long raw = CalculationUtil.fitInRange(value, MV_MIN, MV_MAX);
        Bits.setBitsOfBytes(raw, getUserData(), OFFSET_MV, LENGTH_MV);
    }

}
