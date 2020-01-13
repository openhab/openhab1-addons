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
package org.openhab.binding.powermax.internal.message;

import java.util.HashMap;

import org.openhab.binding.powermax.internal.state.PowerMaxState;

/**
 * A class for PANEL message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxPanelMessage extends PowerMaxBaseMessage {

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxPanelMessage(byte[] message) {
        super(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = new PowerMaxState();

        byte[] message = getRawData();
        int msgCnt = message[2] & 0x000000FF;

        for (int i = 1; i <= msgCnt; i++) {
            byte eventZone = message[2 + 2 * i];
            byte logEvent = message[3 + 2 * i];
            int eventType = logEvent & 0x0000007F;
            String logEventStr = (eventType < PowerMaxEventLogMessage.logEventTable.length)
                    ? PowerMaxEventLogMessage.logEventTable[eventType] : "UNKNOWN";
            String logUserStr = ((eventZone & 0x000000FF) < PowerMaxEventLogMessage.logUserTable.length)
                    ? PowerMaxEventLogMessage.logUserTable[eventZone & 0x000000FF] : "UNKNOWN";
            updatedState.setPanelStatus(logEventStr + " (" + logUserStr + ")");

            HashMap<Integer, String> alarmTypes = new HashMap<Integer, String>();
            alarmTypes.put(0x01, "Intruder");
            alarmTypes.put(0x02, "Intruder");
            alarmTypes.put(0x03, "Intruder");
            alarmTypes.put(0x04, "Intruder");
            alarmTypes.put(0x05, "Intruder");
            alarmTypes.put(0x06, "Tamper");
            alarmTypes.put(0x07, "Tamper");
            alarmTypes.put(0x08, "Tamper");
            alarmTypes.put(0x09, "Tamper");
            alarmTypes.put(0x0B, "Panic");
            alarmTypes.put(0x0C, "Panic");
            alarmTypes.put(0x20, "Fire");
            alarmTypes.put(0x23, "Emergency");
            alarmTypes.put(0x49, "Gas");
            alarmTypes.put(0x4D, "Flood");
            String alarmStatus = alarmTypes.get(eventType);
            if (alarmStatus == null) {
                alarmStatus = "None";
            }
            updatedState.setAlarmType(alarmStatus);

            HashMap<Integer, String> troubleTypes = new HashMap<Integer, String>();
            troubleTypes.put(0x0A, "Communication");
            troubleTypes.put(0x0F, "General");
            troubleTypes.put(0x29, "Battery");
            troubleTypes.put(0x2B, "Power");
            troubleTypes.put(0x2D, "Battery");
            troubleTypes.put(0x2F, "Jamming");
            troubleTypes.put(0x31, "Communication");
            troubleTypes.put(0x33, "Telephone");
            troubleTypes.put(0x36, "Power");
            troubleTypes.put(0x38, "Battery");
            troubleTypes.put(0x3B, "Battery");
            troubleTypes.put(0x3C, "Battery");
            troubleTypes.put(0x40, "Battery");
            troubleTypes.put(0x43, "Battery");
            String troubleStatus = troubleTypes.get(eventType);
            if (troubleStatus == null) {
                troubleStatus = "None";
            }
            updatedState.setTroubleType(troubleStatus);

            if (eventType == 0x60) {
                // System reset
                updatedState.setDownloadSetupRequired(true);
            }
        }

        return updatedState;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        int msgCnt = message[2] & 0x000000FF;

        str += "\n - event count = " + msgCnt;
        for (int i = 1; i <= msgCnt; i++) {
            byte eventZone = message[2 + 2 * i];
            byte logEvent = message[3 + 2 * i];

            str += "\n - event " + i + " zone code = " + String.format("%08X", eventZone);
            str += "\n - event " + i + " event code = " + String.format("%08X", logEvent);
        }

        return str;
    }

}
