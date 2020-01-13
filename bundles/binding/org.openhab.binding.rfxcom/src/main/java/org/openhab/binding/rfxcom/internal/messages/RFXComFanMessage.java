/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for Fan message.
 *
 * @author Damien Servant
 * @since 1.9.0
 *
 */
public class RFXComFanMessage extends RFXComBaseMessage {

    /*
     * Energy packet layout (length 17)
     *
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * id1 = 4
     * id2 = 5
     * id3 = 6
     * cmnd = 7
     * battery_level = 8 //bits 3-0
     * signal_level = 8 //bits 7-4
     */

    public enum SubType {
        SIEMENSSF01(0),
        ITHO_CVT_RFT(1),
        LUCCI_AIR_FAN(2),
        SEAV_TXS4(3),

        UNKNOWN(255);

        private final int subType;

        SubType(int subType) {
            this.subType = subType;
        }

        SubType(byte subType) {
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

    // Note that these commands are only valid for subType SIEMENSSF01
    public enum Commands {
        TIMER(1),
        MIN(2),
        LEARN(3),
        PLUS(4),
        CONFIRM(5),
        LIGHT(6),

        UNKNOWN(255);

        private final int command;

        Commands(int command) {
            this.command = command;
        }

        Commands(byte command) {
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
    }

    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.COMMAND);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public Commands command = Commands.UNKNOWN;
    public int commandId = 0;
    public byte signalLevel = 0;

    public RFXComFanMessage() {
        packetType = PacketType.FAN;
    }

    public RFXComFanMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Command = " + command + "(" + commandId + ")";
        str += "\n - Signal level = " + signalLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 16 | (data[5] & 0xFF) << 8 | (data[6] & 0xFF);
        commandId = data[7];
        command = Commands.fromByte(commandId);
        signalLevel = (byte) ((data[8] & 0xF0) >> 4);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[9];

        data[0] = 0x08;
        data[1] = RFXComBaseMessage.PacketType.FAN.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        // SENSORID
        data[4] = (byte) ((sensorId >> 16) & 0xFF);
        data[5] = (byte) ((sensorId >> 8) & 0xFF);
        data[6] = (byte) (sensorId & 0xFF);

        data[7] = (byte) commandId;
        data[8] = (byte) (((signalLevel & 0x0F) << 4));

        return data;
    }

    @Override
    public String generateDeviceId() {
        return String.valueOf(sensorId);
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        org.openhab.core.types.State state = UnDefType.UNDEF;

        if (valueSelector.getItemClass() == NumberItem.class) {

            if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

                state = new DecimalType(signalLevel);

            } else if (valueSelector == RFXComValueSelector.COMMAND) {

                state = new DecimalType(commandId);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
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

        switch (valueSelector) {
            case COMMAND:
                if (type instanceof DecimalType) {
                    commandId = ((DecimalType) type).intValue();
                } else {
                    throw new RFXComException("Can't convert " + type + " to command");
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
