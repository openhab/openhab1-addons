package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

public class LightwaveRfHeatingInfoCommandTest {

	@Test
	public void test() {
		String message = "*!{\"trans\":1506,\"mac\":\"03:02:71\",\"time\":1423850746,\"prod\":\"valve\",\"serial\":\"064402\",\"signal\":54,\"type\":\"temp\",\"batt\":2.99,\"ver\":56,\"state\":\"boost\",\"cTemp\":22.3,\"cTarg\":24.0,\"output\":100,\"nTarg\":20.0,\"nSlot\":\"18:45\",\"prof\":5}";
		LightwaveRFCommand command;
		assertEquals(new Date(1423850746), "time");
		assertEquals(1506, "trans");
		assertEquals("064402", "serial");
		
		assertEquals("54", "signal");
		assertEquals("2.99", "batt");
		assertEquals("boost", "state");
		assertEquals("22.3", "cTemp");
		assertEquals("24.0", "cTarg");

		
	}

	
}
