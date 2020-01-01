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

import org.openhab.binding.ecobee.messages.Thermostat.Event;

/**
 * The set hold function sets the thermostat into a hold with the specified temperature. Creates a hold for the
 * specified duration. Note that an event is created regardless of whether the program is in the same state as the
 * requested state.
 *
 * <p>
 * There is also support for creating a hold by passing a <code>holdClimateRef</code> request parameter/value pair to
 * this function (See {@link Thermostat.Event}). When an existing and valid {@link Thermostat.Climate#climateRef} value
 * is passed to this function, the <code>coolHoldTemp</code>, <code>heatHoldTemp</code> and <code>fan</code> mode from
 * that {@link Thermostat.Climate} are used in the creation of the hold event. The values from that Climate will take
 * precedence over any <code>coolHoldTemp</code>, <code>heatHoldTemp</code> and <code>fan</code> mode parameters passed
 * into this function separately.
 *
 * <p>
 * To resume from a hold and return to the program, use the {@link ResumeProgramFunction}.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SetHold.shtml">SetHold</a>
 * @author John Cocula
 * @since 1.7.0
 */
public final class SetHoldFunction extends AbstractFunction {

    /**
     * Construct a SetHoldFunction from an Event object and hold options
     *
     * @param event
     *            the event object from which to create the SetHoldFunction
     * @param holdType
     *            the hold duration type (may be <code>null</code> so the API decides "indefinite")
     * @param holdHours
     *            the number of hours to hold for, used and required if holdType='holdHours'
     * @param startDateTime
     *            the start date and time in thermostat time, used if holdType='dateTime'
     * @param endDateTime
     *            the end date and time in thermostat time, used and required if holdType='dateTime'
     */
    public SetHoldFunction(Event event, HoldType holdType, Integer holdHours, Date startDateTime, Date endDateTime) {
        super("setHold");

        // Sanity check parameters
        if (event == null) {
            throw new IllegalArgumentException("event must not be null");
        }
        if (holdType == HoldType.HOLD_HOURS && holdHours == null) {
            throw new IllegalArgumentException("holdHours must be specified when holdType='holdHours'");
        } else if (holdType == HoldType.DATE_TIME && endDateTime == null) {
            throw new IllegalArgumentException("endDateTime must be specified when holdType='dateTime'");
        }
        if (event.getHoldClimateRef() == null) {
            if (Boolean.TRUE.equals(event.isTemperatureAbsolute())
                    && Boolean.TRUE.equals(event.isTemperatureRelative())) {
                throw new IllegalArgumentException("cannot set both absolute and relative temperatures");
            }
            if (Boolean.TRUE.equals(event.isTemperatureAbsolute())
                    && (event.getCoolHoldTemp() == null || event.getHeatHoldTemp() == null)) {
                throw new IllegalArgumentException(
                        "coolHoldTemp and heatHoldTemp must be specified when 'isTemperatureAbsolute' is true");
            }
            if (Boolean.TRUE.equals(event.isTemperatureRelative())
                    && (event.getCoolRelativeTemp() == null || event.getHeatRelativeTemp() == null)) {
                throw new IllegalArgumentException(
                        "coolRelativeTemp and heatRelativeTemp must be specified when 'isTemperatureRelative' is true");
            }
        }

        // Make parameters from the input event
        if (event.isOccupied() != null) {
            makeParams().put("isOccupied", event.isOccupied());
        }
        if (event.isCoolOff() != null) {
            makeParams().put("isCoolOff", event.isCoolOff());
        }
        if (event.isHeatOff() != null) {
            makeParams().put("isHeatOff", event.isHeatOff());
        }
        if (event.getCoolHoldTemp() != null) {
            makeParams().put("coolHoldTemp", event.getCoolHoldTemp());
        }
        if (event.getHeatHoldTemp() != null) {
            makeParams().put("heatHoldTemp", event.getHeatHoldTemp());
        }
        if (event.getFan() != null) {
            makeParams().put("fan", event.getFan());
        }
        if (event.getVent() != null) {
            makeParams().put("vent", event.getVent());
        }
        if (event.getVentilatorMinOnTime() != null) {
            makeParams().put("ventilatorMinOnTime", event.getVentilatorMinOnTime());
        }
        if (event.isOptional() != null) {
            makeParams().put("isOptional", event.isOptional());
        }
        if (event.isTemperatureRelative() != null) {
            makeParams().put("isTemperatureRelative", event.isTemperatureRelative());
        }
        if (event.getCoolRelativeTemp() != null) {
            makeParams().put("coolRelativeTemp", event.getCoolRelativeTemp());
        }
        if (event.getHeatRelativeTemp() != null) {
            makeParams().put("heatRelativeTemp", event.getHeatRelativeTemp());
        }
        if (event.isTemperatureAbsolute() != null) {
            makeParams().put("isTemperatureAbsolute", event.isTemperatureAbsolute());
        }
        if (event.getFanMinOnTime() != null) {
            makeParams().put("fanMinOnTime", event.getFanMinOnTime());
        }
        if (event.getHoldClimateRef() != null) {
            makeParams().put("holdClimateRef", event.getHoldClimateRef());
        }

        // Make parameters from the holdType and hold options
        makeParams().put("holdType", holdType);
        if (holdHours != null) {
            makeParams().put("holdHours", holdHours);
        }
        if (startDateTime != null) {
            makeParams().put("startDate", ymd.format(startDateTime));
            makeParams().put("startTime", hms.format(startDateTime));
        }
        if (endDateTime != null) {
            makeParams().put("endDate", ymd.format(endDateTime));
            makeParams().put("endTime", hms.format(endDateTime));
        }
    }
}
