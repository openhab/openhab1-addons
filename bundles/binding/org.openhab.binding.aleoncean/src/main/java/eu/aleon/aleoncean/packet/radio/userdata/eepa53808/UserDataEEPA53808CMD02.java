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
package eu.aleon.aleoncean.packet.radio.userdata.eepa53808;

import eu.aleon.aleoncean.packet.radio.userdata.UserDataScaleValueException;
import eu.aleon.aleoncean.util.Bits;

/**
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA53808CMD02 extends UserDataEEPA53808 {

    public static final byte CMD = UserDataEEPA53808.CMD_DIMMING;

    private static final int OFFSET_EDIM = 8;
    private static final int LENGTH_EDIM = 8;
    private static final int MIN_RANGE_EDIM = 0;
    private static final int MAX_RANGE_EDIM = 255;
    private static final int MIN_SCALE_EDIM_RELATIVE = 0;
    private static final int MAX_SCALE_EDIM_RELATIVE = 100;
    private static final int MIN_SCALE_EDIM_ABSOLUTE = 0;
    private static final int MAX_SCALE_EDIM_ABSOLUTE = 255;

    private static final int OFFSET_RMP = 16;
    private static final int LENGTH_RMP = 8;
    private static final int MIN_RANGE_RMP = 0;
    private static final int MAX_RANGE_RMP = 255;
    private static final int MIN_SCALE_RMP = 0;
    private static final int MAX_SCALE_RMP = 255;

    private static final int OFFSET_EDIM_R = 29;
    private static final int LENGTH_EDIM_R = 1;

    private static final int OFFSET_STR = 30;
    private static final int LENGTH_STR = 1;

    private static final int OFFSET_SW = 31;
    private static final int LENGTH_SW = 1;

    public UserDataEEPA53808CMD02() {
        super(CMD);
    }

    public UserDataEEPA53808CMD02(final byte[] data) {
        super(data);
    }

    public int getDimmingValueRelative() throws UserDataScaleValueException {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_EDIM, LENGTH_EDIM);
        return (int) getScaleValue(raw, MIN_RANGE_EDIM, MAX_RANGE_EDIM, MIN_SCALE_EDIM_RELATIVE, MAX_SCALE_EDIM_RELATIVE);
    }

    public void setDimmingValueRelative(final int value) throws UserDataScaleValueException {
        final long range = getRangeValue(value, MIN_SCALE_EDIM_RELATIVE, MAX_SCALE_EDIM_RELATIVE, MIN_RANGE_EDIM, MAX_RANGE_EDIM);
        Bits.setBitsOfBytes(range, getUserData(), OFFSET_EDIM, LENGTH_EDIM);
    }

    public int getDimmingValueAbsolute() throws UserDataScaleValueException {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_EDIM, LENGTH_EDIM);
        return (int) getScaleValue(raw, MIN_RANGE_EDIM, MAX_RANGE_EDIM, MIN_SCALE_EDIM_ABSOLUTE, MAX_SCALE_EDIM_ABSOLUTE);
    }

    public void setDimmingValueAbsolute(final int value) throws UserDataScaleValueException {
        final long range = getRangeValue(value, MIN_SCALE_EDIM_ABSOLUTE, MAX_SCALE_EDIM_ABSOLUTE, MIN_RANGE_EDIM, MAX_RANGE_EDIM);
        Bits.setBitsOfBytes(range, getUserData(), OFFSET_EDIM, LENGTH_EDIM);
    }

    public int getRampingTime() throws UserDataScaleValueException {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_RMP, LENGTH_RMP);
        return (int) getScaleValue(raw, MIN_RANGE_RMP, MAX_RANGE_RMP, MIN_SCALE_RMP, MAX_SCALE_RMP);
    }

    public void setRampingTime(final int value) throws UserDataScaleValueException {
        final long range = getRangeValue(value, MIN_SCALE_RMP, MAX_SCALE_RMP, MIN_RANGE_RMP, MAX_RANGE_RMP);
        Bits.setBitsOfBytes(range, getUserData(), OFFSET_RMP, LENGTH_RMP);
    }

    public DimmingRange getDimmingRange() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_EDIM_R, LENGTH_EDIM_R);
        if (raw == 0) {
            return DimmingRange.ABSOLUTE_VALUE;
        } else {
            return DimmingRange.RELATIVE_VALUE;
        }
    }

    public void setDimmingRange(final DimmingRange value) {
        final long range;
        switch (value) {
            case ABSOLUTE_VALUE:
                range = 0;
                break;
            case RELATIVE_VALUE:
                range = 1;
                break;
            default:
                throw new IllegalArgumentException("Unknown case: " + value);
        }
        Bits.setBitsOfBytes(range, getUserData(), OFFSET_EDIM_R, LENGTH_EDIM_R);
    }

    public boolean getStoreFinalValue() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_STR, LENGTH_STR);
        return raw != 0;
    }

    public void setStoreFinalValue(final boolean value) {
        Bits.setBitsOfBytes(value ? 1 : 0, getUserData(), OFFSET_STR, LENGTH_STR);
    }

    public boolean getSwitchingCommand() {
        final long raw = Bits.getBitsFromBytes(getUserData(), OFFSET_SW, LENGTH_SW);
        return raw != 0;
    }

    public void setSwitchingCommand(final boolean value) {
        Bits.setBitsOfBytes(value ? 1 : 0, getUserData(), OFFSET_SW, LENGTH_SW);
    }

}
