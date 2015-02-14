package org.openhab.binding.lightwaverf.internal;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;


public abstract class AbstractLightwaveRfCommand implements LightwaveRFCommand {

	public String getMessageString(LightwaveRfMessageId messageId, String function, String parameter) {
		return messageId.getMessageIdString() + ",!F" + function + "p" + parameter + "\n";
	}
	
	public String getMessageString(LightwaveRfMessageId messageId, String roomId, String deviceId, String function) {
        return messageId.getMessageIdString() + ",!R" + roomId + "D" + deviceId + "F" + function + "\n"; 
	}

	public String getMessageString(LightwaveRfMessageId messageId, String roomId, String deviceId, String function, String parameter) {
        return messageId.getMessageIdString() + ",!R" + roomId + "D" + deviceId + "F" + function + "P" + parameter + "\n"; 
	}
	
	public String getVersionString(LightwaveRfMessageId messageId, String version){
		return messageId.getMessageIdString() + ",?V=" + version + "\n";
	}

	public String getOkString(LightwaveRfMessageId messageId){
		return messageId.getMessageIdString() + ",OK\n";
	}
	

}
