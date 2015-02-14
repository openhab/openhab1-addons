package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfHeatingInfoCommand implements LightwaveRFCommand {

	/*
	 * Commands Like
	 * *!{
	 * 		"trans":1232,
	 * 		"mac":"03:02:71",
	 * 		"time":1423827547,
	 * 		"prod":"valve",
	 * 		"serial":"5A4F02",
	 * 		"signal":0,
	 * 		"type":"temp",
	 * 		"batt":2.72,
	 * 		"ver":56,
	 * 		"state":"run",
	 * 		"cTemp":17.8,
	 * 		"cTarg":19.0,
	 * 		"output":80,
	 * 		"nTarg":24.0,
	 * 		"nSlot":"06:00",
	 * 		"prof":5
	 * }
	 */  

	private static final Pattern SERIAL_ID_REG_EXP = Pattern.compile(".*\"serial\":([^,]*).*");
	private static final Pattern MESSAGE_ID_REG_EXP = Pattern.compile(".*\"trans\":([^,]*).*");
	private static final Pattern BATTERY_REG_EXP = Pattern.compile(".*\"batt\":([^,]*).*");
	private static final Pattern SIGNAL_REG_EXP = Pattern.compile(".*\"signal\":([^,]*).*");
	private static final Pattern CURRENT_TEMP_REG_EXP = Pattern.compile(".*\"cTemp\":([^,]*).*");
	private static final Pattern TARGET_TEMP_REG_EXP = Pattern.compile(".*\"cTarg\":([^,]*).*");
	
	private final LightwaveRfMessageId messageId;
	private final String serial;
	private final double signal;
	private final double currentTemperature;
	private final double currentTargetTemperature;
	private final double batteryLevel;
	
	
	public LightwaveRfHeatingInfoCommand(String message) {
		Matcher serialMatcher = SERIAL_ID_REG_EXP.matcher(message);
		serialMatcher.matches();
		serial = serialMatcher.group(1);

		Matcher messageIdMatcher = MESSAGE_ID_REG_EXP.matcher(message);
		messageIdMatcher.matches();
		messageId = new LightwaveRfMessageId(Integer.valueOf(messageIdMatcher.group(1)));
		
		Matcher batteryMatcher = BATTERY_REG_EXP.matcher(message);
		batteryMatcher.matches();
		batteryLevel = Double.valueOf(serialMatcher.group(1));

		Matcher signalMatcher = SIGNAL_REG_EXP.matcher(message);
		signalMatcher.matches();
		signal = Double.valueOf(signalMatcher.group(1));

		Matcher currentTempMatcher = CURRENT_TEMP_REG_EXP.matcher(message);
		currentTempMatcher.matches();
		currentTemperature = Double.valueOf(currentTempMatcher.group(1));

		Matcher targetTempMatcher = TARGET_TEMP_REG_EXP.matcher(message);
		targetTempMatcher.matches();
		currentTargetTemperature = Double.valueOf(targetTempMatcher.group(1));
	}

	public String getLightwaveRfCommandString() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getRoomId() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDeviceId() {
		// TODO Auto-generated method stub
		return null;
	}

	public State getState() {
		// TODO Auto-generated method stub
		return null;
	}

	public LightwaveRfMessageId getMessageId() {
		// TODO Auto-generated method stub
		return null;
	}

	public static boolean matches(String message) {
		if(message.contains("cTemp")){
			return true;
		}
		return false;
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
}
