/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekozefir.ahucommand;

/**
 * Helps in building message for ahu.
 * 
 * @author Michal Marasz
 * @since 1.6.0
 */
public class MessageBuilder {

	private final byte messageType;
	private byte firstParam = 0x00;
	private byte secondParam = 0x00;
	private byte thirdParam = 0x00;

	private MessageBuilder(int messageType) {
		this.messageType = (byte) messageType;
	}

	private byte[] messageOfBytes(int byte0, int byte1, int byte2, int byte3) {
		byte[] bytes = new byte[5];
		bytes[0] = (byte) byte0;
		bytes[1] = (byte) byte1;
		bytes[2] = (byte) byte2;
		bytes[3] = (byte) byte3;
		bytes[4] = calculateCrc(bytes);
		return bytes;
	}

	private byte convertFloatToHighByte(float value) {
		int newValue = (int) (value * 10);
		return (byte) ((newValue >> 8) & 0xFF);
	}

	private byte convertFloatToLowByte(float value) {
		int newValue = (int) (value * 10);
		return (byte) (newValue & 0xFF);
	}

	private byte calculateCrc(byte[] bytes) {
		byte crc = (byte) 0xAA;
		for (int index = 0; index < 4; index++) {
			crc ^= bytes[index];
		}
		return crc;
	}

	/**
	 * Convert float to 2 bytes and append to message.
	 * 
	 * @param value to append
	 * @return message builder
	 */
	public MessageBuilder appendFirstAndSecondParameter(float value) {
		this.firstParam = convertFloatToHighByte(value);
		this.secondParam = convertFloatToLowByte(value);
		return this;
	}

	/**
	 * Append first parameter in message.
	 * 
	 * @param value parameter
	 * @return message builder
	 */
	public MessageBuilder appendFirstParameter(int value) {
		this.firstParam = (byte) value;
		return this;
	}

	/**
	 * Append second parameter in message.
	 * 
	 * @param value parameter
	 * @return message builder
	 */
	public MessageBuilder appendSecondParameter(int value) {
		this.secondParam = (byte) value;
		return this;
	}

	/**
	 * Build whole message and return as byte array.
	 * 
	 * @return message
	 */
	public byte[] build() {
		return messageOfBytes(messageType, firstParam, secondParam, thirdParam);
	}

	/**
	 * Type of message. Obligatory.
	 * 
	 * @param messageType type
	 * @return message builder
	 */
	public static MessageBuilder setType(int messageType) {
		return new MessageBuilder(messageType);
	}
}
