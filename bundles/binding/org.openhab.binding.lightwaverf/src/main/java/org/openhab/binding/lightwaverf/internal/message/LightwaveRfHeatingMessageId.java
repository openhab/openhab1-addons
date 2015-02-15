package org.openhab.binding.lightwaverf.internal.message;

import java.util.Objects;



public class LightwaveRfHeatingMessageId implements LightwaveRfMessageId {

	private final int messageId;
	
	public LightwaveRfHeatingMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageIdString(){
		return String.valueOf(messageId);
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof LightwaveRfHeatingMessageId){
			return Objects.equals(this.messageId, ((LightwaveRfHeatingMessageId) that).messageId);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}
	
	@Override
	public String toString() {
		return "LightwaveRfHeatingMessageId[" + messageId + "]";
	}

}
