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

/**
 * Used to store main characteristics of each Visonic alarm panel type in an ENUM
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public enum PowerMaxPanelType {

    POWERMAX((byte) 0, "PowerMax", 1, 250, 8, 8, 2, 2, 8, 0, 28, 2, 0),
    POWERMAX_PLUS((byte) 1, "PowerMax+", 1, 250, 8, 8, 2, 2, 8, 0, 28, 2, 5),
    POWERMAX_PRO((byte) 2, "PowerMaxPro", 1, 250, 8, 8, 2, 2, 8, 8, 28, 2, 5),
    POWERMAX_COMPLETE((byte) 3, "PowerMaxComplete", 1, 250, 8, 8, 2, 2, 8, 0, 28, 2, 5),
    POWERMAX_PRO_PART((byte) 4, "PowerMaxProPart", 3, 250, 8, 8, 2, 2, 8, 8, 28, 2, 5),
    POWERMAX_COMPLETE_PART((byte) 5, "PowerMaxCompletePart", 3, 250, 8, 8, 2, 2, 8, 8, 28, 2, 5),
    POWERMAX_EXPRESS((byte) 6, "PowerMaxExpress", 1, 250, 8, 8, 2, 2, 8, 0, 28, 1, 5),
    POWERMASTER_10((byte) 7, "PowerMaster10", 3, 250, 8, 0, 8, 4, 8, 8, 29, 1, 5),
    POWERMASTER_30((byte) 8, "PowerMaster30", 3, 1000, 32, 0, 32, 8, 48, 32, 62, 2, 5);

    private byte code;
    private String label;
    private int partitions;
    private int events;
    private int keyfobs;
    private int keypads1w;
    private int keypads2w;
    private int sirens;
    private int userCodes;
    private int prontags;
    private int wireless;
    private int wired;
    private int customZones;

    private PowerMaxPanelType(byte code, String label, int partitions, int events, int keyfobs, int keypads1w,
            int keypads2w, int sirens, int userCodes, int prontags, int wireless, int wired, int customZones) {
        this.code = code;
        this.label = label;
        this.partitions = partitions;
        this.events = events;
        this.keyfobs = keyfobs;
        this.keypads1w = keypads1w;
        this.keypads2w = keypads2w;
        this.sirens = sirens;
        this.userCodes = userCodes;
        this.prontags = prontags;
        this.wireless = wireless;
        this.wired = wired;
        this.customZones = customZones;
    }

    /**
     * @return the code (number) stored in the panel setup
     */
    public byte getCode() {
        return code;
    }

    /**
     * @return the panel type as a string
     */
    public String getLabel() {
        return label;
    }

    /**
     * @return the number of managed partitions
     */
    public int getPartitions() {
        return partitions;
    }

    /**
     * @return the number of events stored in the event log
     */
    public int getEvents() {
        return events;
    }

    /**
     * @return the number of managed keyfobs
     */
    public int getKeyfobs() {
        return keyfobs;
    }

    /**
     * @return the number of managed uni-directional keypads
     */
    public int getKeypads1w() {
        return keypads1w;
    }

    /**
     * @return the number of managed bi-directional keypads
     */
    public int getKeypads2w() {
        return keypads2w;
    }

    /**
     * @return the number of managed sirens
     */
    public int getSirens() {
        return sirens;
    }

    /**
     * @return the number of managed user codes
     */
    public int getUserCodes() {
        return userCodes;
    }

    public int getProntags() {
        return prontags;
    }

    /**
     * @return the number of managed wireless zones
     */
    public int getWireless() {
        return wireless;
    }

    /**
     * @return the number of managed wired zones
     */
    public int getWired() {
        return wired;
    }

    /**
     * @return the number of zones that can be customized by the user
     */
    public int getCustomZones() {
        return customZones;
    }

    /**
     * @return true is the panel is a PowerMaster panel type
     */
    public boolean isPowerMaster() {
        return this == PowerMaxPanelType.POWERMASTER_10 || this == PowerMaxPanelType.POWERMASTER_30;
    }

    /**
     * Get the ENUM value from its code number
     *
     * @param panelCode
     *            the code stored by the panel
     *
     * @return the corresponding ENUM value
     *
     * @throws IllegalArgumentException
     *             if no ENUM value corresponds to this code
     */
    public static PowerMaxPanelType fromCode(byte panelCode) {
        for (PowerMaxPanelType panelType : PowerMaxPanelType.values()) {
            if (panelType.getCode() == panelCode) {
                return panelType;
            }
        }

        throw new IllegalArgumentException("Invalid code: " + panelCode);
    }

    /**
     * Get the ENUM value from its label
     *
     * @param label
     *            the label
     *
     * @return the corresponding ENUM value
     *
     * @throws IllegalArgumentException
     *             if no ENUM value corresponds to this label
     */
    public static PowerMaxPanelType fromLabel(String label) {
        for (PowerMaxPanelType panelType : PowerMaxPanelType.values()) {
            if (panelType.getLabel().equalsIgnoreCase(label)) {
                return panelType;
            }
        }

        throw new IllegalArgumentException("Invalid label: " + label);
    }
}
