package org.openhab.binding.lightwaverf.internal;


public class LightwaveRfHeatingMessageId implements LightwaveRfMessageId {

	private final int messageId;
	
	public LightwaveRfHeatingMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageIdString(){
		return String.valueOf(messageId);
	}
	
}
