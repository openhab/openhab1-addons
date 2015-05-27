/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.core.library.types.OnOffType;

public class LightwaveRFSenderTest {

	@Test
	@Ignore
	public void test() throws Exception {
		
		LightwaverfConvertor convertor = new LightwaverfConvertor();

		LightwaveRFReceiver receiver2 = new LightwaveRFReceiver(convertor, 9761);
		LightwaveRFSender sender = new LightwaveRFSender("255.255.255.255", 9760, 1000, 1000);
		receiver2.start();
		receiver2.addListener(sender);
		
		sender.start();
		
		
		LightwaveRFCommand command1 = convertor.convertToLightwaveRfMessage("2", "2", LightwaveRfType.DIMMER, OnOffType.OFF);
		LightwaveRFCommand command2 = convertor.convertToLightwaveRfMessage("3", "3", LightwaveRfType.SWITCH, OnOffType.OFF);
		LightwaveRFCommand command3 = convertor.convertToLightwaveRfMessage("3", "5", LightwaveRfType.SWITCH, OnOffType.OFF);
		LightwaveRFCommand command4 = convertor.convertToLightwaveRfMessage("3", "6", LightwaveRfType.DIMMER, OnOffType.OFF);
		LightwaveRFCommand command5 = convertor.convertToLightwaveRfMessage("1", "1", LightwaveRfType.DIMMER, OnOffType.OFF);
		
		sender.sendLightwaveCommand(command1);
		sender.sendLightwaveCommand(command2);
		sender.sendLightwaveCommand(command3);
		sender.sendLightwaveCommand(command4);
		sender.sendLightwaveCommand(command5);
		
		Thread.sleep(10 * 60 * 1000);
		fail("Not yet implemented");
	}

}
