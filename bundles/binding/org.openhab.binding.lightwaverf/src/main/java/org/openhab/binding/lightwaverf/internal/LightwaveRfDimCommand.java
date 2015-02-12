package com.Lightwave;

public class LightwaveRfDimCommand implements LightwaveRFCommand {

    private final String roomId;
    private final String deviceId;
    private final int dimLevel;

    public LightwaveRfDimCommand(String roomId, String deviceId, int dimmingLevel) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.dimLevel = dimmingLevel;
    }
    
    @Override
    public String getLightwaveRfCommandString() {
        return "!R" + roomId + "D" + deviceId + "FdP" + dimLevel;
    }

}
