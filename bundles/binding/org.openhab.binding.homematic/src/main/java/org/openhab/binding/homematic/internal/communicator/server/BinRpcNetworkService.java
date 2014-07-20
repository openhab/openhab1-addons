/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.communicator.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.openhab.binding.homematic.internal.common.HomematicConfig;
import org.openhab.binding.homematic.internal.common.HomematicContext;
import org.openhab.binding.homematic.internal.communicator.HomematicCallbackReceiver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Waits for a message from the Homematic server and starts the
 * BinRpcCallbackHandler to handle the message.
 * 
 * @author Gerhard Riegler
 * @since 1.5.0
 */
public class BinRpcNetworkService implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(BinRpcNetworkService.class);

	private ServerSocket serverSocket;
	private final ExecutorService pool = Executors.newCachedThreadPool();
	private boolean accept = true;
	private HomematicCallbackReceiver callbackReceiver;

	/**
	 * Creates the socket for listening to events from the Homematic server.
	 */
	public BinRpcNetworkService(HomematicCallbackReceiver callbackReceiver) throws Exception {
		this.callbackReceiver = callbackReceiver;

		HomematicConfig config = HomematicContext.getInstance().getConfig();
		serverSocket = new ServerSocket(config.getCallbackPort());
		serverSocket.setReuseAddress(true);
	}

	/**
	 * Listening for events and starts the callbackHandler if a event received.
	 */
	@Override
	public void run() {
		while (accept) {
			try {
				Socket cs = serverSocket.accept();
				BinRpcCallbackHandler rpcHandler = new BinRpcCallbackHandler(cs, callbackReceiver);
				pool.execute(rpcHandler);
			} catch (IOException ex) {
				// ignore
			}
		}
	}

	/**
	 * Stops the listening.
	 */
	public void shutdown() {
		logger.debug("Shutting down {}", this.getClass().getSimpleName());
		accept = false;
		try {
			serverSocket.close();
		} catch (IOException ioe) {
			// ignore
		}
		pool.shutdownNow();
	}

}
