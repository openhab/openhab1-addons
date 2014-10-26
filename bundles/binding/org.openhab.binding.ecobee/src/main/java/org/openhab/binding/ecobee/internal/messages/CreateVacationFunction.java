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
 * The create vacation function creates a vacation event on the thermostat. 
 * If the start/end date/times are not provided for the vacation event, 
 * the vacation event will begin immediately and last 14 days.
 * 
 * <p>
 * If both the <code>coolHoldTemp</code> and <code>heatHoldTemp</code> parameters 
 * provided to this function have the same value, and the {@link Thermostat} is in 
 * auto mode, then the two values will be adjusted during processing to be separated 
 * by the value stored in {@link Thermostat.Settings#heatCoolMinDelta}.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/CreateVacation.shtml">CreateVacation</a>
 * @author John Cocula
 * @author Ecobee
 */
public final class CreateVacationFunction extends AbstractFunction {

	public CreateVacationFunction(  final String name,
									final Temperature coolHoldTemp,
									final Temperature heatHoldTemp,
									final Date startDateTime,
									final Date endDateTime,
									final FanMode fan,
									final Integer fanMinOnTime ) { // TODO doc says String not Integer
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
	}
}
