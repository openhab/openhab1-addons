package com.Lightwave;

public class LightwaveRfDimUpDownCommand implements LightwaveRFCommand {

    private final String roomId;
    private final String deviceId;
    private final boolean up;
    
    public LightwaveRfDimUpDownCommand(String roomId, String deviceId, boolean up) {
        this.roomId = roomId;
        this.deviceId = deviceId;
        this.up = up;
    }

    @Override
    public String getLightwaveRfCommandString() {
        // TODO Auto-generated method stub
        return null;
    }

}
