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
package org.openhab.binding.digitalstrom.internal.client.constants;

import java.util.HashMap;

/**
 * @author Alexander Betker
 * @since 1.3.0
 * @version digitalSTROM-API 1.14.5
 */
public enum OutputModeEnum {

    DISABLED(0),
    SWITCHED(16),
    DIMMED(22),
    DIMMED_2(51),
    UP_DOWN(33),
    SWITCHED_2(35),
    SWITCH(39),
    WIPE(40),
    POWERSAVE(41),
    SLAT(42);

    private final int mode;

    static final HashMap<Integer, OutputModeEnum> outputModes = new HashMap<Integer, OutputModeEnum>();

    static {
        for (OutputModeEnum out : OutputModeEnum.values()) {
            outputModes.put(out.getMode(), out);
        }
    }

    public static boolean containsMode(Integer mode) {
        return outputModes.keySet().contains(mode);
    }

    public static OutputModeEnum getMode(Integer mode) {
        return outputModes.get(mode);
    }

    private OutputModeEnum(int outputMode) {
        this.mode = outputMode;
    }

    public int getMode() {
        return mode;
    }

}
