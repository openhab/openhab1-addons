package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openhab.core.library.types.PercentType;

public class LightwaveRfDimCommandTest {

	@Test
	public void testDimCommandAsMessageLow() {
		String message = "10,!R2D3FdP1";
		LightwaveRFCommand command = new LightwaveRfDimCommand(message);
		assertEquals("010,!R2D3FdP1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("1"), command.getState());
	}

	@Test
	public void testDimCommandAsMessageHigh() {
		String message = "10,!R2D3FdP32";
		LightwaveRFCommand command = new LightwaveRfDimCommand(message);
		assertEquals("010,!R2D3FdP1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("100"), command.getState());
	}

	@Test
	public void testDimCommandAsParametersLow() {
		LightwaveRFCommand command = new LightwaveRfDimCommand(10, "2", "3", 1);
		assertEquals("010,!R2D3FdP1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("1"), command.getState());
	}
	
	@Test
	public void testDimCommandAsParametersHigh() {
		LightwaveRFCommand command = new LightwaveRfDimCommand(10, "2", "3", 100);
		assertEquals("010,!R2D3FdP32\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("100"), command.getState());
	}

		
	
}
