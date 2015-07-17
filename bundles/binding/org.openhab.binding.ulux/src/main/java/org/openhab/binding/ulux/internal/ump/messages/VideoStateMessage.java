/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import java.awt.Point;
import java.math.BigInteger;
import java.nio.ByteBuffer;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class VideoStateMessage extends AbstractUluxMessage {

	private boolean videoActive;

	private boolean videoPossible;

	private Point upperLeft;

	private Point lowerRight;

	public VideoStateMessage() {
		super((byte) 0x04, UluxMessageId.VideoState);
	}

	public VideoStateMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.VideoState, actorId, data);

		// TODO state flags
		final BigInteger stateFlags = BigInteger.valueOf(data.getInt());
		this.videoPossible = stateFlags.testBit(1);
		this.videoActive = stateFlags.testBit(0);

		// X-coordinate of upper-left corner
		// data.getShort();

		// Y-coordinate of upper-left corner
		// data.getShort();

		this.upperLeft = new Point(data.getShort(), data.getShort());

		// X-coordinate of lower-right corner
		// data.getShort();

		// Y-coordinate of lower-right corner
		// data.getShort();

		this.lowerRight = new Point(data.getShort(), data.getShort());
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// nothing to add to an outgoing message
	}

	public boolean isVideoActive() {
		return videoActive;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("videoActive", this.videoActive);
		builder.append("videoPossible", this.videoPossible);
		builder.append("upperLeft", this.upperLeft);
		builder.append("lowerRight", this.lowerRight);

		return builder.toString();
	}
}
