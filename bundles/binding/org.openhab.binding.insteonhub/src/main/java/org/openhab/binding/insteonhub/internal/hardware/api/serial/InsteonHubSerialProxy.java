/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.insteonhub.internal.hardware.api.serial;

import java.io.IOException;
import java.net.Socket;

import org.openhab.binding.insteonhub.internal.hardware.InsteonHubAdjustmentType;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxy;
import org.openhab.binding.insteonhub.internal.hardware.InsteonHubProxyListener;
import org.openhab.binding.insteonhub.internal.hardware.api.InsteonHubCommand;
import org.openhab.binding.insteonhub.internal.util.InsteonHubBindingLogUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * {@link InsteonHubProxy} implementation that uses the Serial API to
 * communicate with the INSTEON Hub. This proxy takes care of the connection and
 * translating method calls to queue-able commands. The transport will take care
 * of actually sending and receiving the messages.
 * 
 * @author Eric Thill
 * 
 */
public class InsteonHubSerialProxy implements InsteonHubProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(InsteonHubSerialProxy.class);

	public static final int DEFAULT_PORT = 9761;
	private static final long RETRY_INTERVAL_SECONDS = 30;
	private static final long MILLIS_PER_SECOND = 1000;

	private final InsteonHubSerialTransport transport;
	private final String host;
	private final int port;

	private volatile Socket socket;
	private volatile Connecter connecter;

	public InsteonHubSerialProxy(String host) {
		this(host, DEFAULT_PORT);
	}

	public InsteonHubSerialProxy(String host, int port) {
		this.host = host;
		this.port = port;
		transport = new InsteonHubSerialTransport(this);
	}

	@Override
	public String getConnectionString() {
		return host + ":" + port;
	}

	@Override
	public synchronized void start() {
		reconnect();
	}

	protected synchronized void reconnect() {
		// if not currently connecting => reconnect
		if (connecter == null) {
			// stop and cleanup existing connection
			stop();
			// asynchronously reconnect
			connecter = new Connecter();
			new Thread(connecter, getConnectionString() + " connecter").start();
		}
	}

	@Override
	public synchronized void stop() {
		// tell all threads to gracefully exit
		connecter = null;
		transport.stop();
		// try to close the socket
		try {
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {
			// ignore
		}
	}

	@Override
	public void setDevicePower(String device, boolean power) {
		if (power) {
			enqueueCommand(InsteonHubCommand.newOnFastCommand(device));
		} else {
			enqueueCommand(InsteonHubCommand.newOffFastCommand(device));
		}
	}

	@Override
	public void setDeviceLevel(String device, int level) {
		if (level == 0) {
			enqueueCommand(InsteonHubCommand.newOffCommand(device));
		} else {
			enqueueCommand(InsteonHubCommand.newOnCommand(device, level));
		}
	}

	@Override
	public void startDeviceAdjustment(String device,
			InsteonHubAdjustmentType adjustmentType) {
		if (adjustmentType == InsteonHubAdjustmentType.DIM) {
			enqueueCommand(InsteonHubCommand.newStartDimCommand(device));
		} else if (adjustmentType == InsteonHubAdjustmentType.BRIGHTEN) {
			enqueueCommand(InsteonHubCommand.newStartBrightenCommand(device));
		}
	}

	@Override
	public void stopDeviceAdjustment(String device) {
		enqueueCommand(InsteonHubCommand.newStopAdjustCommand(device));
	}

	@Override
	public void requestDeviceLevel(String device) {
		enqueueCommand(InsteonHubCommand.newGetLevelCommand(device));
	}

	private void enqueueCommand(InsteonHubCommand command) {
		if (!transport.isStarted()) {
			logger.info("Not sending message - Not connected to Hub");
			return;
		}
		transport.enqueueCommand(command);
	}

	@Override
	public void addListener(InsteonHubProxyListener listener) {
		transport.addListener(listener);
	}

	@Override
	public void removeListener(InsteonHubProxyListener listener) {
		transport.removeListener(listener);
	}

	private class Connecter implements Runnable {
		@Override
		public void run() {
			while (connecter == this) {
				try {
					// try connecting
					socket = new Socket(host, port);
					// no IOException => success => start send/rec threads
					transport.start(socket.getInputStream(),
							socket.getOutputStream());
					connecter = null;
					logger.info("Connected to Insteon Hub: " + host + ":" + port);
					return;
				} catch (IOException e) {
					// connection failure => log and retry in a bit
					InsteonHubBindingLogUtil.logCommunicationFailure(logger,
							InsteonHubSerialProxy.this, e);
					logger.info("Will retry connection in "
							+ RETRY_INTERVAL_SECONDS + " seconds...");
					try {
						Thread.sleep(RETRY_INTERVAL_SECONDS * MILLIS_PER_SECOND);
					} catch (InterruptedException e1) {
						// ignore
					}
				}
			}
		}
	}

}
