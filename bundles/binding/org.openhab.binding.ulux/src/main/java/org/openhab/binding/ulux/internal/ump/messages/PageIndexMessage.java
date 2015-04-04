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
 * Message to query or set the displayed page of a switch.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class PageIndexMessage extends AbstractUluxMessage {

	private final byte pageIndex;

	public PageIndexMessage(final byte pageIndex) {
		super((byte) 0x06, UluxMessageId.PageIndex);
		this.pageIndex = pageIndex;
	}

	public PageIndexMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.PageIndex, actorId, data);

		this.pageIndex = data.get();

		// reserved
		data.get();
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		buffer.put(this.pageIndex);
		buffer.put((byte) 0); // reserved, always 0
	}

	public byte getPageIndex() {
		return pageIndex;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("pageIndex", this.pageIndex);

		return builder.toString();
	}
}
