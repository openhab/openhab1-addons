package org.openhab.binding.lightwaverf.internal;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDeviceRegistrationCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfDimCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfOnOffCommand;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class LightwaverfConvertorTest {

	@Test
	public void testConvertToLightwaveRfMessageOnCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertToLightwaveRfMessage("2", "3", OnOffType.ON);
		LightwaveRFCommand expected = new LightwaveRfOnOffCommand("0,!R2D3F1");
		assertEquals(expected, command);
	}

	@Test
	public void testConvertFromLightwaveRfMessageOnCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertFromLightwaveRfMessage("010,!R2D3F1");
		LightwaveRFCommand expected = new LightwaveRfOnOffCommand(10, "2", "3", true);
		assertEquals(expected, command);
	}

	@Test
	public void testConvertToLightwaveRfMessageOffCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertToLightwaveRfMessage("2", "3", OnOffType.OFF);
		LightwaveRFCommand expected = new LightwaveRfOnOffCommand("0,!R2D3F0");
		assertEquals(expected, command);
	}

	@Test
	public void testConvertFromLightwaveRfMessageOffCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertFromLightwaveRfMessage("010,!R2D3F0");
		LightwaveRFCommand expected = new LightwaveRfOnOffCommand(10, "2", "3", false);
		assertEquals(expected, command);
	}

	@Test
	public void testConvertToLightwaveRfMessageDimCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertToLightwaveRfMessage("2", "3", new PercentType(75));
		LightwaveRFCommand expected = new LightwaveRfDimCommand("0,!R2D3FdP24");
		assertEquals(expected, command);
	}

	@Test
	public void testConvertFromLightwaveRfMessageDimCommand() throws Exception {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertFromLightwaveRfMessage("010,!R2D3FdP24");
		LightwaveRFCommand expected = new LightwaveRfDimCommand(10, "2", "3", 75);
		assertEquals(expected, command);
	}

	
	@Test
	@Ignore(value="Method not implemented yet")
	public void testConvertToLightwaveRfMessageStringType() {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.convertToLightwaveRfMessage("2", OnOffType.ON);
		LightwaveRFCommand expected = null;
		assertEquals(expected, command);
	}


	@Test
	public void testGetRegistrationCommand() {
		LightwaverfConvertor convertor = new LightwaverfConvertor();
		LightwaveRFCommand command = convertor.getRegistrationCommand();
		assertEquals(new LightwaveRfDeviceRegistrationCommand(0), command);
	}

}
