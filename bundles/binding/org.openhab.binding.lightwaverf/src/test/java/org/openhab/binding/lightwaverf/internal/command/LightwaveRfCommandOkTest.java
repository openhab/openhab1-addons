package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfCommandOkTest {

	@Test
	public void test() throws Exception {
		String message = "10,OK";
		LightwaveRfCommandOk command = new LightwaveRfCommandOk(message);
		assertEquals("010,OK\n", command.getLightwaveRfCommandString());
	}

}
