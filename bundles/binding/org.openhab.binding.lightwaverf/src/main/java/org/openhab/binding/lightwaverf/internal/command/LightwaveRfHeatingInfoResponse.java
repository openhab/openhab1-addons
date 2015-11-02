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
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
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
public class LightwaveRfHeatingInfoResponse extends AbstractLightwaveRfJsonMessage implements LightwaveRfSerialMessage {

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

	private final String mac;
	private final Date time;
	private final String prod;
	private final String serial;
	private final String signal;
	private final String type;
	private final String batteryLevel;
	private final String version;
	private final String state;
	private final String currentTemperature;
	private final String currentTargetTemperature;
	private final String output;
	private final String nextTargetTeperature;
	private final String nextSlot;
	private final String prof;

	public LightwaveRfHeatingInfoResponse(String message) throws LightwaveRfMessageException {
		super(message);
		mac = getStringFromText(MAC_ID_REG_EXP, message);
		time = getDateFromText(TIME_ID_REG_EXP, message);
		prod = getStringFromText(PROD_REG_EXP, message);
		serial = getStringFromText(SERIAL_ID_REG_EXP, message);
		signal = getStringFromText(SIGNAL_REG_EXP, message);
		type = getStringFromText(TYPE_REG_EXP, message);
		batteryLevel = getStringFromText(BATTERY_REG_EXP,message);
		version = getStringFromText(VERSION_REG_EXP, message);
		state = getStringFromText(STATE_REG_EXP, message);
		currentTemperature = getStringFromText(CURRENT_TEMP_REG_EXP, message);
		currentTargetTemperature = getStringFromText(TARGET_TEMP_REG_EXP, message);
		output = getStringFromText(OUTPUT_TEMP_REG_EXP, message);
		nextTargetTeperature = getStringFromText(NEXT_TARGET_TEMP_REG_EXP, message);
		nextSlot = getStringFromText(NEXT_SLOT_REG_EXP, message);
		prof = getStringFromText(PROF_REG_EXP, message);
	}

	@Override
	public String getLightwaveRfCommandString() {
		return new StringBuilder("*!{").append("\"trans\":")
				.append(getMessageId().getMessageIdString()).append(",\"mac\":\"")
				.append(mac).append("\",\"time\":").append(getLightwaveDateFromJavaDate(time))
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
		case HEATING_OUTPUT:
			return new PercentType(getOutput());
		case UPDATETIME:
			Calendar cal = Calendar.getInstance();
			cal.setTime(getTime());
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

	public static boolean matches(String message) {
		if (message.contains("cTemp")) {
			return true;
		}
		return false;
	}

	public Date getTime() {
		return time;
	}

	public String getSignal() {
		return signal;
	}

	public String getBatteryLevel() {
		return batteryLevel;
	}

	public String getCurrentTemperature() {
		return currentTemperature;
	}

	public String getCurrentTargetTemperature() {
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

	public String getNextTargetTeperature() {
		return nextTargetTeperature;
	}

	public String getOutput() {
		return output;
	}

	public String getProd() {
		return prod;
	}

	public String getProf() {
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
