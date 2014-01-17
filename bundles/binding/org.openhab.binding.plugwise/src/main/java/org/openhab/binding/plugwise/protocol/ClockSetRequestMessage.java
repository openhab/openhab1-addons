/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Circle Clock Set request - based on what is known about the PW protocol, only the Clock of the Circle+ has to be set
 * and then the Circle+ will set the Clock of all the Circles in the network
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class ClockSetRequestMessage extends Message {
	
	private DateTime stamp;

	public ClockSetRequestMessage(String MAC, DateTime stamp) {
		super(MAC, "");
		type = MessageType.CLOCK_SET_REQUEST;
		
		// Circles expect clock info to be in the UTC timezone
		this.stamp = stamp.toDateTime(DateTimeZone.UTC);
	}

	@Override
	protected String payLoadToHexString() {
		
		String date = String.format("%02X", stamp.year().get() - 2000) + String.format("%02X", stamp.monthOfYear().get()) + String.format("%04X", (stamp.dayOfMonth().get()-1)*24*60 + stamp.minuteOfDay().get());
		// If we set logaddress to FFFFFFFFF then previous buffered data will be kept by the Circle
		String logaddress = "FFFFFFFF";
		String time = String.format("%02X", stamp.hourOfDay().get()) + String.format("%02X", stamp.minuteOfHour().get()) + String.format("%02X", stamp.secondOfMinute().get()) + String.format("%02X", stamp.dayOfWeek().get()-1);
		
		return date + logaddress + time;
	}
	
	protected String sequenceNumberToHexString() {
		return "";
	}

	@Override
	protected void parsePayLoad() {
	}
	
	@Override
	public String getPayLoad() {
		payLoad = payLoadToHexString();
		return payLoad;
	}

}
