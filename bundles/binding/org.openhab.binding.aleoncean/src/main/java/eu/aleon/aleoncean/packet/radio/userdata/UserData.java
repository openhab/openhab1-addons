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
package eu.aleon.aleoncean.packet.radio.userdata;

import java.util.Arrays;
import eu.aleon.aleoncean.packet.RadioPacket;
import eu.aleon.aleoncean.util.Bits;
import eu.aleon.aleoncean.util.CalculationUtil;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class UserData {

    private byte[] userData;

    public UserData(final int size) {
        userData = new byte[size];
    }

    public UserData(final byte[] data) {
        this.userData = data;
    }

    public byte[] getUserData() {
        return userData;
    }

    public void setUserData(final byte[] userData) {
        this.userData = userData;
    }

    protected int convPosDbToReal(final int dbPos) {
        return convPosDbToReal(userData, dbPos);
    }

    public static int convPosDbToReal(final byte[] userData, final int dbPos) {
        return userData.length - 1 - dbPos;
    }

    protected byte getDb(final int dbPos) {
        return getDb(userData, dbPos);
    }

    public static byte getDb(final byte[] userData, final int dbPos) {
        return userData[convPosDbToReal(userData, dbPos)];
    }

    protected void setDb(final int dbPos, final byte value) {
        setDb(userData, dbPos, value);
    }

    public static void setDb(final byte[] userData, final int dbPos, final byte value) {
        userData[convPosDbToReal(userData, dbPos)] = value;
    }

    protected int getDataBit(final int db, final int bit) {
        return getDataBit(userData, db, bit);
    }

    public static int getDataBit(final byte[] userData, final int db, final int bit) {
        return Bits.getBit(getDb(userData, db), bit);
    }

    protected void setDataBit(final int db, final int bit, final boolean value) {
        final int pos = convPosDbToReal(db);
        userData[pos] = Bits.setBit(userData[pos], bit, value);
    }

    protected void setDataBit(final int db, final int bit, final int value) {
        final int pos = convPosDbToReal(db);
        userData[pos] = Bits.setBit(userData[pos], bit, value);
    }

    protected long getDataRange(final int startDB, final int startBit, final int endDB, final int endBit) {
        return getDataRange(userData, startDB, startBit, endDB, endBit);
    }

    public static long getDataRange(final byte[] userData, final int startDB, final int startBit, final int endDB, final int endBit) {
        assert startDB >= endDB || (startDB == endDB && startBit >= endBit);
        assert startDB <= userData.length - 1;

        final int realStartByte = convPosDbToReal(userData, startDB);
        final int realEndByte = convPosDbToReal(userData, endDB);

        return Bits.getBitsFromBytes(userData, realStartByte, startBit, realEndByte, endBit);
    }

    protected void setDataRange(final long value, final int startDB, final int startBit, final int endDB, final int endBit) {
        // e.g. db3.5 ... db2.7
        assert startDB >= endDB || (startDB == endDB && startBit >= endBit);
        assert startDB <= userData.length - 1;

        final int realStartByte = convPosDbToReal(startDB);
        final int realEndByte = convPosDbToReal(endDB);

        Bits.setBitsOfBytes(value, userData, realStartByte, startBit, realEndByte, endBit);
    }

    /**
     * Extract a value of a given bit range and convert it to a scaled one.
     *
     * @param startDB  The data byte (EEP order) the value extraction should be start.
     * @param startBit The bit of the start byte the value extraction should be start.
     * @param endDB    The data byte (EEP order) the value extraction should be end.
     * @param endBit   The bit of the end byte the value extraction should be end.
     * @param rangeMin The lower limit of the range.
     * @param rangeMax The upper limit of the range.
     * @param scaleMin The lower limit of the scaled value.
     * @param scaleMax The upper limit of the scaled value.
     * @return Return a scaled value that does fit in given range.
     * @throws UserDataScaleValueException This exception is raised if the value extracted from given bit range does
     *                                     not fit in range.
     */
    protected double getScaleValue(final int startDB, final int startBit, final int endDB, final int endBit,
                                   final long rangeMin, final long rangeMax,
                                   final double scaleMin, final double scaleMax)
            throws UserDataScaleValueException {
        final long raw = getDataRange(startDB, startBit, endDB, endBit);
        return getScaleValue(raw, rangeMin, rangeMax, scaleMin, scaleMax);
    }

    /**
     * Convert a raw value to a scaled one with respect ranges.
     *
     * @param raw      The value that should be scaled.
     * @param rangeMin The lower limit of the range.
     * @param rangeMax The upper limit of the range.
     * @param scaleMin The lower limit of the scaled value.
     * @param scaleMax The upper limit of the scaled value.
     * @return Return a scaled value that does fit in given range.
     * @throws UserDataScaleValueException This exception is raised if the given value does not fit in range.
     */
    protected double getScaleValue(final long raw,
                                   final long rangeMin, final long rangeMax,
                                   final double scaleMin, final double scaleMax)
            throws UserDataScaleValueException {

        /*
         * The range could also be inverse (255..0 instead of 0..255), so we have to improve the check.
         */
        if (raw < rangeMin && raw < rangeMax
            || raw > rangeMin && raw > rangeMax) {
            throw new UserDataScaleValueException(String.format("The coded value does not fit in range (min: %d, max: %d, value: %d).", rangeMin, rangeMax, raw));
        }

        final double scale = CalculationUtil.rangeToScale(raw, rangeMin, rangeMax, scaleMin, scaleMax);
        return scale;
    }

    /**
     * Convert a value to a raw value with respect ranges.
     *
     * @param scale    The value that should be converted to a raw one.
     * @param scaleMin The lower limit of the value.
     * @param scaleMax The upper limit of the value.
     * @param rangeMin The lower limit of the raw value.
     * @param rangeMax The upper limit of the raw value.
     * @return Return a raw value that does fit in given range.
     * @throws UserDataScaleValueException This exception is raised if the given value does not fit in range.
     */
    protected long getRangeValue(final double scale,
                                 final double scaleMin, final double scaleMax,
                                 final long rangeMin, final long rangeMax)
            throws UserDataScaleValueException {

        /*
         * The range could also be inverse (255..0 instead of 0..255), so we have to improve the check.
         */
        if (scale < scaleMin && scale < scaleMax
            || scale > scaleMin && scale > scaleMax) {
            throw new UserDataScaleValueException(String.format(
                    "The scale value does not fit in range (min: %f, max: %f, value: %f).",
                    scaleMin, scaleMax, scale));
        }

        final long raw = CalculationUtil.scaleToRange(scale, scaleMin, scaleMax, rangeMin, rangeMax);
        return raw;
    }

    @Override
    public String toString() {
        return String.format("UserData{data=%s}", Arrays.toString(userData));
    }

    public abstract RadioPacket generateRadioPacket();
}
