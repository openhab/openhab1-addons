package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LightwaverfConvertor {

	private static final Pattern REG_EXP = Pattern.compile(".*F(.).*");

    public static String convertToLightwaveRfMessage(String roomId, String deviceId, String command){
        if(command == "OnOffType"){
            boolean on = true;
            return new LightwaveRfOnOffCommand(roomId, deviceId, on).getLightwaveRfCommandString();
        }
        else if(command == "PercentType"){
            int dimmingLevel = 0;
            return new LightwaveRfDimCommand(roomId, deviceId, dimmingLevel).getLightwaveRfCommandString();
        }
        else if(command == "IncreaseDecrease"){
            boolean up = true;
            return new LightwaveRfDimUpDownCommand(roomId, deviceId, up).getLightwaveRfCommandString();
            
        }
        throw new RuntimeException("Unsupported Command");
    }
    
    public static LightwaveRFCommand convertFromLightwaveRfMessage(String message){
    	switch (LightwaverfConvertor.getModeCode(message)) {
		case '0':
		case '1':
			return new LightwaveRfOnOffCommand(message);
		case 'd':
			return new LightwaveRfDimCommand(message);
		case '>':
		case '<':
			return new LightwaveRfDimUpDownCommand(message);
		default:
			throw new IllegalArgumentException("Message not recorgnised: " + message);
		}
    	
    }
    
    private static char getModeCode(String message){
    	Matcher m = REG_EXP.matcher(message);
    	String modeCode = m.group(0);
    	return modeCode.charAt(0);
    	
    }
    
    
}
