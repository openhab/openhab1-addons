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
 * RFXCOM data class for power message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComPowerMessage extends RFXComBaseMessage {

    /*
     * Power packet layout (length 16) - ELEC5
     *
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * id1 = 4
     * id2 = 5
     * voltage = 6
     * currentH = 7
     * currentL = 8
     * powerH = 9
     * powerL = 10
     * energyH = 11
     * energyL = 12
     * pf = 13
     * freq = 14
     * battery_level = 15 //bits 3-0
     * signal_level = 15 //bits 7-4
     */

    public enum SubType {
        ELEC5(1), // Revolt

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
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.BATTERY_LEVEL, RFXComValueSelector.VOLTAGE,
            RFXComValueSelector.INSTANT_AMPS, RFXComValueSelector.INSTANT_POWER, RFXComValueSelector.INSTANT_ENERGY,
            RFXComValueSelector.POWER_FACTOR, RFXComValueSelector.FREQUENCY);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public byte count = 0;
    public int voltage = 0;
    public double instantAmps = 0;
    public double instantPower = 0;
    public double instantEnergy = 0;
    public double powerFactor = 0;
    public int frequency = 0;
    public byte signalLevel = 0;
    public byte batteryLevel = 0;

    public RFXComPowerMessage() {
        packetType = PacketType.POWER;
    }

    public RFXComPowerMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Count = " + count;
        str += "\n - Voltage = " + voltage;
        str += "\n - Instant Amps = " + instantAmps;
        str += "\n - Instant Power = " + instantPower;
        str += "\n - Instant Energy = " + instantEnergy;
        str += "\n - Power Factor = " + powerFactor;
        str += "\n - Frequency = " + frequency;
        str += "\n - Signal level = " + signalLevel;
        str += "\n - Battery level = " + batteryLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);
        voltage = (data[6] & 0xFF);

        instantAmps = ((data[7] & 0xFF) << 8 | (data[8] & 0xFF)) / 100.0; // Current = Field / 100
        instantPower = ((data[9] & 0xFF) << 8 | (data[10] & 0xFF)) / 10.0; // Watt
        instantEnergy = ((data[11] & 0xFF) << 8 | (data[12] & 0xFF)) / 100.0; // kWh
        powerFactor = data[13] / 100.0;
        frequency = data[14];

        signalLevel = (byte) ((data[15] & 0xF0) >> 4);
        batteryLevel = (byte) (data[15] & 0x0F);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[16];

        data[0] = 0x0F;
        data[1] = RFXComBaseMessage.PacketType.POWER.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;

        data[4] = (byte) ((sensorId & 0xFF00) >> 8);
        data[5] = (byte) (sensorId & 0x00FF);

        data[6] = (byte) voltage;
        data[7] = (byte) (((int) (instantAmps * 100.0) >> 8) & 0xFF);
        data[8] = (byte) ((int) (instantAmps * 100.0) & 0xFF);
        data[9] = (byte) (((int) (instantPower * 10.0) >> 8) & 0xFF);
        data[10] = (byte) ((int) (instantPower * 10.0) & 0xFF);
        data[11] = (byte) (((int) (instantEnergy * 100.0) >> 8) & 0xFF);
        data[12] = (byte) ((int) (instantEnergy * 100.0) & 0xFF);
        data[13] = (byte) ((int) (powerFactor * 100.0) & 0xFF);
        data[14] = (byte) frequency;

        data[15] = (byte) (((signalLevel & 0x0F) << 4) | (batteryLevel & 0x0F));

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

            } else if (valueSelector == RFXComValueSelector.VOLTAGE) {

                state = new DecimalType(voltage);

            } else if (valueSelector == RFXComValueSelector.INSTANT_AMPS) {

                state = new DecimalType(instantAmps);

            } else if (valueSelector == RFXComValueSelector.INSTANT_POWER) {

                state = new DecimalType(instantPower);

            } else if (valueSelector == RFXComValueSelector.INSTANT_ENERGY) {

                state = new DecimalType(instantEnergy);

            } else if (valueSelector == RFXComValueSelector.POWER_FACTOR) {

                state = new DecimalType(powerFactor);

            } else if (valueSelector == RFXComValueSelector.FREQUENCY) {

                state = new DecimalType(frequency);

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
