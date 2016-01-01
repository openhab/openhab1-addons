/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;

/**
 * This abstract class represents a command that we can send to the LightwaveRF
 * wifi link it includes utility methods that are applicable for use by all
 * LightwaveRfCommands that extend it.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public abstract class AbstractLightwaveRfCommand implements LightwaveRFCommand {

	public String getFunctionMessageString(LightwaveRfMessageId messageId,
			String roomId, String function) {
		return messageId.getMessageIdString() + ",!R" + roomId + "F" + function
				+ "\n";
	}

	public String getMessageString(LightwaveRfMessageId messageId,
			String roomId, String deviceId, String function) {
		return messageId.getMessageIdString() + ",!R" + roomId + "D" + deviceId
				+ "F" + function + "\n";
	}

	public String getMessageString(LightwaveRfMessageId messageId,
			String roomId, String function) {
		return messageId.getMessageIdString() + ",!R" + roomId + "F"
				+ function + "\n";
	}

	public String getMessageString(LightwaveRfMessageId messageId,
			String roomId, String function, int parameter) {
		return messageId.getMessageIdString() + ",!R" + roomId 
				+ "F" + function + "P" + parameter + "\n";
	}
	
	public String getMessageString(LightwaveRfMessageId messageId,
			String roomId, String deviceId, String function, int parameter) {
		return messageId.getMessageIdString() + ",!R" + roomId + "D" + deviceId
				+ "F" + function + "P" + parameter + "\n";
	}
	
	public String getMessageString(LightwaveRfMessageId messageId,
			String roomId, String deviceId, String function, double parameter) {
		return messageId.getMessageIdString() + ",!R" + roomId + "D" + deviceId
				+ "F" + function + "P" + parameter + "\n";
	}

	public String getVersionString(LightwaveRfMessageId messageId,
			String version) {
		return messageId.getMessageIdString() + ",?V=\"" + version + "\"\n";
	}

	public String getOkString(LightwaveRfMessageId messageId) {
		return messageId.getMessageIdString() + ",OK\n";
	}
	
	public String getDeviceRegistrationMessageString(LightwaveRfMessageId messageId,
			String function, String parameter) {
		return messageId.getMessageIdString() + ",!F" + function + "p"
				+ parameter + "\n";
	}
	

}
