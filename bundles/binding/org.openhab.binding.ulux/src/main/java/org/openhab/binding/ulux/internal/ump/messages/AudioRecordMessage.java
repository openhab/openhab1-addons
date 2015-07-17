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
import java.net.InetAddress;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class AudioRecordMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x20;

	private final byte[] destination;

	private final boolean microphoneHighSensitivity = true; // TODO

	private final int microphoneSecurityId;

	public AudioRecordMessage(InetAddress destination, int microphoneSecurityId) {
		super(MESSAGE_LENGTH, UluxMessageId.AudioRecord);

		this.destination = destination.getAddress();
		this.microphoneSecurityId = microphoneSecurityId;
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		BigInteger flags = BigInteger.valueOf(0);

		if (microphoneHighSensitivity) {
			flags = flags.setBit(6);
		}

		// flags
		buffer.putShort(flags.shortValue());

		// sequence id
		buffer.putShort((short) 0x0000); // TODO

		// bytes per ethernet frame, always 882
		buffer.putShort((short) 0x0372);

		// reserved
		buffer.putShort((short) 0x0000);

		// delay between frames in microseconds, always 20,000 i.e. 20 ms
		buffer.putInt((int) 0x4E20);

		// microphone security id
		buffer.putInt(this.microphoneSecurityId);

		// audio destination IP
		buffer.put(destination[0]);
		buffer.put(destination[1]);
		buffer.put(destination[2]);
		buffer.put(destination[3]);

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
		builder.append("destination", this.destination);
		builder.append("microphoneSecurityId", this.microphoneSecurityId);
		builder.append("microphoneHighSensitivity", this.microphoneHighSensitivity);

		return builder.toString();
	}
}
