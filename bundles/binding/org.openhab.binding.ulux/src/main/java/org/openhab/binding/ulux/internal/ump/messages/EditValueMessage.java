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
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class EditValueMessage extends AbstractUluxMessage {

	public static final short ON = 1;

	public static final short OFF = 0;

	private final short value;

	public EditValueMessage(final short actorId, final short value) {
		super((byte) 0x06, UluxMessageId.EditValue, actorId);
		this.value = value;
	}

	public EditValueMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.EditValue, actorId, data);

		this.value = data.getShort();
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.putShort(this.value);
	}

	public short getValue() {
		return value;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("value", this.value);

		return builder.toString();
	}
}
