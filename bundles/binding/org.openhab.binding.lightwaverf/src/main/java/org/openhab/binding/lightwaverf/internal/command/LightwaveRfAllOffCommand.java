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
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfAllOffCommand extends AbstractLightwaveRfCommand implements
		LightwaveRfRoomMessage {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?([0-9]{1,3}),!R([0-9])Fa.*\\s*");
	private static final String FUNCTION = "a";
	
	private final LightwaveRfMessageId messageId;
	private final String roomId;

	/**
	 * Commands are like: 100,!R2Fa 
	 */

	public LightwaveRfAllOffCommand(int messageId, String roomId) {
		this.roomId = roomId;
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
	}

	public LightwaveRfAllOffCommand(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher matcher = REG_EXP.matcher(message);
			matcher.matches();
			this.messageId = new LightwaveRfGeneralMessageId(
					Integer.valueOf(matcher.group(1)));
			this.roomId = matcher.group(2);
		} catch (Exception e) {
			throw new LightwaveRfMessageException(
					"Error converting Dimming message: " + message, e);
		}
	}

	@Override
	public String getLightwaveRfCommandString() {
		return getMessageString(messageId, roomId, FUNCTION);
	}


	@Override
	public String getRoomId() {
		return roomId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case ALL_OFF:
			return OnOffType.OFF;
		default:
			return null;
		}
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	@Override
	public boolean equals(Object that) {
		if (that instanceof LightwaveRfAllOffCommand) {
			return Objects.equals(this.messageId,
					((LightwaveRfAllOffCommand) that).messageId)
					&& Objects.equals(this.roomId,
							((LightwaveRfAllOffCommand) that).roomId);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, roomId);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.ROOM;
	}

	public static boolean matches(String message) {
		return message.contains("Fa");
	}
}
