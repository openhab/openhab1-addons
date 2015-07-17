/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to query or set a user-defined text.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class TextMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x0C;

	private final int color = 0; // TODO

	private final byte textId = 0; // TODO

	private final byte flags = 0; // TODO

	private final String text;

	public TextMessage(final short actorId, final String text) {
		super((byte) (MESSAGE_LENGTH + text.length() + 1), UluxMessageId.Text, actorId);
		this.text = text;
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.putInt(this.color);
		buffer.put(this.textId);
		buffer.put(this.flags);
		buffer.putShort((short) 0x0000); // reserved
		buffer.put(this.text.getBytes(Charset.forName("US-ASCII")));
		buffer.put((byte) 0x00); // NUL
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("text", this.text);
		// TODO

		return builder.toString();
	}
}
