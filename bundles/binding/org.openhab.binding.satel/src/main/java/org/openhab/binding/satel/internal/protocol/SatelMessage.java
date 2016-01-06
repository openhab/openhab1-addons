/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.satel.internal.protocol;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a message sent to (a command) or received from (a response)
 * {@link SatelModule}. It consists of one byte that specifies command type or
 * response status and certain number of payload bytes. Number of payload byte
 * depends on command type. The class allows to serialize a command to bytes and
 * deserialize a response from given bytes. It also computes and validates
 * message checksum.
 * 
 * @author Krzysztof Goworek
 * @since 1.7.0
 */
public class SatelMessage {
	private static final Logger logger = LoggerFactory.getLogger(SatelMessage.class);

	private byte command;
	private byte[] payload;

	private static final byte[] EMPTY_PAYLOAD = new byte[0];

	/**
	 * Creates new instance with specified command code and payload.
	 * 
	 * @param command
	 *            command code
	 * @param payload
	 *            command payload
	 */
	public SatelMessage(byte command, byte[] payload) {
		this.command = command;
		this.payload = payload;
	}

	/**
	 * Creates new instance with specified command code and empty payload.
	 * 
	 * @param command
	 *            command code
	 */
	public SatelMessage(byte command) {
		this(command, EMPTY_PAYLOAD);
	}

	/**
	 * Deserializes new message instance from specified byte buffer.
	 * 
	 * @param buffer
	 *            bytes to deserialize a message from
	 * @return deserialized message instance
	 */
	public static SatelMessage fromBytes(byte[] buffer) {
		// we need at least command and checksum
		if (buffer.length < 3) {
			logger.error("Invalid message length: {}", buffer.length);
			return null;
		}

		// check crc
		int receivedCrc = 0xffff & ((buffer[buffer.length - 2] << 8) | (buffer[buffer.length - 1] & 0xff));
		int expectedCrc = calculateChecksum(buffer, buffer.length - 2);
		if (receivedCrc != expectedCrc) {
			logger.error("Invalid message checksum: received = {}, expected = {}", receivedCrc, expectedCrc);
			return null;
		}

		SatelMessage message = new SatelMessage(buffer[0], new byte[buffer.length - 3]);
		if (message.payload.length > 0)
			System.arraycopy(buffer, 1, message.payload, 0, buffer.length - 3);
		return message;
	}

	/**
	 * Returns command byte.
	 * 
	 * @return the command
	 */
	public byte getCommand() {
		return this.command;
	}

	/**
	 * Returns the payload bytes.
	 * 
	 * @return payload as byte array
	 */
	public byte[] getPayload() {
		return this.payload;
	}

	/**
	 * Returns the message serialized as array of bytes with checksum calculated
	 * at last two bytes.
	 * 
	 * @return the message as array of bytes
	 */
	public byte[] getBytes() {
		byte buffer[] = new byte[this.payload.length + 3];
		buffer[0] = this.command;
		if (this.payload.length > 0)
			System.arraycopy(this.payload, 0, buffer, 1, this.payload.length);
		int checksum = calculateChecksum(buffer, buffer.length - 2);
		buffer[buffer.length - 2] = (byte) ((checksum >> 8) & 0xff);
		buffer[buffer.length - 1] = (byte) (checksum & 0xff);
		return buffer;
	}

	private String getPayloadAsHex() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < this.payload.length; ++i) {
			if (i > 0)
				result.append(" ");
			result.append(String.format("%02X", this.payload[i]));
		}
		return result.toString();
	}

	/**
	 * Calculates a checksum for the specified buffer.
	 * 
	 * @param buffer
	 *            the buffer to calculate.
	 * @return the checksum value.
	 */
	private static int calculateChecksum(byte[] buffer, int length) {
		int checkSum = 0x147a;
		for (int i = 0; i < length; i++) {
			checkSum = ((checkSum << 1) | ((checkSum >> 15) & 1));
			checkSum ^= 0xffff;
			checkSum += ((checkSum >> 8) & 0xff) + (buffer[i] & 0xff);
		}
		checkSum &= 0xffff;
		logger.trace("Calculated checksum = {}", String.format("%04X", checkSum));
		return checkSum;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("Message: command = %02X, payload = %s", this.command, getPayloadAsHex());
	};

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (!obj.getClass().equals(this.getClass()))
			return false;

		SatelMessage other = (SatelMessage) obj;

		if (other.command != this.command)
			return false;

		if (!Arrays.equals(other.payload, this.payload))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + command;
		result = prime * result + Arrays.hashCode(payload);
		return result;
	}
}
