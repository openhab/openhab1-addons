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
import java.util.EnumMap;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * This message is received when a key that is configured to send an event is pressed or released.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class EventMessage extends AbstractUluxMessage {

	private final EnumMap<Key, Boolean> keyState = new EnumMap<Key, Boolean>(Key.class);

	public EventMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.Event, actorId, data);

		// key state
		final BigInteger keyState = BigInteger.valueOf(data.get());
		this.keyState.put(Key.KEY_1, keyState.testBit(0));
		this.keyState.put(Key.KEY_2, keyState.testBit(1));
		this.keyState.put(Key.KEY_3, keyState.testBit(2));
		this.keyState.put(Key.KEY_4, keyState.testBit(3));

		// reserved
		data.get();
	}

	public boolean isKeyPressed(Key key) {
		return this.keyState.get(key);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("keyState", this.keyState);

		return builder.toString();
	}

	public static enum Key {
		KEY_1, KEY_2, KEY_3, KEY_4;
	}
}
