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

import java.util.HashMap;

/**
 * A class to store the state of the alarm system
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxState {

    private static HashMap<String, Boolean> armedTable = null;
    private static HashMap<String, String> armModeTable = null;

    private Boolean powerlinkMode;
    private Boolean downloadMode;
    private PowerMaxZoneState[] zones;
    private Boolean[] pgmX10DevicesStatus;
    private Boolean ready;
    private Boolean bypass;
    private Boolean alarmActive;
    private Boolean trouble;
    private Boolean alertInMemory;
    private String statusStr;
    private String armMode;
    private Boolean downloadSetupRequired;
    private Long lastKeepAlive;
    private byte[] updateSettings;
    private String panelStatus;
    private String alarmType;
    private String troubleType;
    private String[] eventLog;

    /**
     * Constructor (default values)
     */
    public PowerMaxState() {
        PowerMaxPanelSettings settings = PowerMaxPanelSettings.getThePanelSettings();
        powerlinkMode = null;
        downloadMode = null;
        zones = new PowerMaxZoneState[settings.getNbZones()];
        for (int i = 0; i < settings.getNbZones(); i++) {
            zones[i] = new PowerMaxZoneState();
        }
        pgmX10DevicesStatus = new Boolean[settings.getNbPGMX10Devices()];
        for (int i = 0; i < settings.getNbPGMX10Devices(); i++) {
            pgmX10DevicesStatus[i] = null;
        }
        ready = null;
        bypass = null;
        alarmActive = null;
        trouble = null;
        alertInMemory = null;
        statusStr = null;
        armMode = null;
        downloadSetupRequired = null;
        lastKeepAlive = null;
        updateSettings = null;
        panelStatus = null;
        alarmType = null;
        troubleType = null;
        eventLog = null;
    }

    /**
     * Get the current mode (standard or Powerlink)
     *
     * @return true when the current mode is Powerlink; false when standard
     */
    public Boolean isPowerlinkMode() {
        return powerlinkMode;
    }

    /**
     * Set the current mode (standard or Powerlink)
     *
     * @param powerlinkMode
     *            true for Powerlink or false for standard
     */
    public void setPowerlinkMode(Boolean powerlinkMode) {
        this.powerlinkMode = powerlinkMode;
    }

    /**
     * Get whether or not the setup is being downloaded
     *
     * @return true when downloading the setup
     */
    public Boolean isDownloadMode() {
        return downloadMode;
    }

    /**
     * Set whether or not the setup is being downloaded
     *
     * @param downloadMode
     *            true when downloading the setup
     */
    public void setDownloadMode(Boolean downloadMode) {
        this.downloadMode = downloadMode;
    }

    /**
     * Get whether or not the zone sensor is tripped
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     *
     * @return true when the zone sensor is tripped
     */
    public Boolean isSensorTripped(int zone) {
        return ((zone < 1) || (zone > zones.length)) ? null : zones[zone - 1].isTripped();
    }

    /**
     * Set whether or not the zone sensor is tripped
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     * @param tripped
     *            true if tripped
     */
    public void setSensorTripped(int zone, Boolean tripped) {
        if ((zone >= 1) && (zone <= zones.length)) {
            this.zones[zone - 1].setTripped(tripped);
        }
    }

    /**
     * Get the timestamp when the zone sensor was last tripped
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     *
     * @return the timestamp
     */
    public Long getSensorLastTripped(int zone) {
        return ((zone < 1) || (zone > zones.length)) ? null : zones[zone - 1].getLastTripped();
    }

    /**
     * Set the timestamp when the zone sensor was last tripped
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     * @param lastTripped
     *            the timestamp
     */
    public void setSensorLastTripped(int zone, Long lastTripped) {
        if ((zone >= 1) && (zone <= zones.length)) {
            this.zones[zone - 1].setLastTripped(lastTripped);
        }
    }

    /**
     * Get whether or not the battery of the zone sensor is low
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     *
     * @return true when the battery is low
     */
    public Boolean isSensorLowBattery(int zone) {
        return ((zone < 1) || (zone > zones.length)) ? null : zones[zone - 1].isLowBattery();
    }

    /**
     * Set whether or not the battery of the zone sensor is low
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     * @param lowBattery
     *            true if battery is low
     */
    public void setSensorLowBattery(int zone, Boolean lowBattery) {
        if ((zone >= 1) && (zone <= zones.length)) {
            this.zones[zone - 1].setLowBattery(lowBattery);
        }
    }

    /**
     * Get whether or not the zone sensor is bypassed
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     *
     * @return true if bypassed
     */
    public Boolean isSensorBypassed(int zone) {
        return ((zone < 1) || (zone > zones.length)) ? null : zones[zone - 1].isBypassed();
    }

    /**
     * Set whether or not the zone sensor is bypassed
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     * @param bypassed
     *            true if bypassed
     */
    public void setSensorBypassed(int zone, Boolean bypassed) {
        if ((zone >= 1) && (zone <= zones.length)) {
            this.zones[zone - 1].setBypassed(bypassed);
        }
    }

    /**
     * Get whether or not the zone sensor is armed
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     *
     * @return true if armed
     */
    public Boolean isSensorArmed(int zone) {
        return ((zone < 1) || (zone > zones.length)) ? null : zones[zone - 1].isArmed();
    }

    /**
     * Set whether or not the zone sensor is armed
     *
     * @param zone
     *            the index of the zone (first zone is index 1)
     * @param armed
     *            true if armed
     */
    public void setSensorArmed(int zone, Boolean armed) {
        if ((zone >= 1) && (zone <= zones.length)) {
            this.zones[zone - 1].setArmed(armed);
        }
    }

    /**
     * Get the status of a PGM or X10 device
     *
     * @param device
     *            the index of the PGM/X10 device (0 s for PGM; for X10 device is index 1)
     *
     * @return the status (true or false)
     */
    public Boolean getPGMX10DeviceStatus(int device) {
        return ((device < 0) || (device >= pgmX10DevicesStatus.length)) ? null : pgmX10DevicesStatus[device];
    }

    /**
     * Set the status of a PGM or X10 device
     *
     * @param device
     *            the index of the PGM/X10 device (0 s for PGM; for X10 device is index 1)
     * @param status
     *            true or false
     */
    public void setPGMX10DeviceStatus(int device, Boolean status) {
        if ((device >= 0) && (device < pgmX10DevicesStatus.length)) {
            this.pgmX10DevicesStatus[device] = status;
        }
    }

    /**
     * Get whether or not the panel is ready
     *
     * @return true if ready
     */
    public Boolean isReady() {
        return ready;
    }

    /**
     * Set whether or not the panel is ready
     *
     * @param ready
     *            true if ready
     */
    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    /**
     * Get whether or not at least one zone is bypassed
     *
     * @return true if at least one zone is bypassed
     */
    public Boolean isBypass() {
        return bypass;
    }

    /**
     * Set whether or not at least one zone is bypassed
     *
     * @param bypass
     *            true if at least one zone is bypassed
     */
    public void setBypass(Boolean bypass) {
        this.bypass = bypass;
    }

    /**
     * Get whether or not the alarm is active
     *
     * @return true if active
     */
    public Boolean isAlarmActive() {
        return alarmActive;
    }

    /**
     * Set whether or not the alarm is active
     *
     * @param alarmActive
     *            true if the alarm is active
     */
    public void setAlarmActive(Boolean alarmActive) {
        this.alarmActive = alarmActive;
    }

    /**
     * Get whether or not the panel is identifying a trouble
     *
     * @return true if the panel is identifying a trouble
     */
    public Boolean isTrouble() {
        return trouble;
    }

    /**
     * Set whether or not the panel is identifying a trouble
     *
     * @param trouble
     *            the zone name
     */
    public void setTrouble(Boolean trouble) {
        this.trouble = trouble;
    }

    /**
     * Get whether or not the panel has saved an alert in memory
     *
     * @return true if the panel has saved an alert in memory
     */
    public Boolean isAlertInMemory() {
        return alertInMemory;
    }

    /**
     * Set whether or not the panel has saved an alert in memory
     *
     * @param alertInMemory
     *            true if an alert is saved in memory
     */
    public void setAlertInMemory(Boolean alertInMemory) {
        this.alertInMemory = alertInMemory;
    }

    /**
     * Get the partition status
     *
     * @return the status as a short string
     */
    public String getStatusStr() {
        return statusStr;
    }

    /**
     * Set the partition status
     *
     * @param statusStr
     *            the status as a short string
     */
    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }

    /**
     * Get the arming name
     *
     * @return the arming mode
     */
    public String getArmMode() {
        return armMode;
    }

    /**
     * Set the arming name
     *
     * @param armMode
     *            the arming name
     */
    public void setArmMode(String armMode) {
        this.armMode = armMode;
    }

    /**
     * Get whether or not the setup downloading is required
     *
     * @return true when downloading the setup is required
     */
    public Boolean isDownloadSetupRequired() {
        return downloadSetupRequired;
    }

    /**
     * Set whether or not the setup downloading is required
     *
     * @param downloadSetupRequired
     *            true when downloading setup is required
     */
    public void setDownloadSetupRequired(Boolean downloadSetupRequired) {
        this.downloadSetupRequired = downloadSetupRequired;
    }

    /**
     * Get the timestamp of the last received "keep alive" message
     *
     * @return the timestamp
     */
    public Long getLastKeepAlive() {
        return lastKeepAlive;
    }

    /**
     * Set the timestamp of the last received "keep alive" message
     *
     * @param lastKeepAlive
     *            the timestamp
     */
    public void setLastKeepAlive(Long lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
    }

    /**
     * Get the raw buffer containing all the settings
     *
     * @return the raw buffer as a table of bytes
     */
    public byte[] getUpdateSettings() {
        return updateSettings;
    }

    /**
     * Set the raw buffer containing all the settings
     *
     * @param updateSettings
     *            the raw buffer as a table of bytes
     */
    public void setUpdateSettings(byte[] updateSettings) {
        this.updateSettings = updateSettings;
    }

    /**
     * Get the panel status
     *
     * @return the panel status
     */
    public String getPanelStatus() {
        return panelStatus;
    }

    /**
     * Set the panel status
     *
     * @param panelStatus
     *            the status as a short string
     */
    public void setPanelStatus(String panelStatus) {
        this.panelStatus = panelStatus;
    }

    /**
     * Get the kind of the current alarm identified by the panel
     *
     * @return the kind of the current alarm; null if no alarm
     */
    public String getAlarmType() {
        return alarmType;
    }

    /**
     * Set the kind of the current alarm identified by the panel
     *
     * @param alarmType
     *            the kind of alarm (set it to null if no alarm)
     */
    public void setAlarmType(String alarmType) {
        this.alarmType = alarmType;
    }

    /**
     * Get the kind of the current trouble identified by the panel
     *
     * @return the kind of the current trouble; null if no trouble
     */
    public String getTroubleType() {
        return troubleType;
    }

    /**
     * Set the kind of the current trouble identified by the panel
     *
     * @param troubleType
     *            the kind of trouble (set it to null if no trouble)
     */
    public void setTroubleType(String troubleType) {
        this.troubleType = troubleType;
    }

    /**
     * Get the number of entries in the event log
     *
     * @return the number of entries
     */
    public int getEventLogSize() {
        return (eventLog == null) ? 0 : eventLog.length;
    }

    /**
     * Set the number of entries in the event log
     *
     * @param size
     *            the number of entries
     */
    public void setEventLogSize(int size) {
        eventLog = new String[size];
        for (int i = 0; i < eventLog.length; i++) {
            eventLog[i] = null;
        }
    }

    /**
     * Get one entry from the event logs
     *
     * @param index
     *            the entry index (1 for the most recent entry)
     *
     * @return the entry value (event)
     */
    public String getEventLog(int index) {
        return ((index < 1) || (index > getEventLogSize())) ? null : eventLog[index - 1];
    }

    /**
     * Set one entry from the event logs
     *
     * @param index
     *            the entry index (1 for the most recent entry)
     * @param event
     *            the entry value (event)
     */
    public void setEventLog(int index, String event) {
        if ((index >= 1) && (index <= getEventLogSize())) {
            this.eventLog[index - 1] = event;
        }
    }

    /**
     * Get the panel mode
     *
     * @return either Download or Powerlink or Standard
     */
    public String getPanelMode() {
        String mode = null;
        if ((downloadMode != null) && downloadMode.equals(Boolean.TRUE)) {
            mode = "Download";
        } else if ((powerlinkMode != null) && powerlinkMode.equals(Boolean.TRUE)) {
            mode = "Powerlink";
        } else if ((powerlinkMode != null) && powerlinkMode.equals(Boolean.FALSE)) {
            mode = "Standard";
        }
        return mode;
    }

    /**
     * Get whether or not the current arming mode is considered as armed
     *
     * @return true or false
     */
    public Boolean isArmed() {
        return isArmed(getArmMode());
    }

    /**
     * Get whether or not an arming mode is considered as armed
     *
     * @param armMode
     *            the arming mode
     *
     * @return true or false; null if mode is unexpected
     */
    private static Boolean isArmed(String armMode) {
        if (armedTable == null) {
            armedTable = new HashMap<String, Boolean>();
            armedTable.put("Disarmed", false);
            armedTable.put("Home Exit Delay", false);
            armedTable.put("Away Exit Delay", false);
            armedTable.put("Entry Delay", true);
            armedTable.put("Armed Home", true);
            armedTable.put("Armed Away", true);
            armedTable.put("User Test", false);
            armedTable.put("Downloading", false);
            armedTable.put("Programming", false);
            armedTable.put("Installer", false);
            armedTable.put("Home Bypass", true);
            armedTable.put("Away Bypass", true);
            armedTable.put("Ready", false);
            armedTable.put("Not Ready", false);
            armedTable.put("Disarmed Instant", false);
            armedTable.put("Home Instant Exit Delay", false);
            armedTable.put("Away Instant Exit Delay", false);
            armedTable.put("Entry Delay Instant", true);
            armedTable.put("Armed Home Instant", true);
            armedTable.put("Armed Away Instant", true);
        }

        Boolean result = null;
        if (armMode != null) {
            result = armedTable.get(armMode);
            if (result == null) {
                result = Boolean.FALSE;
            }
        }
        return result;
    }

    /**
     * Get the short description associated to the current arming mode
     *
     * @return the short description
     */
    public String getShortArmMode() {
        return getShortArmMode(getArmMode());
    }

    /**
     * Get the short description associated to an arming mode
     *
     * @param armMode
     *            the arming mode
     *
     * @return the short description or null if mode is unexpected
     */
    private static String getShortArmMode(String armMode) {
        if (armModeTable == null) {
            armModeTable = new HashMap<String, String>();
            armModeTable.put("Disarmed", "Disarmed");
            armModeTable.put("Home Exit Delay", "ExitDelay");
            armModeTable.put("Away Exit Delay", "Disarmed");
            armModeTable.put("Entry Delay", "EntryDelay");
            armModeTable.put("Armed Home", "Stay");
            armModeTable.put("Armed Away", "Armed");
            armModeTable.put("User Test", "UserTest");
            armModeTable.put("Downloading", "NotReady");
            armModeTable.put("Programming", "NotReady");
            armModeTable.put("Installer", "NotReady");
            armModeTable.put("Home Bypass", "Force");
            armModeTable.put("Away Bypass", "Force");
            armModeTable.put("Ready", "Ready");
            armModeTable.put("Not Ready", "NotReady");
            armModeTable.put("Disarmed Instant", "Disarmed");
            armModeTable.put("Home Instant Exit Delay", "ExitDelay");
            armModeTable.put("Away Instant Exit Delay", "ExitDelay");
            armModeTable.put("Entry Delay Instant", "EntryDelay");
            armModeTable.put("Armed Home Instant", "StayInstant");
            armModeTable.put("Armed Away Instant", "ArmedInstant");
        }

        String result = null;
        if (armMode != null) {
            result = armModeTable.get(armMode);
            if (result == null) {
                result = armMode;
            }
        }
        return result;
    }

    /**
     * Keep only data that are different from another state and reset all others data to undefined
     *
     * @param otherState
     *            the other state
     */
    public void keepOnlyDifferencesWith(PowerMaxState otherState) {
        if ((powerlinkMode != null) && powerlinkMode.equals(otherState.isPowerlinkMode())) {
            powerlinkMode = null;
        }
        if ((downloadMode != null) && downloadMode.equals(otherState.isDownloadMode())) {
            downloadMode = null;
        }
        for (int i = 1; i <= zones.length; i++) {
            if ((isSensorTripped(i) != null) && isSensorTripped(i).equals(otherState.isSensorTripped(i))) {
                setSensorTripped(i, null);
            }
            if ((getSensorLastTripped(i) != null)
                    && getSensorLastTripped(i).equals(otherState.getSensorLastTripped(i))) {
                setSensorLastTripped(i, null);
            }
            if ((isSensorLowBattery(i) != null) && isSensorLowBattery(i).equals(otherState.isSensorLowBattery(i))) {
                setSensorLowBattery(i, null);
            }
            if ((isSensorBypassed(i) != null) && isSensorBypassed(i).equals(otherState.isSensorBypassed(i))) {
                setSensorBypassed(i, null);
            }
            if ((isSensorArmed(i) != null) && isSensorArmed(i).equals(otherState.isSensorArmed(i))) {
                setSensorArmed(i, null);
            }
        }
        for (int i = 0; i < pgmX10DevicesStatus.length; i++) {
            if ((getPGMX10DeviceStatus(i) != null)
                    && getPGMX10DeviceStatus(i).equals(otherState.getPGMX10DeviceStatus(i))) {
                setPGMX10DeviceStatus(i, null);
            }
        }
        if ((ready != null) && ready.equals(otherState.isReady())) {
            ready = null;
        }
        if ((bypass != null) && bypass.equals(otherState.isBypass())) {
            bypass = null;
        }
        if ((alarmActive != null) && alarmActive.equals(otherState.isAlarmActive())) {
            alarmActive = null;
        }
        if ((trouble != null) && trouble.equals(otherState.isTrouble())) {
            trouble = null;
        }
        if ((alertInMemory != null) && alertInMemory.equals(otherState.isAlertInMemory())) {
            alertInMemory = null;
        }
        if ((statusStr != null) && statusStr.equals(otherState.getStatusStr())) {
            statusStr = null;
        }
        if ((armMode != null) && armMode.equals(otherState.getArmMode())) {
            armMode = null;
        }
        if ((lastKeepAlive != null) && lastKeepAlive.equals(otherState.getLastKeepAlive())) {
            lastKeepAlive = null;
        }
        if ((panelStatus != null) && panelStatus.equals(otherState.getPanelStatus())) {
            panelStatus = null;
        }
        if ((alarmType != null) && alarmType.equals(otherState.getAlarmType())) {
            alarmType = null;
        }
        if ((troubleType != null) && troubleType.equals(otherState.getTroubleType())) {
            troubleType = null;
        }
    }

    /**
     * Update (override) the current state data from another state, ignoring in this other state
     * the undefined data
     *
     * @param update
     *            the other state to consider for the update
     */
    public void merge(PowerMaxState update) {
        if (update.isPowerlinkMode() != null) {
            powerlinkMode = update.isPowerlinkMode();
        }
        if (update.isDownloadMode() != null) {
            downloadMode = update.isDownloadMode();
        }
        for (int i = 1; i <= zones.length; i++) {
            if (update.isSensorTripped(i) != null) {
                setSensorTripped(i, update.isSensorTripped(i));
            }
            if (update.getSensorLastTripped(i) != null) {
                setSensorLastTripped(i, update.getSensorLastTripped(i));
            }
            if (update.isSensorLowBattery(i) != null) {
                setSensorLowBattery(i, update.isSensorLowBattery(i));
            }
            if (update.isSensorBypassed(i) != null) {
                setSensorBypassed(i, update.isSensorBypassed(i));
            }
            if (update.isSensorArmed(i) != null) {
                setSensorArmed(i, update.isSensorArmed(i));
            }
        }
        for (int i = 0; i < pgmX10DevicesStatus.length; i++) {
            if (update.getPGMX10DeviceStatus(i) != null) {
                setPGMX10DeviceStatus(i, update.getPGMX10DeviceStatus(i));
            }
        }
        if (update.isReady() != null) {
            ready = update.isReady();
        }
        if (update.isBypass() != null) {
            bypass = update.isBypass();
        }
        if (update.isAlarmActive() != null) {
            alarmActive = update.isAlarmActive();
        }
        if (update.isTrouble() != null) {
            trouble = update.isTrouble();
        }
        if (update.isAlertInMemory() != null) {
            alertInMemory = update.isAlertInMemory();
        }
        if (update.getStatusStr() != null) {
            statusStr = update.getStatusStr();
        }
        if (update.getArmMode() != null) {
            armMode = update.getArmMode();
        }
        if (update.getLastKeepAlive() != null) {
            lastKeepAlive = update.getLastKeepAlive();
        }
        if (update.getPanelStatus() != null) {
            panelStatus = update.getPanelStatus();
        }
        if (update.getAlarmType() != null) {
            alarmType = update.getAlarmType();
        }
        if (update.getTroubleType() != null) {
            troubleType = update.getTroubleType();
        }
        if (update.getEventLogSize() > getEventLogSize()) {
            setEventLogSize(update.getEventLogSize());
        }
        for (int i = 1; i <= getEventLogSize(); i++) {
            if (update.getEventLog(i) != null) {
                setEventLog(i, update.getEventLog(i));
            }
        }
    }

    @Override
    public String toString() {
        String str = "";

        if (powerlinkMode != null) {
            str += "\n - powerlink mode = " + (powerlinkMode ? "yes" : "no");
        }
        if (downloadMode != null) {
            str += "\n - download mode = " + (downloadMode ? "yes" : "no");
        }
        for (int i = 1; i <= zones.length; i++) {
            if (isSensorTripped(i) != null) {
                str += String.format("\n - sensor zone %d %s", i, isSensorTripped(i) ? "tripped" : "untripped");
            }
            if (getSensorLastTripped(i) != null) {
                str += String.format("\n - sensor zone %d last trip %d", i, getSensorLastTripped(i));
            }
            if (isSensorLowBattery(i) != null) {
                str += String.format("\n - sensor zone %d %s", i, isSensorLowBattery(i) ? "low battery" : "battery ok");
            }
            if (isSensorBypassed(i) != null) {
                str += String.format("\n - sensor zone %d %sbypassed", i, isSensorBypassed(i) ? "" : "not ");
            }
            if (isSensorArmed(i) != null) {
                str += String.format("\n - sensor zone %d %s", i, isSensorArmed(i) ? "armed" : "disarmed");
            }
        }
        for (int i = 0; i < pgmX10DevicesStatus.length; i++) {
            if (getPGMX10DeviceStatus(i) != null) {
                str += String.format("\n - %s status = %s", (i == 0) ? "PGM device" : String.format("X10 device %d", i),
                        getPGMX10DeviceStatus(i) ? "ON" : "OFF");
            }
        }
        if (ready != null) {
            str += "\n - ready = " + (ready ? "yes" : "no");
        }
        if (bypass != null) {
            str += "\n - bypass = " + (bypass ? "yes" : "no");
        }
        if (alarmActive != null) {
            str += "\n - alarm active = " + (alarmActive ? "yes" : "no");
        }
        if (trouble != null) {
            str += "\n - trouble = " + (trouble ? "yes" : "no");
        }
        if (alertInMemory != null) {
            str += "\n - alert in memory = " + (alertInMemory ? "yes" : "no");
        }
        if (statusStr != null) {
            str += "\n - status = " + statusStr;
        }
        if (armMode != null) {
            str += "\n - arm mode = " + armMode;
        }
        if (lastKeepAlive != null) {
            str += "\n - last keep alive = " + lastKeepAlive;
        }
        if (panelStatus != null) {
            str += "\n - panel status = " + panelStatus;
        }
        if (alarmType != null) {
            str += "\n - alarm type = " + alarmType;
        }
        if (troubleType != null) {
            str += "\n - trouble type = " + troubleType;
        }
        for (int i = 1; i <= getEventLogSize(); i++) {
            if (getEventLog(i) != null) {
                str += "\n - event log " + i + " = " + getEventLog(i);
            }
        }

        return str;
    }

}
