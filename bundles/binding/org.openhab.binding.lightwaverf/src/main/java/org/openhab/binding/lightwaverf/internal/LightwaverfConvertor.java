package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.core.library.types.IncreaseDecreaseType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.Type;


public class LightwaverfConvertor {

	private static final Pattern REG_EXP = Pattern.compile(".*F(.).*");


    public static LightwaveRFCommand convertToLightwaveRfMessage(String roomId, String deviceId, Type command){
    	if(deviceId == null){
    		return LightwaverfConvertor.convertToLightwaveRfMessage(roomId, command);
    	}
    	else if(command instanceof OnOffType){
            boolean on = (command == OnOffType.ON);
            return new LightwaveRfOnOffCommand(roomId, deviceId, on);
        }
        else if(command instanceof PercentType){
            int dimmingLevel = ((PercentType) command).intValue();
            return new LightwaveRfDimCommand(roomId, deviceId, dimmingLevel);
        }
        else if(command instanceof IncreaseDecreaseType){
            boolean up = (command == IncreaseDecreaseType.INCREASE);
            return new LightwaveRfDimUpDownCommand(roomId, deviceId, up);
            
        }
        throw new RuntimeException("Unsupported Command: " + command);
    }

    public static LightwaveRFCommand convertToLightwaveRfMessage(String roomId, Type command){
    	if(roomId == null){
    		throw new IllegalArgumentException("Item not found");
    	}
    	throw new IllegalArgumentException("Not implemented yet");
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
