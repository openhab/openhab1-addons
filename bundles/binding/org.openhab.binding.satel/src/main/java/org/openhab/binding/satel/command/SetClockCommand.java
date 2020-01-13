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
package org.openhab.binding.satel.command;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.commons.lang.ArrayUtils;

/**
 * Command class for command to set RTC clock.
 *
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SetClockCommand extends ControlCommand {

    public static final byte COMMAND_CODE = (byte) 0x8e;

    private static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * Creates new command class instance.
     * 
     * @param dateTime
     *            date and time to set
     * @param userCode
     *            code of the user on behalf the control is made
     */
    public SetClockCommand(Calendar dateTime, String userCode) {
        super(COMMAND_CODE, ArrayUtils.addAll(userCodeToBytes(userCode), getDateTimeBytes(dateTime)));
    }

    private static byte[] getDateTimeBytes(Calendar dateTime) {
        return DATETIME_FORMAT.format(dateTime.getTime()).getBytes();
    }

}
