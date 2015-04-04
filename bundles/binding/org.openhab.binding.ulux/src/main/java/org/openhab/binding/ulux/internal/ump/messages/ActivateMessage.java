/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to activate or deactivate the display of a switch.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class ActivateMessage extends AbstractUluxMessage {

	private final boolean active;

	public ActivateMessage(final boolean active) {
		super((byte) 0x06, UluxMessageId.Activate, (short) 0x00);
		this.active = active;
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger flags = BigInteger.valueOf(0);

		if (isActive()) {
			flags = flags.setBit(0);
		} else {
			flags = flags.setBit(1);
		}

		// flags
		buffer.put(flags.byteValue());

		// reserved
		buffer.put((byte) 0x00);
	}

	public boolean isActive() {
		return this.active;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("active", this.active);

		return builder.toString();
	}
}
