package org.openhab.binding.lightwaverf.internal.command;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

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
	public void test() {
		String message = "*!{\"trans\":1506,\"mac\":\"03:02:71\",\"time\":1423850746,\"prod\":\"valve\",\"serial\":\"064402\",\"signal\":54,\"type\":\"temp\",\"batt\":2.99,\"ver\":56,\"state\":\"boost\",\"cTemp\":22.3,\"cTarg\":24.0,\"output\":100,\"nTarg\":20.0,\"nSlot\":\"18:45\",\"prof\":5}";
		LightwaveRfHeatingInfoCommand command = new LightwaveRfHeatingInfoCommand(message);
		
		assertEquals("1506", command.getMessageId().getMessageIdString());
		assertEquals("03:02:71", command.getMac());
		assertEquals(new Date(1423850746), command.getTime());
		assertEquals("valve", command.getProd());
		assertEquals("064402", command.getSerial());
		assertEquals(54.0, command.getSignal(), 0.001);
		assertEquals("temp", command.getType());
		assertEquals(2.99,command.getBatteryLevel(), 0.001);
		assertEquals("56", command.getVersion());
		assertEquals("boost", command.getState());
		assertEquals(22.3, command.getCurrentTemperature(), 0.001);
		assertEquals(24.0, command.getCurrentTargetTemperature(), 0.001);
		assertEquals(100, command.getOutput(), 0.001);
		assertEquals(20.0, command.getNextTargetTeperature(), 0.001);
		assertEquals("18:45", command.getNextSlot());
		assertEquals(5, command.getProf(), 0.01);
		
		assertEquals(message, command.getLightwaveRfCommandString());
	}

	
}
