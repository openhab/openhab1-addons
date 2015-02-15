package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

public class LightwaveRfSetHeatingTemperatureCommand extends AbstractLightwaveRfCommand implements LightwaveRFCommand { 

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])DhF\\*tP([0-9\\.]{1,4})");
	private static final String FUNCTION = "*t";
	/*
	 * Commands Like
	 * 104,!R1DhF*tP19.0
	 */
	
	private final LightwaveRfMessageId messageId;
	private final String roomId;
	private final String deviceId = "h";
	private final double setTemperature;
	
	public LightwaveRfSetHeatingTemperatureCommand(String message) {
		Matcher m = REG_EXP.matcher(message);
		m.matches();
		messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
		roomId = m.group(2);
		setTemperature = Double.valueOf(m.group(3));
	}
	
	public LightwaveRfSetHeatingTemperatureCommand(LightwaveRfMessageId messageId, String roomId, double setTemperature) {
		this.messageId = messageId;
		this.roomId = roomId;
		this.setTemperature = setTemperature;
	}	
	
	public String getLightwaveRfCommandString() {
		return getMessageString(messageId, roomId, deviceId, FUNCTION, String.valueOf(setTemperature));
	}

	public String getRoomId() {
		return roomId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case HEATING_SET_TEMP:
			return new DecimalType(setTemperature);
		default:
			return null;
		}
	}

	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		return message.contains(FUNCTION);
	}

}
