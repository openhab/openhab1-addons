package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfMessageId;
import org.openhab.core.types.State;

public interface LightwaveRFCommand {
    
    LightwaveRFCommand STOP_MESSAGE = new LightwaveRFStopPublisherCommand();

	public String getLightwaveRfCommandString();
	
	public String getRoomId();
	
	public String getDeviceId();

	public State getState();
	
	public LightwaveRfMessageId getMessageId();
	
}
