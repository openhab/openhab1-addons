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
import org.openhab.core.library.types.StringType;

public class LightwaveRfHeatingInfoCommandTest {

	/*
	 * Commands Like
	 * *!{
	 * 		"trans":1232,
	 * 		"mac":"03:02:71",
	 * 		"time":1423827547,
	 * 		"prod":"valve",
	 * 		"serial":"5A4F02",
	 * 		"signal":0,
	 * 		"type":"temp",
	 * 		"batt":2.72,
	 * 		"ver":56,
	 * 		"state":"run",
	 * 		"cTemp":17.8,
	 * 		"cTarg":19.0,
	 * 		"output":80,
	 * 		"nTarg":24.0,
	 * 		"nSlot":"06:00",
	 * 		"prof":5
	 * }
	 */  
	
	@Test
	public void test() throws Exception {
		String message = "*!{\"trans\":1506,\"mac\":\"03:02:71\",\"time\":1423850746,\"prod\":\"valve\",\"serial\":\"064402\",\"signal\":54,\"type\":\"temp\",\"batt\":2.99,\"ver\":56,\"state\":\"boost\",\"cTemp\":22.3,\"cTarg\":24.0,\"output\":100,\"nTarg\":20.0,\"nSlot\":\"18:45\",\"prof\":5}";
		LightwaveRfSerialMessage command = new LightwaveRfHeatingInfoResponse(message);
		//LightwaveRfHeatingInfoResponse 
		assertEquals(new DecimalType("2.99"), command.getState(LightwaveRfType.HEATING_BATTERY));
		assertEquals(new DecimalType("22.3").doubleValue(), ((DecimalType) command.getState(LightwaveRfType.HEATING_CURRENT_TEMP)).doubleValue(), 0.001);
		assertEquals(new DecimalType("24"), command.getState(LightwaveRfType.HEATING_SET_TEMP));
		assertEquals(new DecimalType("54"), command.getState(LightwaveRfType.SIGNAL));
		assertEquals(new StringType("56"), command.getState(LightwaveRfType.VERSION));
		assertEquals(message, command.getLightwaveRfCommandString());
		assertEquals("064402", command.getSerial());
		assertEquals("1506", command.getMessageId().getMessageIdString());
		
		LightwaveRfHeatingInfoResponse heatingInfoCommand = (LightwaveRfHeatingInfoResponse) command;
		
		assertEquals("03:02:71", heatingInfoCommand.getMac());
		assertEquals("valve", heatingInfoCommand.getProd());
		assertEquals("54", heatingInfoCommand.getSignal());
		assertEquals("temp", heatingInfoCommand.getType());
		assertEquals("2.99",heatingInfoCommand.getBatteryLevel());
		assertEquals("56", heatingInfoCommand.getVersion());
		assertEquals("boost", heatingInfoCommand.getState());
		assertEquals("22.3", heatingInfoCommand.getCurrentTemperature());
		assertEquals("24.0", heatingInfoCommand.getCurrentTargetTemperature());
		assertEquals("100", heatingInfoCommand.getOutput());
		assertEquals("20.0", heatingInfoCommand.getNextTargetTeperature());
		assertEquals("18:45", heatingInfoCommand.getNextSlot());
		assertEquals("5", heatingInfoCommand.getProf());
		
	}

	
}
