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
package org.openhab.binding.powermax.internal.state;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;

import org.openhab.binding.powermax.internal.message.PowerMaxSendType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class to store all the settings of the alarm system
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxPanelSettings {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxPanelSettings.class);

    /** Number of PGM and X10 devices managed by the system */
    private static final int NB_PGM_X10_DEVICES = 16;

    private static final Charset CHARSET = Charset.forName("ISO-8859-1");

    /** The unique instance of this class */
    private static PowerMaxPanelSettings thePanelSettings = null;

    /** Raw buffers for settings */
    private Byte[][] rawSettings;

    private PowerMaxPanelType panelType;
    private HashMap<Byte, String> sensorTypes;
    private String[] zoneNames;
    private String[] phoneNumbers;
    private int bellTime;
    private boolean silentPanic;
    private boolean quickArm;
    private boolean bypassEnabled;
    private boolean partitionsEnabled;
    private String[] pinCodes;
    private String panelEprom;
    private String panelSoftware;
    private String panelSerial;
    private PowerMaxZoneSettings[] zoneSettings;
    private PowerMaxX10Settings[] x10Settings;
    private boolean[] keypad1wEnrolled;
    private boolean[] keypad2wEnrolled;
    private boolean[] sirensEnrolled;

    /**
     * Constructor
     *
     * @param defaultPanelType
     *            the default panel type to consider
     */
    private PowerMaxPanelSettings(PowerMaxPanelType defaultPanelType) {
        rawSettings = new Byte[0x100][];
        for (int i = 0; i < 0x100; i++) {
            rawSettings[i] = null;
        }
        panelType = defaultPanelType;
        sensorTypes = new HashMap<Byte, String>();
        if (panelType.isPowerMaster()) {
            sensorTypes.put((byte) 0x01, "Motion");
            sensorTypes.put((byte) 0x04, "Camera");
            sensorTypes.put((byte) 0x16, "Smoke");
            sensorTypes.put((byte) 0x1A, "Temperature");
            sensorTypes.put((byte) 0x2A, "Magnet");
            sensorTypes.put((byte) 0xFE, "Wired");
        } else {
            sensorTypes.put((byte) 0x03, "Motion");
            sensorTypes.put((byte) 0x04, "Motion");
            sensorTypes.put((byte) 0x05, "Magnet");
            sensorTypes.put((byte) 0x06, "Magnet");
            sensorTypes.put((byte) 0x07, "Magnet");
            sensorTypes.put((byte) 0x0A, "Smoke");
            sensorTypes.put((byte) 0x0B, "Gas");
            sensorTypes.put((byte) 0x0C, "Motion");
            sensorTypes.put((byte) 0x0F, "Wired");
        }
        zoneNames = new String[] { "Attic", "Back door", "Basement", "Bathroom", "Bedroom", "Child room", "Closet",
                "Den", "Dining room", "Downstairs", "Emergency", "Fire", "Front door", "Garage", "Garage door",
                "Guest room", "Hall", "Kitchen", "Laundry room", "Living room", "Master bathroom", "Master bedroom",
                "Office", "Upstairs", "Utility room", "Yard", "Custom 1", "Custom 2", "Custom 3", "Custom4", "Custom 5",
                "Not Installed" };
        phoneNumbers = new String[] { null, null, null, null };
        bellTime = 4;
        silentPanic = false;
        quickArm = false;
        bypassEnabled = false;
        partitionsEnabled = false;
        pinCodes = null;
        panelEprom = null;
        panelSoftware = null;
        panelSerial = null;
        int zoneCnt = panelType.getWireless() + panelType.getWired();
        zoneSettings = new PowerMaxZoneSettings[zoneCnt];
        for (int i = 0; i < zoneCnt; i++) {
            zoneSettings[i] = null;
        }
        x10Settings = new PowerMaxX10Settings[NB_PGM_X10_DEVICES];
        for (int i = 0; i < NB_PGM_X10_DEVICES; i++) {
            x10Settings[i] = null;
        }
        keypad1wEnrolled = null;
        keypad2wEnrolled = null;
        sirensEnrolled = null;
    }

    /**
     * Initialize new panel settings
     * Has to be called before using the class
     */
    public static void initPanelSettings(PowerMaxPanelType panelType) {
        thePanelSettings = new PowerMaxPanelSettings(panelType);
    }

    /**
     * Get the panel settings
     *
     * @return the unique instance of class PowerMaxPanelSettings
     */
    public static PowerMaxPanelSettings getThePanelSettings() {
        return thePanelSettings;
    }

    /**
     * @return the panel type
     */
    public PowerMaxPanelType getPanelType() {
        return panelType;
    }

    /**
     * @return true if bypassing zones is enabled; false if not
     */
    public boolean isBypassEnabled() {
        return bypassEnabled;
    }

    /**
     * @return true if partitions usage is enabled; false if not
     */
    public boolean isPartitionsEnabled() {
        return partitionsEnabled;
    }

    /**
     * @return the panel EEPROM version
     */
    public String getPanelEprom() {
        return panelEprom;
    }

    /**
     * @return the panel software version
     */
    public String getPanelSoftware() {
        return panelSoftware;
    }

    /**
     * @return the panel serial ID
     */
    public String getPanelSerial() {
        return panelSerial;
    }

    /**
     * @return the number of zones
     */
    public int getNbZones() {
        return zoneSettings.length;
    }

    /**
     * Get the settings relative to a zone
     *
     * @param zone
     *            the zone index (from 1 to NumberOfZones)
     *
     * @return the settings of the zone
     */
    public PowerMaxZoneSettings getZoneSettings(int zone) {
        return ((zone < 1) || (zone > zoneSettings.length)) ? null : zoneSettings[zone - 1];
    }

    /**
     * @return the number of PGM and X10 devices managed by the system
     */
    public int getNbPGMX10Devices() {
        return NB_PGM_X10_DEVICES;
    }

    /**
     * Get the settings relative to the PGM
     *
     * @return the settings of the PGM
     */
    public PowerMaxX10Settings getPGMSettings() {
        return x10Settings[0];
    }

    /**
     * Get the settings relative to a X10 device
     *
     * @param idx
     *            the index (from 1 to 15)
     *
     * @return the settings of the X10 device
     */
    public PowerMaxX10Settings getX10Settings(int idx) {
        return ((idx < 1) || (idx >= x10Settings.length)) ? null : x10Settings[idx];
    }

    /**
     * @param idx
     *            the keypad index (first is 1)
     *
     * @return true if the 1 way keypad is enrolled; false if not
     */
    public boolean isKeypad1wEnrolled(int idx) {
        return ((keypad1wEnrolled == null) || (idx < 1) || (idx >= keypad1wEnrolled.length)) ? false
                : keypad1wEnrolled[idx - 1];
    }

    /**
     * @param idx
     *            the keypad index (first is 1)
     *
     * @return true if the 2 way keypad is enrolled; false if not
     */
    public boolean isKeypad2wEnrolled(int idx) {
        return ((keypad2wEnrolled == null) || (idx < 1) || (idx >= keypad2wEnrolled.length)) ? false
                : keypad2wEnrolled[idx - 1];
    }

    /**
     * @param idx
     *            the siren index (first is 1)
     *
     * @return true if the siren is enrolled; false if not
     */
    public boolean isSirenEnrolled(int idx) {
        return ((sirensEnrolled == null) || (idx < 1) || (idx >= sirensEnrolled.length)) ? false
                : sirensEnrolled[idx - 1];
    }

    /**
     * @return the PIN code of the first user of null if unknown (standard mode)
     */
    public String getFirstPinCode() {
        return (pinCodes == null) ? null : pinCodes[0];
    }

    public void updateRawSettings(byte[] data) {
        if ((data == null) || (data.length < 3)) {
            return;
        }
        int start = 0;
        int end = data.length - 3;
        int index = data[0] & 0x000000FF;
        int page = data[1] & 0x000000FF;
        int pageMin = page + (index + start) / 0x100;
        int indexPageMin = (index + start) % 0x100;
        int pageMax = page + (index + end) / 0x100;
        int indexPageMax = (index + end) % 0x100;
        index = 2;
        for (int i = pageMin; i <= pageMax; i++) {
            start = 0;
            end = 0xFF;
            if (i == pageMin) {
                start = indexPageMin;
            }
            if (i == pageMax) {
                end = indexPageMax;
            }
            if (rawSettings[i] == null) {
                rawSettings[i] = new Byte[0x100];
                for (int j = 0; j < 0x100; j++) {
                    rawSettings[i][j] = null;
                }
            }
            for (int j = start; j <= end; j++) {
                rawSettings[i][j] = data[index++];
            }
        }
    }

    private byte[] readSettings(PowerMaxSendType msgType, int start, int end) {
        byte[] message = msgType.getMessage();
        int page = message[2] & 0x000000FF;
        int index = message[1] & 0x000000FF;
        return readSettings(page, index + start, index + end);
    }

    private byte[] readSettings(int page, int start, int end) {
        int pageMin = page + start / 0x100;
        int indexPageMin = start % 0x100;
        int pageMax = page + end / 0x100;
        int indexPageMax = end % 0x100;
        int index = 0;
        boolean missingData = false;
        for (int i = pageMin; i <= pageMax; i++) {
            int start2 = 0;
            int end2 = 0xFF;
            if (i == pageMin) {
                start2 = indexPageMin;
            }
            if (i == pageMax) {
                end2 = indexPageMax;
            }
            index += end2 - start2 + 1;
            for (int j = start2; j <= end2; j++) {
                if ((rawSettings[i] == null) || (rawSettings[i][j] == null)) {
                    missingData = true;
                    break;
                }
            }
            if (missingData) {
                break;
            }
        }
        if (missingData) {
            logger.debug("readSettings({}, {}, {}): missing data", page, start, end);
            return null;
        }
        byte[] result = new byte[index];
        index = 0;
        for (int i = pageMin; i <= pageMax; i++) {
            int start2 = 0;
            int end2 = 0xFF;
            if (i == pageMin) {
                start2 = indexPageMin;
            }
            if (i == pageMax) {
                end2 = indexPageMax;
            }
            for (int j = start2; j <= end2; j++) {
                result[index++] = rawSettings[i][j];
            }
        }
        return result;
    }

    /**
     * Process and store all the panel settings from the raw buffers
     *
     * @param PowerlinkMode
     *            true if in Powerlink mode or false if in standard mode
     * @param defaultPanelType
     *            the default panel type to consider if not found in the raw buffers
     * @param timeSet
     *            the time in milliseconds used to set time and date; null if no sync time requested
     */
    public void process(boolean PowerlinkMode, PowerMaxPanelType defaultPanelType, Long timeSet) {

        // Identify panel type
        panelType = defaultPanelType;
        byte[] data = readSettings(PowerMaxSendType.DL_SERIAL, 7, 7);
        if (data != null) {
            try {
                panelType = PowerMaxPanelType.fromCode(data[0]);
            } catch (IllegalArgumentException e) {
                logger.warn("PowerMax alarm binding: unknwon panel type for code {}", data[0] & 0x000000FF);
                panelType = defaultPanelType;
            }
        }

        sensorTypes = new HashMap<Byte, String>();
        if (panelType.isPowerMaster()) {
            sensorTypes.put((byte) 0x01, "Motion");
            sensorTypes.put((byte) 0x04, "Camera");
            sensorTypes.put((byte) 0x16, "Smoke");
            sensorTypes.put((byte) 0x1A, "Temperature");
            sensorTypes.put((byte) 0x2A, "Magnet");
            sensorTypes.put((byte) 0xFE, "Wired");
        } else {
            sensorTypes.put((byte) 0x03, "Motion");
            sensorTypes.put((byte) 0x04, "Motion");
            sensorTypes.put((byte) 0x05, "Magnet");
            sensorTypes.put((byte) 0x06, "Magnet");
            sensorTypes.put((byte) 0x07, "Magnet");
            sensorTypes.put((byte) 0x0A, "Smoke");
            sensorTypes.put((byte) 0x0B, "Gas");
            sensorTypes.put((byte) 0x0C, "Motion");
            sensorTypes.put((byte) 0x0F, "Wired");
        }

        int zoneCnt = panelType.getWireless() + panelType.getWired();
        int customCnt = panelType.getCustomZones();
        int userCnt = panelType.getUserCodes();
        int partitionCnt = panelType.getPartitions();
        int sirenCnt = panelType.getSirens();
        int keypad1wCnt = panelType.getKeypads1w();
        int keypad2wCnt = panelType.getKeypads2w();

        zoneNames = new String[] { "Attic", "Back door", "Basement", "Bathroom", "Bedroom", "Child room", "Closet",
                "Den", "Dining room", "Downstairs", "Emergency", "Fire", "Front door", "Garage", "Garage door",
                "Guest room", "Hall", "Kitchen", "Laundry room", "Living room", "Master bathroom", "Master bedroom",
                "Office", "Upstairs", "Utility room", "Yard", "Custom 1", "Custom 2", "Custom 3", "Custom 4",
                "Custom 5", "Not Installed" };
        phoneNumbers = new String[] { null, null, null, null };
        bellTime = 4;
        silentPanic = false;
        quickArm = false;
        bypassEnabled = false;
        partitionsEnabled = false;
        pinCodes = new String[userCnt];
        for (int i = 0; i < userCnt; i++) {
            pinCodes[i] = null;
        }
        panelEprom = null;
        panelSoftware = null;
        panelSerial = null;
        zoneSettings = new PowerMaxZoneSettings[zoneCnt];
        for (int i = 0; i < zoneCnt; i++) {
            zoneSettings[i] = null;
        }
        x10Settings = new PowerMaxX10Settings[NB_PGM_X10_DEVICES];
        for (int i = 0; i < NB_PGM_X10_DEVICES; i++) {
            x10Settings[i] = null;
        }
        keypad1wEnrolled = new boolean[keypad1wCnt];
        for (int i = 0; i < keypad1wCnt; i++) {
            keypad1wEnrolled[i] = false;
        }
        keypad2wEnrolled = new boolean[keypad2wCnt];
        for (int i = 0; i < keypad2wCnt; i++) {
            keypad2wEnrolled[i] = false;
        }
        sirensEnrolled = new boolean[sirenCnt];
        for (int i = 0; i < sirenCnt; i++) {
            sirensEnrolled[i] = false;
        }

        if (PowerlinkMode) {
            // Check time and date
            data = readSettings(PowerMaxSendType.DL_TIME, 0, 5);
            GregorianCalendar cal = new GregorianCalendar();
            cal.set(Calendar.MILLISECOND, 0);
            cal.set(Calendar.SECOND, data[0] & 0x000000FF);
            cal.set(Calendar.MINUTE, data[1] & 0x000000FF);
            cal.set(Calendar.HOUR_OF_DAY, data[2] & 0x000000FF);
            cal.set(Calendar.DAY_OF_MONTH, data[3] & 0x000000FF);
            cal.set(Calendar.MONTH, (data[4] & 0x000000FF) - 1);
            cal.set(Calendar.YEAR, (data[5] & 0x000000FF) + 2000);
            long timeRead = cal.getTimeInMillis();
            logger.debug(String.format("PowerMax alarm binding: date %02d/%02d/%04d time %02d:%02d:%02d",
                    cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.YEAR),
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));

            // Check if time sync was OK
            if (timeSet != null) {
                cal.setTimeInMillis(timeSet);
                if ((timeRead - timeSet) <= 1000) {
                    logger.info("PowerMax alarm binding: time sync OK");
                } else {
                    logger.warn("PowerMax alarm binding: time sync failed !");
                }
            }

            // Process zone names
            for (int i = 0; i < (26 + customCnt); i++) {
                data = readSettings(PowerMaxSendType.DL_ZONESTR, i * 16, (i + 1) * 16 - 1);
                if ((data != null) && ((data[0] & 0x000000FF) != 0x000000FF)) {
                    zoneNames[i] = new String(data, CHARSET).trim();
                }
            }

            // Process communication settings
            for (int i = 0; i < phoneNumbers.length; i++) {
                data = readSettings(PowerMaxSendType.DL_PHONENRS, 8 * i, 8 * i + 7);
                if (data != null) {
                    for (int j = 0; j < 8; j++) {
                        if ((data[j] & 0x000000FF) != 0x000000FF) {
                            if (j == 0) {
                                phoneNumbers[i] = "";
                            }
                            if (phoneNumbers[i] != null) {
                                phoneNumbers[i] += String.format("%02X", data[j] & 0x000000FF);
                            }
                        }
                    }
                }
            }

            // Process alarm settings
            data = readSettings(PowerMaxSendType.DL_COMMDEF, 0, 0x1B);
            if (data != null) {
                bellTime = data[3] & 0x000000FF;
                silentPanic = (data[0x19] & 0x00000010) == 0x00000010;
                quickArm = (data[0x1A] & 0x00000008) == 0x00000008;
                bypassEnabled = (data[0x1B] & 0x000000C0) != 0;
            }

            // Process user PIN codes
            data = readSettings(
                    panelType.isPowerMaster() ? PowerMaxSendType.DL_MR_PINCODES : PowerMaxSendType.DL_PINCODES, 0,
                    2 * userCnt - 1);
            if (data != null) {
                for (int i = 0; i < userCnt; i++) {
                    pinCodes[i] = String.format("%02X%02X", data[i * 2] & 0x000000FF, data[i * 2 + 1] & 0x000000FF);
                }
            }

            // Process EEPROM version
            data = readSettings(PowerMaxSendType.DL_PANELFW, 0, 15);
            if (data != null) {
                panelEprom = new String(data, CHARSET).trim();
            }

            // Process software version
            data = readSettings(PowerMaxSendType.DL_PANELFW, 16, 31);
            if (data != null) {
                panelSoftware = new String(data, CHARSET).trim();
            }

            // Process serial ID
            panelSerial = "";
            data = readSettings(PowerMaxSendType.DL_SERIAL, 0, 5);
            if (data != null) {
                for (int i = 0; i <= 5; i++) {
                    if ((data[i] & 0x000000FF) != 0x000000FF) {
                        panelSerial += String.format("%02X", data[i] & 0x000000FF);
                    } else {
                        panelSerial += ".";
                    }
                }
            }

            // Check if partitions are enabled
            byte[] partitions = readSettings(PowerMaxSendType.DL_PARTITIONS, 0, 0x10 + zoneCnt);
            if (partitions != null) {
                partitionsEnabled = (partitions[0] & 0x000000FF) == 1;
            }
            if (!partitionsEnabled) {
                partitionCnt = 1;
            }

            // Process zone settings
            data = readSettings(PowerMaxSendType.DL_ZONES, 0, zoneCnt * 4 - 1);
            byte[] zoneNr = null;
            byte[] dataMr = null;
            if (panelType.isPowerMaster()) {
                zoneNr = readSettings(PowerMaxSendType.DL_MR_ZONENAMES, 0, zoneCnt - 1);
                dataMr = readSettings(PowerMaxSendType.DL_MR_ZONES, 0, zoneCnt * 10 - 2);
            } else {
                zoneNr = readSettings(PowerMaxSendType.DL_ZONENAMES, 0, zoneCnt - 1);
            }
            if ((data != null) && (zoneNr != null)) {

                byte[] zero3 = new byte[] { 0, 0, 0 };
                byte[] zero5 = new byte[] { 0, 0, 0, 0, 0 };

                for (int i = 0; i < zoneCnt; i++) {
                    String zoneName = zoneNames[zoneNr[i] & 0x0000001F];

                    boolean zoneEnrolled;
                    byte zoneInfo;
                    byte sensorId;
                    String sensorType;
                    if (panelType.isPowerMaster()) {
                        zoneEnrolled = !Arrays.equals(Arrays.copyOfRange(dataMr, i * 10 + 4, i * 10 + 9), zero5);
                        zoneInfo = data[i];
                        sensorId = dataMr[i * 10 + 5];
                        sensorType = sensorTypes.get(sensorId);
                    } else {
                        zoneEnrolled = !Arrays.equals(Arrays.copyOfRange(data, i * 4, i * 4 + 3), zero3);
                        zoneInfo = data[i * 4 + 3];
                        sensorId = data[i * 4 + 2];
                        sensorType = sensorTypes.get((byte) (sensorId & 0x0000000F));
                    }
                    if (zoneEnrolled) {
                        byte zoneType = (byte) (zoneInfo & 0x0000000F);
                        byte zoneChime = (byte) ((zoneInfo >> 4) & 0x00000003);

                        boolean[] part = new boolean[partitionCnt];
                        if (partitionCnt > 1) {
                            for (int j = 0; j < partitionCnt; j++) {
                                part[j] = (partitions != null) ? ((partitions[0x11 + i] & (1 << j)) != 0) : true;
                            }
                        } else {
                            part[0] = true;
                        }

                        zoneSettings[i] = new PowerMaxZoneSettings(zoneName, zoneType, zoneChime, sensorType, part);
                    }
                }
            }

            data = readSettings(PowerMaxSendType.DL_PGMX10, 0, 148);
            zoneNr = readSettings(PowerMaxSendType.DL_X10NAMES, 0, NB_PGM_X10_DEVICES - 2);
            if ((data != null) && (zoneNr != null)) {
                for (int i = 0; i < NB_PGM_X10_DEVICES; i++) {
                    boolean enabled = false;
                    String zoneName = null;
                    for (int j = 0; j <= 8; j++) {
                        if (data[5 + i + j * 0x10] != 0) {
                            enabled = true;
                            break;
                        }
                    }
                    if (i > 0) {
                        zoneName = zoneNames[zoneNr[i - 1] & 0x0000001F];
                    }
                    x10Settings[i] = new PowerMaxX10Settings(zoneName, enabled);
                }
            }

            if (panelType.isPowerMaster()) {
                // Process 2 way keypad settings
                data = readSettings(PowerMaxSendType.DL_MR_KEYPADS, 0, keypad2wCnt * 10 - 1);
                if (data != null) {
                    byte[] zero5 = new byte[] { 0, 0, 0, 0, 0 };

                    for (int i = 0; i < keypad2wCnt; i++) {
                        keypad2wEnrolled[i] = !Arrays.equals(Arrays.copyOfRange(data, i * 10 + 4, i * 10 + 9), zero5);
                    }
                }
                // Process siren settings
                data = readSettings(PowerMaxSendType.DL_MR_SIRENS, 0, sirenCnt * 10 - 1);
                if (data != null) {
                    byte[] zero5 = new byte[] { 0, 0, 0, 0, 0 };

                    for (int i = 0; i < sirenCnt; i++) {
                        sirensEnrolled[i] = !Arrays.equals(Arrays.copyOfRange(data, i * 10 + 4, i * 10 + 9), zero5);
                    }
                }
            } else {
                // Process 1 way keypad settings
                data = readSettings(PowerMaxSendType.DL_1WKEYPAD, 0, keypad1wCnt * 4 - 1);
                if (data != null) {
                    byte[] zero2 = new byte[] { 0, 0 };

                    for (int i = 0; i < keypad1wCnt; i++) {
                        keypad1wEnrolled[i] = !Arrays.equals(Arrays.copyOfRange(data, i * 4, i * 4 + 2), zero2);
                    }
                }
                // Process 2 way keypad settings
                data = readSettings(PowerMaxSendType.DL_2WKEYPAD, 0, keypad2wCnt * 4 - 1);
                if (data != null) {
                    byte[] zero3 = new byte[] { 0, 0, 0 };

                    for (int i = 0; i < keypad2wCnt; i++) {
                        keypad2wEnrolled[i] = !Arrays.equals(Arrays.copyOfRange(data, i * 4, i * 4 + 3), zero3);
                    }
                }
                // Process siren settings
                data = readSettings(PowerMaxSendType.DL_SIRENS, 0, sirenCnt * 4 - 1);
                if (data != null) {
                    byte[] zero3 = new byte[] { 0, 0, 0 };

                    for (int i = 0; i < sirenCnt; i++) {
                        sirensEnrolled[i] = !Arrays.equals(Arrays.copyOfRange(data, i * 4, i * 4 + 3), zero3);
                    }
                }
            }
        } else {
            if (!partitionsEnabled) {
                partitionCnt = 1;
            }
            boolean[] part = new boolean[partitionCnt];
            for (int j = 0; j < partitionCnt; j++) {
                part[j] = true;
            }
            for (int i = 0; i < zoneCnt; i++) {
                zoneSettings[i] = new PowerMaxZoneSettings(null, (byte) 0xFF, (byte) 0xFF, null, part);
            }
        }
    }

    /**
     * Update the name of a zone
     *
     * @param zoneIdx
     *            the zone index (first zone is index 1)
     * @param zoneNameIdx
     *            the index in the table of zone names
     */
    public void updateZoneName(int zoneIdx, byte zoneNameIdx) {
        PowerMaxZoneSettings zone = getZoneSettings(zoneIdx);
        if (zone != null) {
            zone.setName(zoneNames[zoneNameIdx & 0x0000001F]);
        }
    }

    /**
     * Update the type of a zone
     *
     * @param zoneIdx
     *            the zone index (first zone is index 1)
     * @param zoneInfo
     *            the zone info as an internal code
     */
    public void updateZoneInfo(int zoneIdx, int zoneInfo) {
        PowerMaxZoneSettings zone = getZoneSettings(zoneIdx);
        if (zone != null) {
            zone.setType((byte) (zoneInfo & 0x0000000F));
        }
    }

    /**
     * Log information about the current settings
     */
    public void log() {
        String str = "\nPanel is of type " + panelType.getLabel();

        int zoneCnt = panelType.getWireless() + panelType.getWired();
        int partitionCnt = panelType.getPartitions();
        int sirenCnt = panelType.getSirens();
        int keypad1wCnt = panelType.getKeypads1w();
        int keypad2wCnt = panelType.getKeypads2w();

        if (!partitionsEnabled) {
            partitionCnt = 1;
        }

        // for (int i = 0; i < (26 + customCnt); i++) {
        // str += String.format("zone name %d; %s", i + 1, zoneNames[i]);
        // }
        for (int i = 0; i < phoneNumbers.length; i++) {
            if (phoneNumbers[i] != null) {
                str += String.format("\nPhone number %d: %s", i + 1, phoneNumbers[i]);
            }
        }
        str += String.format("\nBell time: %d minutes", bellTime);
        str += String.format("\nSilent panic: %s", silentPanic ? "enabled" : "disabled");
        str += String.format("\nQuick arm: %s", quickArm ? "enabled" : "disabled");
        str += String.format("\nZone bypass: %s", bypassEnabled ? "enabled" : "disabled");
        str += String.format("\nEPROM: %s", (panelEprom != null) ? panelEprom : "Undefined");
        str += String.format("\nSW: %s", (panelSoftware != null) ? panelSoftware : "Undefined");
        str += String.format("\nSerial: %s", (panelSerial != null) ? panelSerial : "Undefined");
        str += String.format("\nUse partitions: %s", partitionsEnabled ? "enabled" : "disabled");
        str += String.format("\nNumber of partitions: %d", partitionCnt);
        for (int i = 0; i < zoneCnt; i++) {
            if (zoneSettings[i] != null) {
                String partStr = "";
                for (int j = 1; j <= partitionCnt; j++) {
                    if (zoneSettings[i].isInPartition(j)) {
                        partStr += j + " ";
                    }
                }
                str += String.format("\nZone %d %s: %s (chime = %s; sensor type = %s; partitions = %s)", i + 1,
                        zoneSettings[i].getName(), zoneSettings[i].getType(), zoneSettings[i].getChime(),
                        zoneSettings[i].getSensorType(), partStr);
            }
        }
        for (int i = 0; i < NB_PGM_X10_DEVICES; i++) {
            if (x10Settings[i] != null && x10Settings[i].isEnabled()) {
                str += String.format("\n%s: %s enabled", (i == 0) ? "PGM" : ("X10 " + i),
                        (x10Settings[i].getName() != null) ? x10Settings[i].getName() : "");
            }
        }
        for (int i = 1; i <= sirenCnt; i++) {
            if (isSirenEnrolled(i)) {
                str += String.format("\nSiren %d enrolled", i);
            }
        }
        for (int i = 1; i <= keypad1wCnt; i++) {
            if (isKeypad1wEnrolled(i)) {
                str += String.format("\nKeypad 1w %d enrolled", i);
            }
        }
        for (int i = 1; i <= keypad2wCnt; i++) {
            if (isKeypad2wEnrolled(i)) {
                str += String.format("\nKeypad 2w %d enrolled", i);
            }
        }
        logger.info("PowerMax alarm binding:" + str);
    }

    /**
     * Log help information relative to items and sitemap entries to be created
     */
    public void helpItems() {
        int zoneCnt = panelType.getWireless() + panelType.getWired();

        String items = "Help for defining items:\n" + "\nGroup GPowerMax \"Alarm\""
                + "\nString Powermax_partition_status \"Partition status [%s]\" (GPowerMax) {powermax=\"partition_status\"}"
                + "\nSwitch Powermax_partition_ready \"Partition ready\" (GPowerMax) {powermax=\"partition_ready\", autoupdate=\"false\"}"
                + "\nSwitch Powermax_partition_bypass \"Partition bypass\" (GPowerMax) {powermax=\"partition_bypass\", autoupdate=\"false\"}"
                + "\nSwitch Powermax_partition_alarm \"Partition alarm\" (GPowerMax) {powermax=\"partition_alarm\", autoupdate=\"false\"}"
                + "\nSwitch Powermax_panel_trouble \"Panel trouble\" (GPowerMax) {powermax=\"panel_trouble\", autoupdate=\"false\"}"
                + "\nSwitch Powermax_panel_alert_in_mem \"Panel alert in memory\" (GPowerMax) {powermax=\"panel_alert_in_memory\", autoupdate=\"false\"}"
                + "\nSwitch Powermax_partition_armed \"Partition armed\" (GPowerMax) {powermax=\"partition_armed\", autoupdate=\"false\"}"
                + "\nString Powermax_partition_arm_mode \"Partition arm mode [%s]\" (GPowerMax) {powermax=\"partition_arm_mode\", autoupdate=\"false\"}";

        String sitemap = "Help for defining sitemap:\n" + "\nText label=\"Security\" icon=\"lock\" {"
                + "\nSwitch item=Powermax_partition_armed mappings=[OFF=\"Disarmed\", ON=\"Armed\"]"
                + "\nSwitch item=Powermax_partition_arm_mode mappings=[Disarmed=\"Disarmed\", Stay=\"Armed home\", Armed=\"Armed away\"] valuecolor=[==\"Armed\"=\"green\",==\"Stay\"=\"orange\"]"
                + "\nSwitch item=Powermax_command mappings=[get_event_log=\"Event log\", download_setup=\"Get setup\", log_setup=\"Log setup\", help_items=\"Help items\"]";

        for (int i = 1; i <= zoneCnt; i++) {
            if (zoneSettings[i - 1] != null) {

                items += String.format(
                        "\nSwitch Powermax_zone%d_status \"Zone %d status\" (GPowerMax) {powermax=\"zone_status:%d\", autoupdate=\"false\"}"
                                + "\nContact Powermax_zone%d_status2 \"Zone %d status [%%s]\" (GPowerMax) {powermax=\"zone_status:%d\"}"
                                + "\nDateTime Powermax_zone%d_last_trip \"Zone %d last trip [%%1$tH:%%1$tM]\" (GPowerMax) {powermax=\"zone_last_trip:%d\"}"
                                + "\nSwitch Powermax_zone%d_bypassed \"Zone %d bypassed\" (GPowerMax) {powermax=\"zone_bypassed:%d\", autoupdate=\"false\"}"
                                + "\nSwitch Powermax_zone%d_armed \"Zone %d armed\" (GPowerMax) {powermax=\"zone_armed:%d\", autoupdate=\"false\"}"
                                + "\nSwitch Powermax_zone%d_low_battery \"Zone %d low battery\" (GPowerMax) {powermax=\"zone_low_battery:%d\", autoupdate=\"false\"}",
                        i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i, i);
            }
        }

        items += "\nString Powermax_command \"Command\" (GPowerMax) {powermax=\"command\", autoupdate=\"false\"}"
                + "\nString Powermax_event_log_1 \"Event log 1 [%s]\" (GPowerMax) {powermax=\"event_log:1\"}"
                + "\nString Powermax_event_log_2 \"Event log 2 [%s]\" (GPowerMax) {powermax=\"event_log:2\"}"
                + "\nString Powermax_event_log_3 \"Event log 3 [%s]\" (GPowerMax) {powermax=\"event_log:3\"}"
                + "\nString Powermax_event_log_4 \"Event log 4 [%s]\" (GPowerMax) {powermax=\"event_log:4\"}"
                + "\nString Powermax_event_log_5 \"Event log 5 [%s]\" (GPowerMax) {powermax=\"event_log:5\"}"
                + "\nString Powermax_panel_mode \"Panel mode [%s]\" (GPowerMax) {powermax=\"panel_mode\"}"
                + "\nString Powermax_panel_type \"Panel type [%s]\" (GPowerMax) {powermax=\"panel_type\"}"
                + "\nString Powermax_panel_eeprom \"EPROM [%s]\" (GPowerMax) {powermax=\"panel_eprom\"}"
                + "\nString Powermax_panel_software \"Software version [%s]\" (GPowerMax) {powermax=\"panel_software\"}"
                + "\nString Powermax_panel_serial \"Serial [%s]\" (GPowerMax) {powermax=\"panel_serial\"}";

        if (x10Settings[0] != null && x10Settings[0].isEnabled()) {
            items += "\nSwitch Powermax_PGM_status \"PGM status\" (GPowerMax) {powermax=\"PGM_status\", autoupdate=\"false\"}";
        }

        for (int i = 1; i < NB_PGM_X10_DEVICES; i++) {
            if (x10Settings[i] != null && x10Settings[i].isEnabled()) {
                items += String.format(
                        "\nSwitch Powermax_X10_%d_status \"X10 %d status\" (GPowerMax) {powermax=\"X10_status:%d\", autoupdate=\"false\"}"
                                + "\nString Powermax_X10_%d_status2 \"X10 %d status [%%s]\" (GPowerMax) {powermax=\"X10_status:%d\", autoupdate=\"false\"}",
                        i, i, i, i, i, i);
                sitemap += String.format(
                        "\nSwitch item=Powermax_X10_%d_status2 mappings=[OFF=\"Off\", ON=\"On\", DIM=\"Dim\", BRIGHT=\"Bright\"]",
                        i);
            }
        }

        sitemap += "\nGroup item=GPowerMax label=\"Alarm\"" + "\n}";

        logger.info("PowerMax alarm binding:\n" + items + "\n\n" + sitemap + "\n");
    }
}
