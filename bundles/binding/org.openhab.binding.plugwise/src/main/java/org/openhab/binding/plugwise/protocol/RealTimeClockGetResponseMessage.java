/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.plugwise.protocol;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
/**
 * Real time clock response message. The Circle+ is the only device to hold a real real-time clock
 *
 * @author Karel Goderis
 * @since 1.1.0
 */
public class RealTimeClockGetResponseMessage extends Message {

	private int seconds;
	private int minutes;
	private int hour;
	@SuppressWarnings("unused")
	private int weekday;
	private int day;
	private int month;
	private int year;

	public RealTimeClockGetResponseMessage(int sequenceNumber, String payLoad) {
		super(sequenceNumber, payLoad);
		type = MessageType.REALTIMECLOCK_GET_RESPONSE;	}

	@Override
	protected String payLoadToHexString() {
		return payLoad;
	}

	@Override
	protected void parsePayLoad() {
		
		Pattern RESPONSE_PATTERN = Pattern.compile("(\\w{16})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})(\\w{2})");
		
		Matcher matcher = RESPONSE_PATTERN.matcher(payLoad);
		if(matcher.matches()) {
			MAC = matcher.group(1);
			seconds =  Integer.parseInt(matcher.group(2));
			minutes =  Integer.parseInt(matcher.group(3));	
			hour = Integer.parseInt(matcher.group(4));
			weekday =  Integer.parseInt(matcher.group(5));
			day = Integer.parseInt(matcher.group(6));
			month = Integer.parseInt(matcher.group(7));
			year = Integer.parseInt(matcher.group(8)) + 2000;
		}
		else {
			logger.debug("Plugwise protocol RealTimeClockGetResponseMessage error: {} does not match", payLoad);
		}
	}

	public DateTime getTime() {
		return new DateTime(year, month, day, hour, minutes, seconds, DateTimeZone.UTC).toDateTime(DateTimeZone.getDefault());
	}

}
