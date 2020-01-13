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
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.items.SwitchItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for Security2 message.
 * (i.e. KeeLoq)
 *
 * KeeLoq protocol can send data for 4 buttons, they are mapped as
 * Button 0 - Contact
 * Button 1 - Contact 1
 * Button 2 - Contact 2
 *
 * SerialNumber is mapped to deviceId as S2_serialnumber
 *
 *
 * This code is based on RFXComSecurity1Message
 *
 * @author Ivan Martinez
 * @since 1.9.0
 */
public class RFXComSecurity2Message extends RFXComBaseMessage {

    public enum SubType {
        CLASSIC(0),
        ROLLING_CODE(1), // Not Implemented
        RAW_AES_KEELOQ(2), // Not Implemented

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

    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.BATTERY_LEVEL,
            // Buttons from 0 to 3
            RFXComValueSelector.CONTACT, RFXComValueSelector.CONTACT_1, RFXComValueSelector.CONTACT_2,
            RFXComValueSelector.CONTACT_3);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public int buttonStatus = 0;
    public byte batteryLevel = 0;
    public byte signalLevel = 0;

    public RFXComSecurity2Message() {
        packetType = PacketType.SECURITY2;
    }

    public RFXComSecurity2Message(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + generateDeviceId();
        str += "\n - Button Status = " + buttonStatus;
        str += "\n - Battery level = " + batteryLevel;
        str += "\n - Signal level = " + signalLevel;

        return str;
    }

    private final int BUTTON_0_BIT = 0x02;
    private final int BUTTON_1_BIT = 0x04;
    private final int BUTTON_2_BIT = 0x08;
    private final int BUTTON_3_BIT = 0x01;

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        // Encrypted portion id1(4)-id2(5)-id3(6)-id4(7)
        // Not implemented

        // Fixed Portion id5(8)-id6(9)-id7(10)-id8(11)-id9(12)
        sensorId = (data[11] & 0x0F) << 24 | (data[10] & 0xFF) << 16 | (data[9] & 0xFF) << 8 | (data[8] & 0xFF);
        buttonStatus = (data[11] & 0xF0) >> 4;
        // id10-id24
        // not implemented

        batteryLevel = (byte) ((data[28] & 0xF0) >> 4);
        signalLevel = (byte) (data[28] & 0x0F);
    }

    @Override
    public byte[] decodeMessage() {

        byte[] data = new byte[29];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.SECURITY2.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        data[4] = 0;
        data[5] = 0;
        data[6] = 0;
        data[7] = 0;

        data[8] = (byte) (sensorId & 0xff);
        data[9] = (byte) ((sensorId >> 8) & 0xff);
        data[10] = (byte) ((sensorId >> 16) & 0xff);
        data[11] = (byte) ((buttonStatus & 0x0f) << 4 | ((sensorId >> 24) & 0x0f));
        data[12] = 0;

        data[13] = 0;
        data[14] = 0;
        data[15] = 0;
        data[16] = 0;
        data[17] = 0;
        data[18] = 0;
        data[19] = 0;
        data[20] = 0;
        data[21] = 0;
        data[22] = 0;
        data[23] = 0;
        data[24] = 0;
        data[25] = 0;
        data[26] = 0;
        data[27] = 0;
        data[28] = (byte) (((batteryLevel & 0x0F) << 4) | (signalLevel & 0x0F));

        return data;
    }

    @Override
    public String generateDeviceId() {
        // As the sensorId can be the same used on other device protocols use "S2_" as Security2 prefix
        return "S2_" + String.valueOf(sensorId);
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        org.openhab.core.types.State state = UnDefType.UNDEF;

        if (valueSelector.getItemClass() == SwitchItem.class) {
            throw new RFXComException("Can't convert " + valueSelector + " to SwitchItem");
        } else if (valueSelector.getItemClass() == ContactItem.class) {

            if (valueSelector == RFXComValueSelector.CONTACT) {
                state = ((buttonStatus & BUTTON_0_BIT) == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;

            } else if (valueSelector == RFXComValueSelector.CONTACT_1) {
                state = ((buttonStatus & BUTTON_1_BIT) == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;

            } else if (valueSelector == RFXComValueSelector.CONTACT_2) {
                state = ((buttonStatus & BUTTON_2_BIT) == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;

            } else if (valueSelector == RFXComValueSelector.CONTACT_3) {
                state = ((buttonStatus & BUTTON_3_BIT) == 0) ? OpenClosedType.CLOSED : OpenClosedType.OPEN;

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to ContactItem");
            }

        } else if (valueSelector.getItemClass() == StringItem.class) {

            if (valueSelector == RFXComValueSelector.RAW_DATA) {

                state = new StringType(DatatypeConverter.printHexBinary(rawMessage));

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to StringItem");
            }

        } else if (valueSelector.getItemClass() == NumberItem.class) {

            if (valueSelector == RFXComValueSelector.SIGNAL_LEVEL) {

                state = new DecimalType(signalLevel);

            } else if (valueSelector == RFXComValueSelector.BATTERY_LEVEL) {

                state = new DecimalType(batteryLevel);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to StringItem");
            }

        } else if (valueSelector.getItemClass() == DateTimeItem.class) {

            state = new DateTimeType();

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
        String ids = id;
        sensorId = Integer.parseInt(ids);

        throw new RFXComException("Can't convert " + type + " to " + valueSelector);
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
