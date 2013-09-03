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

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command sender. Runs in dedicated thread to send commands asynchronously to
 * the Nikobus interface.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusCommandSender implements Runnable {

	private static Logger log = LoggerFactory.getLogger(NikobusCommandSender.class);

	private LinkedBlockingQueue<NikobusCommand> sendQueue = new LinkedBlockingQueue<NikobusCommand>();

	private NikobusInterface serialInterface;

	private boolean stopped;

	/**
	 * Create new instance linked to the given serial interface.
	 * 
	 * @param serialInterface
	 *            Nikobus interface.
	 */
	public NikobusCommandSender(NikobusInterface serialInterface) {
		this.serialInterface = serialInterface;
	}

	/**
	 * Start sending thread.
	 */
	@Override
	public void run() {
		log.debug("Command sender started.");

		try {
			while (true && !stopped) {
				NikobusCommand command = sendQueue.take();

				if (command.getWaitForSilence()) {
					waitForQuietBus();
				}

				log.trace("Sending command {}", command.getCommand());
				for (int i = 0; i < command.getRepeats(); i++) {
					serialInterface.writeMessage(command.getCommand());
				}

				command.incrementSentCount();
				
				// leave a little time between consecutive commands
				Thread.sleep(50);
			}

		} catch (InterruptedException e) {
			log.debug("Command sender stopped.");
		} catch (Exception e) {
			log.error("Error writing command.", e);
		}
	}
	
	/**
	 * Wait until there has been no activity on the bus in the last 150 ms.
	 */
	private void waitForQuietBus() {
		while (System.currentTimeMillis()- serialInterface.getLastEventTimestamp() < 150) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				break;
			}
		}
	}

	/**
	 * Send a command to the Nikobus. Sending is done asynchronously. This
	 * method will return immediately.
	 * 
	 * @param cmd
	 *            command to send.
	 */
	public void sendCommand(NikobusCommand cmd) {
		if (isCommandRedundant(cmd)) {
			return;
		}
		sendQueue.add(cmd);
	}

	/**
	 * Stop execution.
	 */
	public void stop() {
		stopped = true;
	}
	
	/**
	 * Check if the sending of the command is redundant or not allowed.
	 * @param cmd Nikobus Command
	 * @return true if the command is already scheduled for sending..
	 */
	public boolean isCommandRedundant(NikobusCommand cmd) {
		if (!cmd.getAllowDuplicates() && sendQueue.contains(cmd)) {
			log.trace("Ignoring duplicate command {}", cmd.toString());
			return true;
		}
		return false;
	}
	
}
