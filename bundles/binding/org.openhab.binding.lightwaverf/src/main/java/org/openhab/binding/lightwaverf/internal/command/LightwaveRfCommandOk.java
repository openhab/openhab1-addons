package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.types.State;

public class LightwaveRfCommandOk extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),OK");
	
	private final LightwaveRfMessageId messageId;
	
	public LightwaveRfCommandOk(String message) throws LightwaveRfMessageException {
		try{
			Matcher m = REG_EXP.matcher(message);
			this.messageId = new LightwaveRfMessageId(Integer.valueOf(m.group(0)));
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error converting message: " + message, e);
		}
	}

	public String getLightwaveRfCommandString() {
		return getOkString(messageId);
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
