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
 * Message to set some state flags.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class InitMessage extends AbstractUluxMessage {

	private boolean reset;
	private boolean initRequest;
	private boolean timeRequest;

	public InitMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x08, UluxMessageId.Init, actorId, data);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger initFlags = BigInteger.valueOf(0);

		if (this.reset) {
			initFlags = initFlags.setBit(15);
			initFlags = initFlags.setBit(14);
			initFlags = initFlags.setBit(13);
			initFlags = initFlags.setBit(12);
		}
		if (this.initRequest) {
			initFlags = initFlags.setBit(6);
		}
		if (this.timeRequest) {
			initFlags = initFlags.setBit(5);
		}

		buffer.putShort(initFlags.shortValue());
		buffer.putShort((short) 0x5AA5);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("reset", this.reset);
		builder.append("initRequest", this.initRequest);
		builder.append("timeRequest", this.timeRequest);

		return builder.toString();
	}
}
