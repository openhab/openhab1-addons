package org.openhab.binding.lightwaverf.internal.message;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfHeatingMessageIdTest {

	@Test
	public void testGetMessageIdString() {
		LightwaveRfMessageId messageId001 = new LightwaveRfHeatingMessageId(1);
		assertEquals("1", messageId001.getMessageIdString());

		LightwaveRfMessageId messageId012 = new LightwaveRfHeatingMessageId(12);
		assertEquals("12", messageId012.getMessageIdString());

		LightwaveRfMessageId messageId = new LightwaveRfHeatingMessageId(123);
		assertEquals("123", messageId.getMessageIdString());
	}

	@Test
	public void testEqualsObject() {
		LightwaveRfMessageId messageId = new LightwaveRfHeatingMessageId(123);
		LightwaveRfMessageId otherMessageId = new LightwaveRfHeatingMessageId(123);
		assertTrue(otherMessageId.equals(messageId));
		assertTrue(messageId.hashCode() == otherMessageId.hashCode());
	}

}
