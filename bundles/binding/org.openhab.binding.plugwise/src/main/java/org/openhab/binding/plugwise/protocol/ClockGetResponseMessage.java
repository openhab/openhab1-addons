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
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalTime;

/**
 * Circle Clock response message - not all response fields have been reverse engineered
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class ClockGetResponseMessage extends Message {

    private static final Pattern RESPONSE_PATTERN = Pattern
            .compile("(\\w{16})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})");

    private int hour;
    private int minutes;
    private int seconds;
    private int weekday;

    public int getHour() {
        return hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getWeekday() {
        return weekday;
    }

    public LocalTime getTime() {
        return new LocalTime(hour, minutes, seconds);
    }

    public ClockGetResponseMessage(int sequenceNumber, String payLoad) {
        super(sequenceNumber, payLoad);
        type = MessageType.CLOCK_GET_RESPONSE;
    }

    @Override
    protected String payLoadToHexString() {
        return payLoad;
    }

    @Override
    protected void parsePayLoad() {
        Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
        if (matcher.matches()) {
            MAC = matcher.group(1);
            hour = Integer.parseInt(matcher.group(2), 16);
            minutes = Integer.parseInt(matcher.group(3), 16);
            seconds = Integer.parseInt(matcher.group(4), 16);
            weekday = Integer.parseInt(matcher.group(5), 16);
        } else {
            logger.debug("Plugwise protocol ClockGetResponseMessage error: {} does not match", payLoad);
        }
    }

}
