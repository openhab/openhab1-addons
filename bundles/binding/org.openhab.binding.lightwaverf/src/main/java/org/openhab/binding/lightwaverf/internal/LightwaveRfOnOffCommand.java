package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LightwaveRfOnOffCommand implements LightwaveRFCommand {

	/**
	 * Commands are like: 
	 *     100,!R2D3F0 (Off)
	 *     101,!R2D3F1 (On)
	 */
	
	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),!R([0-9])D([0-9])F([0,1])");
	private static final String ON_FUNCTION = "1";
	private static final String OFF_FUNCTION = "0";
	
    private final String roomId;
    private final String deviceId;
    private final boolean on;
    
    public LightwaveRfOnOffCommand(String roomId, String deviceId, boolean on) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.on = on;
    }
    
    public LightwaveRfOnOffCommand(String message) {
    	Matcher matcher = REG_EXP.matcher(message);
    	roomId = matcher.group(1);
    	deviceId = matcher.group(2);
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

	@Override
    public String getLightwaveRfCommandString() {
        char funtion = on ? '1' : '0';
        return "!R" + roomId + "D" + deviceId + "F" + funtion; 
    }

}
