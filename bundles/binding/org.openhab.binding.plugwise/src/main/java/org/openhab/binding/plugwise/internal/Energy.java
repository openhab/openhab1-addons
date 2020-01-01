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
package org.openhab.binding.plugwise.internal;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A simple class to represent energy usage, converting back and forward between Plugwise date representations
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class Energy {

    private DateTime time;
    private long pulses;
    private double interval;

    public Energy(String logdate, long l, double interval) {

        if (logdate.length() == 8) {

            if (!logdate.equals("FFFFFFFF")) {

                int year = 0;
                int month = 0;
                long minutes = 0;

                year = Integer.parseInt(StringUtils.left(logdate, 2), 16) + 2000;
                month = Integer.parseInt(StringUtils.mid(logdate, 2, 2), 16);
                minutes = Long.parseLong(StringUtils.right(logdate, 4), 16);

                time = new DateTime(year, month, 1, 0, 0, DateTimeZone.UTC).plusMinutes((int) minutes)
                        .toDateTime(DateTimeZone.getDefault()).minusHours(1);

            } else {
                time = DateTime.now();
                this.interval = interval;
                this.pulses = 0;
            }

        } else {
            time = DateTime.now();
        }

        this.interval = interval;
        this.pulses = l;

    }

    public Energy(DateTime logdate, long pulses, double interval) {
        time = logdate;
        this.interval = interval;
        this.pulses = pulses;
    }

    @Override
    public String toString() {
        return time.toString() + "-" + Double.toString(interval) + "-" + Long.toString(pulses);
    }

    public DateTime getTime() {
        return time;
    }

    public long getPulses() {
        return pulses;
    }

    public double getInterval() {
        return interval;
    }

}
