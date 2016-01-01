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
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Represents LightwaveRf Relay commands. On the LAN commands look like:
 * 103,!R1D4F^|Stop|Switch 1
 * 104,!R1D4F)|Close|Switch 1
 * 106,!R1D4F(|Open|Switch 1 * 
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfRelayCommand extends AbstractLightwaveRfCommand
		implements LightwaveRfRoomDeviceMessage {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?(\\d{1,3}),!R(\\d)D(\\d)F([\\^\\)\\(]).*\\s*");
	private static final String STOP_FUNCTION = "^";
	private static final String OPEN_FUNCTION = ")";
	private static final String CLOSE_FUNCTION = "(";

	private final LightwaveRfMessageId messageId;
	private final String roomId;
	private final String deviceId;
	private final int state;

	public LightwaveRfRelayCommand(int messageId, String roomId,
			String deviceId, int state) {
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
		this.roomId = roomId;
		this.deviceId = deviceId;
		this.state = state;
	}

	public LightwaveRfRelayCommand(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher matcher = REG_EXP.matcher(message);
			matcher.matches();
			this.messageId = new LightwaveRfGeneralMessageId(
					Integer.valueOf(matcher.group(1)));
			this.roomId = matcher.group(2);
			this.deviceId = matcher.group(3);
			String function = matcher.group(4);
			if (STOP_FUNCTION.equals(function)) {
				state = 0;
			} else if (OPEN_FUNCTION.equals(function)) {
				state = 1;
			} else if (CLOSE_FUNCTION.equals(function)) {
				state = -1;
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
		String function = null;
		switch (state) {
		case -1:
			function = CLOSE_FUNCTION;
			break;
		case 0:
			function = STOP_FUNCTION;
			break;
		case 1:
			function = OPEN_FUNCTION;
			break;
		default:
			break;
		}
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
		case RELAY:
			return new DecimalType(state);
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
		if (that instanceof LightwaveRfRelayCommand) {
			return Objects.equals(this.messageId,
					((LightwaveRfRelayCommand) that).messageId)
					&& Objects.equals(this.roomId,
							((LightwaveRfRelayCommand) that).roomId)
					&& Objects.equals(this.deviceId,
							((LightwaveRfRelayCommand) that).deviceId)
					&& Objects.equals(this.state,
							((LightwaveRfRelayCommand) that).state);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, roomId, deviceId, state);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.ROOM_DEVICE;
	}

	public static boolean matches(String message) {
		return message.contains("F^") || message.contains("F(") || message.contains("F)");
	}

}
