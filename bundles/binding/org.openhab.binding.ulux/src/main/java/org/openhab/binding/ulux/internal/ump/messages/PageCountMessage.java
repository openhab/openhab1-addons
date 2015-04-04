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
 * Message to query the number of pages.
 * 
 * @author Andreas Brenk
 * @since 1.7.0
 */
public class PageCountMessage extends AbstractUluxMessage {

	private static final byte MESSAGE_LENGTH = (byte) 0x04;

	private static final UluxMessageId MESSAGE_ID = UluxMessageId.PageCount;

	private static final short ACTOR_ID = (short) 0;

	private byte pageCount;

	public PageCountMessage(final ByteBuffer data) {
		super(MESSAGE_LENGTH, MESSAGE_ID, ACTOR_ID, data);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		// empty data
	}

	@Override
	protected void readData(ByteBuffer data) {
		this.pageCount = data.get();
	}

	public byte getPageCount() {
		return pageCount;
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());
		builder.append("pageCount", this.pageCount);

		return builder.toString();
	}
}
