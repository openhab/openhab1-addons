package org.openhab.binding.lightwaverf.internal.message;

import java.text.DecimalFormat;
import java.util.Objects;

public class LightwaveRfGeneralMessageId implements LightwaveRfMessageId {

	private final int messageId;
	private static final DecimalFormat formatter = new DecimalFormat("000");
	
	public LightwaveRfGeneralMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageIdString(){
		return formatter.format(messageId);
	}
	
	@Override
	public boolean equals(Object that) {
		if(that instanceof LightwaveRfGeneralMessageId){
			return Objects.equals(this.messageId, ((LightwaveRfGeneralMessageId) that).messageId);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}
	
	@Override
	public String toString() {
		return "LightwaveRfGeneralMessageId[" + messageId + "]";
	}
}
