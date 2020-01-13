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
package org.openhab.binding.ecobee.messages;

import java.util.Date;

/**
 * The create vacation function creates a vacation event on the thermostat. If the start/end date/times are not provided
 * for the vacation event, the vacation event will begin immediately and last 14 days.
 *
 * <p>
 * If both the <code>coolHoldTemp</code> and <code>heatHoldTemp</code> parameters provided to this function have the
 * same value, and the {@link Thermostat} is in auto mode, then the two values will be adjusted during processing to be
 * separated by the value stored in {@link Thermostat.Settings#heatCoolMinDelta}.
 *
 * @see <a
 *      href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/CreateVacation.shtml">CreateVacation
 *      </a>
 * @author John Cocula
 * @author Ecobee
 * @since 1.7.0
 */
public final class CreateVacationFunction extends AbstractFunction {

    public CreateVacationFunction(final String name, final Temperature coolHoldTemp, final Temperature heatHoldTemp,
            final Date startDateTime, final Date endDateTime, final FanMode fan, final Integer fanMinOnTime) {
        super("createVacation");

        if (name == null || coolHoldTemp == null || heatHoldTemp == null) {
            throw new IllegalArgumentException("name, coolHoldTemp and heatHoldTemp arguments are required.");
        }

        makeParams().put("name", name);
        makeParams().put("coolHoldTemp", coolHoldTemp);
        makeParams().put("heatHoldTemp", heatHoldTemp);
        if (startDateTime != null) {
            makeParams().put("startDate", ymd.format(startDateTime));
            makeParams().put("startTime", hms.format(startDateTime));
        }
        if (endDateTime != null) {
            makeParams().put("endDate", ymd.format(endDateTime));
            makeParams().put("endTime", hms.format(endDateTime));
        }
        if (fan != null) {
            makeParams().put("fan", fan);
        }
        if (fanMinOnTime != null) {
            // doc says String not Integer for fanMinOnTime parameter (@watou)
            makeParams().put("fanMinOnTime", fanMinOnTime.toString());
        }
    }
}
