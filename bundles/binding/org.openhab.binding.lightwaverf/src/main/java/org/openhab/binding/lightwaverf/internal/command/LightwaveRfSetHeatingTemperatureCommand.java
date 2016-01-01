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
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * This represents a command to set heating to a particular value. On the LAN
 * commands are like: 104,!R1DhF*tP19.0
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfSetHeatingTemperatureCommand extends
		AbstractLightwaveRfCommand implements LightwaveRfRoomMessage {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?([0-9]{1,3}),!R([0-9])DhF\\*tP([0-9\\.]{1,5}).*\\s*");
	private static final String FUNCTION = "*t";
	private final LightwaveRfMessageId messageId;
	private final String roomId;
	private final String deviceId = "h";
	private final double setTemperature;

	public LightwaveRfSetHeatingTemperatureCommand(String message) {
		Matcher m = REG_EXP.matcher(message);
		m.matches();
		messageId = new LightwaveRfGeneralMessageId(Integer.valueOf(m.group(1)));
		roomId = m.group(2);
		setTemperature = Double.valueOf(m.group(3));
	}

	public LightwaveRfSetHeatingTemperatureCommand(int messageId,
			String roomId, double setTemperature) {
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
		this.roomId = roomId;
		this.setTemperature = setTemperature;
	}

	@Override
	public String getLightwaveRfCommandString() {
		return getMessageString(messageId, roomId, deviceId, FUNCTION,
				setTemperature);
	}

	@Override
	public String getRoomId() {
		return roomId;
	}

	@Override
	public State getState(LightwaveRfType type) {
		switch (type) {
		case HEATING_SET_TEMP:
			return new DecimalType(setTemperature);
		default:
			return null;
		}
	}

	@Override
	public LightwaveRfMessageId getMessageId() {
		return messageId;
	}

	public static boolean matches(String message) {
		return message.contains(FUNCTION);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.ROOM;
	}

}
