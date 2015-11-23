/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfJsonMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;


/**
 * A message received from the Energy Monitor. On
 * the LAN the messages look like: *!{"trans":215955,"mac":"03:41:C4","time":1435620183,
 * "prod":"pwrMtr","serial":"9470FE","signal":79,"type":"energy",
 * "cUse":271,"maxUse":2812,"todUse":8414,"yesUse":8377}
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfEnergyMonitorMessage implements LightwaveRfSerialMessage {

	private static final Pattern MESSAGE_ID_REG_EXP = Pattern
			.compile(".*\"trans\":([^,}]*).*");
	private static final Pattern MAC_ID_REG_EXP = Pattern
			.compile(".*\"mac\":\"([^\"}]*)\".*");
	private static final Pattern TIME_ID_REG_EXP = Pattern
			.compile(".*\"time\":([^,}]*).*");
	private static final Pattern PROD_REG_EXP = Pattern
			.compile(".*\"prod\":\"([^\"}]*)\".*");
	private static final Pattern SERIAL_ID_REG_EXP = Pattern
			.compile(".*\"serial\":\"([^\"}]*)\".*");
	private static final Pattern SIGNAL_REG_EXP = Pattern
			.compile(".*\"signal\":([^,}]*).*");
	private static final Pattern TYPE_REG_EXP = Pattern
			.compile(".*\"type\":\"([^\"}]*)\".*");
	private static final Pattern CURRENT_USE_REG_EXP = Pattern
			.compile(".*\"cUse\":([^,}]*).*");
	private static final Pattern MAX_USE_REG_EXP = Pattern
			.compile(".*\"maxUse\":([^,}]*).*");
	private static final Pattern TODAY_USE_REG_EXP = Pattern
			.compile(".*\"todUse\":([^,}]*).*");
	private static final Pattern YESTERDAY_USE_REG_EXP = Pattern
			.compile(".*\"yesUse\":([^,}]*).*");
	
	
	private final LightwaveRfMessageId messageId;
	private final String mac;
	private final Date time;
	private final String prod;
	private final String serial;
	private final int signal;
	private final String type;
	private final int cUse;
	private final int maxUse;
	private final int todUse;
	private final int yesUse;
	
	public LightwaveRfEnergyMonitorMessage(String message) {
		messageId = new LightwaveRfJsonMessageId(
				Integer.valueOf(getStringFromText(MESSAGE_ID_REG_EXP, message)));
		mac = getStringFromText(MAC_ID_REG_EXP, message);
		time = new Date(
				Long.valueOf(getStringFromText(TIME_ID_REG_EXP, message)));
		prod = getStringFromText(PROD_REG_EXP, message);
		serial = getStringFromText(SERIAL_ID_REG_EXP, message);
		signal = Integer.valueOf(getStringFromText(SIGNAL_REG_EXP, message));
		type = getStringFromText(TYPE_REG_EXP, message);

		cUse = Integer.valueOf(getStringFromText(CURRENT_USE_REG_EXP, message));
		maxUse = Integer.valueOf(getStringFromText(MAX_USE_REG_EXP, message));
		todUse = Integer.valueOf(getStringFromText(TODAY_USE_REG_EXP, message));
		yesUse = Integer.valueOf(getStringFromText(YESTERDAY_USE_REG_EXP, message));
	}
	
	private String getStringFromText(Pattern regExp, String message) {
		Matcher matcher = regExp.matcher(message);
		matcher.matches();
		return matcher.group(1);
	}	
	
	@Override
	public String getLightwaveRfCommandString() {
		return new StringBuilder("*!{")
				.append("\"trans\":").append(messageId.getMessageIdString())
				.append(",\"mac\":\"").append(mac)
				.append("\",\"time\":").append(time.getTime())
				.append(",\"prod\":\"").append(prod)
				.append("\",\"serial\":\"").append(serial)
				.append("\",\"signal\":").append(signal)
				.append(",\"type\":\"").append(type)
				.append(",\"cUse\":\"").append(type)
				.append(",\"maxUse\":\"").append(type)
				.append(",\"todUse\":\"").append(type)
				.append(",\"yesUse\":\"").append(type)
				.append("}").toString();
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case SIGNAL:
			return new DecimalType(getSignal());
		case ENERGY_CURRENT_USAGE:
			return new DecimalType(getcUse());
		case ENERGY_MAX_USAGE:
			return new DecimalType(getMaxUse());
		case ENERGY_TODAY_USAGE:
			return new DecimalType(getTodUse());
		case ENERGY_YESTERDAY_USAGE:
			return new DecimalType(getYesUse());
		case UPDATETIME:
			Calendar cal = Calendar.getInstance();
			// The date seems to be in a strange timezone so at the moment we
			// use server date.
			// cal.setTime(getTime());
			return new DateTimeType(cal);
		default:
			return null;
		}
	}
	
	public static boolean matches(String message) {
		if (message.contains("pwrMtr")) {
			return true;
		}
		return false;
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.SERIAL;
	}

	@Override
	public String getSerial() {
		return serial;
	}
	
	public String getMac() {
		return mac;
	}
	
	public int getMaxUse() {
		return maxUse;
	}
	
	public String getProd() {
		return prod;
	}
	
	public int getSignal() {
		return signal;
	}
	
	public Date getTime() {
		return time;
	}
	
	public int getTodUse() {
		return todUse;
	}
	
	public String getType() {
		return type;
	}
	
	public int getYesUse() {
		return yesUse;
	}
	
	public int getcUse() {
		return cUse;
	}
}
