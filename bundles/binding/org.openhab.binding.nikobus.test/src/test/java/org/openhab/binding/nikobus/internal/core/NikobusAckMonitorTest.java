/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openhab.binding.nikobus.internal.NikobusBinding;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
@RunWith(MockitoJUnitRunner.class)
public class NikobusAckMonitorTest {

	@Mock
	private NikobusCommandReceiver receiver;

	@Mock
	private NikobusBinding binding;
	
	private NikobusCommandSender sender;

	private void testForAck(String ack, String command, boolean expected) {

		NikobusCommand expectedAck = new NikobusCommand("", ack, 100);
		NikobusCommand receivedCommand = new NikobusCommand(command);
		NikobusAckMonitor mon = new NikobusAckMonitor(expectedAck);

		try {
			mon.processNikobusCommand(receivedCommand, binding);
			mon.waitForAck(sender);
			assertTrue(expected);
		} catch (TimeoutException e) {
			assertFalse(expected);
		}

	}

	@Test
	public void canDetectAck() {

		sender = new NikobusCommandSender(null) {

			@Override
			public void sendCommand(NikobusCommand cmd) {
				cmd.incrementSentCount();
			}
		};

		testForAck("mytest", "ADFADFMYTE", false);
		testForAck("mytest", "MYTESTTT", true);
		testForAck("mytest", "ADFADFMYTE", false);
		testForAck("mytest1", "MYTEST1", true);

	}

}
