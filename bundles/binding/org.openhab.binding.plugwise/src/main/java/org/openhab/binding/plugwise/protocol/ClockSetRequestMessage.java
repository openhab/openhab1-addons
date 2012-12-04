/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
