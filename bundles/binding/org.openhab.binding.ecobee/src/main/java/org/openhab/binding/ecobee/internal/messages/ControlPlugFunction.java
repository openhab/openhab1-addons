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

import org.codehaus.jackson.annotate.JsonValue;

/**
 * Control the on/off state of a plug by setting a hold on the plug. 
 * Creates a hold for the on or off state of the plug for the specified duration. 
 * Note that an event is created regardless of whether the program is in the same 
 * state as the requested state.
 * 
 * @see <a href="https://www.ecobee.com/home/developer/api/documentation/v1/functions/ControlPlug.shtml">ControlPlug</a>
 * @author John Cocula
 * @author Ecobee
 */
public final class ControlPlugFunction extends AbstractFunction {

	/**
	 * The state to put the plug into. Valid values: on, off, resume.
	 */
	public static enum PlugState {
		
		/**
		 * Sets the plug into the on state for the start/end period 
		 * specified. Creates a plug hold in the on state.
		 */
		ON("on"),
		
		/**
		 * Sets the plug into the off state for the start/end period 
		 * specified. Creates a plug hold in the off state.
		 */
		OFF("off"),
		
		/**
		 * Causes the plug to resume its regular program and to follow it. 
		 * Removes the currently active plug hold, if no hold is currently 
		 * running, nothing happens. No other optional properties are used.
		 */
		RESUME("resume");
		
		private final String state;
		
		private PlugState(String state) { this.state = state; }
		
		@Override
		@JsonValue
		public String toString() { return this.state; }
	}

	/**
	 * Construct a ControlPlug function.
	 * 
	 * @param plugName the name of the plug. Ensure each plug has a unique name. Required.
	 * @param plugState the state to put the plug into. Valid values: on, off, resume. Required.
	 * @param startDateTime the start date and time in thermostat time
	 * @param endDateTime the end date and time in thermostat time
	 * @param holdType the hold duration type
	 * @param holdHours the number of hours to hold for, used and required if holdType='holdHours'
	 * @throws IllegalArgumentException if the parameters are incorrect.
	 */
	public ControlPlugFunction( final String plugName,
								final PlugState plugState,
								final Date startDateTime,
								final Date endDateTime,
								final HoldType holdType,
								final Integer holdHours ) {
		super("controlPlug");
	
		if (plugName == null || plugState == null) {
			throw new IllegalArgumentException("plugName and plugState arguments are required.");
		}
		if (holdType == HoldType.DATE_TIME && endDateTime == null) {
			throw new IllegalArgumentException("End date/time is required for dateTime hold type.");
		}
		if (holdType == HoldType.HOLD_HOURS && holdHours == null) {
			throw new IllegalArgumentException("holdHours must be specified when using holdHours hold type.");
		}

		makeParams().put("plugName", plugName);
		makeParams().put("plugState", plugState);
		if (startDateTime != null) {
			makeParams().put("startDate", ymd.format(startDateTime) );
			makeParams().put("startTime", hms.format(startDateTime) );
		}
		if (endDateTime != null) {
			makeParams().put("endDate", ymd.format(endDateTime) );
			makeParams().put("endTime", hms.format(endDateTime) );
		}
		if (holdType != null) {
			makeParams().put("holdType", holdType );
		}
		if (holdHours != null) {
			makeParams().put("holdHours", holdHours );
		}
	}
}
