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
package org.openhab.binding.plugwise;

import org.apache.commons.lang.StringUtils;
import org.openhab.binding.plugwise.internal.Stick;
import org.openhab.core.items.Item;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.Type;
import org.quartz.Job;

/**
 * An Enum that defines the commands that can be use in binding configuration, providing info on what kind of Item this
 * command be used, as well as the type of value it should return to the OH return time, and finally also the Job.class
 * that should be scheduled by Quartz - a Job.class that will poll the hardware for updates
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public enum PlugwiseCommandType {

    /** The currentpower. */
    CURRENTPOWER {
        {
            command = "power";
            typeClass = DecimalType.class;
            jobClass = Stick.PowerInformationJob.class;
        }
    },

    /** The currentpowerstamp. */
    CURRENTPOWERSTAMP {
        {
            command = "power-stamp";
            typeClass = DateTimeType.class;
            jobClass = Stick.PowerInformationJob.class;
        }
    },

    /** The lasthourconsumption. */
    LASTHOURCONSUMPTION {
        {
            command = "lasthour";
            typeClass = DecimalType.class;
            jobClass = Stick.PowerBufferJob.class;

        }
    },

    /** The lasthourconsumptionstamp. */
    LASTHOURCONSUMPTIONSTAMP {
        {
            command = "lasthour-stamp";
            typeClass = DateTimeType.class;
            jobClass = Stick.PowerBufferJob.class;

        }
    },

    CURRENTCLOCK {
        {
            command = "clock";
            typeClass = StringType.class;
            jobClass = Stick.ClockJob.class;

        }
    },

    REALTIMECLOCK {
        {
            command = "realtime-clock";
            typeClass = DateTimeType.class;
            jobClass = Stick.RealTimeClockJob.class;

        }
    },

    /** The current state of a Circle/Circle+. */
    CURRENTSTATE {
        {
            command = "state";
            typeClass = OnOffType.class;
            jobClass = Stick.InformationJob.class;
        }

    },

    /** The moment when the last message from battery powered device was received (Scan, Sense or Switch). */
    LASTSEEN {
        {
            command = "lastseen";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    },

    /** The state of a Scan, Sense trigger. */
    TRIGGERED {
        {
            command = "triggered";
            typeClass = OnOffType.class;
            jobClass = null;
        }
    },

    /** The timestamp of the most recent Scan, Sense trigger change. */
    TRIGGEREDSTAMP {
        {
            command = "triggered-stamp";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    },

    /** The most recently measured humidity by a Sense. */
    HUMIDITY {
        {
            command = "humidity";
            typeClass = DecimalType.class;
            jobClass = null;
        }
    },

    /** The timestamp of the most recently measured humidity by a Sense. */
    HUMIDITYSTAMP {
        {
            command = "humidity-stamp";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    },

    /** The most recently measured temperature by a Sense. */
    TEMPERATURE {
        {
            command = "temperature";
            typeClass = DecimalType.class;
            jobClass = null;
        }
    },

    /** The timestamp of the most recently measured temperature by a Sense. */
    TEMPERATURESTAMP {
        {
            command = "temperature-stamp";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    },

    /** The state of the left button of a Switch. */
    LEFTBUTTONSTATE {
        {
            command = "left-button-state";
            typeClass = OnOffType.class;
            jobClass = null;
        }
    },

    /** The timestamp of the most recent left button press of a Switch. */
    LEFTBUTTONSTATESTAMP {
        {
            command = "left-button-state-stamp";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    },

    /** The state of the right button of a Switch. */
    RIGHTBUTTONSTATE {
        {
            command = "right-button-state";
            typeClass = OnOffType.class;
            jobClass = null;
        }
    },

    /** The timestamp of the most recent right button press of a Switch. */
    RIGHTBUTTONSTATESTAMP {
        {
            command = "right-button-state-stamp";
            typeClass = DateTimeType.class;
            jobClass = null;
        }
    };

    // Represents the Plugwise command as it will be used in *.items configuration
    String command;

    // type of the item supported by this command
    Class<? extends Type> typeClass;

    // class of the Job that will fetch the value(s) for this command.
    // A single poll/query could yield values/updates for different command types
    Class<? extends Job> jobClass;

    /**
     * Gets the plugwise command.
     *
     * @return the plugwise command
     */
    public String getPlugwiseCommand() {
        return command;
    }

    /**
     * Gets the type class.
     *
     * @return the type class
     */
    public Class<? extends Type> getTypeClass() {
        return typeClass;
    }

    /**
     * Gets the job class.
     *
     * @return the job class
     */
    public Class<? extends Job> getJobClass() {
        return jobClass;
    }

    /**
     * Validate binding.
     *
     * @param PlugwiseCommand command string e.g. message, volume, channel
     * @param item the item
     * @return true if item can bound to PlugwiseCommand
     */

    public static boolean validateBinding(PlugwiseCommandType type, Item item) {
        if (item.getAcceptedDataTypes().contains(type.getTypeClass())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the valid item types.
     *
     * @param PlugwiseCommand command string e.g. state,...
     * @return simple name of all valid item classes
     */

    public static String getValidItemTypes(String PlugwiseCommand) {
        String ret = "";
        for (PlugwiseCommandType c : PlugwiseCommandType.values()) {
            if (PlugwiseCommand.equals(c.getPlugwiseCommand()) && c.getPlugwiseCommand() != null) {
                if (StringUtils.isEmpty(ret)) {
                    ret = c.getTypeClass().getSimpleName();
                } else {
                    if (!ret.contains(c.getTypeClass().getSimpleName())) {
                        ret = ret + ", " + c.getTypeClass().getSimpleName();
                    }
                }
            }
        }
        return ret;
    }

    /**
     * Gets the command type.
     *
     * @param PlugwiseCommand the plugwise command
     * @return the command type
     */
    public static PlugwiseCommandType getCommandType(String PlugwiseCommand) {

        if (StringUtils.isEmpty(PlugwiseCommand)) {
            return null;
        }

        for (PlugwiseCommandType c : PlugwiseCommandType.values()) {

            if (PlugwiseCommand.equals(c.getPlugwiseCommand())) {
                return c;
            }
        }

        throw new IllegalArgumentException("cannot find PlugwiseCommandType for '" + PlugwiseCommand + "'");
    }

}
