/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import static org.openhab.binding.rfxcom.internal.messages.RFXComLighting5Message.SubType.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.rfxcom.RFXComValueSelector;
import org.openhab.binding.rfxcom.internal.RFXComException;
import org.openhab.core.library.items.ContactItem;
import org.openhab.core.library.items.DimmerItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.RollershutterItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.library.types.StopMoveType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for lighting5 message.
 *
 * @author Paul Hampson, Neil Renaud, Martin van Wingerden
 * @since 1.3.0
 */
public class RFXComLighting5Message extends RFXComBaseMessage {

    public enum SubType {
        LIGHTWAVERF(0),
        EMW100(1),
        BBSB_NEW(2),
        MDREMOTE(3),
        CONRAD_RSL2(4),
        LIVOLO(5),
        AOKE(7),
        EURODOMEST(9),
        MDREMOTE_107(12),
        AVANTEK(14),
        IT(15),
        MDREMOTE_108(16),

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

    /**
     * Note: for the lighting5 commands, some command are only supported for certain sub types and
     * command-bytes might even have a different meaning for another sub type.
     *
     * If no sub types are specified for a command, its supported by all sub types.
     * An example is the command OFF which is represented by the byte 0x00 for all subtypes.
     *
     * Otherwise the list of sub types after the command-bytes indicates the sub types
     * which support this command with this byte.
     * Example byte value 0x03 means GROUP_ON for IT and some others while it means MOOD1 for LIGHTWAVERF
     */
    public enum Commands {
        OFF(0x00),
        ON(0x01),
        GROUP_OFF(0x02, LIGHTWAVERF, BBSB_NEW, CONRAD_RSL2, EURODOMEST, AVANTEK, IT),
        LEARN(0x02, EMW100),
        GROUP_ON(0x03, BBSB_NEW, CONRAD_RSL2, EURODOMEST, AVANTEK, IT),
        MOOD1(0x03, LIGHTWAVERF),
        MOOD2(0x04, LIGHTWAVERF),
        MOOD3(0x05, LIGHTWAVERF),
        MOOD4(0x06, LIGHTWAVERF),
        MOOD5(0x07, LIGHTWAVERF),
        RESERVED1(0x08, LIGHTWAVERF),
        RESERVED2(0x09, LIGHTWAVERF),
        UNLOCK(0x0A, LIGHTWAVERF),
        LOCK(0x0B, LIGHTWAVERF),
        ALL_LOCK(0x0C, LIGHTWAVERF),
        CLOSE_RELAY(0x0D, LIGHTWAVERF),
        STOP_RELAY(0x0E, LIGHTWAVERF),
        OPEN_RELAY(0x0F, LIGHTWAVERF),
        SET_LEVEL(0x10, LIGHTWAVERF, IT),
        COLOUR_PALETTE(0x11, LIGHTWAVERF),
        COLOUR_TONE(0x12, LIGHTWAVERF),
        COLOUR_CYCLE(0x13, LIGHTWAVERF),

        UNKNOWN(255);

        private final int command;
        private final List<SubType> supportedBySubTypes;

        Commands(int command) {
            this(command, SubType.values());
        }

        Commands(int command, SubType... supportedBySubTypes) {
            this.command = command;
            this.supportedBySubTypes = Arrays.asList(supportedBySubTypes);
        }

        public byte toByte() {
            return (byte) command;
        }

        public static Commands fromByte(int input, SubType subType) {
            for (Commands c : Commands.values()) {
                if (c.command == input && c.supportedBySubTypes.contains(subType)) {
                    return c;
                }
            }

            return Commands.UNKNOWN;
        }
    }

    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.COMMAND, RFXComValueSelector.MOOD,
            RFXComValueSelector.DIMMING_LEVEL, RFXComValueSelector.CONTACT, RFXComValueSelector.SHUTTER);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public byte unitcode = 0;
    public Commands command = Commands.UNKNOWN;
    public byte dimmingLevel = 0;
    public byte signalLevel = 0;

    public RFXComLighting5Message() {
        packetType = PacketType.LIGHTING5;
    }

    public RFXComLighting5Message(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Unit code = " + unitcode;
        str += "\n - Command = " + command;
        str += "\n - Dim level = " + dimmingLevel;
        str += "\n - Signal level = " + signalLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 16 | (data[5] & 0xFF) << 8 | (data[6] & 0xFF);
        unitcode = data[7];
        command = Commands.fromByte(data[8], subType);
        dimmingLevel = data[9];
        signalLevel = (byte) ((data[10] & 0xF0) >> 4);
    }

    @Override
    public byte[] decodeMessage() {

        byte[] data = new byte[11];

        data[0] = 0x0A;
        data[1] = RFXComBaseMessage.PacketType.LIGHTING5.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = (byte) ((sensorId >> 16) & 0xFF);
        data[5] = (byte) ((sensorId >> 8) & 0xFF);
        data[6] = (byte) (sensorId & 0xFF);

        data[7] = unitcode;
        data[8] = command.toByte();
        data[9] = dimmingLevel;
        data[10] = (byte) ((signalLevel & 0x0F) << 4);

        return data;
    }

    @Override
    public String generateDeviceId() {
        return sensorId + "." + unitcode;
    }

    /**
     * Convert a 0-31 scale value to a percent type.
     *
     * @param pt
     *            percent type to convert
     * @return converted value 0-31
     */
    public static int getDimLevelFromPercentType(PercentType pt) {
        return pt.toBigDecimal().multiply(BigDecimal.valueOf(31))
                .divide(PercentType.HUNDRED.toBigDecimal(), 0, BigDecimal.ROUND_UP).intValue();
    }

    /**
     * Convert a 0-31 scale value to a percent type.
     *
     * @param value
     *            percent type to convert
     * @return converted value 0-31
     */
    public static PercentType getPercentTypeFromDimLevel(int value) {
        value = Math.min(value, 31);

        return new PercentType(BigDecimal.valueOf(value).multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(31), 0, BigDecimal.ROUND_UP).intValue());
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        org.openhab.core.types.State state = UnDefType.UNDEF;

        if (valueSelector.getItemClass() == NumberItem.class) {

            if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

                state = new DecimalType(signalLevel);

            } else if (valueSelector == RFXComValueSelector.MOOD) {
                switch (command) {
                    case GROUP_OFF:
                        state = new DecimalType(0);
                        break;
                    case MOOD1:
                        state = new DecimalType(1);
                        break;
                    case MOOD2:
                        state = new DecimalType(2);
                        break;
                    case MOOD3:
                        state = new DecimalType(3);
                        break;
                    case MOOD4:
                        state = new DecimalType(4);
                        break;
                    case MOOD5:
                        state = new DecimalType(5);
                        break;
                    default:
                        throw new RFXComException("Unexpected mood: " + command);
                }
            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
            }

        } else if (valueSelector.getItemClass() == RollershutterItem.class) {

            if (valueSelector == RFXComValueSelector.COMMAND || valueSelector == RFXComValueSelector.SHUTTER) {

                switch (command) {
                    case CLOSE_RELAY:
                        state = OpenClosedType.CLOSED;
                        break;

                    case OPEN_RELAY:
                        state = OpenClosedType.OPEN;
                        break;

                    default:
                        break;
                }
            } else if (valueSelector == RFXComValueSelector.DIMMING_LEVEL) {
                state = RFXComLighting5Message.getPercentTypeFromDimLevel(dimmingLevel);

            } else {
                throw new NumberFormatException("Can't convert " + valueSelector + " to RollershutterItem");
            }

        } else if (valueSelector.getItemClass() == DimmerItem.class) {

            if (valueSelector == RFXComValueSelector.DIMMING_LEVEL) {
                state = RFXComLighting5Message.getPercentTypeFromDimLevel(dimmingLevel);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to DimmerItem");
            }

        } else if (valueSelector.getItemClass() == SwitchItem.class) {

            if (valueSelector == RFXComValueSelector.COMMAND) {

                switch (command) {
                    case OFF:
                    case GROUP_OFF:
                    case CLOSE_RELAY:
                        state = OnOffType.OFF;
                        break;

                    case ON:
                    case OPEN_RELAY:
                        state = OnOffType.ON;
                        break;

                    case SET_LEVEL:
                    default:
                        throw new RFXComException("Can't convert " + command + " to SwitchItem");

                }

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to SwitchItem");
            }

        } else if (valueSelector.getItemClass() == ContactItem.class) {

            if (valueSelector == RFXComValueSelector.CONTACT) {

                switch (command) {
                    case OFF:
                    case GROUP_OFF:
                    case CLOSE_RELAY:
                        state = OpenClosedType.CLOSED;
                        break;

                    case ON:
                    case OPEN_RELAY:
                        state = OpenClosedType.OPEN;
                        break;

                    case SET_LEVEL:
                    default:
                        throw new RFXComException("Can't convert " + command + " to ContactItem");
                }

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
        unitcode = Byte.parseByte(ids[1]);

        switch (valueSelector) {
            case SHUTTER:
                if (type instanceof OpenClosedType) {
                    command = (type == OpenClosedType.CLOSED ? Commands.CLOSE_RELAY : Commands.OPEN_RELAY);
                } else if (type instanceof StopMoveType) {
                    command = Commands.STOP_RELAY;
                } else {
                    throw new NumberFormatException("Can't convert " + type + " to Command");
                }
                break;

            case COMMAND:
                if (type instanceof OnOffType) {
                    command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
                    dimmingLevel = 0;
                } else {
                    throw new RFXComException("Can't convert " + type + " to Command");
                }
                break;

            case DIMMING_LEVEL:
                if (type instanceof OnOffType) {
                    command = (type == OnOffType.ON ? Commands.ON : Commands.OFF);
                    dimmingLevel = 0;
                } else if (type instanceof PercentType) {
                    command = Commands.SET_LEVEL;
                    dimmingLevel = (byte) getDimLevelFromPercentType((PercentType) type);

                    if (dimmingLevel == 0) {
                        command = Commands.OFF;
                    }

                } else if (type instanceof IncreaseDecreaseType) {
                    command = Commands.SET_LEVEL;
                    // Evert: I do not know how to get previous object state...
                    dimmingLevel = 5;

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
