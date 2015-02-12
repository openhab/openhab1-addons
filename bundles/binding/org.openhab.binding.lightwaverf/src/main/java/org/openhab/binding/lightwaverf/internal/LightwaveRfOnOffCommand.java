package com.Lightwave;

public class LightwaveRfOnOffCommand implements LightwaveRFCommand {
    
    private final String roomId;
    private final String deviceId;
    private final boolean on;
    
    public LightwaveRfOnOffCommand(String roomId, String deviceId, boolean on) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.on = on;
    }
    
    @Override
    public String getLightwaveRfCommandString() {
        char funtion = on ? '1' : '0';
        return "!R" + roomId + "D" + deviceId + "F" + funtion; 
    }

}
