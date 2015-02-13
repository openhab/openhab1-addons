package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;

public class LightwaveRfOnOffCommand extends AbstractLightwaveRfCommand implements LightwaveRFCommand {

	/**
	 * Commands are like: 
	 *     100,!R2D3F0 (Off)
	 *     101,!R2D3F1 (On)
	 */
	
	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])D([0-9])F([0,1])");
	private static final String ON_FUNCTION = "1";
	private static final String OFF_FUNCTION = "0";
	
	private final LightwaveRfMessageId messageId;
    private final String roomId;
    private final String deviceId;
    private final boolean on;
    
    public LightwaveRfOnOffCommand(int messageId, String roomId, String deviceId, boolean on) {
        this.messageId = new LightwaveRfMessageId(messageId);
    	this.roomId = roomId;
        this.deviceId = deviceId;
        this.on = on;
    }
    
    public LightwaveRfOnOffCommand(String message) {
    	Matcher matcher = REG_EXP.matcher(message);
		this.messageId = new LightwaveRfMessageId(Integer.valueOf(matcher.group(0)));
    	this.roomId = matcher.group(1);
    	this.deviceId = matcher.group(2);
    	String function = matcher.group(3);
    	if(ON_FUNCTION.equals(function)){
    		on = true;
    	}
    	else if(OFF_FUNCTION.equals(function)){
    		on = false;
    	}
    	else {
    		throw new IllegalArgumentException("Received Message is invalid: " + message);
    	}
	}

    public String getLightwaveRfCommandString() {
        String function = on ? "1" : "0";
        return getMessageString(messageId, roomId, deviceId, function);
    }

	public String getRoomId() {
		return roomId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public State getState() {
		return on ? OnOffType.ON : OnOffType.OFF;
	}
	
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

}
