/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2013, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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
