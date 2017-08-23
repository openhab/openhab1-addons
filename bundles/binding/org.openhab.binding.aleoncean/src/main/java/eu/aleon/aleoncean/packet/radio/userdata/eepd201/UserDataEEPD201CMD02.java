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

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201CMD02 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_SET_LOCAL;

    private static final int DATA_LENGTH = 4;

    // Taught-in devices
    private static final int OFFSET_DE = 0;
    private static final int LENGTH_DE = 1;

    // Over current shut down
    private static final int OFFSET_OC = 8;
    private static final int LENGTH_OC = 1;

    // reset over current shut down
    private static final int OFFSET_RO = 9;
    private static final int LENGTH_RO = 1;

    // local control
    private static final int OFFSET_LC = 10;
    private static final int LENGTH_LC = 1;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    // Dim timer 2
    private static final int OFFSET_DT2 = 16;
    private static final int LENGTH_DT2 = 4;

    // Dim timer 3
    private static final int OFFSET_DT3 = 20;
    private static final int LENGTH_DT3 = 4;

    // Users interface indication
    private static final int OFFSET_DN = 24;
    private static final int LENGTH_DN = 1;

    // Power failure
    private static final int OFFSET_PF = 25;
    private static final int LENGTH_PF = 1;

    // Default state
    private static final int OFFSET_DS = 26;
    private static final int LENGTH_DS = 2;

    // Dim timer 1
    private static final int OFFSET_DT1 = 28;
    private static final int LENGTH_DT1 = 4;

    public UserDataEEPD201CMD02() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD02(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public TaughtInDevices getTaughtInDevices() {
        return TaughtInDevices.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DE, LENGTH_DE));
    }

    public void setTaughtInDevices(final TaughtInDevices value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DE, LENGTH_DE);
    }

    public OverCurrentShutDown getOverCurrentShutDown() {
        return OverCurrentShutDown.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_OC, LENGTH_OC));
    }

    public void setOverCurrentShutDown(final OverCurrentShutDown value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_OC, LENGTH_OC);
    }

    public ResetOverCurrentShutDown getResetOverCurrentShutDown() {
        return ResetOverCurrentShutDown.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_RO, LENGTH_RO));
    }

    public void setResetOverCurrentShutDown(final ResetOverCurrentShutDown value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_RO, LENGTH_RO);
    }

    public LocalControl getLocalControl() {
        return LocalControl.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_LC, LENGTH_LC));
    }

    public void setLocalControl(final LocalControl value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_LC, LENGTH_LC);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel ioChannel) {
        Bits.setBitsOfBytes(ioChannel.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
    }

    public DimTimer getDimTimer1() {
        return DimTimer.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DT1, LENGTH_DT1));
    }

    public void setDimTimer1(final DimTimer value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DT1, LENGTH_DT1);
    }

    public DimTimer getDimTimer2() {
        return DimTimer.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DT2, LENGTH_DT2));
    }

    public void setDimTimer2(final DimTimer value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DT2, LENGTH_DT2);
    }

    public DimTimer getDimTimer3() {
        return DimTimer.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DT3, LENGTH_DT3));
    }

    public void setDimTimer3(final DimTimer value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DT3, LENGTH_DT3);
    }

    public UserInterfaceIndication getUserInterfaceIndication() {
        return UserInterfaceIndication.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DN, LENGTH_DN));
    }

    public void setUserInterfaceIndication(final UserInterfaceIndication value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DN, LENGTH_DN);
    }

    public PowerFailure getPowerFailure() {
        return PowerFailure.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_PF, LENGTH_PF));
    }

    public void setPowerFailure(final PowerFailure value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_PF, LENGTH_PF);
    }

    public DefaultState getDefaultState() {
        return DefaultState.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_DS, LENGTH_DS));
    }

    public void setDefaultState(final DefaultState value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_DS, LENGTH_DS);
    }

}
