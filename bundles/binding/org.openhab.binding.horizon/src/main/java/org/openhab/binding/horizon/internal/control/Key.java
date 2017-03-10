/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.horizon.internal.control;

/**
 * This class holds the mapping between the supported key commands and their Integer value
 *
 * @author Jurgen Kuijpers
 * @since 1.9.0
 */
public enum Key {

    KEY_POWER(0xe000, "POWER"),
    KEY_OK(0xe001, "OK"),
    KEY_BACK(0xe002, "BACK"),
    KEY_CHAN_UP(0xe006, "CHANNEL_UP"),
    KEY_CHAN_DWN(0xe007, "CHANNEL_DOWN"),
    KEY_HELP(0xe009, "HELP"),
    KEY_MENU(0xe00a, "MENU"),
    KEY_GUIDE(0xe00b, "GUIDE"),
    KEY_INFO(0xe00e, "INFO"),
    KEY_TEXT(0xe00f, "TEXT"),
    KEY_MENU1(0xe011, "MENU1"),
    KEY_MENU2(0xe015, "MENU2"),
    KEY_DPAD_UP(0xe100, "DPAD_UP"),
    KEY_DPAD_DOWN(0xe101, "DPAD_DOWN"),
    KEY_DPAD_LEFT(0xe102, "DPAD_LEFT"),
    KEY_DPAD_RIGHT(0xe103, "DPAD_RIGHT"),
    KEY_NUM_0(0xe300, "0"),
    KEY_NUM_1(0xe301, "1"),
    KEY_NUM_2(0xe302, "2"),
    KEY_NUM_3(0xe303, "3"),
    KEY_NUM_4(0xe304, "4"),
    KEY_NUM_5(0xe305, "5"),
    KEY_NUM_6(0xe306, "6"),
    KEY_NUM_7(0xe307, "7"),
    KEY_NUM_8(0xe308, "8"),
    KEY_NUM_9(0xe309, "9"),
    KEY_PAUSE(0xe400, "PAUSE"),
    KEY_STOP(0xe402, "STOP"),
    KEY_RECORD(0xe403, "RECORD"),
    KEY_FWD(0xe405, "FWD"),
    KEY_RWD(0xe407, "RWD"),
    KEY_MENU3(0xef00, "MENU3"),
    KEY_TIMESHIFT_INFO(0xef06, "TS_INFO"),
    KEY_POWER2(0xef15, "POWER2"),
    KEY_ID(0xef16, "ID"),
    KEY_RC_PAIR(0xef17, "RC_PAIR"),
    KEY_TIMINGS(0xef19, "TIMINGS"),
    KEY_ONDEMAND(0xef28, "ONDEMAND"),
    KEY_DVR(0xef29, "DVR"),
    KEY_TV(0xef2a, "TV");

    private int value;
    private String name;

    Key(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return this.name;
    }

    /**
     * Gets the Integer value of a key command by name
     *
     */
    public static Integer getByName(final String name) {
        for (Key key : values()) {
            if (key.getName().equals(name.toUpperCase())) {
                return key.getValue();
            }
        }
        return null;
    }
}
