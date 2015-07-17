/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public abstract class AbstractUluxMessage implements UluxMessage {

	private final byte length;

	private final UluxMessageId messageId;

	private final short actorId;

	public AbstractUluxMessage(final byte length, final UluxMessageId messageId) {
		this(length, messageId, (short) 0);
	}

	public AbstractUluxMessage(final byte length, final UluxMessageId messageId, final short actorId) {
		this.length = length;
		this.messageId = messageId;
		this.actorId = actorId;
	}

	public AbstractUluxMessage(final byte length, final UluxMessageId messageId, final short actorId,
			final ByteBuffer data) {
		this(length, messageId, actorId);

		readData(data);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.append("actorId", this.actorId);

		return builder.toString();
	}

	protected final ToStringBuilder createToStringBuilder() {
		return new ToStringBuilder(this, SHORT_PREFIX_STYLE);
	}

	@Override
	public final ByteBuffer getBuffer() {
		final ByteBuffer buffer = ByteBuffer.wrap(new byte[this.length]);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.put(this.length);
		buffer.put(this.messageId.toByte());
		buffer.putShort(this.actorId);

		addData(buffer);

		buffer.flip();

		return buffer;
	}

	/**
	 * Add this message to the send buffer.
	 */
	protected void addData(ByteBuffer buffer) {
		throw new UnsupportedOperationException("This message can only be received.");
	}

	/**
	 * Read the incoming data into this message.
	 */
	protected void readData(ByteBuffer buffer) {
		// TODO
		// throw new UnsupportedOperationException(
		// "This message can only be sent.");
	}

	@Override
	public final UluxMessageId getMessageId() {
		return this.messageId;
	}

	public final short getActorId() {
		return this.actorId;
	}
}
