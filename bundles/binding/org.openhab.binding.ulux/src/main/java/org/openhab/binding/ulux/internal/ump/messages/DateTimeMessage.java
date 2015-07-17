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
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openhab.binding.ulux.internal.ump.UluxMessageId;

/**
 * Message to query or set the date and time of a switch.
 * 
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class DateTimeMessage extends AbstractUluxMessage {

	public DateTimeMessage() {
		super((byte) 0x0c, UluxMessageId.DateTime);
	}

	public DateTimeMessage(final short actorId, final ByteBuffer data) {
		super((byte) 0x0c, UluxMessageId.DateTime, actorId, data);
	}

	@Override
	protected void addData(final ByteBuffer buffer) {
		final Calendar calender = GregorianCalendar.getInstance();

		// second
		buffer.put((byte) calender.get(Calendar.SECOND));
		// minute
		buffer.put((byte) calender.get(Calendar.MINUTE));
		// hour
		buffer.put((byte) calender.get(Calendar.HOUR));
		// day of week
		buffer.put((byte) (calender.get(Calendar.DAY_OF_WEEK) - 1));
		// day
		buffer.put((byte) calender.get(Calendar.DAY_OF_MONTH));
		// month
		buffer.put((byte) calender.get(Calendar.MONTH));
		// year
		buffer.put((byte) (calender.get(Calendar.YEAR) - 2000));
		buffer.put((byte) (calender.get(Calendar.MINUTE) / 100));
	}

	@Override
	public String toString() {
		final ToStringBuilder builder = createToStringBuilder();
		builder.appendSuper(super.toString());

		return builder.toString();
	}
}
