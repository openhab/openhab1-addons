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
 * RFXCOM data class for temperature, humidity and barometric message.
 *
 * @author Damien Servant
 * @since 1.9.0
 */
public class RFXComTemperatureHumidityBarometricMessage extends RFXComBaseMessage {

    /*
     * Current packet layout (length 13) - BTHR918, BTHGN129, BTHR918N, BTHR968
     * packetlength = 0
     * packettype = 1
     * subtype = 2
     * seqnbr = 3
     * id1 = 4
     * id2 = 5
     * temperatureh = 6 'bits 6-0
     * tempsign = 6 'bit 7
     * temperaturel = 7
     * humidity = 8
     * humidity_status = 9
     * baroh = 10
     * barol = 11
     * forecast = 12
     * battery_level = 13 'bits 3-0
     * signal_level = 13 'bits 7-4
     */

    public enum SubType {
        BTHR918_BTHGN129(1), // BTHR918, BTHGN129
        BTHR918N_BTHR968(2), // BTHR918N, BTHR968

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

    public enum HumidityStatus {
        NORMAL(0),
        COMFORT(1),
        DRY(2),
        WET(3),

        UNKNOWN(255);

        private final int humidityStatus;

        HumidityStatus(int humidityStatus) {
            this.humidityStatus = humidityStatus;
        }

        HumidityStatus(byte humidityStatus) {
            this.humidityStatus = humidityStatus;
        }

        public byte toByte() {
            return (byte) humidityStatus;
        }

        public static HumidityStatus fromByte(int input) {
            for (HumidityStatus status : HumidityStatus.values()) {
                if (status.humidityStatus == input) {
                    return status;
                }
            }

            return HumidityStatus.UNKNOWN;
        }
    }

    public enum ForecastStatus {
        NO_INFO_AVAILABLE(0),
        SUNNY(1),
        PARTLY_CLOUDY(2),
        CLOUDY(3),
        RAIN(4),

        UNKNOWN(255);

        private final int forecastStatus;

        ForecastStatus(int forecastStatus) {
            this.forecastStatus = forecastStatus;
        }

        ForecastStatus(byte forecastStatus) {
            this.forecastStatus = forecastStatus;
        }

        public byte toByte() {
            return (byte) forecastStatus;
        }

        public static ForecastStatus fromByte(int input) {
            for (ForecastStatus status : ForecastStatus.values()) {
                if (status.forecastStatus == input) {
                    return status;
                }
            }

            return ForecastStatus.UNKNOWN;
        }
    }

    private final static List<RFXComValueSelector> supportedValueSelectors = Arrays.asList(RFXComValueSelector.RAW_DATA,
            RFXComValueSelector.SIGNAL_LEVEL, RFXComValueSelector.BATTERY_LEVEL, RFXComValueSelector.TEMPERATURE,
            RFXComValueSelector.HUMIDITY, RFXComValueSelector.HUMIDITY_STATUS, RFXComValueSelector.PRESSURE,
            RFXComValueSelector.FORECAST);

    public SubType subType = SubType.UNKNOWN;
    public int sensorId = 0;
    public double temperature = 0;
    public byte humidity = 0;
    public HumidityStatus humidityStatus = HumidityStatus.UNKNOWN;
    public double pressure = 0;
    public ForecastStatus forecastStatus = ForecastStatus.UNKNOWN;
    public byte signalLevel = 0;
    public byte batteryLevel = 0;

    public RFXComTemperatureHumidityBarometricMessage() {
        packetType = PacketType.TEMPERATURE_HUMIDITY_BAROMETRIC;
    }

    public RFXComTemperatureHumidityBarometricMessage(byte[] data) {
        encodeMessage(data);
    }

    @Override
    public String toString() {
        String str = "";

        str += super.toString();
        str += "\n - Sub type = " + subType;
        str += "\n - Id = " + sensorId;
        str += "\n - Temperature = " + temperature;
        str += "\n - Humidity = " + humidity;
        str += "\n - Humidity status = " + humidityStatus;
        str += "\n - Pressure = " + pressure;
        str += "\n - Forecast = " + forecastStatus;
        str += "\n - Signal level = " + signalLevel;
        str += "\n - Battery level = " + batteryLevel;

        return str;
    }

    @Override
    public void encodeMessage(byte[] data) {

        super.encodeMessage(data);

        subType = SubType.fromByte(super.subType);
        sensorId = (data[4] & 0xFF) << 8 | (data[5] & 0xFF);

        temperature = (short) ((data[6] & 0x7F) << 8 | (data[7] & 0xFF)) * 0.1;
        if ((data[6] & 0x80) != 0) {
            temperature = -temperature;
        }

        humidity = data[8];
        humidityStatus = HumidityStatus.fromByte(data[9]);

        pressure = (data[10] & 0xFF) << 8 | (data[11] & 0xFF);
        forecastStatus = ForecastStatus.fromByte(data[12]);

        signalLevel = (byte) ((data[13] & 0xF0) >> 4);
        batteryLevel = (byte) (data[13] & 0x0F);
    }

    @Override
    public byte[] decodeMessage() {
        byte[] data = new byte[14];

        data[0] = 0x0D;
        data[1] = RFXComBaseMessage.PacketType.TEMPERATURE_HUMIDITY_BAROMETRIC.toByte();
        data[2] = subType.toByte();
        data[3] = seqNbr;
        data[4] = (byte) ((sensorId & 0xFF00) >> 8);
        data[5] = (byte) (sensorId & 0x00FF);

        short temp = (short) Math.abs(temperature * 10);
        data[6] = (byte) ((temp >> 8) & 0xFF);
        data[7] = (byte) (temp & 0xFF);
        if (temperature < 0) {
            data[6] |= 0x80;
        }

        data[8] = humidity;
        data[9] = humidityStatus.toByte();

        temp = (short) (pressure);
        data[10] = (byte) ((temp >> 8) & 0xFF);
        data[11] = (byte) (temp & 0xFF);

        data[12] = forecastStatus.toByte();

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

            } else if (valueSelector == RFXComValueSelector.TEMPERATURE) {

                state = new DecimalType(temperature);

            } else if (valueSelector == RFXComValueSelector.HUMIDITY) {

                state = new DecimalType(humidity);

            } else if (valueSelector == RFXComValueSelector.PRESSURE) {

                state = new DecimalType(pressure);

            } else {
                throw new RFXComException("Can't convert " + valueSelector + " to NumberItem");
            }

        } else if (valueSelector.getItemClass() == StringItem.class) {

            if (valueSelector == RFXComValueSelector.RAW_DATA) {

                state = new StringType(DatatypeConverter.printHexBinary(rawMessage));

            } else if (valueSelector == RFXComValueSelector.HUMIDITY_STATUS) {

                state = new StringType(humidityStatus.toString());

            } else if (valueSelector == RFXComValueSelector.FORECAST) {

                state = new StringType(forecastStatus.toString());

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
