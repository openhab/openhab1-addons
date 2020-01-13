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
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM undecoded message
 *
 * @author Ivan Martinez
 * @since 1.9.0
 */
public class RFXComUndecodedRFMessage extends RFXComBaseMessage {

    public enum SubType {
        AC(0x00),
        ARC(0x01),
        ATI(0x02),
        HIDEKI_UPM(0x03),
        LACROSSE_VIKING(0x04),
        AD(0x05),
        MERTIK(0x06),
        OREGON1(0x07),
        OREGON2(0x08),
        OREGON3(0x09),
        PROGUARD(0x0a),
        VISONIC(0x0b),
        NEC(0x0c),
        FS20(0x0d),
        RESERVED(0x0e),
        BLINDS(0x0f),
        RUBICSON(0x10),
        AE(0x11),
        FINE_OFFSET(0x12),
        RGB(0x13),
        RTS(0x14),
        SELECT_PLUS(0x15),
        HOME_CONFORT(0x16),

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
            RFXComValueSelector.DATA);

    public SubType subType = SubType.UNKNOWN;
    private byte[] rawData = new byte[0];

    public RFXComUndecodedRFMessage() {
        packetType = PacketType.UNDECODED_RF_MESSAGE;
    }

    public RFXComUndecodedRFMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + generateDeviceId();
        str += "\n - Data = " + DatatypeConverter.printHexBinary(rawData);

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        rawData = Arrays.copyOfRange(rawMessage, 4, rawMessage.length);
    }

    @Override
    public byte[] decodeMessage() {
        final int rawLen = Math.min(rawData.length, 33);
        byte[] data = new byte[4 + rawLen];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.UNDECODED_RF_MESSAGE.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        for (int i = 0; i < rawLen; i++) {
            data[i + 4] = rawData[i];
        }
        return data;
    }

    @Override
    public String generateDeviceId() {
        return "UNDECODED";
    }

    @Override
    public State convertToState(RFXComValueSelector valueSelector) throws RFXComException {

        org.openhab.core.types.State state = UnDefType.UNDEF;

        if (valueSelector.getItemClass() == StringItem.class) {
            if (valueSelector == RFXComValueSelector.RAW_DATA) {
                state = new StringType(DatatypeConverter.printHexBinary(rawMessage));
            } else if (valueSelector == RFXComValueSelector.DATA) {
                state = new StringType(DatatypeConverter.printHexBinary(rawData));
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

        throw new RFXComException("Not supported");
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
