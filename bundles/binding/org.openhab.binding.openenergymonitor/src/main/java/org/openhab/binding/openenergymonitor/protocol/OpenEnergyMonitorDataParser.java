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
package org.openhab.binding.openenergymonitor.protocol;

import java.util.HashMap;
import java.util.Map.Entry;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorBinding;
import org.openhab.binding.openenergymonitor.protocol.OpenEnergyMonitorParserRule.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class for parse data packets from Open Energy Monitor devices.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorDataParser {

    private final Logger logger = LoggerFactory.getLogger(OpenEnergyMonitorBinding.class);

    private HashMap<String, OpenEnergyMonitorParserRule> parsingRules = null;

    private HashMap<Byte, Long> updateTimes = new HashMap<Byte, Long>();
    private int throttleTime = 0;

    public OpenEnergyMonitorDataParser(HashMap<String, OpenEnergyMonitorParserRule> parsingRules, int throttleTime) {

        this.parsingRules = parsingRules;
        this.throttleTime = throttleTime;
    }

    /**
     * Method to parse Open Energy Monitoring device datagram to variables by defined parsing rules.
     *
     * @param data datagram from Open Energy Monitoring device
     *
     * @return Hash table which contains all parsed variables.
     */
    public HashMap<String, Number> parseData(byte[] data) {

        HashMap<String, Number> variables = new HashMap<String, Number>();

        byte address = data[0];
        boolean parse = true;

        logger.debug("Received message from address '{}'", address);

        if (throttleTime > 0) {
            if ((getLastUpdateTime(address) + throttleTime) > System.currentTimeMillis()) {
                logger.debug("Skipping message parsing");
                parse = false;
            }
        }

        if (parse) {
            logger.debug("Parsing message");
            setLastUpdateTime(address, System.currentTimeMillis());
            for (Entry<String, OpenEnergyMonitorParserRule> entry : parsingRules.entrySet()) {
                OpenEnergyMonitorParserRule rule = entry.getValue();

                if (rule.getAddress() == address) {
                    Number obj = convertTo(rule.getDataType(), getBytes(rule.getParseBytes(), data));
                    variables.put(entry.getKey(), obj);
                }
            }
        }
        return variables;
    }

    private long getLastUpdateTime(byte address) {
        Long upudateTime = updateTimes.get(address);
        return upudateTime != null ? upudateTime : 0;
    }

    private void setLastUpdateTime(byte address, long time) {
        updateTimes.put(address, time);
    }

    private byte[] getBytes(int[] byteIndexes, byte[] data) {
        byte[] bytes = new byte[byteIndexes.length];

        for (int i = 0; i < byteIndexes.length; i++) {
            bytes[i] = data[byteIndexes[i]];
        }

        return bytes;
    }

    private Number convertTo(DataType dataType, byte[] data) {
        Number val = null;

        switch (dataType) {
            case DOUBLE:
                val = toDouble(data);
                break;
            case FLOAT:
                val = toFloat(data);
                break;
            case S16:
                val = toShort(data);
                break;
            case S32:
                val = toInt(data);
                break;
            case S64:
                val = toLong(data);
                break;
            case S8:
                val = toByte(data);
                break;
            case U16:
                val = (int) (toShort(data) & 0xFFFF);
                break;
            case U32:
                val = (long) (toInt(data) & 0xFFFFFFFFL);
                break;
            case U8:
                val = (short) (toByte(data) & 0xFF);
                break;
        }

        return val;
    }

    public static byte toByte(byte[] data) {
        return (data == null || data.length == 0) ? 0x0 : data[0];
    }

    public static short toShort(byte[] data) {
        // if (data == null || data.length != 2) return 0x0;

        return (short) ((0xff & data[0]) << 8 | (0xff & data[1]) << 0);
    }

    public static char toChar(byte[] data) {
        // if (data == null || data.length != 2) return 0x0;

        return (char) ((0xff & data[0]) << 8 | (0xff & data[1]) << 0);
    }

    public static int toInt(byte[] data) {
        // if (data == null || data.length != 4) return 0x0;

        return (0xff & data[0]) << 24 | (0xff & data[1]) << 16 | (0xff & data[2]) << 8 | (0xff & data[3]) << 0;
    }

    public static long toLong(byte[] data) {
        // if (data == null || data.length != 8) return 0x0;

        return (long) (0xff & data[0]) << 56 | (long) (0xff & data[1]) << 48 | (long) (0xff & data[2]) << 40
                | (long) (0xff & data[3]) << 32 | (long) (0xff & data[4]) << 24 | (long) (0xff & data[5]) << 16
                | (long) (0xff & data[6]) << 8 | (long) (0xff & data[7]) << 0;
    }

    public static float toFloat(byte[] data) {
        // if (data == null || data.length != 4) return 0x0;

        return Float.intBitsToFloat(toInt(data));
    }

    public static double toDouble(byte[] data) {
        // if (data == null || data.length != 8) return 0x0;

        return Double.longBitsToDouble(toLong(data));
    }

    public static boolean toBoolean(byte[] data) {
        return data[0] != 0x00;
    }

}
