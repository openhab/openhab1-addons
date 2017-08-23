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

import eu.aleon.aleoncean.packet.radio.userdata.eepd201.Unit;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA51201 extends UserData4BS {

    public static final int METER_READING_RAW_MIN = 0;
    public static final int METER_READING_RAW_MAX = 16777215;

    public static final int TARIF_INFO_MIN = 0;
    public static final int TARIF_INFO_MAX = 15;

    public enum DataType {
        CURRENT_VALUE,
        CUMULATIVE_VALUE
    }

    public UserDataEEPA51201(final byte[] eepData) {
        super(eepData);
    }

    public double getMeterReading() {
        return (double) getMeterReadingRaw() / getDivisor();
    }

    public int getMeterReadingRaw() {
        final long value = getDataRange(3, 7, 1, 0);
        return (int) value;
    }

    public void setMeterReadingRaw(final int value) throws UserDataScaleValueException {
        if (value < METER_READING_RAW_MIN || value > METER_READING_RAW_MAX) {
            throw new UserDataScaleValueException(String.format(
                    "Value does not fit in range (min: %d, max: %d, value: %d)", METER_READING_RAW_MIN,
                    METER_READING_RAW_MAX, value));
        }
        setDataRange(value, 3, 7, 1, 0);
    }

    public int getTarifInfo() {
        final long value = getDataRange(0, 7, 0, 4);
        return (int) value;
    }

    public void setTarifInfo(final int value) throws UserDataScaleValueException {
        if (value < TARIF_INFO_MIN || value > TARIF_INFO_MAX) {
            throw new UserDataScaleValueException(String.format(
                    "Value does not fit in range (min: %d, max: %d, value: %d)", TARIF_INFO_MIN, TARIF_INFO_MAX, value));
        }
        setDataRange(value, 0, 7, 0, 4);
    }

    public DataType getDataType() {
        if (getDataBit(0, 2) == 0) {
            return DataType.CUMULATIVE_VALUE;
        } else {
            return DataType.CURRENT_VALUE;
        }
    }

    public void setDataType(final DataType dataType) throws UserDataScaleValueException {
        switch (dataType) {
            case CUMULATIVE_VALUE:
                setDataBit(0, 2, 0);
                return;
            case CURRENT_VALUE:
                setDataBit(0, 2, 1);
                return;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled data type: %s", dataType.toString()));
        }
    }

    public Unit getUnit() {
        switch (getDataType()) {
            case CUMULATIVE_VALUE:
                return Unit.ENERGY_KWH;
            case CURRENT_VALUE:
                return Unit.POWER_W;
            default:
                // Handle default case, but this case will never be evaluated.
                return null;
        }
    }

    public int getDivisor() {
        final long value = getDataRange(0, 1, 0, 0);
        switch ((int) value) {
            case 0:
                return 1;
            case 1:
                return 10;
            case 2:
                return 100;
            case 3:
                return 1000;
            default:
                // Handle default case, but this case will never be evaluated.
                return 0;
        }
    }

    public void setDivisor(final int divisor) throws UserDataScaleValueException {
        switch (divisor) {
            case 1:
                setDataRange(0, 0, 1, 0, 0);
                break;
            case 10:
                setDataRange(1, 0, 1, 0, 0);
                break;
            case 100:
                setDataRange(2, 0, 1, 0, 0);
                break;
            case 1000:
                setDataRange(3, 0, 1, 0, 0);
                break;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled divisor: %d", divisor));
        }
    }

}
