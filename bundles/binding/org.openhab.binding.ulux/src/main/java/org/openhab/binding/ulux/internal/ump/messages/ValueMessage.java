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

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to query or set the "real" and "edit" values as well as LED status of an actor.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class ValueMessage extends AbstractUluxMessage {

	private final short editValue;
	private final short realValue;

	public ValueMessage(final short actorId, final short editValue, final Short realValue) {
		super((byte) 0x08, UluxMessageId.Value, actorId);
		this.editValue = editValue;
		this.realValue = realValue;
	}

	public ValueMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x08, UluxMessageId.Value, actorId, data);
		this.editValue = 0; // TODO editValue
		this.realValue = 0; // TODO realValue
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.putShort(this.editValue);
		buffer.putShort(this.realValue);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
