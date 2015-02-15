package org.openhab.binding.lightwaverf.internal.message;

import static org.junit.Assert.*;

import org.junit.Test;

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
