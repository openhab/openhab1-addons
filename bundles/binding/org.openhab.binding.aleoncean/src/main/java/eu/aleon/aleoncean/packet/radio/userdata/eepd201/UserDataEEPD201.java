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

import eu.aleon.aleoncean.packet.radio.userdata.UserDataVLD;
import eu.aleon.aleoncean.util.Bits;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201 extends UserDataVLD {

    private static final int OFFSET_CMD = 4;
    private static final int LENGTH_CMD = 4;

    public static final byte CMD_ACTUATOR_SET_OUTPUT = (byte) 0x01;
    public static final byte CMD_ACTUATOR_SET_LOCAL = (byte) 0x02;
    public static final byte CMD_ACTUATOR_STATUS_QUERY = (byte) 0x03;
    public static final byte CMD_ACTUATOR_STATUS_RESPONSE = (byte) 0x04;
    public static final byte CMD_ACTUATOR_SET_MEASUREMENT = (byte) 0x05;
    public static final byte CMD_ACTUATOR_MEASUREMENT_QUERY = (byte) 0x06;
    public static final byte CMD_ACTUATOR_MEASUREMENT_RESPONSE = (byte) 0x07;

    public UserDataEEPD201(final byte cmd, final int size) {
        super(size);
        setCmd(cmd);
    }

    public UserDataEEPD201(final byte[] data) {
        super(data);
    }

    public UserDataEEPD201(final byte cmd, final byte[] data) {
        super(data);
        setCmd(cmd);
    }

    public final byte getCmd() {
        return getCmd(getUserData());
    }

    public static final byte getCmd(final byte[] userData) {
        return (byte) Bits.getBitsFromBytes(userData, OFFSET_CMD, LENGTH_CMD);
    }

    public final void setCmd(final byte cmd) {
        Bits.setBitsOfBytes(cmd, getUserData(), OFFSET_CMD, LENGTH_CMD);
    }

}
