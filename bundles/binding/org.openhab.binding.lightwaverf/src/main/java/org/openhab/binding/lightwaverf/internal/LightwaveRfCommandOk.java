package org.openhab.binding.lightwaverf.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LightwaveRfCommandOk implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern.compile("([0-9]{1,3}),OK");
	
	private final LightwaveRfMessageId messageId;
	
	public LightwaveRfCommandOk(String message) {
		Matcher m = REG_EXP.matcher(message);
		this.messageId = new LightwaveRfMessageId(Integer.valueOf(m.group(0)));
	}

	@Override
	public String getLightwaveRfCommandString() {
		return messageId + ",OK\n";
	}
}
