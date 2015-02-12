package org.openhab.binding.lightwaverf.internal;

public class LightwaveRfDimUpDownCommand implements LightwaveRFCommand {

    private final String roomId;
    private final String deviceId;
    private final boolean up;
    
	/**
	 * Commands could be but this currently doesnt work: 
	 *     100,!R2D3F> (Dim Up)
	 *     101,!R2D3F< (Dim down)
	 */ 
    
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
