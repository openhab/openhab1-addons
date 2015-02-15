package org.openhab.binding.lightwaverf.internal.command;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRfDeviceRegistrationCommand extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!F\\*p");
	private final LightwaveRfMessageId messageId;
	private static final String FUNCTION = "*";
	private static final String PARAMETER = "";
	
	public LightwaveRfDeviceRegistrationCommand(String message) throws LightwaveRfMessageException {
		try{
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
		}
		catch(Exception e){
			throw new LightwaveRfMessageException("Error converting message: " + message, e);
		}
	}
	
	public LightwaveRfDeviceRegistrationCommand(int messageId) {
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
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

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}
	
	public static boolean matches(String message) {
		Matcher m = REG_EXP.matcher(message);
		return m.matches();
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof LightwaveRfDeviceRegistrationCommand){
			return Objects.equals(this.messageId, ((LightwaveRfDeviceRegistrationCommand) that).messageId);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}
	
	@Override
	public String toString() {
		return "LightwaveRfDeviceRegistration[MessageId: " + messageId + "]";
	}

}
