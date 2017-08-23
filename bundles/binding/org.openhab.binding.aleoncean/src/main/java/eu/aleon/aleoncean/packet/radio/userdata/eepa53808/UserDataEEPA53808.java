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

import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;
import eu.aleon.aleoncean.util.Bits;

/**
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPA53808 extends UserData4BS {

    protected static final byte CMD_SWITCHING = (byte) 0x01;
    protected static final byte CMD_DIMMING = (byte) 0x02;
    protected static final byte CMD_SETPOINT_SHIFT = (byte) 0x03;
    protected static final byte CMD_BASIC_SETPOINT = (byte) 0x04;
    protected static final byte CMD_CONTROL_VARIABLE = (byte) 0x05;
    protected static final byte CMD_FAN_STAGE = (byte) 0x06;
    protected static final byte CMD_BLIND_CENTRAL_COMMAND = (byte) 0x07;

    private static final int OFFSET_COM = 0;
    private static final int LENGTH_COM = 8;

    public static byte getCommandId(final byte[] data) {
        return (byte) Bits.getBitsFromBytes(data, OFFSET_COM, LENGTH_COM);
    }

    public UserDataEEPA53808(final byte cmd) {
        super();
        setCommandId(cmd);
    }

    public UserDataEEPA53808(final byte[] data) {
        super(data);
    }

    public UserDataEEPA53808(final byte cmd, final byte[] data) {
        super(data);
        setCommandId(cmd);
    }

    public byte getCommandId() {
        return getCommandId(getUserData());
    }

    public void setCommandId(final byte commandId) {
        Bits.setBitsOfBytes(commandId & 0xFF, getUserData(), OFFSET_COM, LENGTH_COM);
    }

}
