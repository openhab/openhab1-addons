/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.oppoblurayplayer.internal.core;

import java.util.concurrent.LinkedBlockingQueue;

import org.openhab.binding.oppoblurayplayer.connector.OppoBlurayPlayerConnector;
import org.openhab.binding.oppoblurayplayer.internal.OppoBlurayPlayerCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Command sender. Runs in dedicated thread to send commands asynchronously to
 * the Oppo Bluray Player.
 * 
 * @author  (http://netwolfuk.wordpress.com)
 * @since 1.9.0
 */
public class OppoBlurayPlayerCommandSender implements Runnable {

	private static Logger log = LoggerFactory.getLogger(OppoBlurayPlayerCommandSender.class);

	private LinkedBlockingQueue<String> sendQueue = new LinkedBlockingQueue<String>();

	private OppoBlurayPlayerConnector serialConnection;
	private boolean stopped;
	private int timeout;

	/**
	 * Create new instance linked to the given serial interface.
	 * 
	 * @param serialInterface
	 *            Serial connection interface for Bluray player.
	 * @param timeout
	 *            Serial comms timeout when sending messages to player (milliseconds).
	 */
	public OppoBlurayPlayerCommandSender(OppoBlurayPlayerConnector serialInterface, int timeout) {
		this.serialConnection = serialInterface;
		this.timeout = timeout;
	}

	/**
	 * Start sending thread.
	 */
	@Override
	public void run() {
		log.debug("Command sender started.");

		try {
			log.debug("Sleeping for two seconds before sending first command.");
			Thread.sleep(2000);
			while (true && !stopped) {
				String command = sendQueue.take();

				log.debug("Sending command {}", command);
					serialConnection.sendMessage(command, timeout);
				
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
	 * Send a command to the Bluray player. Sending is done asynchronously. This
	 * method will return immediately.
	 * 
	 * @param cmd
	 *            command to send.
	 */
	public void sendCommand(OppoBlurayPlayerCommand cmd) {
		log.debug("Sending command {}. Will attempt to add it to the event queue.", cmd);
		sendQueue.add(cmd.getCommandString());
	}
	
	/**
	 * Send a query to the Bluray player. Sending is done asynchronously. This
	 * method will return immediately.
	 * 
	 * @param cmd
	 *            command to send.
	 */
	public void sendQuery(OppoBlurayPlayerCommand cmd) {
		log.debug("Sending query {}. Will attempt to add it to the event queue.", cmd);
		sendQueue.add(cmd.getQuery());
	}

	/**
	 * Stop execution.
	 */
	public void stop() {
		stopped = true;
	}

	
}
