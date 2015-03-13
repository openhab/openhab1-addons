package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.core.library.types.DecimalType;

public class LightwaveRfSetHeatingTemperatureCommandTest {

	@Test
	public void testHeatingSetTemperatureCommandAsMessage() throws Exception {
		String message = "010,!R1DhF*tP19.0";
		LightwaveRfRoomMessage command = new LightwaveRfSetHeatingTemperatureCommand(message);
		assertEquals("010,!R1DhF*tP19.0\n", command.getLightwaveRfCommandString());
		assertEquals("1", command.getRoomId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(new DecimalType(19.0), command.getState(LightwaveRfType.HEATING_SET_TEMP));
	}

	@Test
	public void testHeatingSetTemperatureCommandAsParameters() throws Exception {
		LightwaveRfSetHeatingTemperatureCommand command = new LightwaveRfSetHeatingTemperatureCommand(10, "1", 19.0);
		assertEquals("010,!R1DhF*tP19.0\n", command.getLightwaveRfCommandString());
		assertEquals("1", command.getRoomId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(new DecimalType(19.0), command.getState(LightwaveRfType.HEATING_SET_TEMP));
	}

	@Test
	public void testHeatingSetTemperatureCommandRealWorldMessage() throws Exception {

		String message = "200,!R1DhF*tP21.5\n";
		LightwaveRfRoomMessage command = new LightwaveRfSetHeatingTemperatureCommand(message);
		assertEquals("200,!R1DhF*tP21.5\n", command.getLightwaveRfCommandString());
		assertEquals("1", command.getRoomId());
		assertEquals("200", command.getMessageId().getMessageIdString());
		assertEquals(new DecimalType(21.5), command.getState(LightwaveRfType.HEATING_SET_TEMP));
		
	}
}
