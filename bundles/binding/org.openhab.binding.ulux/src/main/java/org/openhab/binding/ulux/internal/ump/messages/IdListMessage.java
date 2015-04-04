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
 * Message to query the list of actors.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class IdListMessage extends AbstractUluxMessage {

	private final short actorCount;

	private final short[] actors;

	public IdListMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x04, UluxMessageId.IdList, actorId, data);

		this.actorCount = data.getShort();

		this.actors = new short[this.actorCount];
		for (short i = 0; i < actorCount; i++) {
			this.actors[i] = data.getShort();
		}
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// empty data
	}

	public short getActorCount() {
		return actorCount;
	}

	public short[] getActors() {
		return actors;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("actorCount", this.actorCount);
		builder.append("actors", this.actors);

		return builder.toString();
	}
}
