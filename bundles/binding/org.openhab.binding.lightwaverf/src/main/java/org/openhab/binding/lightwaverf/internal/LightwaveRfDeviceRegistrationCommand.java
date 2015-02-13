package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.types.State;

public class LightwaveRfDeviceRegistrationCommand extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),F*p");
	private final LightwaveRfMessageId messageId;
	private static final String FUNCTION = "*";
	private static final String PARAMETER = "";
	

	public LightwaveRfDeviceRegistrationCommand(String message){
		Matcher m = REG_EXP.matcher(message);
		messageId = new LightwaveRfMessageId(Integer.valueOf(m.group(0)));
	}
	
	public LightwaveRfDeviceRegistrationCommand(int messageId) {
		this.messageId = new LightwaveRfMessageId(messageId);
	}
	
	
	public String getLightwaveRfCommandString() {
		
		return getMessageString(messageId, FUNCTION, PARAMETER);
	}

	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}
	

	public String getRoomId() {
		return null;
	}

	public String getDeviceId() {
		return null;
	}

	public State getState() {
		return null;
	}

	public static boolean matches(String message) {
		Matcher m = REG_EXP.matcher(message);
		return m.matches();
	}

}
