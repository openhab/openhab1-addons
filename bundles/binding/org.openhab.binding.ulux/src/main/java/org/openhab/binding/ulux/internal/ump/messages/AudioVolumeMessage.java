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
 * Message to query or set the audio volume of a switch.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class AudioVolumeMessage extends AbstractUluxMessage {

	private static final byte INVALID_VOLUME = -1;

	private final byte volume;

	public AudioVolumeMessage() {
		super((byte) 0x04, UluxMessageId.AudioVolume);

		this.volume = INVALID_VOLUME;
	}

	public AudioVolumeMessage(final byte volume) {
		super((byte) 0x06, UluxMessageId.AudioVolume);

		this.volume = volume;
	}

	public AudioVolumeMessage(short actorId, ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.AudioVolume, actorId, data);

		this.volume = data.get();
		data.get(); // reserved
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		if (this.volume != INVALID_VOLUME) {
			buffer.put(this.volume);
			buffer.put((byte) 0x00); // reserved
		}
	}

	public byte getVolume() {
		return this.volume;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("volume", this.volume);

		return builder.toString();
	}
}
