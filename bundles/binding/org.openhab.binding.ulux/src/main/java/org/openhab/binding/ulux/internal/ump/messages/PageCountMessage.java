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
 * @since 1.8.0
 */
public class PageCountMessage extends AbstractUluxMessage {

	private byte pageCount;

	public PageCountMessage() {
		super((byte) 0x04, UluxMessageId.PageCount);
	}

	public PageCountMessage(final ByteBuffer data) {
		super((byte) 0x06, UluxMessageId.PageCount, (short) 0, data);
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
