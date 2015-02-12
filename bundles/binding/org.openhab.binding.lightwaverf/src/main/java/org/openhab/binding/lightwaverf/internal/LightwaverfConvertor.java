package com.Lightwave;


public class LightwaverfConvertor {

    
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
    
    
}
