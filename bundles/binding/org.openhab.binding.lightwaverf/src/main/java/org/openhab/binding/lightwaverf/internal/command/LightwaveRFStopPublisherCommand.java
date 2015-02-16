package org.openhab.binding.lightwaverf.internal.command;

import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.types.State;

public class LightwaveRFStopPublisherCommand implements LightwaveRFCommand {

	public String getLightwaveRfCommandString() {
		return null;
	}

	public String getRoomId() {
		return null;
	}

	public String getDeviceId() {
		return null;
	}

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}
	
	public LightwaveRfMessageId getMessageId() {
		return null;
	}
	
	@Override
	public LightwaveRfMessageType getMessageType() {
		return null;
	}


}
