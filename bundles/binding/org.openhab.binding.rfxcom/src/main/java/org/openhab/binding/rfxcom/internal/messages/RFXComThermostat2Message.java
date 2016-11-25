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
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for thermostat2 message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComThermostat2Message extends RFXComBaseMessage {

    /*
     * Thermostat2 packet layout (length 7)

	 * packetlength = 0
	 * packettype = 1
	 * subtype = 2
	 * seqnbr = 3
	 * unitcode = 4
	 * cmnd = 5
	 * filler = 6  'bits 3-0
	 * rssi = 6    'bits 7-4
	 */

    public enum SubType {
        HE105(0),
        RTS10(1),

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
    }
 
    public enum Commands {
        OFF(0),
        ON(1),
        PROGRAM(2),	
		
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
    }
	
    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.TEMPERATURE,
            RFXComValueSelector.SET_POINT, RFXComValueSelector.CONTACT);

    public SubType subType = SubType.HE105;
    public byte unitcode = 0;
    public Commands command = Commands.OFF;
    public int commandId = 0;
    public byte signalLevel = 0;

    public RFXComThermostat2Message() {
        packetType = PacketType.THERMOSTAT2;
    }

    public RFXComThermostat2Message(byte[] data) {
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

        try {
            subType = SubType.values()[super.subType];
        } catch (Exception e) {
            subType = SubType.UNKNOWN;
        }

        unitcode = data[4];
        commandId = data[5];

        command = Commands.UNKNOWN;
        for (Commands loCmd : Commands.values()) {
            if (loCmd.toByte() == commandId) {
                command = loCmd;
                break;
            }
        }		

        signalLevel = (byte) ((data[9] & 0xF0) >> 4);
    }
		
    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[7];

        data[0] = (byte)(data.length-1);
        data[1] = RFXComBaseMessage.PacketType.THERMOSTAT2.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = unitcode;
        data[5] = command.toByte();
        data[6] = (byte) (((signalLevel & 0x0F) << 4));

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
                switch (command) {
                    case OFF:
                        state = OpenClosedType.OPEN;
                        break;
                    case ON:
                        state = OpenClosedType.CLOSED;
                        break;
                    default:
                        break;
                }
            }
			
        } else if (valueSelector.getItemClass() == SwitchItem.class) {

            if (valueSelector == RFXComValueSelector.COMMAND) {
                switch (command) {
                    case OFF:
                        state = OnOffType.OFF;
                        break;
                    case ON:
                        state = OnOffType.ON;
                        break;
                    default:
                        break;
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
