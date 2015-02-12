package org.openhab.binding.lightwaverf.internal;

import java.text.DecimalFormat;

public class LightwaveRfMessageId {
	
	private final int messageId;
	private static final DecimalFormat formatter = new DecimalFormat("000");
	
	public LightwaveRfMessageId(int messageId) {
		this.messageId = messageId;
	}
	
	public String getMessageIdString(){
		return formatter.format(messageId);
	}

}
