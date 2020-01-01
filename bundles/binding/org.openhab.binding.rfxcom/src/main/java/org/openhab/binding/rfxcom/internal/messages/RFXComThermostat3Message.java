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
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for Thermostat3 message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComThermostat3Message extends RFXComBaseMessage {

    /*
     * Thermostat3 packet layout (length 10)
     *
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * unitcode1 = 4
     * unitcode2 = 5
     * unitcode3 = 6
     * cmnd = 7
     * filler = 9 'bits 3-0
     * rssi = 9 'bits 7-4
     */

    private static final int FILLER = 0x01;

    public enum SubType {
        MERTIK_G6RH4T1(0), // Mertik G6R-H4T1
        MERTIK_G6RH4TB(1), // Mertik G6R-H4TB
        MERTIK_G6RH4TD(2), // Mertik G6R-H4TD
        MERTIK_G6RH4S(3), // Mertik G6R-H4S

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

    public enum Commands {
        OFF(0),
        ON(1),
        UP(2),
        DOWN(3),
        RUNUP_OFF2ND(4),
        RUNDOWN_ON2ND(5),
        STOP(6),

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
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.TEMPERATURE, RFXComValueSelector.SET_POINT,
            RFXComValueSelector.CONTACT);

    public SubType subType = SubType.UNKNOWN;
    public int unitcode = 0;
    public Commands command = Commands.UNKNOWN;
    public int commandId = 0;
    public byte signalLevel = 0;

    public RFXComThermostat3Message() {
        packetType = PacketType.THERMOSTAT3;
    }

    public RFXComThermostat3Message(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Unit code = " + unitcode;
        str += "\n - Command = " + command + "(" + commandId + ")";
        str += "\n - Signal level = " + signalLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        unitcode = (data[4] & 0xFF) << 16 | (data[5] & 0xFF) << 8 | (data[6] & 0xFF);
        commandId = data[7];
        command = Commands.fromByte(commandId);

        signalLevel = (byte) ((data[8] & 0xF0) >> 4);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[9];

        data[0] = 0x08;
        data[1] = RFXComBaseMessage.PacketType.THERMOSTAT3.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = (byte) ((unitcode >> 16) & 0xFF);
        data[5] = (byte) ((unitcode >> 8) & 0xFF);
        data[6] = (byte) (unitcode & 0xFF);
        data[7] = command.toByte();
        data[8] = (byte) ((signalLevel << 4) | FILLER);

        return data;
    }

    @Override
    public String generateDeviceId() {
        return String.valueOf(unitcode);
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
                throw new NumberFormatException("Can't convert " + valueSelector + " to NumberItem");
            }

        } else if (valueSelector.getItemClass() == StringItem.class) {
            if (valueSelector == RFXComValueSelector.RAW_DATA) {

                state = new StringType(DatatypeConverter.printHexBinary(rawMessage));

            } else {
                throw new NumberFormatException("Can't convert " + valueSelector + " to StringItem");
            }

        } else if (valueSelector.getItemClass() == ContactItem.class) {
            if (valueSelector == RFXComValueSelector.COMMAND) {
                if (subType == SubType.MERTIK_G6RH4T1) {
                    switch (command) {
                        case OFF:
                        case STOP:
                            state = OpenClosedType.OPEN;
                            break;
                        case ON:
                            state = OpenClosedType.CLOSED;
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (command) {
                        case OFF:
                        case RUNUP_OFF2ND:
                        case STOP:
                            state = OpenClosedType.CLOSED;
                            break;
                        case ON:
                        case RUNDOWN_ON2ND:
                            state = OpenClosedType.OPEN;
                            break;
                        default:
                            break;
                    }
                }
            }

        } else if (valueSelector.getItemClass() == SwitchItem.class) {

            if (valueSelector == RFXComValueSelector.COMMAND) {
                if (subType == SubType.MERTIK_G6RH4T1) {
                    switch (command) {
                        case OFF:
                        case STOP:
                            state = OnOffType.OFF;
                            break;
                        case ON:
                            state = OnOffType.ON;
                            break;
                        default:
                            break;
                    }
                } else {
                    switch (command) {
                        case OFF:
                        case RUNUP_OFF2ND:
                        case STOP:
                            state = OnOffType.OFF;
                            break;
                        case ON:
                        case RUNDOWN_ON2ND:
                            state = OnOffType.ON;
                            break;
                        default:
                            break;
                    }
                }
            }
        }

        else {
            throw new NumberFormatException("Can't convert " + valueSelector + " to " + valueSelector.getItemClass());
        }

        return state;
    }

    @Override
    public void convertFromState(RFXComValueSelector valueSelector, String id, Object subType, Type type,
            byte seqNumber) throws RFXComException {

        this.subType = ((SubType) subType);
        seqNbr = seqNumber;
        String[] ids = id.split("\\.");
        unitcode = Byte.parseByte(ids[0]);

        switch (valueSelector) {
            case COMMAND:
                if (type instanceof OnOffType) {
                    command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
                } else if (type instanceof OpenClosedType) {
                    command = (type == OpenClosedType.CLOSED ? Commands.ON : Commands.OFF);
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
