/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Ignore;
import org.openhab.binding.lightwaverf.internal.LightwaveRfType;
import org.openhab.core.library.types.PercentType;

public class LightwaveRfDimCommandTest {

	@Test
	public void testDimCommandAsMessageLow() throws Exception {
		String message = "109,!R2D1FdP1|Kitchen|Kitchen Ceiling 4%\n";
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(message);
		assertEquals("109,!R2D1FdP1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("1", command.getDeviceId());
		assertEquals("109", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("4"), command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testDimCommandAsMessageHigh() throws Exception {
		String message = "10,!R2D3FdP31|Kitchen|Kitchen Ceiling 96%\n";
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(message);
		assertEquals("010,!R2D3FdP31\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("97"), command.getState(LightwaveRfType.DIMMER));
	}
	
	@Test
	@Ignore("Not sure that the lightwave app actually sends this message when turning off")
	public void testDimCommandAsMessageOff() throws Exception {
		String message = "109,!R2D1F0|Kitchen|Kitchen Ceiling OFF\n";
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(message);
		assertEquals("109,!R2D1F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("1", command.getDeviceId());
		assertEquals("109", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("0"), command.getState(LightwaveRfType.DIMMER));
	}	

	@Test
	public void testDimCommandAsParametersLow() throws Exception {
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(10, "2", "3", 1);
		assertEquals("010,!R2D3FdP1\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("1"), command.getState(LightwaveRfType.DIMMER));
	}
	
	@Test
	public void testDimCommandAsParametersHigh() throws Exception {
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(10, "2", "3", 100);
		assertEquals("010,!R2D3FdP32\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("100"), command.getState(LightwaveRfType.DIMMER));
	}

	@Test
	public void testDimCommandAsParametersOff() throws Exception {
		LightwaveRfRoomDeviceMessage command = new LightwaveRfDimCommand(10, "2", "3", 0);
		assertEquals("010,!R2D3F0\n", command.getLightwaveRfCommandString());
		assertEquals("2", command.getRoomId());
		assertEquals("3", command.getDeviceId());
		assertEquals("010", command.getMessageId().getMessageIdString());
		assertEquals(PercentType.valueOf("0"), command.getState(LightwaveRfType.DIMMER));
	}
	
}
