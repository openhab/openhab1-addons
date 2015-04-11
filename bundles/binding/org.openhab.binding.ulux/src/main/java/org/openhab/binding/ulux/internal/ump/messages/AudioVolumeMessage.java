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
 * @since 1.7.0
 */
public class AudioVolumeMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x06;

	private byte volume;

	public AudioVolumeMessage(final byte volume) {
		super(MESSAGE_LENGTH, UluxMessageId.AudioVolume);

		this.volume = volume;
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.put(this.volume);
		buffer.put((byte) 0x00); // reserved
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
