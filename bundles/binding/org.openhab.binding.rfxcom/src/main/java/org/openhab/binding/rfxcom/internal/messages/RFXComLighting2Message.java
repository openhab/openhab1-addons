/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.messages;

import java.math.BigDecimal;

import org.openhab.core.library.types.PercentType;

/**
 * RFXCOM data class for lighting2 message.
 * 
 * @author Pauli Anttila
 * @since 1.2.0
 */
public class RFXComLighting2Message extends RFXComBaseMessage {

	public enum Commands {
		OFF(0),
		ON(1),
		SET_LEVEL(2),
		GROUP_OFF(3),
		GROUP_ON(4),
		SET_GROUP_LEVEL(5);

		private final int command;

		Commands(int command) {
			this.command = command;
		}

		Commands(byte command) {
			this.command = command;
		}

		public byte toByte() {
			return (byte) command;
		}
	}


	public enum SubType {
		AC(0),
		HOME_EASY_EU(1),
		ANSLUT(2);

		private final int subType;

		SubType(int subType) {
			this.subType = subType;
		}

		SubType(byte subType) {
			this.subType = subType;
		}

		public byte toByte() {
			return (byte) subType;
		}
	}

	public SubType subType = SubType.AC;
	public int sensorId = 0;
	public byte unitcode = 0;
	public Commands command = Commands.OFF;
	public byte dimmingLevel = 0;
	public byte signalLevel = 0;

	public RFXComLighting2Message() {
		packetType = PacketType.LIGHTING2;

	}

	public RFXComLighting2Message(byte[] data) {

		encodeMessage(data);
	}

	@Override
	public String toString() {
		String str = "";

		str += super.toString();
		str += "\n - Sub type = " + subType;
		str += "\n - Id = " + sensorId;
		str += "\n - Unit code = " + unitcode;
		str += "\n - Command = " + command;
		str += "\n - Dim level = " + dimmingLevel;
		str += "\n - Signal level = " + signalLevel;

		return str;
	}

	@Override
	public void encodeMessage(byte[] data) {

		super.encodeMessage(data);

		subType = SubType.values()[super.subType];
		sensorId = (data[4] & 0xFF) << 24 | (data[5] & 0xFF) << 16
				| (data[6] & 0xFF) << 8 | (data[7] & 0xFF);
		unitcode = data[8];
		command = Commands.values()[data[9]];
		dimmingLevel = data[10];
		signalLevel = (byte) ((data[11] & 0xF0) >> 4);
	}

	@Override
	public byte[] decodeMessage() {

		byte[] data = new byte[12];

		data[0] = 0x0B;
		data[1] = RFXComBaseMessage.PacketType.LIGHTING2.toByte();
		data[2] = subType.toByte();
		data[3] = seqNbr;
		data[4] = (byte) ((sensorId >> 24) & 0xFF);
		data[5] = (byte) ((sensorId >> 16) & 0xFF);
		data[6] = (byte) ((sensorId >> 8) & 0xFF);
		data[7] = (byte) (sensorId & 0xFF);

		data[8] = unitcode;
		data[9] = command.toByte();
		data[10] = dimmingLevel;
		data[11] = (byte) ((signalLevel & 0x0F) << 4);

		return data;
	}
	
	@Override
	public String generateDeviceId() {
		 return sensorId + "." + unitcode;
	}

	

	/**
	 * Convert a 0-15 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-15
	 */
	public static int getDimLevelFromPercentType(PercentType pt) {
		return pt
				.toBigDecimal()
				.multiply(BigDecimal.valueOf(15))
				.divide(PercentType.HUNDRED.toBigDecimal(), 0,
						BigDecimal.ROUND_UP).intValue();
	}

	/**
	 * Convert a 0-15 scale value to a percent type.
	 * 
	 * @param pt
	 *            percent type to convert
	 * @return converted value 0-15
	 */
	public static PercentType getPercentTypeFromDimLevel(int value) {
		value = Math.min(value, 15);
		
		return new PercentType(BigDecimal
				.valueOf(value)
				.multiply(BigDecimal.valueOf(100))
				.divide(BigDecimal.valueOf(15), 0,
						BigDecimal.ROUND_UP).intValue());
	}
	

}
