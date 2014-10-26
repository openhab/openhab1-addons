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
 * The set occupied function may only be used by EMS thermostats.
 * 
 * <p>
 * The function switches a thermostat from occupied mode to unoccupied, or vice versa. 
 * If used on a Smart thermostat, the function will throw an error. Switch occupancy 
 * events are treated as Holds. There may only be one Switch Occupancy at one time, 
 * and the new event will replace any previous event.
 * 
 * <p>
 * Note that an occupancy event is created regardless what the program on the thermostat 
 * is set to. For example, if the program is currently unoccupied and you set 
 * <code>occupied=false</code>, an occupancy event will be created using the heat/cool settings of 
 * the unoccupied program climate. If your intent is to go back to the program and remove 
 * the occupancy event, use {@link ResumeProgramFunction} instead.
 *
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/SetOccupied.shtml">SetOccupied</a>
 * @author John Cocula
 */
public final class SetOccupiedFunction extends AbstractFunction {

	/**
	 * Construct a SetOccupiedFunction.
	 * 
	 * @param occupied the climate to use for the temperature, 
	 * occupied (true) or unoccupied (false)
	 * @param startDateTime the start date and time in thermostat time
	 * @param endDateTime the end date and time in thermostat time
	 * @param holdType the hold duration type
	 * @param holdHours the number of hours to hold for, 
	 * used and required if holdType='holdHours'
	 */
	public SetOccupiedFunction(
			Boolean occupied,
			Date startDateTime,
			Date endDateTime,
			HoldType holdType,
			Integer holdHours) {
		super("setOccupied"); // not in doc; assuming
		
		if (occupied == null) {
			throw new IllegalArgumentException("occupied state is required.");
		}
		if (holdType == HoldType.HOLD_HOURS && holdHours == null) {
			throw new IllegalArgumentException("holdHours must be specified when holdType='holdHours'");
		}
		if (holdType == HoldType.DATE_TIME && endDateTime == null) {
			throw new IllegalArgumentException("endDateTime must be specific when holdType='dateTime'");
		}

		makeParams().put("occupied", occupied);
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
