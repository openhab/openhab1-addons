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
 * @since 1.8.0
 */
public class AudioStopMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x06;

	/**
	 * Stop normal playback or recording.
	 */
	private boolean stopNormal = true; // TODO

	/**
	 * Stop a playing alarm.
	 */
	private boolean stopAlarm = true; // TODO

	public AudioStopMessage() {
		super(MESSAGE_LENGTH, UluxMessageId.AudioStop);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger flags = BigInteger.valueOf(0);

		if (stopNormal) {
			flags = flags.setBit(0);
		}
		if (stopAlarm) {
			flags = flags.setBit(1);
		}

		// flags
		buffer.put(flags.byteValue());

		// reserved
		buffer.put((byte) 0x00);
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("stopNormal", this.stopNormal);
		builder.append("stopAlarm", this.stopAlarm);

		return builder.toString();
	}
}
