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
public class UserDataEEPA52001ToActuator extends UserData4BS {

    // Valve position or temperature setpoint
    private static final int OFFSET_SP = 0;
    private static final int LENGTH_SP = 8;
    private static final int MIN_RANGE_SP_POS = 0;
    private static final int MAX_RANGE_SP_POS = 100;
    private static final int MIN_SCALE_SP_POS = 0;
    private static final int MAX_SCALE_SP_POS = 100;
    private static final int MIN_RANGE_SP_TMP = 0;
    private static final int MAX_RANGE_SP_TMP = 255;
    private static final int MIN_SCALE_SP_TMP = 0;
    private static final int MAX_SCALE_SP_TMP = 40;

    // Temperature from RCU
    private static final int OFFSET_TMP = 8;
    private static final int LENGTH_TMP = 8;
    private static final int MIN_RANGE_TMP = 255;
    private static final int MAX_RANGE_TMP = 0;
    private static final int MIN_SCALE_TMP = 0;
    private static final int MAX_SCALE_TMP = 40;

    // Run init sequence
    private static final int OFFSET_RIN = 16;
    private static final int LENGTH_RIN = 1;

    // Lift set
    private static final int OFFSET_LFS = 17;
    private static final int LENGTH_LFS = 1;

    // Valve open / maintenance
    private static final int OFFSET_VO = 18;
    private static final int LENGTH_VO = 1;

    // Valve closed
    private static final int OFFSET_VC = 19;
    private static final int LENGTH_VC = 1;

    // Summer bit, reduction of energy consumption
    private static final int OFFSET_SB = 20;
    private static final int LENGTH_SB = 1;

    // Set point selection
    private static final int OFFSET_SPS = 21;
    private static final int LENGTH_SPS = 1;

    // Set point inverse
    private static final int OFFSET_SPN = 22;
    private static final int LENGTH_SPN = 1;

    // Select function
    private static final int OFFSET_RCU = 23;
    private static final int LENGTH_RCU = 1;

    public UserDataEEPA52001ToActuator() {
        super();
    }

    public UserDataEEPA52001ToActuator(final byte[] data) {
        super(data);
    }

    /**
     * Get the current valve position.
     *
     * You have to check the set point selection, if valve position is used.
     *
     * @return Return the current valve position.
     * @throws UserDataScaleValueException if the data does not fit in expected range.
     */
    public int getValvePosition() throws UserDataScaleValueException {
        // The getScaleValue function will already test the ranges (min, max).
        return (int) getScaleValue(Bits.getBitsFromBytes(getUserData(), OFFSET_SP, LENGTH_SP), MIN_RANGE_SP_POS, MAX_RANGE_SP_POS, MIN_SCALE_SP_POS, MAX_SCALE_SP_POS);
    }

    /**
     * Set the current valve position.
     *
     * You have to set the set point selection to valve position mode.
     *
     * @param position The current valve position.
     * @throws UserDataScaleValueException if the data does not fit in expected range.
     */
    public void setValvePosition(final int position) throws UserDataScaleValueException {
        // The getRangeValue function will already test the ranges (min, max).
        Bits.setBitsOfBytes(getRangeValue(position, MIN_SCALE_SP_POS, MAX_SCALE_SP_POS, MIN_RANGE_SP_POS, MAX_RANGE_SP_POS), getUserData(), OFFSET_SP, LENGTH_SP);
    }

    /**
     * Get the current temperature set point.
     *
     * You have to check the set point selection, if temperature setpoint is used.
     *
     * @return Return the current temperature set point.
     * @throws UserDataScaleValueException if the data does not fit in expected range.
     */
    public double getTemperatureSetpoint() throws UserDataScaleValueException {
        // The getScaleValue function will already test the ranges (min, max).
        return getScaleValue(Bits.getBitsFromBytes(getUserData(), OFFSET_SP, LENGTH_SP), MIN_RANGE_SP_TMP, MAX_RANGE_SP_TMP, MIN_SCALE_SP_TMP, MAX_SCALE_SP_TMP);
    }

    /**
     * Set the current temperature set point.
     *
     * You have to set the set point selection to temperature mode.
     *
     * @param temperature The current temperature set point.
     * @throws UserDataScaleValueException if the data does not fit in expected range.
     */
    public void setTemperatureSetpoint(final double temperature) throws UserDataScaleValueException {
        // The getRangeValue function will already test the ranges (min, max).
        Bits.setBitsOfBytes(getRangeValue(temperature, MIN_SCALE_SP_TMP, MAX_SCALE_SP_TMP, MIN_RANGE_SP_TMP, MAX_RANGE_SP_TMP), getUserData(), OFFSET_SP, LENGTH_SP);
    }

    public double getCurrentTemperature() throws UserDataScaleValueException {
        // The getScaleValue function will already test the ranges (min, max).
        return getScaleValue(Bits.getBitsFromBytes(getUserData(), OFFSET_TMP, LENGTH_TMP), MIN_RANGE_TMP, MAX_RANGE_TMP, MIN_SCALE_TMP, MAX_SCALE_TMP);
    }

    public void setCurrentTemperature(final double temperature) throws UserDataScaleValueException {
        // The getRangeValue function will already test the ranges (min, max).
        Bits.setBitsOfBytes(getRangeValue(temperature, MIN_SCALE_TMP, MAX_SCALE_TMP, MIN_RANGE_TMP, MAX_RANGE_TMP), getUserData(), OFFSET_TMP, LENGTH_TMP);
    }

    public boolean isRunInitSequence() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_RIN, LENGTH_RIN);
        return raw == 1;
    }

    public void setRunInitSequence(final boolean initSequence) {
        Bits.setBitsOfBytes(initSequence ? 1 : 0, getUserData(), OFFSET_RIN, LENGTH_RIN);
    }

    public boolean isLiftSet() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_LFS, LENGTH_LFS);
        return raw == 1;
    }

    public void setLiftSet(final boolean liftSet) {
        Bits.setBitsOfBytes(liftSet ? 1 : 0, getUserData(), OFFSET_LFS, LENGTH_LFS);
    }

    public boolean isValveOpen() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_VO, LENGTH_VO);
        return raw == 1;
    }

    public void setValveOpen(final boolean open) {
        Bits.setBitsOfBytes(open ? 1 : 0, getUserData(), OFFSET_VO, LENGTH_VO);
    }

    public boolean isValveClosed() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_VC, LENGTH_VC);
        return raw == 1;
    }

    public void setValveClosed(final boolean closed) {
        Bits.setBitsOfBytes(closed ? 1 : 0, getUserData(), OFFSET_VC, LENGTH_VC);
    }

    public boolean isEnergyConsumptionReduced() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_SB, LENGTH_SB);
        return raw == 1;
    }

    public void setEnergyConsumotionReduced(final boolean reduced) {
        Bits.setBitsOfBytes(reduced ? 1 : 0, getUserData(), OFFSET_SB, LENGTH_SB);
    }

    public SetPointSelection getSetPointSelection() throws UserDataScaleValueException {
        final int raw = (int) Bits.getBitsFromBytes(getUserData(), OFFSET_SPS, LENGTH_SPS);
        switch (raw) {
            case 0:
                return SetPointSelection.VALVE_POSITION;
            case 1:
                return SetPointSelection.TEMPERATURE;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled set point selection: %d", raw));
        }
    }

    public void setSetPointSelection(final SetPointSelection setPointSelection) throws UserDataScaleValueException {
        switch (setPointSelection) {
            case VALVE_POSITION:
                Bits.setBitsOfBytes(0, getUserData(), OFFSET_SPS, LENGTH_SPS);
                return;
            case TEMPERATURE:
                Bits.setBitsOfBytes(1, getUserData(), OFFSET_SPS, LENGTH_SPS);
                return;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled set point selection: %s", setPointSelection.toString()));
        }
    }

    public boolean isSetPointInverse() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_SPN, LENGTH_SPN);
        return raw == 1;
    }

    public void setSetPointInverse(final boolean inverse) {
        Bits.setBitsOfBytes(inverse ? 1 : 0, getUserData(), OFFSET_SPN, LENGTH_SPN);
    }

    public Function getFunction() throws UserDataScaleValueException {
        final int raw = (int) Bits.getBitsFromBytes(getUserData(), OFFSET_RCU, LENGTH_RCU);
        switch (raw) {
            case 0:
                return Function.RCU;
            case 1:
                return Function.SERVICE_ON;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled function: %d", raw));
        }
    }

    public void setFunction(final Function function) throws UserDataScaleValueException {
        switch (function) {
            case RCU:
                Bits.setBitsOfBytes(0, getUserData(), OFFSET_RCU, LENGTH_RCU);
                return;
            case SERVICE_ON:
                Bits.setBitsOfBytes(1, getUserData(), OFFSET_RCU, LENGTH_RCU);
                return;
            default:
                throw new UserDataScaleValueException(String.format("Unhandled function: %s", function.toString()));
        }
    }
}
