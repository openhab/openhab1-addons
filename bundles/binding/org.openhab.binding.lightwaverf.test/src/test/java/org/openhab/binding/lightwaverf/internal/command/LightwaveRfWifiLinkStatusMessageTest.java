package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfJsonMessageId;
import org.openhab.core.library.types.DateTimeType;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.StringType;

public class LightwaveRfWifiLinkStatusMessageTest {

	private String messageString = "*!{\"trans\":452,\"mac\":\"ab:cd:ef\",\"time\":1447712274,\"type\":\"hub\",\"prod\":\"wfl\",\"fw\":\"U2.91Y\","
	 + "\"uptime\":1386309,\"timeZone\":0,\"lat\":52.48,\"long\":-87.89,\"duskTime\":1447690400,"
	 + "\"dawnTime\":1447659083,\"tmrs\":0,\"evns\":1,\"run\":0,\"macs\":8,\"ip\":\"192.168.0.1\",\"devs\":0}";

	
	@Test
	public void testDecodingMessage() throws Exception {
		LightwaveRfWifiLinkStatusMessage message = new LightwaveRfWifiLinkStatusMessage(messageString);
		assertEquals(new LightwaveRfJsonMessageId(452), message.getMessageId());
		assertEquals("ab:cd:ef", message.getMac());
		assertEquals(new Date(1447712274000L), message.getTime());
		assertEquals("hub", message.getType());
		assertEquals("wfl", message.getProd());
		assertEquals("U2.91Y", message.getFirmware());
		assertEquals(1386309, message.getUptime());
		assertEquals("0", message.getTimeZone());
		assertEquals("52.48", message.getLatitude());
		assertEquals("-87.89", message.getLongitude());
		assertEquals(new Date(1447690400000L), message.getDuskTime());
		assertEquals(new Date(1447659083000L), message.getDawnTime());
		assertEquals("0", message.getTmrs());
		assertEquals("1", message.getEnvs());
		assertEquals("0", message.getRun());
		assertEquals("8", message.getMacs());
		assertEquals("192.168.0.1", message.getIp());
		assertEquals("0", message.getDevs());
		assertEquals("wifilink", message.getSerial());
	}
	
	@Test
	public void testMatches() {
		boolean matches = LightwaveRfWifiLinkStatusMessage.matches(messageString);
		assertTrue(matches);
	}
	
	@Test
	public void testGetState() throws Exception {
		LightwaveRfWifiLinkStatusMessage message = new LightwaveRfWifiLinkStatusMessage(messageString);
		Calendar dawnTime = Calendar.getInstance();
		dawnTime.setTime(new Date(1447659083000L));
		Calendar duskTime = Calendar.getInstance();
		duskTime.setTime(new Date(1447690400000L));
		
		assertEquals(new DateTimeType(dawnTime), message.getState(LightwaveRfType.WIFILINK_DAWN_TIME));
		assertEquals(new DateTimeType(duskTime), message.getState(LightwaveRfType.WIFILINK_DUSK_TIME));
		assertEquals(new StringType("52.48"), message.getState(LightwaveRfType.WIFILINK_LATITUDE));
		assertEquals(new StringType("-87.89"), message.getState(LightwaveRfType.WIFILINK_LONGITUDE));
		assertEquals(new StringType("U2.91Y"), message.getState(LightwaveRfType.WIFILINK_FIRMWARE));
		assertEquals(new StringType("192.168.0.1"), message.getState(LightwaveRfType.WIFILINK_IP));
		assertEquals(new DecimalType(1386309), message.getState(LightwaveRfType.WIFILINK_UPTIME));
	}

}
