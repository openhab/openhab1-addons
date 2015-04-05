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
 * Message to query the lux value of a switch.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class LuxMessage extends AbstractUluxMessage {

	private short lux;

	private boolean valid;

	public LuxMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x08, UluxMessageId.Lux, actorId, data);

		this.lux = data.getShort();
		this.valid = data.get() == (byte) 0x01;
		data.get(); // reserved
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// TODO
	}

	public short getLux() {
		return this.lux;
	}

	public boolean isValid() {
		return this.valid;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("lux", this.lux);
		builder.append("valid", this.valid);

		return builder.toString();
	}
}
