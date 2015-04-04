/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump;

import static org.openhab.binding.ulux.internal.UluxBinding.LOG;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.openhab.binding.ulux.internal.UluxException;

/**
 * The message parser class allows the binding to convert a raw byte buffer into a list of messages.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class UluxMessageParser {

	private UluxMessageFactory messageFactory = new UluxMessageFactory();

	/**
	 * Parses the given buffer into a list of messages.
	 * <p>
	 * Flips the buffer if needed.
	 */
	@SuppressWarnings("unused")
	public List<UluxMessage> parse(final ByteBuffer buffer) {
		if (buffer.position() > 0) {
			buffer.flip();
		}

		if (buffer.limit() < 16) {
			throw new UluxException("Invalid datagram length: " + buffer.limit());
		}

		// 0: 0186, magic bytes, i.e. 0x8601
		final short magic = buffer.getShort();

		short s = (short) 0x8600;

		if (magic != (short) 0x8601) {
			throw new UluxException("Invalid magic bytes: " + magic);
		}

		// 2: 3200, length, e.g. 50 Bytes
		final short length = buffer.getShort();

		// 4: 0602, protocol version, e.g. "v2.06"
		final short version = buffer.getShort();

		// 6: 0000, package id, 0 = Ereignis
		final short packageId = buffer.getShort();

		// 8: 0100, project id
		final short projectId = buffer.getShort();

		// 10: 2301, firmware version, e.g. "v1.23"
		final short firmware = buffer.getShort();

		// 12: 0800, switch id, e.g. "8"
		final short switchId = buffer.getShort();

		// 14: 0100, design id, e.g. "1"
		final short designId = buffer.getShort();

		if (buffer.limit() != length) {
			throw new UluxException("Datagram length does not match!");
		}

		if (length == 16) {
			return Collections.emptyList(); // no further messages
		}

		final List<UluxMessage> messages = new LinkedList<UluxMessage>();
		while (buffer.position() < length) {
			final UluxMessage message = parseNextMessage(buffer);
			if (message != null) {
				messages.add(message);
			}
		}

		return messages;
	}

	/**
	 * @return a {@link UluxMessage} or <code>null</code> if the message id is not supported
	 */
	protected UluxMessage parseNextMessage(final ByteBuffer buffer) {
		final byte messageLength = buffer.get();

		if (messageLength < 4) {
			LOG.warn("Message too short: " + messageLength);
			return dropMessage(buffer, messageLength);
		}

		final byte messageIdRaw = buffer.get();
		final UluxMessageId messageId = UluxMessageId.valueOf(messageIdRaw);

		if (messageId == null) {
			LOG.warn(String.format("Unknown message id: %02x!", messageIdRaw));
			return dropMessage(buffer, messageLength);
		}

		final short actorId = buffer.getShort();
		final byte[] data = new byte[messageLength - 4];
		buffer.get(data);

		final ByteBuffer dataBuffer = ByteBuffer.wrap(data);
		dataBuffer.order(ByteOrder.LITTLE_ENDIAN);

		return this.messageFactory.createMessage(messageId, actorId, dataBuffer);
	}

	/**
	 * @return always <code>null</code>
	 */
	private UluxMessage dropMessage(final ByteBuffer buffer, byte messageLength) {
		for (byte i = 2; i < messageLength; i++) {
			buffer.get(); // read and drop remaining message
		}

		return null;
	}

}
