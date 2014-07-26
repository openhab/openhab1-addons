/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcul.internal.messages;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Message class to handle time information
 * 
 * @author Paul Hampson (cyclingengineer)
 * @since 1.6.0
 */
public class TimeInfoMsg extends BaseMsg {

	final static private int TIME_INFO_PAYLOAD_LEN = 5; /* in bytes */
	final static private int TIME_INFO_REQUEST_PAYLOAD_LEN = 0;

	private static final Logger logger = LoggerFactory
			.getLogger(TimeInfoMsg.class);

	private Calendar messageTimeInfo;
	private TimeZone tz = TimeZone.getTimeZone("Europe/London");

	public TimeInfoMsg(String rawMsg) {
		super(rawMsg);

		if (this.payload.length == TIME_INFO_PAYLOAD_LEN) {
			logger.debug("Year  => " + (this.payload[0] + 2000));
			logger.debug("Month => "
					+ (((this.payload[3] & 0xC0) >> 4) | ((this.payload[4] & 0xC0) >> 6)));
			logger.debug("DoM   => " + this.payload[1]);
			logger.debug("Hour  => " + (this.payload[2] & 0x3F));
			logger.debug("Min   => " + (this.payload[3] & 0x3F));
			logger.debug("Sec   => " + (this.payload[4] & 0x3F));

			messageTimeInfo = new GregorianCalendar(
					this.payload[0] + 2000, // year
					(((this.payload[3] & 0xC0) >> 4) | ((this.payload[4] & 0xC0) >> 6)) - 1, // month
					this.payload[1], // day of month
					(this.payload[2] & 0x3F), // hour of day
					(this.payload[3] & 0x3F), // minute
					(this.payload[4] & 0x3F)); // seconds
		} else if (this.payload.length == TIME_INFO_REQUEST_PAYLOAD_LEN) {
			/*
			 * set time to the beginning of history - this will trigger time
			 * update
			 */
			messageTimeInfo = new GregorianCalendar(0, 0, 1);
			logger.debug("Received Time request - setting time to 1/1/0");
		} else {
			logger.error("TimeInfoMsg raw packet was of incorrect length to parse! Expect "
					+ TIME_INFO_PAYLOAD_LEN + " got " + payload.length);
		}
	}

	public TimeInfoMsg(byte msgCount, byte msgFlag, byte groupId,
			String srcAddr, String dstAddr, String TimeZoneStr) {
		super(msgCount, msgFlag, MaxCulMsgType.TIME_INFO, groupId, srcAddr,
				dstAddr);

		tz = TimeZone.getTimeZone(TimeZoneStr);
		updateTime();
	}

	public void updateTime() {
		byte[] payload = new byte[TIME_INFO_PAYLOAD_LEN];
		byte tmp;
		Calendar now = new GregorianCalendar(tz);

		payload[0] = (byte) (now.get(Calendar.YEAR) - 2000);
		payload[1] = (byte) now.get(Calendar.DAY_OF_MONTH);
		payload[2] = (byte) now.get(Calendar.HOUR_OF_DAY); // TODO ?? can set
															// DST flag in
															// bit[6] to advance
															// time by 1hour,
															// but java handles
															// this
		/* build byte 3. [0:5] contain minute, [6:7] contain bits [2:3] of month */
		tmp = (byte) (now.get(Calendar.MONTH) + 1);
		tmp &= 0x0c;
		tmp <<= 4;
		tmp |= (byte) (now.get(Calendar.MINUTE) & 0x3f);
		payload[3] = tmp;

		/*
		 * build byte 3. [0:5] contain seconds, [6:7] contain bits [0:1] of
		 * month
		 */
		tmp = (byte) (now.get(Calendar.MONTH) + 1);
		tmp &= 0x03;
		tmp <<= 6;
		tmp |= (byte) (now.get(Calendar.SECOND) & 0x3f);
		payload[4] = tmp;

		super.appendPayload(payload);
		super.printDebugPayload();
	}

	@Override
	protected void printFormattedPayload() {
		super.printDebugPayload();
		logger.debug("\tDecoded Time: "
				+ this.messageTimeInfo.get(Calendar.YEAR) + "-"
				+ this.messageTimeInfo.get(Calendar.MONTH + 1) + "-"
				+ this.messageTimeInfo.get(Calendar.DAY_OF_MONTH) + " "
				+ this.messageTimeInfo.get(Calendar.HOUR_OF_DAY) + ":"
				+ this.messageTimeInfo.get(Calendar.MINUTE) + ":"
				+ this.messageTimeInfo.get(Calendar.SECOND));
	}

	public Calendar getTimeInfo() {
		return messageTimeInfo;
	}
}
