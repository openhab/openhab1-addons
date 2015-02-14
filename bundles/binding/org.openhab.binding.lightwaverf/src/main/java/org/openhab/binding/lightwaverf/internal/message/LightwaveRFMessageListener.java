package org.openhab.binding.lightwaverf.internal.message;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;

public interface LightwaveRFMessageListener {
	
	public void messageRecevied(LightwaveRFCommand command);

}
