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
package org.openhab.binding.mpd.internal;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.Type;
import org.openhab.core.types.UnDefType;

/**
 * Represents all valid commands which could be processed by this binding
 *
 * @author Thomas.Eichstaedt-Engelen
 */
public enum PlayerCommandTypeMapping {

    PAUSE {
        {
            command = "pause";
            type = OnOffType.OFF;
        }
    },

    PLAYSONG {
        {
            command = "playsong";
            type = OnOffType.ON;
        }
    },

    PLAYSONGID {
        {
            command = "playsongid";
        }
    },

    PLAY {
        {
            command = "play";
            type = OnOffType.ON;
        }
    },

    STOP {
        {
            command = "stop";
            type = OnOffType.OFF;
        }
    },

    VOLUME {
        {
            command = "volume";
        }
    },

    TRACKINFO {
        {
            command = "tracktitle";
        }
    },

    TRACKARTIST {
        {
            command = "trackartist";
        }
    },

    VOLUME_INCREASE {
        {
            command = "volume_increase";
            type = IncreaseDecreaseType.INCREASE;
        }
    },

    VOLUME_DECREASE {
        {
            command = "volume_decrease";
            type = IncreaseDecreaseType.DECREASE;
        }
    },

    NEXT {
        {
            command = "next";
            type = OnOffType.ON;
        }
    },

    PREV {
        {
            command = "prev";
            type = OnOffType.OFF;
        }
    },

    ENABLE {
        {
            command = "enable";
            type = OnOffType.ON;
        }
    },

    DISABLE {
        {
            command = "disable";
            type = OnOffType.OFF;
        }
    };

    /** Represents the player command as it will be used in *.items configuration */
    String command;

    /** The corresponding openHAB-{@link Type} or {@link UnDefType}<code>.NULL</code> by default */
    Type type = UnDefType.NULL;

    public String getPlayerCommand() {
        return command;
    }

    public Type getType() {
        return type;
    }

    public static PlayerCommandTypeMapping fromString(String playerCommand) {

        if ("".equals(playerCommand)) {
            return null;
        }

        for (PlayerCommandTypeMapping c : PlayerCommandTypeMapping.values()) {

            if (c.getPlayerCommand().equals(playerCommand)) {
                return c;
            }
        }

        throw new IllegalArgumentException("cannot find playerCommand for '" + playerCommand + "'");
    }

}
