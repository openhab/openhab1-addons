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
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * A message received from the Radiator Valves detailing their current state. On
 * the LAN the messages look like: !{ "trans":1232, "mac":"03:02:71",
 * "time":1423827547, "prod":"valve", "serial":"5A4F02", "signal":0,
 * "type":"temp", "batt":2.72, "ver":56, "state":"run", "cTemp":17.8,
 * "cTarg":19.0, "output":80, "nTarg":24.0, "nSlot":"06:00", "prof":5 }
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfHeatingInfoResponse implements LightwaveRfSerialMessage {

	/*
	 * Commands Like
	 */

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
	private static final Pattern BATTERY_REG_EXP = Pattern
			.compile(".*\"batt\":([^,}]*).*");
	private static final Pattern VERSION_REG_EXP = Pattern
			.compile(".*\"ver\":([^,}]*).*");
	private static final Pattern STATE_REG_EXP = Pattern
			.compile(".*\"state\":\"([^\"}]*)\".*");
	private static final Pattern CURRENT_TEMP_REG_EXP = Pattern
			.compile(".*\"cTemp\":([^,}]*).*");
	private static final Pattern TARGET_TEMP_REG_EXP = Pattern
			.compile(".*\"cTarg\":([^,}]*).*");
	private static final Pattern OUTPUT_TEMP_REG_EXP = Pattern
			.compile(".*\"output\":([^,}]*).*");
	private static final Pattern NEXT_TARGET_TEMP_REG_EXP = Pattern
			.compile(".*\"nTarg\":([^,}]*).*");
	private static final Pattern NEXT_SLOT_REG_EXP = Pattern
			.compile(".*\"nSlot\":\"([^\"}]*)\".*");
	private static final Pattern PROF_REG_EXP = Pattern
			.compile(".*\"prof\":([^,}]*).*");

	private final LightwaveRfMessageId messageId;
	private final String mac;
	private final Date time;
	private final String prod;
	private final String serial;
	private final int signal;
	private final String type;
	private final double batteryLevel;
	private final String version;
	private final String state;
	private final double currentTemperature;
	private final double currentTargetTemperature;
	private final int output;
	private final double nextTargetTeperature;
	private final String nextSlot;
	private final int prof;

	public LightwaveRfHeatingInfoResponse(String message) {
		messageId = new LightwaveRfJsonMessageId(
				Integer.valueOf(getStringFromText(MESSAGE_ID_REG_EXP, message)));
		mac = getStringFromText(MAC_ID_REG_EXP, message);
		time = new Date(
				Long.valueOf(getStringFromText(TIME_ID_REG_EXP, message)));
		prod = getStringFromText(PROD_REG_EXP, message);
		serial = getStringFromText(SERIAL_ID_REG_EXP, message);
		signal = Integer.valueOf(getStringFromText(SIGNAL_REG_EXP, message));
		type = getStringFromText(TYPE_REG_EXP, message);
		batteryLevel = Double.valueOf(getStringFromText(BATTERY_REG_EXP,
				message));
		version = getStringFromText(VERSION_REG_EXP, message);
		state = getStringFromText(STATE_REG_EXP, message);
		currentTemperature = Double.valueOf(getStringFromText(
				CURRENT_TEMP_REG_EXP, message));
		currentTargetTemperature = Double.valueOf(getStringFromText(
				TARGET_TEMP_REG_EXP, message));
		output = Integer
				.valueOf(getStringFromText(OUTPUT_TEMP_REG_EXP, message));
		nextTargetTeperature = Double.valueOf(getStringFromText(
				NEXT_TARGET_TEMP_REG_EXP, message));
		nextSlot = getStringFromText(NEXT_SLOT_REG_EXP, message);
		prof = Integer.valueOf(getStringFromText(PROF_REG_EXP, message));
	}

	private String getStringFromText(Pattern regExp, String message) {
		Matcher matcher = regExp.matcher(message);
		matcher.matches();
		return matcher.group(1);
	}

	@Override
	public String getLightwaveRfCommandString() {
		return new StringBuilder("*!{").append("\"trans\":")
				.append(messageId.getMessageIdString()).append(",\"mac\":\"")
				.append(mac).append("\",\"time\":").append(time.getTime())
				.append(",\"prod\":\"").append(prod).append("\",\"serial\":\"")
				.append(serial).append("\",\"signal\":").append(signal)
				.append(",\"type\":\"").append(type).append("\",\"batt\":")
				.append(batteryLevel).append(",\"ver\":").append(version)
				.append(",\"state\":\"").append(state).append("\",\"cTemp\":")
				.append(currentTemperature).append(",\"cTarg\":")
				.append(currentTargetTemperature).append(",\"output\":")
				.append(output).append(",\"nTarg\":")
				.append(nextTargetTeperature).append(",\"nSlot\":\"")
				.append(nextSlot).append("\",\"prof\":").append(prof)
				.append("}").toString();
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case HEATING_BATTERY:
			return new DecimalType(getBatteryLevel());
		case SIGNAL:
			return new DecimalType(getSignal());
		case HEATING_CURRENT_TEMP:
			return new DecimalType(getCurrentTemperature());
		case HEATING_SET_TEMP:
			return new DecimalType(getCurrentTargetTemperature());
		case HEATING_MODE:
			return new StringType(getState());
		case UPDATETIME:
			Calendar cal = Calendar.getInstance();
			// The date seems to be in a strange timezone so at the moment we
			// use server date.
			// cal.setTime(getTime());
			return new DateTimeType(cal);
		case VERSION:
			return new StringType(getVersion());
		default:
			return null;
		}
	}

	public String getState() {
		return state;
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		if (message.contains("cTemp")) {
			return true;
		}
		return false;
	}

	public Date getTime() {
		return time;
	}

	public double getSignal() {
		return signal;
	}

	public double getBatteryLevel() {
		return batteryLevel;
	}

	public double getCurrentTemperature() {
		return currentTemperature;
	}

	public double getCurrentTargetTemperature() {
		return currentTargetTemperature;
	}

	@Override
	public String getSerial() {
		return serial;
	}

	public String getMac() {
		return mac;
	}

	public String getNextSlot() {
		return nextSlot;
	}

	public double getNextTargetTeperature() {
		return nextTargetTeperature;
	}

	public double getOutput() {
		return output;
	}

	public String getProd() {
		return prod;
	}

	public double getProf() {
		return prof;
	}

	public String getType() {
		return type;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.SERIAL;
	}
}
