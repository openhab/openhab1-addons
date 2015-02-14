package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.core.types.State;


public class LightwaveRfVersionMessage extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

//	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),.*V=(.*)");
	private static final Pattern REG_EXP = Pattern.compile("(\\d{1,3}).*V=(.*)");
	
	
	private final LightwaveRfMessageId messageId;
	private final String version;
	
	public LightwaveRfVersionMessage(String message) throws LightwaveRfMessageException {
		try{
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			this.messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
			this.version = m.group(2);
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error decoding message: " + message, e);
		}
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
		return message.contains("?V=");
	}

}
