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

import org.openhab.binding.powermax.internal.state.PowerMaxPanelSettings;
import org.openhab.binding.powermax.internal.state.PowerMaxState;

/**
 * A class for EVENTLOG message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxEventLogMessage extends PowerMaxBaseMessage {

    public static final String[] logEventTable = new String[] { "None", "Interior Alarm", "Perimeter Alarm",
            "Delay Alarm", "24h Silent Alarm", "24h Audible Alarm", "Tamper", "Control Panel Tamper", "Tamper Alarm",
            "Tamper Alarm", "Communication Loss", "Panic From Keyfob", "Panic From Control Panel", "Duress",
            "Confirm Alarm", "General Trouble", "General Trouble Restore", "Interior Restore", "Perimeter Restore",
            "Delay Restore", "24h Silent Restore", "24h Audible Restore", "Tamper Restore",
            "Control Panel Tamper Restore", "Tamper Restore", "Tamper Restore", "Communication Restore", "Cancel Alarm",
            "General Restore", "Trouble Restore", "Not used", "Recent Close", "Fire", "Fire Restore", "No Active",
            "Emergency", "No used", "Disarm Latchkey", "Panic Restore", "Supervision (Inactive)",
            "Supervision Restore (Active)", "Low Battery", "Low Battery Restore", "AC Fail", "AC Restore",
            "Control Panel Low Battery", "Control Panel Low Battery Restore", "RF Jamming", "RF Jamming Restore",
            "Communications Failure", "Communications Restore", "Telephone Line Failure", "Telephone Line Restore",
            "Auto Test", "Fuse Failure", "Fuse Restore", "Keyfob Low Battery", "Keyfob Low Battery Restore",
            "Engineer Reset", "Battery Disconnect", "1-Way Keypad Low Battery", "1-Way Keypad Low Battery Restore",
            "1-Way Keypad Inactive", "1-Way Keypad Restore Active", "Low Battery", "Clean Me", "Fire Trouble",
            "Low Battery", "Battery Restore", "AC Fail", "AC Restore", "Supervision (Inactive)",
            "Supervision Restore (Active)", "Gas Alert", "Gas Alert Restore", "Gas Trouble", "Gas Trouble Restore",
            "Flood Alert", "Flood Alert Restore", "X-10 Trouble", "X-10 Trouble Restore", "Arm Home", "Arm Away",
            "Quick Arm Home", "Quick Arm Away", "Disarm", "Fail To Auto-Arm", "Enter To Test Mode",
            "Exit From Test Mode", "Force Arm", "Auto Arm", "Instant Arm", "Bypass", "Fail To Arm", "Door Open",
            "Communication Established By Control Panel", "System Reset", "Installer Programming", "Wrong Password",
            "Not Sys Event", "Not Sys Event", "Extreme Hot Alert", "Extreme Hot Alert Restore", "Freeze Alert",
            "Freeze Alert Restore", "Human Cold Alert", "Human Cold Alert Restore", "Human Hot Alert",
            "Human Hot Alert Restore", "Temperature Sensor Trouble", "Temperature Sensor Trouble Restore",
            // new values partition models
            "PIR Mask", "PIR Mask Restore", "", "", "", "", "", "", "", "", "", "", "Alarmed", "Restore", "Alarmed",
            "Restore", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "Exit Installer", "Enter Installer",
            "", "", "", "", "" };

    public static final String[] logUserTable = new String[] { "System", "Zone 1", "Zone 2", "Zone 3", "Zone 4",
            "Zone 5", "Zone 6", "Zone 7", "Zone 8", "Zone 9", "Zone 10", "Zone 11", "Zone 12", "Zone 13", "Zone 14",
            "Zone 15", "Zone 16", "Zone 17", "Zone 18", "Zone 19", "Zone 20", "Zone 21", "Zone 22", "Zone 23",
            "Zone 24", "Zone 25", "Zone 26", "Zone 27", "Zone 28", "Zone 29", "Zone 30", "Fob 1", "Fob 2", "Fob 3",
            "Fob 4", "Fob 5", "Fob 6", "Fob 7", "Fob 8", "User 1", "User 2", "User 3", "User 4", "User 5", "User 6",
            "User 7", "User 8", "Pad 1", "Pad 2", "Pad 3", "Pad 4", "Pad 5", "Pad 6", "Pad 7", "Pad 8", "Siren 1",
            "Siren 2", "2Pad 1", "2Pad 2", "2Pad 3", "2Pad 4", "X10 1", "X10 2", "X10 3", "X10 4", "X10 5", "X10 6",
            "X10 7", "X10 8", "X10 9", "X10 10", "X10 11", "X10 12", "X10 13", "X10 14", "X10 15", "PGM", "GSM",
            "P-LINK", "PTag 1", "PTag 2", "PTag 3", "PTag 4", "PTag 5", "PTag 6", "PTag 7", "PTag 8" };

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxEventLogMessage(byte[] message) {
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
        int eventNum = message[3] & 0x000000FF;
        eventNum--;

        if (eventNum == 0) {
            int eventCnt = message[2] & 0x000000FF;
            updatedState.setEventLogSize(eventCnt - 1);
        } else {
            int second = message[4] & 0x000000FF;
            int minute = message[5] & 0x000000FF;
            int hour = message[6] & 0x000000FF;
            int day = message[7] & 0x000000FF;
            int month = message[8] & 0x000000FF;
            int year = (message[9] & 0x000000FF) + 2000;
            byte eventZone = message[10];
            byte logEvent = message[11];
            String logEventStr = ((logEvent & 0x000000FF) < logEventTable.length) ? logEventTable[logEvent & 0x000000FF]
                    : "UNKNOWN";
            String logUserStr = ((eventZone & 0x000000FF) < logUserTable.length) ? logUserTable[eventZone & 0x000000FF]
                    : "UNKNOWN";

            String eventStr;
            if (PowerMaxPanelSettings.getThePanelSettings().getPanelType().getPartitions() > 1) {
                String part;
                if ((second & 0x01) == 0x01) {
                    part = "Part. 1";
                } else if ((second & 0x02) == 0x02) {
                    part = "Part. 2";
                } else if ((second & 0x04) == 0x04) {
                    part = "Part. 3";
                } else {
                    part = "Panel";
                }
                eventStr = String.format("%02d/%02d/%04d %02d:%02d / %s: %s (%s)", day, month, year, hour, minute, part,
                        logEventStr, logUserStr);
            } else {
                eventStr = String.format("%02d/%02d/%04d %02d:%02d:%02d: %s (%s)", day, month, year, hour, minute,
                        second, logEventStr, logUserStr);
            }
            updatedState.setEventLogSize(eventNum);
            updatedState.setEventLog(eventNum, eventStr);
        }

        return updatedState;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        int eventNum = message[3] & 0x000000FF;

        str += "\n - event number = " + eventNum;

        if (eventNum == 1) {
            int eventCnt = message[2] & 0x000000FF;

            str += "\n - event count = " + eventCnt;
        } else {
            int second = message[4] & 0x000000FF;
            int minute = message[5] & 0x000000FF;
            int hour = message[6] & 0x000000FF;
            int day = message[7] & 0x000000FF;
            int month = message[8] & 0x000000FF;
            int year = (message[9] & 0x000000FF) + 2000;
            byte eventZone = message[10];
            byte logEvent = message[11];

            String logEventStr = ((logEvent & 0x000000FF) < logEventTable.length) ? logEventTable[logEvent & 0x000000FF]
                    : "UNKNOWN";
            String logUserStr = ((eventZone & 0x000000FF) < logUserTable.length) ? logUserTable[eventZone & 0x000000FF]
                    : "UNKNOWN";

            if (PowerMaxPanelSettings.getThePanelSettings().getPanelType().getPartitions() > 1) {
                str += "\n - event " + eventNum + " date & time = "
                        + String.format("%02d/%02d/%04d %02d:%02d", day, month, year, hour, minute);
                str += "\n - event " + eventNum + " partition = " + String.format("%02X", second);
            } else {
                str += "\n - event " + eventNum + " date & time = "
                        + String.format("%02d/%02d/%04d %02d:%02d:%02d", day, month, year, hour, minute, second);
            }
            str += "\n - event " + eventNum + " zone code = " + String.format("%08X - %s", eventZone, logUserStr);
            str += "\n - event " + eventNum + " event code = " + String.format("%08X - %s", logEvent, logEventStr);
        }

        return str;
    }

}
