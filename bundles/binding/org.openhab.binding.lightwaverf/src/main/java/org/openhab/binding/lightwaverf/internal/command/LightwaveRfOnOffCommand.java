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
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * Represents LightwaveRf On/Off commands. On the LAN commands look like:
 * 100,!R2D3F0 (Off) 101,!R2D3F1 (On)
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfOnOffCommand extends AbstractLightwaveRfCommand
		implements LightwaveRfRoomDeviceMessage {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?(\\d{1,3}),!R(\\d)D(\\d)F([0,1]).*\\s*");
	private static final String ON_FUNCTION = "1";
	private static final String OFF_FUNCTION = "0";

	private final LightwaveRfMessageId messageId;
	private final String roomId;
	private final String deviceId;
	private final boolean on;

	public LightwaveRfOnOffCommand(int messageId, String roomId,
			String deviceId, boolean on) {
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
		this.roomId = roomId;
		this.deviceId = deviceId;
		this.on = on;
	}

	public LightwaveRfOnOffCommand(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher matcher = REG_EXP.matcher(message);
			matcher.matches();
			this.messageId = new LightwaveRfGeneralMessageId(
					Integer.valueOf(matcher.group(1)));
			this.roomId = matcher.group(2);
			this.deviceId = matcher.group(3);
			String function = matcher.group(4);
			if (ON_FUNCTION.equals(function)) {
				on = true;
			} else if (OFF_FUNCTION.equals(function)) {
				on = false;
			} else {
				throw new LightwaveRfMessageException(
						"Received Message has invalid function[" + function
								+ "]: " + message);
			}
		} catch (Exception e) {
			throw new LightwaveRfMessageException("Error converting message: "
					+ message, e);
		}
	}

	@Override
	public String getLightwaveRfCommandString() {
		String function = on ? "1" : "0";
		return getMessageString(messageId, roomId, deviceId, function);
	}

	@Override
	public String getRoomId() {
		return roomId;
	}

	@Override
	public String getDeviceId() {
		return deviceId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case DIMMER:
			return on ? PercentType.HUNDRED : OnOffType.OFF;
		case SWITCH:
			return on ? OnOffType.ON : OnOffType.OFF;
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
		if (that instanceof LightwaveRfOnOffCommand) {
			return Objects.equals(this.messageId,
					((LightwaveRfOnOffCommand) that).messageId)
					&& Objects.equals(this.roomId,
							((LightwaveRfOnOffCommand) that).roomId)
					&& Objects.equals(this.deviceId,
							((LightwaveRfOnOffCommand) that).deviceId)
					&& Objects.equals(this.on,
							((LightwaveRfOnOffCommand) that).on);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, roomId, deviceId, on);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.ROOM_DEVICE;
	}

	public static boolean matches(String message) {
		return message.contains("F0") || message.contains("F1");
	}

}
