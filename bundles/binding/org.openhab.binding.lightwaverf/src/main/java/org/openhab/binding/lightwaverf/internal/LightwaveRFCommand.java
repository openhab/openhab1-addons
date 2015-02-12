package org.openhab.binding.lightwaverf.internal;

import org.openhab.core.types.State;

public interface LightwaveRFCommand {
    
    LightwaveRFCommand STOP_MESSAGE = new LightwaveRFStopPublisherCommand();

	public String getLightwaveRfCommandString();

	public String getRoomId();

	public String getDeviceId();

	public State getState();
	

}
