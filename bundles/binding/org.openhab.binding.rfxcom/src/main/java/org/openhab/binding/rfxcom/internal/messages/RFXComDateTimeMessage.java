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
import org.openhab.core.library.items.DateTimeItem;
import org.openhab.core.library.items.NumberItem;
import org.openhab.core.library.items.StringItem;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * RFXCOM data class for Date and Time message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComDateTimeMessage extends RFXComBaseMessage {

    /*
     * Current packet layout (length 13) - RTGR328N
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * id1 = 4
     * id2 = 5
     * yy = 6
     * mm = 7
     * dd = 8
     * dow = 9
     * hr = 10
     * Min = 11
     * sec = 12
     * battery_level = 13 'bits 3-0
     * rssi = 13 'bits 7-4
     */
    public enum SubType {
        RTGR328N(1),

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
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.BATTERY_LEVEL, RFXComValueSelector.DATE_TIME);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public String dateTime = "yyyy-MM-dd'T'HH:mm:ss";
    public int yy = 0;
    public int MM = 0;
    public int dd = 0;
    public int dow = 0;
    public int HH = 0;
    public int mm = 0;
    public int ss = 0;

    public byte signalLevel = 0;
    public byte batteryLevel = 0;

    public RFXComDateTimeMessage() {
        packetType = PacketType.DATE_TIME;
    }

    public RFXComDateTimeMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Date Time = " + dateTime;
        str += "\n - Signal level = " + signalLevel;
        str += "\n - Battery level = " + batteryLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        /*
         * Current packet layout (length 13) - RTGR328N
         * packetlength = 0
         * packettype = 1
         * subtype = 2
         * seqnbr = 3
         * id1 = 4
         * id2 = 5
         * yy = 6
         * mm = 7
         * dd = 8
         * dow = 9
         * hr = 10
         * Min = 11
         * sec = 12
         * battery_level = 13 'bits 3-0
         * rssi = 13 'bits 7-4
         */
        sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);

        // dateTime = "yyyy-MM-dd'T'HH:mm:ss";
        yy = data[6] & 0xFF;
        MM = data[7] & 0xFF;
        dd = data[8] & 0xFF;
        dow = data[9] & 0xFF;
        HH = data[10] & 0xFF;
        mm = data[11] & 0xFF;
        ss = data[12] & 0xFF;

        dateTime = "20" + yy;
        dateTime += "-" + MM;
        dateTime += "-" + dd;
        dateTime += "T" + HH;
        dateTime += ":" + mm;
        dateTime += ":" + ss;

        signalLevel = (byte) ((data[13] & 0xF0) >> 4);
        batteryLevel = (byte) (data[13] & 0x0F);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[14];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.DATE_TIME.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = (byte) ((sensorId & 0xFF00) >> 8);
        data[5] = (byte) (sensorId & 0x00FF);
        data[6] = (byte) (yy & 0x00FF);
        data[7] = (byte) (MM & 0x00FF);
        data[8] = (byte) (dd & 0x00FF);
        data[9] = (byte) (dow & 0x00FF);
        data[10] = (byte) (HH & 0x00FF);
        data[11] = (byte) (mm & 0x00FF);
        data[12] = (byte) (ss & 0x00FF);
        data[13] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

            } else if (valueSelector == RFXComValueSelector.BATTERY_LEVEL) {

                state = new DecimalType(batteryLevel);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
            }

        } else if (valueSelector.getItemClass() == DateTimeItem.class) {

            if (valueSelector == RFXComValueSelector.DATE_TIME) {

                state = new DateTimeType(dateTime);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to StringItem");
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
