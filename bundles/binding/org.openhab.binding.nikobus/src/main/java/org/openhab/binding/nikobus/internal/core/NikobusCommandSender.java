/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
