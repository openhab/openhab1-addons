package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public interface LightwaveRFCommand {
    
    LightwaveRFCommand STOP_MESSAGE = new LightwaveRFStopPublisherCommand();

	public String getLightwaveRfCommandString();
	
	public State getState(LightwaveRfType type);
	
	public LightwaveRfMessageId getMessageId();
	public LightwaveRfMessageType getMessageType();
	
}
