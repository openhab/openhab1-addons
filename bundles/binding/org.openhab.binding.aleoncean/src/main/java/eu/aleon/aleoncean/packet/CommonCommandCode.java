/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.packet;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class CommonCommandCode {

    /**
     * This is not defined in specification, we use it to signal an undefined common command.
     */
    public static final byte UNDEF = (byte) 0;

    /**
     * Order to enter in energy saving mode.
     */
    public static final byte CO_WR_SLEEP = (byte) 1;

    /**
     * Order to reset the device.
     */
    public static final byte CO_WR_RESET = (byte) 2;

    /**
     * Read the device (SW) version / (HW) version, chip ID etc.
     */
    public static final byte CO_RD_VERSION = (byte) 3;

    /**
     * Read system log from device databank.
     */
    public static final byte CO_RD_SYS_LOG = (byte) 4;

    /**
     * Reset system log from device databank.
     */
    public static final byte CO_WR_SYS_LOG = (byte) 5;

    /**
     * Perform flash BIST operation.
     */
    public static final byte CO_WR_BIST = (byte) 6;

    /**
     * Write ID range base number.
     */
    public static final byte CO_WR_IDBASE = (byte) 7;

    /**
     * Read ID range base number.
     */
    public static final byte CO_RD_IDBASE = (byte) 8;

    /**
     * Write repeater Level off, 1, 2.
     */
    public static final byte CO_WR_REPEATER = (byte) 9;

    /**
     * Read repeater level off, 1, 2.
     */
    public static final byte CO_RD_REPEATER = (byte) 10;

    /**
     * Add filter to filter list.
     */
    public static final byte CO_WR_FILTER_ADD = (byte) 11;

    /**
     * Delete filter from filter list.
     */
    public static final byte CO_WR_FILTER_DEL = (byte) 12;

    /**
     * Delete all filter.
     */
    public static final byte CO_WR_FILTER_DEL_ALL = (byte) 13;

    /**
     * Enable / disable supplied filters.
     */
    public static final byte CO_WR_FILTER_ENABLE = (byte) 14;

    /**
     * Read supplied filters.
     */
    public static final byte CO_RD_FILTER = (byte) 15;

    /**
     * Waiting till end of maturity time before received radio telegrams will transmitted.
     */
    public static final byte CO_WR_WAIT_MATURITY = (byte) 16;

    /**
     * Enable / disable transmitting additional subtelegram info.
     */
    public static final byte CO_WR_SUBTEL = (byte) 17;

    /**
     * Write x bytes of the flash, XRAM, RAM0 ....
     */
    public static final byte CO_WR_MEM = (byte) 18;

    /**
     * Read x bytes of the flash, XRAM, RAM0 ...
     */
    public static final byte CO_RD_MEM = (byte) 19;

    /**
     * Feedback about the used address and length of the config area and Smart Ack table.
     */
    public static final byte CO_RD_MEM_ADDRESS = (byte) 20;

    /**
     * Read own security information (level, key).
     */
    public static final byte CO_RD_SECURITY = (byte) 21;

    /**
     * Write own security information (level, key).
     */
    public static final byte CO_WR_SECURITY = (byte) 22;

    /**
     * Enable / disable learn mode.
     */
    public static final byte CO_WR_LEARNMODE = (byte) 23;

    /**
     * Read learn mode.
     */
    public static final byte CO_RD_LEARNMODE = (byte) 24;

    /**
     * Add a secure device.
     */
    public static final byte CO_WR_SECUREDEIVCE_ADD = (byte) 25;

    /**
     * Delete a secure device.
     */
    public static final byte CO_WR_SECUREDEVICE_DEL = (byte) 26;

    /**
     * Read all secure devices (SLF, ID, channel).
     */
    public static final byte CO_RD_SECUREDEVICE = (byte) 27;

    private CommonCommandCode() {
    }

}
