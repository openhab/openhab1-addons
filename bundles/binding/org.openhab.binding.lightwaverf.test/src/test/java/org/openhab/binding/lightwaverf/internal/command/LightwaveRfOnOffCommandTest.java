/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;

public class LightwaveRfOnOffCommandTest {

	@Test
	public void testOnOffCommandAsMessageOn() throws Exception {
		String message = "104,!R2D3F1|Living Room|Angle Poise Off\n";
		LightwaveRfRoomDeviceMessage command = new LightwaveRfOnOffCommand(message);
		assertEquals("104,!R2D3F1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("104", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.ON, command.getState(LightwaveRfType.SWITCH));
		assertEquals(PercentType.HUNDRED, command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testOnOffCommandAsMessageOff() throws Exception {
		String message = "10,!R2D3F0";
		LightwaveRfRoomDeviceMessage command = new LightwaveRfOnOffCommand(message);
		assertEquals("010,!R2D3F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.SWITCH));
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testOnOffCommandAsParametersOn() throws Exception {
		LightwaveRfRoomDeviceMessage command = new LightwaveRfOnOffCommand(10, "2", "3", true);
		assertEquals("010,!R2D3F1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.ON, command.getState(LightwaveRfType.SWITCH));	
		assertEquals(PercentType.HUNDRED, command.getState(LightwaveRfType.DIMMER));

	}
	
	@Test
	public void testOnOffCommandAsParametersOff() throws Exception {
		LightwaveRfRoomDeviceMessage command = new LightwaveRfOnOffCommand(10, "2", "3", false);
		assertEquals("010,!R2D3F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.SWITCH));
		assertEquals(OnOffType.OFF, command.getState(LightwaveRfType.DIMMER));
	}

}
