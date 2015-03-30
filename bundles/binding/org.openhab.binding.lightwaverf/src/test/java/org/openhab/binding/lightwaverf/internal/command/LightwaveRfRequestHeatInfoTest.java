package org.openhab.binding.lightwaverf.internal.command;

import org.junit.Test;
import static org.junit.Assert.*;

public class LightwaveRfRequestHeatInfoTest {
	
	@Test
	public void test() throws Exception {
		String message = "105,!R3F*r\n";
		LightwaveRfRoomMessage command = new LightwaveRfHeatInfoRequest(message);
		assertEquals("3", command.getRoomId());
		assertEquals("105,!R3F*r\n",command.getLightwaveRfCommandString());
	}

}
