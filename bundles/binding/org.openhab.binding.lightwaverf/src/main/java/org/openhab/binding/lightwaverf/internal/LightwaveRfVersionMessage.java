package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.types.State;


public class LightwaveRfVersionMessage extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),.*V=(.*)");
	
	private final LightwaveRfMessageId messageId;
	private final String version;
	
	public LightwaveRfVersionMessage(String message) {

		Matcher m = REG_EXP.matcher(message);
		this.messageId = new LightwaveRfMessageId(Integer.valueOf(m.group(0)));
		this.version = m.group(1);
	}
	
	public String getLightwaveRfCommandString() {
		return getVersionString(messageId, version); 
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

	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		Matcher m = REG_EXP.matcher(message);
		return m.matches();
	}

}
