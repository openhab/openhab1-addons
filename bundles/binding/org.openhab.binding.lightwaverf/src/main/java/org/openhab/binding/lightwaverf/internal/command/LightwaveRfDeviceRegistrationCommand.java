/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfRegistrationMessageId;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfDeviceRegistrationCommand extends
		AbstractLightwaveRfCommand implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?([0-9]{1,3}),!F\\*p\\s*");
	private final LightwaveRfMessageId messageId;
	private static final String FUNCTION = "*";
	private static final String PARAMETER = "";

	public LightwaveRfDeviceRegistrationCommand(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
		} catch (Exception e) {
			throw new LightwaveRfMessageException("Error converting message: "
					+ message, e);
		}
	}

	public LightwaveRfDeviceRegistrationCommand() {
		this.messageId = new LightwaveRfRegistrationMessageId();
	}

	@Override
	public String getLightwaveRfCommandString() {
		return getDeviceRegistrationMessageString(messageId, FUNCTION, PARAMETER);
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		return null;
	}

	public static boolean matches(String message) {
		Matcher m = REG_EXP.matcher(message);
		return m.matches();
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof LightwaveRfDeviceRegistrationCommand) {
			return Objects.equals(this.messageId,
					((LightwaveRfDeviceRegistrationCommand) that).messageId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId);
	}

	@Override
	public String toString() {
		return "LightwaveRfDeviceRegistration[MessageId: " + messageId + "]";
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.DEVICE_REGISTRATION;
	}

}
