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

import eu.aleon.aleoncean.packet.radio.RadioPacketUTE;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataUTE extends UserData {

    public static final int DATA_LENGTH = 7;

    private static final byte CMD_QUERY = (byte) 0x00;
    private static final byte CMD_RESPONSE = (byte) 0x01;

    public enum Command {

        QUERY(CMD_QUERY),
        RESPONSE(CMD_RESPONSE),
        UNKNOWN((byte) 0xFF);

        private final byte cmd;

        private Command(final byte cmd) {
            this.cmd = cmd;
        }

        public byte toByte() {
            return cmd;
        }

        public static Command fromByte(final byte cmd) {
            switch (cmd) {
                case CMD_QUERY:
                    return Command.QUERY;
                case CMD_RESPONSE:
                    return Command.RESPONSE;
                default:
                    return Command.UNKNOWN;
            }
        }
    }

    public UserDataUTE() {
        super(DATA_LENGTH);
    }

    public UserDataUTE(final byte[] data) {
        super(data);
        assert data.length == DATA_LENGTH;
    }

    public byte getChoice() {
        return getDb(0);
    }

    public void setChoice(final byte choice) {
        setDb(0, choice);
    }

    public byte getFunc() {
        return getDb(1);
    }

    public void setFunc(final byte func) {
        setDb(1, func);
    }

    public byte getType() {
        return getDb(2);
    }

    public void setType(final byte type) {
        setDb(2, type);
    }

    public short getManufacturerId() {
        final long msb3 = getDataRange(3, 2, 3, 0);
        final long lsb8 = getDb(4) & 0xFF;
        final long manufacturerId = (msb3 << 8) | lsb8;
        return (short) manufacturerId;
    }

    public void setManufacturerId(final short manufacturerId) {
        final long id = ((long) manufacturerId) & 0xFFFF;
        final long msb3 = (id >> 8) & 0x07;
        final long lsb8 = id & 0xFF;
        setDataRange(msb3, 3, 2, 3, 0);
        setDb(4, (byte) lsb8);
    }

    /**
     * Get the number of channels to be taught in.
     *
     * 0x00 - 0xFE: Number of individual channels to be taught in.
     * 0xFF: Teach-in all channels supported by the device.
     *
     * @return Return a value between 0x00 and 0xFF. Short is used to signal an value &gt; 0.
     */
    public short getNumOfChannels() {
        return (short) (getDb(5) & 0xFF);
    }

    public void setNumOfChannels(final short num) {
        setDb(5, (byte) num);
    }

    public boolean getBidirectionalCommunication() {
        return getDataBit(6, 7) == 1;
    }

    public void setBidirectionalCommunication(final boolean bidirectional) {
        setDataBit(6, 7, bidirectional ? 1 : 0);
    }

    public final Command getCmd() {
        return getCmd(getUserData());
    }

    public static Command getCmd(final byte[] userData) {
        return Command.fromByte((byte) getDataRange(userData, 6, 3, 6, 0));
    }

    public final void setCmd(final Command cmd) {
        setDataRange(cmd.toByte(), 6, 3, 6, 0);
    }

    @Override
    public RadioPacketUTE generateRadioPacket() {
        final RadioPacketUTE packet = new RadioPacketUTE();
        packet.setUserDataRaw(getUserData());
        return packet;
    }

    @Override
    public String toString() {
        final String str = String.format("UserDataUTEQuery{choice=0x%02X, func=0x%02X, type=0x%02X, manu=0x%04X, channels=0x%02X, %s}",
                                         getChoice(), getFunc(), getType(), getManufacturerId(), getNumOfChannels(), getBidirectionalCommunication() ? "bidirectional" : "unidirectional");
        return str;
    }
}
