/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class that listens to the LAN for commands either sent to the LightwaveRF
 * Wifi link or replies from the lightwaveRF Wifi link. Upon receiving a valid
 * command this will notify all listeners of the message received.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
public class LightwaveRFReceiver {
	private static final Logger logger = LoggerFactory.getLogger(LightwaveRFReceiver.class);
	private static final int DELAY_BETWEEN_RECEIVES_MS = 10;

	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

	private final DatagramSocket receiveSocket;

	private final LightwaveRFReceiverThread receiverThread;
	
	public LightwaveRFReceiver(LightwaverfConvertor messageConvertor, int port)
			throws SocketException, UnknownHostException {
		
//		System.setProperty("java.net.preferIPv4Stack" , "true");
//		this.receiveSocket = new DatagramSocket(port);
		this.receiveSocket = new DatagramSocket(new InetSocketAddress("0.0.0.0", port));
//		this.receiveSocket = new DatagramSocket(null);
//		this.receiveSocket.bind(new InetSocketAddress(port));
		
		receiverThread = new LightwaveRFReceiverThread(messageConvertor, receiveSocket);
		
//		System.out.println(receiveSocket.getBroadcast());
//		System.out.println(receiveSocket.getInetAddress());
//		System.out.println(receiveSocket.getLocalAddress());
//		System.out.println(receiveSocket.getLocalPort());
//		System.out.println(receiveSocket.isBound());
//		System.out.println(receiveSocket.isConnected());
		
	}
	
	/**
	 * Start the LightwaveRFReceiver Will set running true, initialise the
	 * socket and start the thread.
	 */
	public synchronized void start() {
		logger.info("Starting LightwaveRFReceiver");
		receiverThread.start();
		executor.scheduleWithFixedDelay(receiverThread, 0, DELAY_BETWEEN_RECEIVES_MS, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stop the LightwaveRFSender Will close the socket wait for the thread to
	 * exit and null the socket
	 */
	public synchronized void stop() {
		logger.info("Stopping LightwaveRFReceiver");
		receiverThread.stop();
		executor.shutdownNow();
		receiveSocket.close();
		logger.info("LightwaveRFReceiver Stopped");
	}

	/**
	 * Add listener to be notified of messages received on the socket
	 * 
	 * @param listener
	 */
	public void addListener(LightwaveRFMessageListener listener) {
		receiverThread.addListener(listener);
	}

	/**
	 * Remove listener to stop being notified of messages being received on the
	 * socket.
	 * 
	 * @param listener
	 */
	public void removeListener(LightwaveRFMessageListener listener) {
		receiverThread.removeListener(listener);
	}
}