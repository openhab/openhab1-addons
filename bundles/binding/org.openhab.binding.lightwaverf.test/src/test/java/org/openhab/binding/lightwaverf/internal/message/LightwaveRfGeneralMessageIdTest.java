/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfGeneralMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;

public class LightwaveRfGeneralMessageIdTest {

	@Test
	public void testGetMessageIdString() {
		LightwaveRfMessageId messageId001 = new LightwaveRfGeneralMessageId(1);
		assertEquals("001", messageId001.getMessageIdString());

		LightwaveRfMessageId messageId012 = new LightwaveRfGeneralMessageId(12);
		assertEquals("012", messageId012.getMessageIdString());

		LightwaveRfMessageId messageId = new LightwaveRfGeneralMessageId(123);
		assertEquals("123", messageId.getMessageIdString());
	}

	@Test
	public void testEqualsObject() {
		LightwaveRfMessageId messageId = new LightwaveRfGeneralMessageId(123);
		LightwaveRfMessageId otherMessageId = new LightwaveRfGeneralMessageId(123);
		assertTrue(otherMessageId.equals(messageId));
		assertTrue(messageId.hashCode() == otherMessageId.hashCode());
	}

}
