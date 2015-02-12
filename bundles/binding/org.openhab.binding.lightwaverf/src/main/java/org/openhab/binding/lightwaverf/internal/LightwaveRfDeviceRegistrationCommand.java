package org.openhab.binding.lightwaverf.internal;

public class LightwaveRfDeviceRegistrationCommand implements LightwaveRFCommand {

	private final LightwaveRfMessageId messageId;
	
	public LightwaveRfDeviceRegistrationCommand(int messageId) {
		this.messageId = new LightwaveRfMessageId(messageId);
	}
	
	@Override
	public String getLightwaveRfCommandString() {
		return messageId.getMessageIdString() + ",!F*p\n";
	}

}
