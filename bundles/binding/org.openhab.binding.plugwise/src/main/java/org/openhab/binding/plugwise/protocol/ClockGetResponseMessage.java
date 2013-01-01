/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
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
		return new LocalTime(hour,minutes,seconds);
	}

	public ClockGetResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.CLOCK_GET_RESPONSE;	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
		
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})");

		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()){
			MAC = matcher.group(1);
			hour = Integer.parseInt(matcher.group(2),16);
			minutes =  Integer.parseInt(matcher.group(3),16);	
			seconds =  Integer.parseInt(matcher.group(4),16);
			weekday =  Integer.parseInt(matcher.group(5),16);
		}
		else {
			logger.debug("Plugwise protocol ClockGetResponseMessage error: {} does not match", payLoad);
		}
	}

}
