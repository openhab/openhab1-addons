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

import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.util.Bits;
import eu.aleon.aleoncean.util.CalculationUtil;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201CMD05 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_SET_MEASUREMENT;

    private static final int DATA_LENGTH = 6;

    // Report measurement
    private static final int OFFSET_RM = 8;
    private static final int LENGTH_RM = 1;

    // Reset measurement
    private static final int OFFSET_RE = 9;
    private static final int LENGTH_RE = 1;

    // Measurement mode
    private static final int OFFSET_EP = 10;
    private static final int LENGTH_EP = 1;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    // Measurement delta to be reported (LSB)
    private static final int OFFSET_MD_LSB = 16;
    private static final int LENGTH_MD_LSB = 4;

    // Unit
    private static final int OFFSET_UN = 21;
    private static final int LENGTH_UN = 3;

    // Measurement delta to be reposted (MSB)
    private static final int OFFSET_MD_MSB = 24;
    private static final int LENGTH_MD_MSB = 8;

    // Maximum time between two subsequent actuator
    private static final int OFFSET_MAT = 32;
    private static final int LENGTH_MAT = 8;
    private static final int MAT_RANGE_MIN = 0;
    private static final int MAT_RANGE_MAX = 255;
    public static final int MAT_SCALE_MIN = 10;
    public static final int MAT_SCALE_MAX = 2550;

    // Minimum time between two subsequent actuator
    private static final int OFFSET_MIT = 40;
    private static final int LENGTH_MIT = 8;
    private static final int MIT_RANGE_MIN = 0;
    private static final int MIT_RANGE_MAX = 255;
    public static final int MIT_SCALE_MIN = 0;
    public static final int MIT_SCALE_MAX = 255;

    public static final int MD_MIN = 0;
    public static final int MD_MAX = 4095;

    public UserDataEEPD201CMD05() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD05(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public ReportMeasurement getReportMeasurement() {
        return ReportMeasurement.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_RM, LENGTH_RM));
    }

    public void setReportMeasurement(final ReportMeasurement value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_RM, LENGTH_RM);
    }

    public ResetMeasurement getResetMeasurement() {
        return ResetMeasurement.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_RE, LENGTH_RE));
    }

    public void setResetMeasurement(final ResetMeasurement value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_RE, LENGTH_RE);
    }

    public MeasurementMode getMeasurementMode() {
        return MeasurementMode.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_EP, LENGTH_EP));
    }

    public void setMeasurementMode(final MeasurementMode value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_EP, LENGTH_EP);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
    }

    public Unit getUnit() {
        return Unit.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_UN, LENGTH_UN));
    }

    public void setUnit(final Unit value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_UN, LENGTH_UN);
    }

    public int getMeasurementDeltaToBeReported() {
        final int mdLsb4 = (int) Bits.getBitsFromBytes(getUserData(), OFFSET_MD_LSB, LENGTH_MD_LSB);
        final int mdMsb8 = (int) Bits.getBitsFromBytes(getUserData(), OFFSET_MD_MSB, LENGTH_MD_MSB);
        final int md = (mdMsb8 << 4) | mdLsb4;
        return md;
    }

    public void setMeasurementDeltaToBeReported(final int value) {
        final int md = CalculationUtil.fitInRange(value, MD_MIN, MD_MAX);
        final int mdLsb4 = md & 0xF;
        final int mdMsb8 = (md >> 4) & 0xFF;

        Bits.setBitsOfBytes(mdLsb4, getUserData(), OFFSET_MD_LSB, LENGTH_MD_LSB);
        Bits.setBitsOfBytes(mdMsb8, getUserData(), OFFSET_MD_MSB, LENGTH_MD_MSB);
    }

    public int getMaximumTimeBetweenTwoSubsequentActuator() throws UserDataScaleValueException {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_MAT, LENGTH_MAT);
        final int scaled = (int) getScaleValue(raw, MAT_RANGE_MIN, MAT_RANGE_MAX, MAT_SCALE_MIN, MAT_SCALE_MAX);
        return scaled;
    }

    /**
     * Set the maximum time between two subsequent actuator reports.
     *
     * The value must fit in range of from {@value #MAT_SCALE_MIN} to {@value #MAT_SCALE_MAX}.
     *
     * @param value The maximum time between two subsequent actuator reports in seconds (see range above).
     * @throws UserDataScaleValueException This exception if thrown, if the value does not fit in range (see above).
     */
    public void setMaximumTimeBetweenTwoSubsequentActuator(final int value) throws UserDataScaleValueException {
        final long raw = getRangeValue(value, MAT_SCALE_MIN, MAT_SCALE_MAX, MAT_RANGE_MIN, MAT_RANGE_MAX);
        Bits.setBitsOfBytes(raw, getUserData(), OFFSET_MAT, LENGTH_MAT);
    }

    public int getMinimumTimeBetweenTwoSubsequentActuator() throws UserDataScaleValueException {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_MIT, LENGTH_MIT);
        final int scaled = (int) getScaleValue(raw, MIT_RANGE_MIN, MIT_RANGE_MAX, MIT_SCALE_MIN, MIT_SCALE_MAX);
        return scaled;
    }

    /**
     * Set the minimum time between two subsequent actuator reports.
     *
     * The value must fit in range of from {@value #MIT_SCALE_MIN} to {@value #MIT_SCALE_MAX}.
     *
     * @param value The minimum time between two subsequent actuator reports in seconds (see range above).
     * @throws UserDataScaleValueException This exception if thrown, if the value does not fit in range (see above).
     */
    public void setMinimumTimeBetweenTwoSubsequentActuator(final int value) throws UserDataScaleValueException {
        final long raw = getRangeValue(value, MIT_SCALE_MIN, MIT_SCALE_MAX, MIT_RANGE_MIN, MIT_RANGE_MAX);
        Bits.setBitsOfBytes(raw, getUserData(), OFFSET_MIT, LENGTH_MIT);
    }

}
