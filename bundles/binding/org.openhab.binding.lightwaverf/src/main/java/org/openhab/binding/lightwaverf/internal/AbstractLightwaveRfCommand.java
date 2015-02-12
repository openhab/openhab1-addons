package org.openhab.binding.lightwaverf.internal;

public abstract class AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private final String messageId;
	
	public AbstractLightwaveRfCommand(String messageId) {
		this.messageId = messageId;
	}

}
