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

import org.openhab.binding.powermax.internal.state.PowerMaxPanelType;
import org.openhab.binding.powermax.internal.state.PowerMaxState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for INFO message handling
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxInfoMessage extends PowerMaxBaseMessage {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxInfoMessage.class);

    /**
     * Whether an automatic sync time is requested when handling the INFO
     * message
     */
    private boolean autoSyncTime = false;

    /**
     * Constructor
     *
     * @param message
     *            the received message as a buffer of bytes
     */
    public PowerMaxInfoMessage(byte[] message) {
        super(message);
    }

    /**
     * @return true if automatic sync time is requested; false if not
     */
    public boolean isAutoSyncTime() {
        return autoSyncTime;
    }

    /**
     * Set whether an automatic sync time is requested when handling the INFO
     * message
     *
     * @param autoSyncTime
     *            true if automatic sync time is requested; false if not
     */
    public void setAutoSyncTime(boolean autoSyncTime) {
        this.autoSyncTime = autoSyncTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PowerMaxState handleMessage() {
        super.handleMessage();

        PowerMaxState updatedState = new PowerMaxState();

        byte[] message = getRawData();

        PowerMaxPanelType panelType = null;
        try {
            panelType = PowerMaxPanelType.fromCode(message[7]);
        } catch (IllegalArgumentException e) {
            logger.warn("PowerMax alarm binding: unknwon panel type for code {}", message[7] & 0x000000FF);
            panelType = null;
        }

        logger.debug("Reading panel settings");
        updatedState.setDownloadMode(true);
        PowerMaxCommDriver comm = PowerMaxCommDriver.getTheCommDriver();
        comm.sendMessage(PowerMaxSendType.DL_PANELFW);
        comm.sendMessage(PowerMaxSendType.DL_SERIAL);
        comm.sendMessage(PowerMaxSendType.DL_ZONESTR);
        if (isAutoSyncTime()) {
            comm.sendSetTime();
        }
        if ((panelType != null) && panelType.isPowerMaster()) {
            comm.sendMessage(PowerMaxSendType.DL_MR_SIRKEYZON);
        }
        comm.sendMessage(PowerMaxSendType.START);
        comm.sendMessage(PowerMaxSendType.EXIT);

        return updatedState;
    }

    @Override
    public String toString() {
        String str = super.toString();

        byte[] message = getRawData();
        byte panelTypeNr = message[7];

        str += "\n - panel type number = " + String.format("%02X", panelTypeNr);

        return str;
    }

}
