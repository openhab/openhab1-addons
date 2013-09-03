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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.junit.Test;
import org.openhab.binding.nikobus.internal.NikobusBinding;

/**
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusCommandParserTest {

	@Test
	public void canSplitCommands() throws InterruptedException {

		testSplit(new String[] { "AA#N1234", "56\r#N12345", "6\rDD" },
				new NikobusCommand[] { new NikobusCommand("AA"),
						new NikobusCommand("#N123456", 2) });

		String[] tmp = new String[60];
		for (int i = 0; i < 60; i++) {
			tmp[i] = "#N123456\r";
		}

		testSplit(new String[] { "#N123456\r" },
				new NikobusCommand[] { new NikobusCommand("#N123456", 1) });

		// when long presses are received, only full long presses should
		// be published
		testSplit(tmp, new NikobusCommand[] {
				new NikobusCommand("#N123456", 25),
				new NikobusCommand("#N123456", 37),
				new NikobusCommand("#N123456", 49) });

		testSplit(new String[] { "AA#N123456\r#$5012#N12345", "6\rDD" },
				new NikobusCommand[] { new NikobusCommand("AA"),
						new NikobusCommand("#N123456"),
						new NikobusCommand("#"), new NikobusCommand("$5012"),
						new NikobusCommand("#N123456") });

		testSplit(new String[] { "AA#N1234", "56\r#N12345", "6\rDD" },
				new NikobusCommand[] { new NikobusCommand("AA"),
						new NikobusCommand("#N123456", 2) });

	}

	private void testSplit(String[] values, NikobusCommand[] commands)
			throws InterruptedException {

		NikobusCommandReceiver parser = new NikobusCommandReceiver(null);

		final List<NikobusCommand> receivedCmds = new ArrayList<NikobusCommand>();

		LinkedBlockingQueue<byte[]> queue = new LinkedBlockingQueue<byte[]>();
		parser.setBufferQueue(queue);
		parser.register(new NikobusCommandListener() {

			@Override
			public void processNikobusCommand(NikobusCommand command,
					NikobusBinding binding) {
				receivedCmds.add(new NikobusCommand(command.getCommand(),
						command.getRepeats()));
			}
		});

		for (String s : values) {
			byte[] bb = s.getBytes();
			queue.put(bb);
		}
		Thread t = new Thread(parser);
		t.start();
		Thread.sleep(1000);
		t.interrupt();

		for (int i = 0; i < commands.length; i++) {
			NikobusCommand c = receivedCmds.get(i);
			assertEquals(commands[i].getCommand(), c.getCommand());
			assertEquals(commands[i].getRepeats(), c.getRepeats());
		}

		assertEquals(commands.length, receivedCmds.size());
	}

}
