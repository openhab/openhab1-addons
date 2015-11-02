/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openhab.binding.lightwaverf.internal.AbstractLightwaveRfCommand;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRfDimCommand extends AbstractLightwaveRfCommand implements
		LightwaveRfRoomDeviceMessage {

	private static final Pattern REG_EXP = Pattern
			.compile(".*?([0-9]{1,3}),!R([0-9])D([0-9])FdP([0-9]{1,2}).*\\s*");
	private static final BigDecimal HUNDRED = new BigDecimal(100);
	private static final String DIM_FUNCTION = "d";
	private static final String OFF_FUNCTION = "0";
	
	private final String roomId;
	private final String deviceId;
	private final int openhabDimLevel;
	private final int lightWaveDimLevel;
	private final LightwaveRfMessageId messageId;

	/**
	 * Commands are like: 100,!R2D3FdP1 (Lowest Brightness) 101,!R2D3FdP32 (High
	 * brightness)
	 */

	public LightwaveRfDimCommand(int messageId, String roomId, String deviceId,
			int dimmingLevel) {
		this.roomId = roomId;
		this.deviceId = deviceId;
		this.openhabDimLevel = dimmingLevel;
		this.lightWaveDimLevel = convertOpenhabDimToLightwaveDim(dimmingLevel);
		this.messageId = new LightwaveRfGeneralMessageId(messageId);
	}

	public LightwaveRfDimCommand(String message)
			throws LightwaveRfMessageException {
		try {
			Matcher matcher = REG_EXP.matcher(message);
			matcher.matches();
			this.messageId = new LightwaveRfGeneralMessageId(
					Integer.valueOf(matcher.group(1)));
			this.roomId = matcher.group(2);
			this.deviceId = matcher.group(3);
			this.lightWaveDimLevel = Integer.valueOf(matcher.group(4));
			this.openhabDimLevel = convertLightwaveDimToOpenhabDim(lightWaveDimLevel);
		} catch (Exception e) {
			throw new LightwaveRfMessageException(
					"Error converting Dimming message: " + message, e);
		}
	}

	@Override
	public String getLightwaveRfCommandString() {
		//Sending a Dim command with 0 sets light to full brightness
		if(lightWaveDimLevel == 0){
			return getMessageString(messageId, roomId, deviceId, OFF_FUNCTION);
		}
		else{
			return getMessageString(messageId, roomId, deviceId, DIM_FUNCTION,
				lightWaveDimLevel);
		}
	}

	/**
	 * Convert a 0-31 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static int convertOpenhabDimToLightwaveDim(int openhabDim) {
		return BigDecimal.valueOf(openhabDim).multiply(BigDecimal.valueOf(32))
				.divide(HUNDRED, 0, BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * Convert a 0-31 scale value to a percent type. 0 -> 0%, 1 -> 4%, 2 -> 7%,
	 * 3 -> 10%, 4 -> 13%, 5 -> 16% 6 -> 19%, 7 -> 22%, 8 -> 25%, 9 -> 29%, 10
	 * -> 32%, 11 -> 35%, 12 -> 38%, 13 -> 41%, 14 -> 44%, 15 -> 47%, 16 -> 50%,
	 * 17 -> 53%, 18 -> 57%, 19 -> 60%, 20 -> 63%, 21 -> 66%, 22 -> 69%, 23 ->
	 * 72%, 24 -> 75%, 25 -> 79%, 26 -> 82%, 27 -> 85%, 28 -> 88%, 29 -> 91%, 30
	 * -> 94%, 31 -> 97%, 32 -> 100%,
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-31
	 */
	public static int convertLightwaveDimToOpenhabDim(int lightwavedim) {
		lightwavedim = Math.min(lightwavedim, 32);

		return BigDecimal.valueOf(lightwavedim)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(32), 0, BigDecimal.ROUND_UP)
				.intValue();
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
			return new PercentType(openhabDimLevel);
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
		if (that instanceof LightwaveRfDimCommand) {
			return Objects.equals(this.messageId,
					((LightwaveRfDimCommand) that).messageId)
					&& Objects.equals(this.roomId,
							((LightwaveRfDimCommand) that).roomId)
					&& Objects.equals(this.deviceId,
							((LightwaveRfDimCommand) that).deviceId)
					&& Objects.equals(this.openhabDimLevel,
							((LightwaveRfDimCommand) that).openhabDimLevel)
					&& Objects.equals(this.lightWaveDimLevel,
							((LightwaveRfDimCommand) that).lightWaveDimLevel);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Objects.hash(messageId, roomId, deviceId, openhabDimLevel,
				lightWaveDimLevel);
	}

	@Override
	public LightwaveRfMessageType getMessageType() {
		return LightwaveRfMessageType.ROOM_DEVICE;
	}

	public static boolean matches(String message) {
		return message.contains("FdP");
	}

}
