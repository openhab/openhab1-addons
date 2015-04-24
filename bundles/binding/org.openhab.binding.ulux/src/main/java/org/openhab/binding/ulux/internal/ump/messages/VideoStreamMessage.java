/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ulux.internal.ump.messages;

import static org.apache.commons.lang.builder.ToStringStyle.SHORT_PREFIX_STYLE;
import static org.openhab.binding.ulux.internal.ump.UluxMessageId.VideoStream;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessage;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class VideoStreamMessage implements UluxMessage {

	private final boolean acknowledge = false; // TODO

	private final int sequenceId = 0x00000001; // TODO

	private final short startLine;

	private final short lineCount;

	private final short[] videoData;

	public VideoStreamMessage(short startLine, short lineCount, int[] videoData) {
		this(startLine, lineCount, toRGB565(videoData));
	}

	/**
	 * @param startLine
	 *            the line number of the first line in this frame
	 * @param lineCount
	 *            number of video lines in this frame
	 * @param videoData
	 *            video data as RGB565 pixels
	 */
	public VideoStreamMessage(short startLine, short lineCount, short[] videoData) {
		if (videoData.length > 704) {
			throw new IllegalArgumentException("Too much data: " + videoData.length);
		}

		this.startLine = startLine;
		this.lineCount = lineCount;
		this.videoData = videoData;
	}

	@Override
	public UluxMessageId getMessageId() {
		return VideoStream;
	}

	@Override
	public ByteBuffer getBuffer() {
		final BigInteger streamFlags = BigInteger.valueOf(0);
		if (this.acknowledge) {
			streamFlags.setBit(0);
		}

		final int length = 12 + this.videoData.length * 2;
		final ByteBuffer buffer = ByteBuffer.allocate(length);
		buffer.order(ByteOrder.LITTLE_ENDIAN);

		buffer.putInt(streamFlags.intValue());
		buffer.putInt(this.sequenceId);
		buffer.putShort(this.startLine);
		buffer.putShort(this.lineCount);

		for (short pixel : this.videoData) {
			buffer.putShort(pixel);
		}

		buffer.flip();

		return buffer;
	}

	private static short[] toRGB565(int[] argb) {
		final short[] converted = new short[argb.length];

		for (int i = 0; i < argb.length; i++) {
			converted[i] = toRGB565(argb[i]);
		}

		return converted;
	}

	/**
	 * Converts an ARGB8 pixel to RGB565.
	 */
	private static short toRGB565(int argb) {
		int a = (argb & 0xFF000000) >> 24;
		int r = (argb & 0x00FF0000) >> 16;
		int g = (argb & 0x0000FF00) >> 8;
		int b = (argb & 0x000000FF);

		// Convert full transparency to white.
		if (a == 0) {
			r = 0xFF;
			g = 0xFF;
			b = 0xFF;
		}

		r = r >> 3;
		g = g >> 2;
		b = b >> 3;

		return (short) (b | (g << 5) | (r << (5 + 6)));
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = new ToStringBuilder(this, SHORT_PREFIX_STYLE);
		builder.append("sequenceId", this.sequenceId);
		builder.append("startLine", this.startLine);
		builder.append("lineCount", this.lineCount);
		builder.append("videoData", this.videoData.length);

		return builder.toString();
	}
}
