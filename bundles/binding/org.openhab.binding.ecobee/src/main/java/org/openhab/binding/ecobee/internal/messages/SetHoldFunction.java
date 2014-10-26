/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ecobee.internal.messages;

import java.util.Date;

/**
 * The set hold function sets the thermostat into a hold with the specified temperature. 
 * Creates a hold for the specified duration. Note that an event is created regardless 
 * of whether the program is in the same state as the requested state.
 * 
 * <p>
 * There is also support for creating a hold by passing a <code>holdClimateRef</code> request 
 * parameter/value pair to this function (See {@link Thermostat.Event}). When an existing 
 * and valid {@link Thermostat.Climate#climateRef} value is passed to this function, 
 * the <code>coolHoldTemp</code>, <code>heatHoldTemp</code> and <code>fan</code> mode from 
 * that {@link Thermostat.Climate} are used in the creation 
 * of the hold event. The values from that Climate will take precedence over any 
 * <code>coolHoldTemp</code>, <code>heatHoldTemp</code> and <code>fan</code> mode parameters 
 * passed into this function separately.
 * 
 * <p>
 * To resume from a hold and return to the program, use the {@link ResumeProgramFunction}.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SetHold.shtml">SetHold</a>
 * @author John Cocula
 */
public final class SetHoldFunction extends AbstractFunction {

	/**
	 * Construct a SetHoldFunction.
	 * 
	 * @param coolHoldTemp the temperature to set the cool hold at
	 * @param heatHoldTemp the temperature to set the heat hold at
	 * @param holdClimateRef the Climate to use as reference for setting the coolHoldTemp, 
	 * heatHoldTemp and fan settings for this hold. If this value is passed, the 
	 * coolHoldTemp and heatHoldTemp are not required.
	 * @param startDateTime the start date and time in thermostat time
	 * @param endDateTime the end date and time in thermostat time
	 * @param holdType the hold duration type
	 * @param holdHours the number of hours to hold for, used and required if holdType='holdHours'
	 */
	public SetHoldFunction(
			Temperature coolHoldTemp,
			Temperature heatHoldTemp,
			String holdClimateRef,
			Date startDateTime,
			Date endDateTime,
			HoldType holdType,
			Integer holdHours ) {
		super("setHold");

		if (holdClimateRef == null && (coolHoldTemp == null || heatHoldTemp == null)) {
			throw new IllegalArgumentException("coolHoldTemp and heatHoldTemp are required when holdClimateRef is not supplied.");
		}
		if (holdType == HoldType.HOLD_HOURS && holdHours == null) {
			throw new IllegalArgumentException("holdHours must be specified when holdType='holdHours'");
		}
		if (holdType == HoldType.DATE_TIME && endDateTime == null) {
			throw new IllegalArgumentException("endDateTime must be specific when holdType='dateTime'");
		}

		if (coolHoldTemp != null) {
			makeParams().put("coolHoldTemp", coolHoldTemp);
		}
		if (heatHoldTemp != null) {
			makeParams().put("heatHoldTemp", heatHoldTemp);
		}
		if (holdClimateRef != null) {
			makeParams().put("holdClimateRef", holdClimateRef);
		}
		if (startDateTime != null) {
			makeParams().put("startDate", ymd.format(startDateTime));
			makeParams().put("startTime", hms.format(startDateTime));
		}
		if (endDateTime != null) {
			makeParams().put("endDate", ymd.format(endDateTime));
			makeParams().put("endTime", hms.format(endDateTime));
		}
		if (holdType != null) {
			makeParams().put("holdType", holdType );
		}
		if (holdHours != null) {
			makeParams().put("holdHours", holdHours );
		}
	}
}
