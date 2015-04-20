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
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class VideoStopMessage extends AbstractUluxMessage {

	public VideoStopMessage() {
		super((byte) 0x0C, UluxMessageId.VideoStop);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// stop flags, not used at the moment, always 0x00000000
		buffer.putInt(0x00000000);

		// sequence id
		buffer.putInt(0x00000001); // TODO
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
