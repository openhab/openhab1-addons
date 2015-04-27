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
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class AudioPlayRemoteMessage extends AbstractUluxMessage {

	public AudioPlayRemoteMessage() {
		super((byte) 0x20, UluxMessageId.AudioPlayRemote);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger playFlags = BigInteger.ZERO;
		playFlags = playFlags.setBit(3); // no audio page
		playFlags = playFlags.setBit(1); // ignore AMS Net ID

		buffer.put((byte) 100); // TODO volume
		buffer.put((byte) 0); // TODO equalizer
		buffer.putShort(playFlags.shortValue()); // TODO play flags
		buffer.putShort((short) 0); // TODO inc volume time
		buffer.putShort((short) 0x0000); // TODO sequence id

		// bytes per ethernet frame, always 882
		buffer.putShort((short) 0x0372);

		// reserved
		buffer.putShort((short) 0x0000);

		// delay between frames in microseconds, always 20,000 i.e. 20 ms
		buffer.putInt((int) 0x4E20);

		// audio source IP
		buffer.put((byte) 0xFF);
		buffer.put((byte) 0xFF);
		buffer.put((byte) 0xFF);
		buffer.put((byte) 0xFF);

		// TODO AMSNetId
		buffer.putShort((byte) 0x0000);
		buffer.putShort((byte) 0x0000);
		buffer.putShort((byte) 0x0000);

		// reserved
		buffer.putShort((short) 0x0000);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
