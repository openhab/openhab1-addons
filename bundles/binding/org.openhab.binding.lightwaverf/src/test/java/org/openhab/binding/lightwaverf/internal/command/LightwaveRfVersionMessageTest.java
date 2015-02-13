package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfVersionMessageTest {

	@Test
	public void testValidMessage() throws Exception {
		String message = "1,?V=\"U2.91Q\"";
		
		LightwaveRfVersionMessage versionMessage = new LightwaveRfVersionMessage(message);
		assertEquals("001,?V=\"U2.91Q\"\n", versionMessage.getLightwaveRfCommandString());
	}

}
