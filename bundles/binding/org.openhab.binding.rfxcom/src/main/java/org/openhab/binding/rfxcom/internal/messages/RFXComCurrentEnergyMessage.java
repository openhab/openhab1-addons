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
 * RFXCOM data class for Current and Energy message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComCurrentEnergyMessage extends RFXComBaseMessage {

    /*
     * Current packet layout (length 19) - ELEC4 - OWL - CM180i
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * id1 = 4
     * id2 = 5
     * count = 6
     * ch1h = 7
     * ch1l = 8
     * ch2h = 9
     * ch2l = 10
     * ch3h = 11
     * ch3l = 12
     * total1 = 13
     * total2 = 14
     * total3 = 15
     * total4 = 16
     * total5 = 17
     * total6 = 18
     * battery_level : 19 //bits 3-0
     * signal_level : 19 //bits 7-4
     */

    private static float TOTAL_USAGE_CONVERSION_FACTOR = 223.666F;

    public enum SubType {
        ELEC4(1), // OWL - CM180i

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
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.BATTERY_LEVEL, RFXComValueSelector.CHANNEL1_AMPS,
            RFXComValueSelector.CHANNEL2_AMPS, RFXComValueSelector.CHANNEL3_AMPS, RFXComValueSelector.TOTAL_USAGE);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public byte count = 0;
    public double channel1Amps = 0.0;
    public double channel2Amps = 0.0;
    public double channel3Amps = 0.0;
    public double totalUsage = 0.0;
    public byte signalLevel = 0;
    public byte batteryLevel = 0;

    public RFXComCurrentEnergyMessage() {
        packetType = PacketType.CURRENT_ENERGY;
    }

    public RFXComCurrentEnergyMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Count = " + count;
        str += "\n - Channel1 Amps = " + channel1Amps;
        str += "\n - Channel2 Amps = " + channel2Amps;
        str += "\n - Channel3 Amps = " + channel3Amps;
        str += "\n - Total Usage = " + totalUsage;
        str += "\n - Signal level = " + signalLevel;
        str += "\n - Battery level = " + batteryLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);
        count = data[6];

        // Current = Field / 10
        channel1Amps = ((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 10.0;
        channel2Amps = ((data[9] & 0xFF) << 8 | (data[10] & 0xFF)) / 10.0;
        channel3Amps = ((data[11] & 0xFF) << 8 | (data[12] & 0xFF)) / 10.0;

        totalUsage = ((long) (data[13] & 0xFF) << 40 | (long) (data[14] & 0xFF) << 32 | (data[15] & 0xFF) << 24
                | (data[16] & 0xFF) << 16 | (data[17] & 0xFF) << 8 | (data[18] & 0xFF));
        totalUsage = totalUsage / TOTAL_USAGE_CONVERSION_FACTOR;

        signalLevel = (byte) ((data[19] & 0xF0) >> 4);
        batteryLevel = (byte) (data[19] & 0x0F);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[20];

        data[0] = (byte) (data.length - 1);
        data[1] = RFXComBaseMessage.PacketType.CURRENT_ENERGY.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        data[4] = (byte) ((sensorId & 0xFF00) >> 8);
        data[5] = (byte) (sensorId & 0x00FF);
        data[6] = count;

        data[7] = (byte) (((int) (channel1Amps * 10.0) >> 8) & 0xFF);
        data[8] = (byte) ((int) (channel1Amps * 10.0) & 0xFF);
        data[9] = (byte) (((int) (channel2Amps * 10.0) >> 8) & 0xFF);
        data[10] = (byte) ((int) (channel2Amps * 10.0) & 0xFF);
        data[11] = (byte) (((int) (channel3Amps * 10.0) >> 8) & 0xFF);
        data[12] = (byte) ((int) (channel3Amps * 10.0) & 0xFF);

        long totalUsageLoc = (long) (totalUsage * TOTAL_USAGE_CONVERSION_FACTOR);

        data[13] = (byte) ((totalUsageLoc >> 40) & 0xFF);
        data[14] = (byte) ((totalUsageLoc >> 32) & 0xFF);
        data[15] = (byte) ((totalUsageLoc >> 24) & 0xFF);
        data[16] = (byte) ((totalUsageLoc >> 16) & 0xFF);
        data[17] = (byte) ((totalUsageLoc >> 8) & 0xFF);
        data[18] = (byte) (totalUsageLoc & 0xFF);

        data[19] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

            } else if (valueSelector == RFXComValueSelector.CHANNEL1_AMPS) {

                state = new DecimalType(channel1Amps);

            } else if (valueSelector == RFXComValueSelector.CHANNEL2_AMPS) {

                state = new DecimalType(channel2Amps);

            } else if (valueSelector == RFXComValueSelector.CHANNEL3_AMPS) {

                state = new DecimalType(channel3Amps);

            } else if (valueSelector == RFXComValueSelector.TOTAL_USAGE) {

                state = new DecimalType(totalUsage);

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
