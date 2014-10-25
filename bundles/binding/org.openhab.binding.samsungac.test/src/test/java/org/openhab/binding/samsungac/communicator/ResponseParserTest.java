package org.openhab.binding.samsungac.communicator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.binding.openhab.samsungac.communicator.ResponseParser;
import org.junit.Test;
import org.openhab.binding.samsungac.internal.CommandEnum;
import org.xml.sax.SAXException;

public class ResponseParserTest {

	@Test
	public void shouldHandleInvalidAccount() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Update Type=\"InvalidateAccount\"/>";
		assertTrue(ResponseParser.isNotLoggedInResponse(response));
	}

	@Test
	public void shouldFindFirstLine() {
		String response = "DRC-1.00";
		assertTrue(ResponseParser.isFirstLine(response));
	}

	@Test
	public void shoudleReturnTrueIfReadyForTokenResponse() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"GetToken\" Status=\"Ready\"/>";
		assertTrue(ResponseParser.isReadyForTokenResponse(response));
	}

	@Test
	public void shoudleReturnTrueIfDeviceControl() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"DeviceControl\"/>";
		assertTrue(ResponseParser.isDeviceControl(response));
	}

	@Test
	public void shoudleReturnTrueIfResponseIsDeviceState() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"DeviceState\" Status=\"Okay\"/>";
		assertTrue(ResponseParser.isDeviceState(response));
	}

	@Test
	public void shouldReturnFalseIfNotStatusResponse() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"DeviceState\" Status=\"Okay\" DUID=\"7825AD1243BA\" CommandID=\"cmd3227\"/>";
		assertFalse(ResponseParser.isDeviceState(response));
	}

	@Test
	public void shouldCheckIfItIsCorrectResponseCommand() {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"DeviceControl\" Status=\"Okay\" DUID=\"7825AD1243BA\" CommandID=\"cmd4946\"/>";
		assertTrue(ResponseParser.isCorrectCommandResponse(response, "cmd4946"));
	}
	
	@Test
	public void shouldParseXMLStatusResponseCorrectly() throws SAXException {
		String response = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><Response Type=\"DeviceState\" Status=\"Okay\"><DeviceState><Device DUID=\"7825AD1243BA\" GroupID=\"AC\" ModelID=\"AC\" ><Attr ID=\"AC_FUN_ENABLE\" Type=\"RW\" Value=\"Enable\"/><Attr ID=\"AC_FUN_POWER\" Type=\"RW\" Value=\"On\"/><Attr ID=\"AC_FUN_SUPPORTED\" Type=\"R\" Value=\"0\"/><Attr ID=\"AC_FUN_OPMODE\" Type=\"RW\" Value=\"Heat\"/><Attr ID=\"AC_FUN_TEMPSET\" Type=\"RW\" Value=\"20\"/><Attr ID=\"AC_FUN_COMODE\" Type=\"RW\" Value=\"Off\"/><Attr ID=\"AC_FUN_ERROR\" Type=\"RW\" Value=\"00000000\"/><Attr ID=\"AC_FUN_TEMPNOW\" Type=\"R\" Value=\"21\"/><Attr ID=\"AC_FUN_SLEEP\" Type=\"RW\" Value=\"0\"/><Attr ID=\"AC_FUN_WINDLEVEL\" Type=\"RW\" Value=\"Auto\"/><Attr ID=\"AC_FUN_DIRECTION\" Type=\"RW\" Value=\"Fixed\"/><Attr ID=\"AC_ADD_AUTOCLEAN\" Type=\"RW\" Value=\"Off\"/><Attr ID=\"AC_ADD_APMODE_END\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_ADD_STARTWPS\" Type=\"RW\" Value=\"Direct\"/><Attr ID=\"AC_ADD_SPI\" Type=\"RW\" Value=\"Off\"/><Attr ID=\"AC_SG_WIFI\" Type=\"W\" Value=\"Connected\"/><Attr ID=\"AC_SG_INTERNET\" Type=\"W\" Value=\"Connected\"/><Attr ID=\"AC_ADD2_VERSION\" Type=\"RW\" Value=\"0\"/><Attr ID=\"AC_SG_MACHIGH\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_SG_MACMID\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_SG_MACLOW\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_SG_VENDER01\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_SG_VENDER02\" Type=\"W\" Value=\"0\"/><Attr ID=\"AC_SG_VENDER03\" Type=\"W\" Value=\"0\"/></Device></DeviceState></Response>";
		Map<CommandEnum,String> result = ResponseParser.parseStatusResponse(response);
		
		assertEquals("On", result.get(CommandEnum.AC_FUN_POWER));
		assertEquals("21", result.get(CommandEnum.AC_FUN_TEMPNOW));
		assertEquals("20", result.get(CommandEnum.AC_FUN_TEMPSET));
	}

}
