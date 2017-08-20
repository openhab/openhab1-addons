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
package eu.aleon.aleoncean.packet.radio.userdata.eepa520;

import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.util.Bits;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA52001FromActuator extends UserData4BS {

    // Current value
    private static final int OFFSET_CV = 0;
    private static final int LENGTH_CV = 8;
    private static final int MIN_RANGE_CV = 0;
    private static final int MAX_RANGE_CV = 100;
    private static final int MIN_SCALE_CV = 0;
    private static final int MAX_SCALE_CV = 100;

    // Service on
    private static final int OFFSET_SO = 8;
    private static final int LENGTH_SO = 1;

    // Energy input enabled
    private static final int OFFSET_ENIE = 9;
    private static final int LENGTH_ENIE = 1;

    // Energy storage
    private static final int OFFSET_ES = 10;
    private static final int LENGTH_ES = 1;

    // Battery capacity
    private static final int OFFSET_BCAP = 11;
    private static final int LENGTH_BCAP = 1;

    // Contact, cover open
    private static final int OFFSET_CCO = 12;
    private static final int LENGTH_CCO = 1;

    // Failure temperature sensor, out off range
    private static final int OFFSET_FTS = 13;
    private static final int LENGTH_FTS = 1;

    // Detection, window open
    private static final int OFFSET_DWO = 14;
    private static final int LENGTH_DWO = 1;

    // Actuator obstructed
    private static final int OFFSET_ACO = 15;
    private static final int LENGTH_ACO = 1;

    // Temperature
    private static final int OFFSET_TMP = 16;
    private static final int LENGTH_TMP = 8;
    private static final int MIN_RANGE_TMP = 0;
    private static final int MAX_RANGE_TMP = 255;
    private static final int MIN_SCALE_TMP = 0;
    private static final int MAX_SCALE_TMP = 40;

    public UserDataEEPA52001FromActuator(final byte[] data) {
        super(data);
    }

    /**
     * Get the current valve position value in percent.
     *
     * @return Return the current valve position value in percent.
     * @throws UserDataScaleValueException if the value does not fit in defined range.
     */
    public int getCurrentValue() throws UserDataScaleValueException {
        // The getScaleValue function will already test the ranges (min, max).
        return (int) getScaleValue(Bits.getBitsFromBytes(getUserData(), OFFSET_CV, LENGTH_CV), MIN_RANGE_CV, MAX_RANGE_CV, MIN_SCALE_CV, MAX_SCALE_CV);
    }

    /**
     * Set the current valve position value in percent.
     *
     * @param percent The current valve position value in percent.
     * @throws UserDataScaleValueException if the value does not fit in defined range.
     */
    public void setCurrentValue(final int percent) throws UserDataScaleValueException {
        // The getRangeValue function will already test the ranges (min, max).
        Bits.setBitsOfBytes(getRangeValue(percent, MIN_SCALE_CV, MAX_SCALE_CV, MIN_RANGE_CV, MAX_RANGE_CV), getUserData(), OFFSET_CV, LENGTH_CV);
    }

    public boolean isServiceOn() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_SO, LENGTH_SO);
        return raw == 1;
    }

    public void setServiceOn(final boolean on) {
        Bits.setBitsOfBytes(on ? 1 : 0, getUserData(), OFFSET_SO, LENGTH_SO);
    }

    public boolean isEnergyInputEnabled() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_ENIE, LENGTH_ENIE);
        return raw == 1;
    }

    public void setEnergyInputEnabled(final boolean enabled) {
        Bits.setBitsOfBytes(enabled ? 1 : 0, getUserData(), OFFSET_ENIE, LENGTH_ENIE);
    }

    public boolean isEnergyStorageSufficientlyCharged() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_ES, LENGTH_ES);
        return raw == 1;
    }

    public void setEnergyStorageSufficientlyCharged(final boolean charged) {
        Bits.setBitsOfBytes(charged ? 1 : 0, getUserData(), OFFSET_ES, LENGTH_ES);
    }

    public boolean isBatteryCapacityLow() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_BCAP, LENGTH_BCAP);
        return raw == 0;
    }

    public void setBatteryCapacityLow(final boolean low) {
        Bits.setBitsOfBytes(low ? 0 : 1, getUserData(), OFFSET_BCAP, LENGTH_BCAP);
    }

    public boolean isCoverOpen() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_CCO, LENGTH_CCO);
        return raw == 1;
    }

    public void setCoverOpen(final boolean open) {
        Bits.setBitsOfBytes(open ? 1 : 0, getUserData(), OFFSET_CCO, LENGTH_CCO);
    }

    public boolean isTemperatureSensorOutOfRange() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_FTS, LENGTH_FTS);
        return raw == 1;
    }

    public void setTemperatureSensorOutOfRange(final boolean failed) {
        Bits.setBitsOfBytes(failed ? 1 : 0, getUserData(), OFFSET_FTS, LENGTH_FTS);
    }

    public boolean isWindowOpen() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_DWO, LENGTH_DWO);
        return raw == 1;
    }

    public void setWindowOpen(final boolean open) {
        Bits.setBitsOfBytes(open ? 1 : 0, getUserData(), OFFSET_DWO, LENGTH_DWO);
    }

    public boolean isActuatorObstructed() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_ACO, LENGTH_ACO);
        return raw == 1;
    }

    public void setActuatorObstructed(final boolean obstructed) {
        Bits.setBitsOfBytes(obstructed ? 1 : 0, getUserData(), OFFSET_ACO, LENGTH_ACO);
    }

    /**
     * Get the current temperature (linear).
     *
     * @return The current temperature (linear).
     * @throws UserDataScaleValueException if the value does not fit in defined range.
     */
    public double getTemperature() throws UserDataScaleValueException {
        // The getScaleValue function will already test the ranges (min, max).
        return getScaleValue(Bits.getBitsFromBytes(getUserData(), OFFSET_TMP, LENGTH_TMP), MIN_RANGE_TMP, MAX_RANGE_TMP, MIN_SCALE_TMP, MAX_SCALE_TMP);
    }

    /**
     * Set the current temperature (linear).
     *
     * @param temperature The current temperature (linear).
     * @throws UserDataScaleValueException if the value does not fit in defined range.
     */
    public void setTemperature(final double temperature) throws UserDataScaleValueException {
        // The getRangeValue function will already test the ranges (min, max).
        Bits.setBitsOfBytes(getRangeValue(temperature, MIN_SCALE_TMP, MAX_SCALE_TMP, MIN_RANGE_TMP, MAX_RANGE_TMP), getUserData(), OFFSET_TMP, LENGTH_TMP);
    }

}
