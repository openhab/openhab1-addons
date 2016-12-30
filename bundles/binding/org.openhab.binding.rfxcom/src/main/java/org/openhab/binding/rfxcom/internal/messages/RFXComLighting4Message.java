/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;

/**
 * RFXCOM data class for lighting4 message.
 *
 * a Lighting4 Base command is composed of 24 bit DATA plus PULSE information
 *
 * DATA:
 * Code = 014554
 * S1- S24 = <0000 0001 0100 0101 0101> <0100>
 * first 20 are DeviceID last 4 are for Command
 *
 * PULSE:
 * default 350
 *
 * Tested on a PT2262 remote PlugIn module
 *
 * Example:
 *
 * Switch TESTout "TestOut" (All) {rfxcom=">83205.350:LIGHTING4.PT2262:Command"}
 * (SendCommand DeviceID(int).Pulse(int):LIGHTING4.Subtype:Command )
 *
 * Switch TESTin "TestIn" (All) {rfxcom="<83205:Command"}
 * (ReceiveCommand ON/OFF Command )
 *
 * @author Alessandro Ballini (ITA)
 * @since 1.5.1
 */
public class RFXComLighting4Message extends RFXComBaseMessage {

    public enum SubType {
        PT2262(0),

        UNKNOWN(255);

        private final int subType;

        SubType(int subType) {
            this.subType = subType;
        }

        public byte toByte() {
            return (byte) subType;
        }

        public static SubType fromByte(int input) {
            for (SubType c : SubType.values()) {
                if (c.subType == input) {
                    return c;
                }
            }

            return SubType.UNKNOWN;
        }
    }

    public enum Commands {
        OFF_0(0),
        ON_1(1),
        OFF_2(2),
        ON_3(3),
        OFF_4(4),
        ON_5(5),
        ON_7(7),
        ON_9(9),
        ON_12(12),

        UNKNOWN(255);

        private final int command;

        Commands(int command) {
            this.command = command;
        }

        public byte toByte() {
            return (byte) command;
        }

        public static Commands fromByte(int input) {
            for (Commands c : Commands.values()) {
                if (c.command == input) {
                    return c;
                }
            }

            return Commands.UNKNOWN;
        }

        public boolean isOn() throws RFXComException {
            switch (this) {
                case OFF_0:
                case OFF_2:
                case OFF_4:
                    return false;

                case ON_1:
                case ON_3:
                case ON_5:
                case ON_7:
                case ON_9:
                case ON_12:
                    return true;

                default:
                    throw new RFXComException("Can't convert " + command + " to state");
            }
        }
    }

    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.COMMAND, RFXComValueSelector.CONTACT, RFXComValueSelector.SIGNAL_LEVEL);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public int commandId = 0;
    public Commands command = Commands.UNKNOWN;
    public int pulse = 0;
    public byte signalLevel = 0;

    public RFXComLighting4Message() {
        packetType = PacketType.LIGHTING4;
    }

    public RFXComLighting4Message(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Command = " + command + "(" + commandId + ")";
        str += "\n - Pulse = " + pulse;
        str += "\n - Signal level = " + signalLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 12 | (data[5] & 0xFF) << 4 | (data[6] & 0xFF) >> 4;
        commandId = (data[6] & 0x0F); // 4 OFF - 1 ON
        command = Commands.fromByte(commandId);

        pulse = (data[7] & 0xFF) << 8 | (data[8] & 0xFF);

        signalLevel = (byte) ((data[9] & 0xF0) >> 4);
    }

    @Override
    public byte[] decodeMessage() {

        byte[] data = new byte[10];

        data[0] = 0x09;
        data[1] = RFXComBaseMessage.PacketType.LIGHTING4.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        // SENSORID + COMMAND
        data[4] = (byte) ((sensorId >> 12) & 0xFF);
        data[5] = (byte) ((sensorId >> 4) & 0xFF);
        data[6] = (byte) (((sensorId << 4) & 0xF0) | command.toByte());

        // PULSE
        data[7] = (byte) ((pulse >> 8) & 0xFF);
        data[8] = (byte) (pulse & 0xFF);

        // SIGNAL
        data[9] = (byte) ((signalLevel & 0x0F) << 4);

        return data;
    }

    @Override
    public String generateDeviceId() {
        return "" + sensorId;
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        State state;

        if (valueSelector.getItemClass() == NumberItem.class) {

            if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

                state = new DecimalType(signalLevel);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
            }
        } else if (valueSelector.getItemClass() == SwitchItem.class) {

            if (valueSelector == RFXComValueSelector.COMMAND) {

                state = command.isOn() ? OnOffType.ON : OnOffType.OFF;

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to SwitchItem");
            }

        } else if (valueSelector.getItemClass() == ContactItem.class) {

            if (valueSelector == RFXComValueSelector.CONTACT) {

                state = command.isOn() ? OpenClosedType.OPEN : OpenClosedType.CLOSED;

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to ContactItem");
            }

        } else if (valueSelector.getItemClass() == StringItem.class) {
            if (valueSelector == RFXComValueSelector.RAW_DATA) {
                state = new StringType(DatatypeConverter.printHexBinary(rawMessage));
            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to StringItem");
            }
        } else {

            throw new RFXComException("Can't convert " + valueSelector + " to " + valueSelector.getItemClass());
        }

        return state;

    }

    @Override
    public void convertFromState(RFXComValueSelector valueSelector, String id, Object subType, Type type,
            byte seqNumber) throws RFXComException {

        this.subType = ((SubType) subType);
        seqNbr = seqNumber;
        String[] ids = id.split("\\.");
        sensorId = Integer.parseInt(ids[0]);
        pulse = Integer.parseInt(ids[1]);

        switch (valueSelector) {

            case COMMAND:
                if (type instanceof OnOffType) {
                    command = (type == OnOffType.ON ? Commands.ON_1 : Commands.OFF_4);
                } else {
                    throw new RFXComException("Can't convert " + type + " to Command");
                }
                break;

            default:
                throw new RFXComException("Can't convert " + type + " to " + valueSelector);
        }
    }

    @Override
    public Object convertSubType(String subType) throws RFXComException {

        for (SubType s : SubType.values()) {
            if (s.toString().equals(subType)) {
                return s;
            }
        }

        throw new RFXComException("Unknown sub type " + subType);
    }

    @Override
    public List<RFXComValueSelector> getSupportedValueSelectors() throws RFXComException {
        return supportedValueSelectors;
    }

}
