package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;

public class LightwaveRfDeviceRegistrationCommandTest {

	@Test
	public void test() {
		String message = "10,!F*p";
		LightwaveRFCommand command = new LightwaveRfDeviceRegistrationCommand(message);
		assertEquals("010,!F*p\n", command.getLightwaveRfCommandString());
	}

}
