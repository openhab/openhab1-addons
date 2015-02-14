package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class LightwaveRfOnOffCommandTest {

	@Test
	public void testOnOffCommandAsMessageOn() throws Exception {
		String message = "10,!R2D3F1";
		LightwaveRFCommand command = new LightwaveRfOnOffCommand(message);
		assertEquals("010,!R2D3F1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.ON, command.getState(LightwaveRfType.SWITCH));
		assertEquals(PercentType.HUNDRED, command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testOnOffCommandAsMessageOff() throws Exception {
		String message = "10,!R2D3F0";
		LightwaveRFCommand command = new LightwaveRfOnOffCommand(message);
		assertEquals("010,!R2D3F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.SWITCH));
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testOnOffCommandAsParametersOn() throws Exception {
		LightwaveRFCommand command = new LightwaveRfOnOffCommand(10, "2", "3", true);
		assertEquals("010,!R2D3F1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.ON, command.getState(LightwaveRfType.SWITCH));	
		assertEquals(PercentType.HUNDRED, command.getState(LightwaveRfType.DIMMER));

	}
	
	@Test
	public void testOnOffCommandAsParametersOff() throws Exception {
		LightwaveRFCommand command = new LightwaveRfOnOffCommand(10, "2", "3", false);
		assertEquals("010,!R2D3F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.SWITCH));
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.DIMMER));
	}

}
