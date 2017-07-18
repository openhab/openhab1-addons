/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import java.util.HashMap;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationCommandType;
import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;

/**
 * Class for parse data packets from Swegon ventilation system.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationDataParser {

    public static HashMap<SwegonVentilationCommandType, Integer> parseData(byte[] data)
            throws SwegonVentilationException {

        if (data[0] == (byte) 0x64) {

            @SuppressWarnings("unused")
            byte unknownByte = data[1];
            @SuppressWarnings("unused")
            byte destinationAddress = data[2];
            @SuppressWarnings("unused")
            byte sourceAddress = data[3];
            byte dataLen = data[4];
            byte msgType = data[5];

            byte[] d = new byte[dataLen - 3];

            for (int i = 8; i < (data.length - 3); i++) {
                d[i - 8] = data[i];
            }

            switch (msgType) {
                case (byte) 0x21:
                    return parseMessage21(d);
                case (byte) 0x71:
                    return parseMessage71(d);
                case (byte) 0x73:
                    return parseMessage73(d);
            }

        } else {
            throw new SwegonVentilationException("Illegal data received, first byte mismatch!");
        }

        return null;
    }

    private static HashMap<SwegonVentilationCommandType, Integer> parseMessage21(byte[] data) {

        HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

        int operatingMode = data[0];
        int unitState = data[1];
        int fanSpeed = data[3] & 0x0F;

        map.put(SwegonVentilationCommandType.OPERATING_MODE, operatingMode);
        map.put(SwegonVentilationCommandType.UNIT_STATE, unitState);
        map.put(SwegonVentilationCommandType.FAN_SPEED, fanSpeed);

        return map;
    }

    private static HashMap<SwegonVentilationCommandType, Integer> parseMessage71(byte[] data) {

        HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

        int outdoorTemp = data[0];
        int supplyTemp = data[1];
        int extractTemp = data[2];
        int supplyTempHeated = data[3];
        int t5 = data[4];
        int t6 = data[5];
        int t7 = data[6];
        int exhaustTemp = data[7];
        int co2 = data[8];
        int rh = data[9];
        int supplyFanSpeed = (data[10] & 0xFF) * 10;
        int extractFanSpeed = (data[11] & 0xFF) * 10;
        int efficiency = data[12];

        map.put(SwegonVentilationCommandType.T1, outdoorTemp);
        map.put(SwegonVentilationCommandType.OUTDOOR_TEMP, outdoorTemp);
        map.put(SwegonVentilationCommandType.T2, supplyTemp);
        map.put(SwegonVentilationCommandType.SUPPLY_TEMP, supplyTemp);
        map.put(SwegonVentilationCommandType.T3, extractTemp);
        map.put(SwegonVentilationCommandType.EXTRACT_TEMP, extractTemp);
        map.put(SwegonVentilationCommandType.T4, supplyTempHeated);
        map.put(SwegonVentilationCommandType.SUPPLY_TEMP_HEATED, supplyTempHeated);
        map.put(SwegonVentilationCommandType.T8, exhaustTemp);
        map.put(SwegonVentilationCommandType.EXHAUST_TEMP, exhaustTemp);
        map.put(SwegonVentilationCommandType.T5, t5);
        map.put(SwegonVentilationCommandType.T6, t6);
        map.put(SwegonVentilationCommandType.T7, t7);
        map.put(SwegonVentilationCommandType.CO2, co2);
        map.put(SwegonVentilationCommandType.HUMIDITY, rh);
        map.put(SwegonVentilationCommandType.SUPPLY_AIR_FAN_SPEED, supplyFanSpeed);
        map.put(SwegonVentilationCommandType.EXTRACT_AIR_FAN_SPEED, extractFanSpeed);
        map.put(SwegonVentilationCommandType.EFFICIENCY, efficiency);

        // Calculate supply efficiency
        int calcEfficiency = (int) (((double) supplyTemp - (double) outdoorTemp)
                / ((double) extractTemp - (double) outdoorTemp) * 100);
        map.put(SwegonVentilationCommandType.EFFICIENCY_SUPPLY, calcEfficiency);

        // Calculate extract efficiency
        calcEfficiency = (int) (((double) extractTemp - (double) exhaustTemp)
                / ((double) extractTemp - (double) outdoorTemp) * 100);
        map.put(SwegonVentilationCommandType.EFFICIENCY_EXTRACT, calcEfficiency);

        return map;
    }

    private static HashMap<SwegonVentilationCommandType, Integer> parseMessage73(byte[] data) {

        HashMap<SwegonVentilationCommandType, Integer> map = new HashMap<SwegonVentilationCommandType, Integer>();

        map.put(SwegonVentilationCommandType.HEATING_STATE, (data[0] & 0x01) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.COOLING_STATE, (data[0] & 0x02) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.BYBASS_STATE, (data[0] & 0x04) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.FREEZE_PROTECTION_STATE, (data[0] & 0x08) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.PREHEAT_STATE, (data[0] & 0x10) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.PREHEATING_STATE, (data[0] & 0x10) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.CHILLING_STATE, (data[0] & 0x20) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.PREHEATER_OVERHEAT_STATE, (data[0] & 0x40) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.REHEAT_STATE, (data[0] & 0x80) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.REHEATING_STATE, (data[0] & 0x80) > 0 ? 1 : 0);

        map.put(SwegonVentilationCommandType.FIREPLACE_FUNCTION_STATE, (data[1] & 0x01) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.UNDERPRESSURE_COMPENSATION_STATE, (data[1] & 0x02) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.EXTERNAL_BOOST_STATE, (data[1] & 0x04) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.HUMIDITY_BOOST_STATE, (data[1] & 0x08) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.CO2_BOOST_STATE, (data[1] & 0x10) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.DEFROSTING_STATE, (data[1] & 0x20) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.DEFROST_STARTER_MODE, (data[1] & 0x40));
        map.put(SwegonVentilationCommandType.TF_STOP_STATE, (data[1] & 0x80) > 0 ? 1 : 0);

        map.put(SwegonVentilationCommandType.EXTERNAL_BOOST_FUNCTION_STATE, (data[3] & 0x04) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.EXTERNAL_FIREPLACE_FUNCTION_STATE, (data[3] & 0x08) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.FILTER_GUARD_STATUS, (data[3] & 0x10) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.IR_FREEZE_PROTECTION_STATUS, (data[3] & 0x20) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.EMERGENCY_STOP_STATE, (data[3] & 0x80) > 0 ? 1 : 0);

        map.put(SwegonVentilationCommandType.REHEATING_FREEZING_ALARM, (data[7] & 0x01) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.REHEATING_OVERHEAT_ALARM, (data[7] & 0x02) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.IR_SENSOR_FAILURE, (data[7] & 0x04) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.SUPPLY_FAN_FAILURE, (data[7] & 0x08) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.EXTRACT_FAN_FAILURE, (data[7] & 0x10) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.TEMPERATURE_DEVIATION_FAILURE, (data[7] & 0x20) > 0 ? 1 : 0);

        map.put(SwegonVentilationCommandType.EFFICINECY_ALARM, (data[8] & 0x01) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.FILTER_GUARD_ALARM, (data[8] & 0x02) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.SERVICE_REMINDER, (data[8] & 0x04) > 0 ? 1 : 0);
        map.put(SwegonVentilationCommandType.TEMPERATURE_FAILURE, (data[8] & 0x08) > 0 ? 1 : 0);

        map.put(SwegonVentilationCommandType.AFTERHEATING_SETPOINT_SUPPLY_AIR_REGULATED, (int) data[10]);
        map.put(SwegonVentilationCommandType.AFTERHEATING_SETPOINT_ROOM_REGULATED, (int) data[11]);
        map.put(SwegonVentilationCommandType.SUPPLY_FAN_VIRTUAL_SPEED, (int) data[12]);
        map.put(SwegonVentilationCommandType.EXTRACT_FAN_VIRTUAL_SPEED, (int) data[13]);
        map.put(SwegonVentilationCommandType.UNIT_STATUS, (int) data[14]);

        return map;
    }
}
