package org.openhab.binding.lightwaverf.internal;

public class LightwaveRfDimUpDownCommand implements LightwaveRFCommand {

	/**
	 * Commands could be but this currently doesnt work: 
	 *     100,!R2D3F> (Dim Up)
	 *     101,!R2D3F< (Dim down)
	 */ 
    
    public LightwaveRfDimUpDownCommand(String message){
    	throw new IllegalArgumentException("Not implemented");
    }
    
    public LightwaveRfDimUpDownCommand(String roomId, String deviceId, boolean up) {
    	throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public String getLightwaveRfCommandString() {
    	throw new IllegalArgumentException("Not implemented");
    }

}
