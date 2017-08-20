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
public class UserDataEEPD201CMD01 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_SET_OUTPUT;

    private static final int DATA_LENGTH = 3;

    // Dim value
    private static final int OFFSET_DV = 4;
    private static final int LENGTH_DV = 3;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    // Output value
    private static final int OFFSET_OV = 17;
    private static final int LENGTH_OV = 7;

    public UserDataEEPD201CMD01() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD01(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public DimValue getDimValue() {
        return DimValue.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DV, LENGTH_DV));
    }

    public void setDimValue(final DimValue dimValue) {
        Bits.setBitsOfBytes(dimValue.toByte(), getUserData(), OFFSET_DV, LENGTH_DV);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel ioChannel) {
        Bits.setBitsOfBytes(ioChannel.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
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
