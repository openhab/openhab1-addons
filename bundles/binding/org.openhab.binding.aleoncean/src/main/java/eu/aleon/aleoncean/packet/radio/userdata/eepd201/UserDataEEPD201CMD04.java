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

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201CMD04 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_STATUS_RESPONSE;

    private static final int DATA_LENGTH = 3;

    // Power failure
    private static final int OFFSET_PF = 0;
    private static final int LENGTH_PF = 1;

    // Power failure detection
    private static final int OFFSET_PFD = 1;
    private static final int LENGTH_PFD = 1;

    // Over current switch off
    private static final int OFFSET_OC = 8;
    private static final int LENGTH_OC = 1;

    // Error level
    private static final int OFFSET_EL = 9;
    private static final int LENGTH_EL = 2;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    // Local control
    private static final int OFFSET_LC = 16;
    private static final int LENGTH_LC = 1;

    // Output value
    private static final int OFFSET_OV = 17;
    private static final int LENGTH_OV = 7;

    public UserDataEEPD201CMD04() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD04(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public PowerFailure getPowerFailure() {
        return PowerFailure.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_PF, LENGTH_PF));
    }

    public void setPowerFailure(final PowerFailure value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_PF, LENGTH_PF);
    }

    public PowerFailureDetection getPowerFailureDetection() {
        return PowerFailureDetection.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_PFD, LENGTH_PFD));
    }

    public void setPowerFailureDetection(final PowerFailureDetection value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_PFD, LENGTH_PFD);
    }

    public OverCurrentSwitchOff getOverCurrentSwitchOff() {
        return OverCurrentSwitchOff.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_OC, LENGTH_OC));
    }

    public void setOverCurrentSwitchOff(final OverCurrentSwitchOff value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_OC, LENGTH_OC);
    }

    public ErrorLevel getErrorLevel() {
        return ErrorLevel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_EL, LENGTH_EL));
    }

    public void setErrorLevel(final ErrorLevel value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_EL, LENGTH_EL);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
    }

    public LocalControl getLocalControl() {
        return LocalControl.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_LC, LENGTH_LC));
    }

    public void setLocalControl(final LocalControl value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_LC, LENGTH_LC);
    }

    private byte getOutputValue() throws UserDataScaleValueException {
        final byte outputValue = (byte) Bits.getBitsFromBytes(getUserData(), OFFSET_OV, LENGTH_OV);
        if (!OutputValue.isRawValueEEPConform(outputValue)) {
            throw new UserDataScaleValueException();
        }
        return outputValue;
    }

    private void setOutputValue(final byte value) {
        Bits.setBitsOfBytes(value, getUserData(), OFFSET_OV, LENGTH_OV);
    }

    public boolean getOutputValueOnOff() throws UserDataScaleValueException {
        return OutputValue.onOffValueRawToScale(getOutputValue());
    }

    public int getOutputValueDim() throws UserDataScaleValueException {
        return OutputValue.dimValueRawToScale(getOutputValue());
    }

    public boolean isOutputValueInvalid() throws UserDataScaleValueException {
        return getOutputValue() == OutputValue.NOT_VALID_OR_NOT_APPLICABLE;
    }

    public void setOutputValueOnOff(final boolean on) {
        setOutputValue(OutputValue.onOffValueScaleToRaw(on));
    }

    public void setOutputValueDim(final int value) {
        setOutputValue(OutputValue.dimValueScaleToRaw(value));
    }

    public void setOutputValueInvalid() {
        setOutputValue(OutputValue.NOT_VALID_OR_NOT_APPLICABLE);
    }

}
