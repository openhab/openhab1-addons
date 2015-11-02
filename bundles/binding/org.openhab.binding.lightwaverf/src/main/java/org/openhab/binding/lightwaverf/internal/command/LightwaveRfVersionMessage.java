/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.library.types.StringType;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfVersionMessage extends AbstractLightwaveRfCommand
		implements LightwaveRFCommand {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?(\\d{1,3}).*V=\"(.*)\"\\s*");

	private final LightwaveRfMessageId messageId;
	private final String version;

	public LightwaveRfVersionMessage(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher m = REG_EXP.matcher(message);
			m.matches();
			this.messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m
					.group(1)));
			this.version = m.group(2);
		} catch (Exception e) {
			throw new LightwaveRfMessageException("Error decoding message: "
					+ message, e);
		}
	}

	@Override
	public String getLightwaveRfCommandString() {
		return getVersionString(messageId, version);
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case VERSION:
			return StringType.valueOf(version);
		default:
			return null;
		}
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		return message.contains("?V=");
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.VERSION;
	}

}
