/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSetHeatingTemperatureCommand;

public class LightwaveRFSenderThreadTest {

	@Mock DatagramSocket mockSocket;
	private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
	
	@Before
	public void init(){
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void test() throws Exception {
		
		LightwaveRfSetHeatingTemperatureCommand tempCommand = new LightwaveRfSetHeatingTemperatureCommand("768,!R3DhF*tP18.0");
		final LightwaveRfCommandOk okCommand = new LightwaveRfCommandOk("768,OK");
		
		final LightwaveRFSenderThread senderThread = new LightwaveRFSenderThread(mockSocket, "192.168.1.1", 8000, 120000);

		executor.schedule(new Runnable() {
			@Override 
			public void run() {
				senderThread.okMessageReceived(okCommand);
			}
		}, 1000, TimeUnit.MILLISECONDS);
		
		senderThread.sendLightwaveCommand(tempCommand);
		senderThread.run();
		
		verify(mockSocket, times(1)).send(any(DatagramPacket.class));
	}
}
