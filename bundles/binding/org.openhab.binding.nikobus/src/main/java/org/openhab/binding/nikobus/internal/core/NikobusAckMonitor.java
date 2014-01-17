/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.nikobus.internal.core;

import java.util.UUID;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.openhab.binding.nikobus.internal.NikobusBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Monitor which listens to nikobus commands and checks if ACK commands are
 * received within a given timeout.
 * 
 * @author Davy Vanherbergen
 * @since 1.3.0
 */
public class NikobusAckMonitor implements NikobusCommandListener {

	private static Logger log = LoggerFactory.getLogger(NikobusAckMonitor.class);

	private NikobusCommand command;

	private LinkedBlockingQueue<String> receivedCommands = new LinkedBlockingQueue<String>();

	private String name = UUID.randomUUID().toString();
	
	/**
	 * Create a new monitor for an ACK command.
	 * 
	 * @param command
	 *            for which to monitor an ACK
	 */
	public NikobusAckMonitor(NikobusCommand command) {		
		this.command = command;
	}

	@Override
	public void processNikobusCommand(NikobusCommand command, NikobusBinding binding) {
		log.trace("Processing nikobus command {}", command.getCommand());
		receivedCommands.add(command.getCommand());
	}

	/**
	 * Wait for the reception of an ACK message. If no message was received
	 * within the timeout specified in the command, an exception is thrown.
	 * 
	 * When the maxRetryCount of a command is > 1, the command will be resent up
	 * to maxRetryCount times or until a successful ACK is reached. When a
	 * command is being resent, the timeout is used for every send, so the
	 * effective timeout = (maxRetryCount * timeout)
	 * 
	 * @param commandSender
	 *            sender to use for (re)sending command
	 * 
	 * @throws Exception
	 *             if no ACK received within timeout..
	 */
	public void waitForAck(NikobusCommandSender commandSender) throws TimeoutException {

		// initial send...
		commandSender.sendCommand(command);

		// check if the ACK is there
		if (isAckReceived()) {
			return;
		}

		// resend...
		while (command.getSentCount() < command.getMaxRetryCount()) {
			if (command.getSentCount() > 0) {
				// only resend if the first command was actually sent..
				commandSender.sendCommand(command);
			}

			// check if the ACK is there
			if (isAckReceived()) {
				return;
			}
		}

		// no ACK received if we get here...
		throw new TimeoutException("No ACK received within timeout and retry count.");
	}

	/**
	 * Send a command and wait for an ACK until the timeout has been reached.
	 * 
	 * @return true if an ACK was received within the timeout limit.
	 */
	private boolean isAckReceived() {

		String ack = null;
		long availableTime = command.getTimeout();
		long startTime = System.currentTimeMillis();

		while (availableTime > 0) {

			try {
				ack = receivedCommands.poll(availableTime,
						TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.warn("MonitorThread interrupted.");
				break;
			}
			
			if (ack != null && ack.startsWith(command.getAck().toUpperCase())) {
				// good ACK received..
				log.trace("Received expected ack '{}'", ack);
				return true;
			} else if (ack == null) {
				log.trace("No ack received within poll time ({}).", command.getTimeout());
			}
			
			availableTime = command.getTimeout() - (System.currentTimeMillis() - startTime);
		}

		return false;
	}

	@Override
	public String getName() {
		return name;
	}
	
}
