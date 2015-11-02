package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfJsonMessageId;
import org.openhab.core.library.types.DecimalType;

public class LightwaveRfEnergyMonitorMessageTest {

	private String messageString = "*!{\"trans\":215955,\"mac\":\"03:41:C4\",\"time\":1435620183,\"prod\":\"pwrMtr\",\"serial\":\"9470FE\","
			+ "\"signal\":79,\"type\":\"energy\",\"cUse\":271,\"maxUse\":2812,"
			+ "\"todUse\":8414,\"yesUse\":8377}";

	
	@Test
	public void testDecodingMessage() throws Exception {
		LightwaveRfEnergyMonitorMessage message = new LightwaveRfEnergyMonitorMessage(messageString);
		assertEquals(new LightwaveRfJsonMessageId(215955), message.getMessageId());
		assertEquals("03:41:C4", message.getMac());
		assertEquals("pwrMtr", message.getProd());
		assertEquals("9470FE", message.getSerial());
		assertEquals(79, message.getSignal());
		assertEquals("energy", message.getType());
		assertEquals(271, message.getcUse());
		assertEquals(2812, message.getMaxUse());
		assertEquals(8414, message.getTodUse());
		assertEquals(8377, message.getYesUse());
	}
	
	@Test
	public void testMatches() {
		boolean matches = LightwaveRfEnergyMonitorMessage.matches(messageString);
		assertTrue(matches);
	}
	
	@Test
	public void testGetState() throws Exception {
		LightwaveRfEnergyMonitorMessage message = new LightwaveRfEnergyMonitorMessage(messageString);
		assertEquals(new DecimalType(271), message.getState(LightwaveRfType.ENERGY_CURRENT_USAGE));
		assertEquals(new DecimalType(2812), message.getState(LightwaveRfType.ENERGY_MAX_USAGE));
		assertEquals(new DecimalType(8414), message.getState(LightwaveRfType.ENERGY_TODAY_USAGE));
		assertEquals(new DecimalType(8377), message.getState(LightwaveRfType.ENERGY_YESTERDAY_USAGE));
		assertEquals(new DecimalType(79), message.getState(LightwaveRfType.SIGNAL));
	}
}
