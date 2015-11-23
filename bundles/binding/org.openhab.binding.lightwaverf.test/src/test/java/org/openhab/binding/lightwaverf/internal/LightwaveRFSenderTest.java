/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;
import org.openhab.core.library.types.OnOffType;

public class LightwaveRFSenderTest {
	
	private final String LIGHTWAVE_IP = "255.255.255.255";
	private final int TRANSMIT_PORT = 9760;
	private final int RECEIVE_PORT = 9761;
	private final LightwaverfConvertor CONVERTOR = new LightwaverfConvertor();
	private final int TIME_BETWEEN_COMMANDS = 2000;
	private final int TIMEOUT_OK = 2000;
	private final int THREAD_SLEEP = 10 * 1000;
	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testSwitch() throws Exception {
		LightwaveRFReceiver receiver2 = new LightwaveRFReceiver(CONVERTOR, TRANSMIT_PORT);
		LightwaveRFSender sender = new LightwaveRFSender(LIGHTWAVE_IP, TRANSMIT_PORT, RECEIVE_PORT, CONVERTOR, TIME_BETWEEN_COMMANDS, TIMEOUT_OK);
		receiver2.start();
		sender.start();
		
		LightwaveRFCommand command = CONVERTOR.convertToLightwaveRfMessage("3", "5", LightwaveRfType.SWITCH, OnOffType.OFF);
		sender.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}	
	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testDimmer() throws Exception {
		LightwaveRFReceiver receiver2 = new LightwaveRFReceiver(CONVERTOR, TRANSMIT_PORT);
		LightwaveRFSender sender = new LightwaveRFSender(LIGHTWAVE_IP, TRANSMIT_PORT, RECEIVE_PORT, CONVERTOR, TIME_BETWEEN_COMMANDS, TIMEOUT_OK);
		receiver2.start();
		sender.start();
		
		LightwaveRFCommand command = CONVERTOR.convertToLightwaveRfMessage("2", "2", LightwaveRfType.DIMMER, OnOffType.OFF);
		sender.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}	

	
	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testSetHeating() throws Exception {
		LightwaveRFReceiver receiver2 = new LightwaveRFReceiver(CONVERTOR, TRANSMIT_PORT);
		LightwaveRFSender sender = new LightwaveRFSender(LIGHTWAVE_IP, TRANSMIT_PORT, RECEIVE_PORT, CONVERTOR, TIME_BETWEEN_COMMANDS, TIMEOUT_OK);
		receiver2.start();
		sender.start();
		
		LightwaveRFCommand command = new LightwaveRfSetHeatingTemperatureCommand(100, "4", 17);
		sender.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}			

	@Test
	@Ignore(value="This is a functional test to ensure the code is working")
	public void testHeatingInfo() throws Exception {
		LightwaveRFReceiver receiver2 = new LightwaveRFReceiver(CONVERTOR, TRANSMIT_PORT);
		LightwaveRFSender sender = new LightwaveRFSender(LIGHTWAVE_IP, TRANSMIT_PORT, RECEIVE_PORT, CONVERTOR, TIME_BETWEEN_COMMANDS, TIMEOUT_OK);
		receiver2.start();
		sender.start();
		
		LightwaveRfHeatInfoRequest command = new LightwaveRfHeatInfoRequest(500, "4");
		sender.sendLightwaveCommand(command);
		
		Thread.sleep(THREAD_SLEEP);
	}			
}
