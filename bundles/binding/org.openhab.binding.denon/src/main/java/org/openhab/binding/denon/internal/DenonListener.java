/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.telnet.TelnetClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage telnet connection to the Denon Receiver
 * 
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonListener extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(DenonListener.class);

	private static final Integer RECONNECT_DELAY = 60000; // 1 minute

	private static final Integer TIMEOUT = 60000; // 1 minute

	private DenonConnectionProperties connection;

	private DenonUpdateReceivedCallback callback;

	private TelnetClient tc;

	private boolean running = true;

	private boolean connected = false;

	public DenonListener(DenonConnectionProperties connection, DenonUpdateReceivedCallback callback) {
		logger.debug("Denon listener created");
		this.connection = connection;
		this.callback = callback;

		this.tc = createTelnetClient();
	}

	@Override
	public void run() {
		while (running) {
			if (!connected) {
				connectTelnetClient();
			}

			InputStream is = tc.getInputStream();
			PrintWriter out = new PrintWriter(tc.getOutputStream(), true);

			int readCount = 0;
			byte[] buffer = new byte[4096];

			do {
				try {
					readCount = is.read(buffer);
					if (readCount > -1) {
						String line = StringUtils.trim(new String(buffer, 0, readCount));
						if (!StringUtils.isBlank(line)) {
							logger.trace("Received from {}: {}", connection.getHost(), line);
							callback.updateReceived(line);
						}
					} else {
						throw new IOException("Telnet connection disconnected");
					}
				} catch (SocketTimeoutException e) {
					logger.trace("Socket timeout");
					// Disconnects are not always detected unless you write to the socket.
					out.print(" ");
					out.flush();
				} catch (IOException e) {
					callback.listenerDisconnected();
					logger.debug("Error in telnet connection", e);
					connected = false;
				}
			} while (running && connected);
		}
	}

	public void shutdown() {
		this.running = false;
		disconnect();
	}

	private void connectTelnetClient() {
		disconnect();
		int delay = 0;

		while (!tc.isConnected()) {
			try {
				Thread.sleep(delay);
				logger.debug("Connecting to {}", connection.getHost());
				tc.connect(connection.getHost(), connection.getTelnetPort());
				callback.listenerConnected();
				connected = true;
			} catch (IOException e) {
				logger.debug("Cannot connect to {}", connection.getHost(), e);
			} catch (InterruptedException e) {
				logger.debug("Interrupted while connecting to {}", connection.getHost(), e);
			}
			delay = RECONNECT_DELAY;
		}

		logger.debug("Denon telnet client connected to {}", connection.getHost());
	}

	private void disconnect() {
		if (tc != null) {
			try {
				this.tc.disconnect();
			} catch (IOException e) {
				logger.debug("Error while disconnecting telnet client", e);
			}
		}
	}

	private TelnetClient createTelnetClient() {
		TelnetClient tc = new TelnetClient();
		tc.setDefaultTimeout(TIMEOUT);
		return tc;
	}

}
