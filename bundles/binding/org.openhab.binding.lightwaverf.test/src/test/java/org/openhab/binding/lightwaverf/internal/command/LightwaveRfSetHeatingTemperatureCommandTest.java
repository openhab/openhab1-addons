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
