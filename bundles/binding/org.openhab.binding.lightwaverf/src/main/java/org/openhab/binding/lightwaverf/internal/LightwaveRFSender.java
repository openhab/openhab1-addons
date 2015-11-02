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
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for transmitting LightwaveRF commands on to the
 * network to the LightwaveRF link which will then act upon these commands.
 * 
 * This class implements retry logic in case it doesn't get an "OK" response
 * from the LightwaveRF wifi link. However please note that due to the way the
 * wifi link works it may receive a command but may not actually action it. In
 * particular this happens when messages are sent to close together. As such we
 * also implement a delay between each message we send.
 * 
 * @author Neil Renaud
 * @since 1.7.0
 */
	public class LightwaveRFSender {
		
		private static final Logger logger = LoggerFactory
				.getLogger(LightwaveRFSender.class);
		private static final int DELAY_BETWEEN_RECEIVES_MS = 10;

		private final LightwaveRFSenderThread sender;
		private final LightwaveRFReceiverThread receiverForWifiLink;

		private final DatagramSocket wifilinkSocket;
		/** Time between commands so we don't flood the LightwaveRF hub */
		private final int timeBetweenCommandMs;
		/** Executor to keep executing this thread with a fixed delay */
		private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

		
		public LightwaveRFSender(String lightwaveWifiLinkIp, int lightwaveWifiLinkTransmitPort, int lightwaveWifiLinkReceivePort, LightwaverfConvertor messageConvertor, int timeBetweenCommandMs,
				int timeoutForOkMessagesMs) throws SocketException, UnknownHostException {
			this.timeBetweenCommandMs = timeBetweenCommandMs;
			
			this.wifilinkSocket = new DatagramSocket(lightwaveWifiLinkReceivePort);
			this.sender = new LightwaveRFSenderThread(wifilinkSocket, lightwaveWifiLinkIp, lightwaveWifiLinkTransmitPort, timeoutForOkMessagesMs);
			this.receiverForWifiLink = new LightwaveRFReceiverThread(messageConvertor, wifilinkSocket);
		}
		
		/**
		 * Start the LightwaveRFSender Will set running to true, initalise the
		 * socket and start the sender thread
		 */
		public synchronized void start() {
			logger.info("Starting LightwaveRFSender");
			receiverForWifiLink.addListener(sender);
			sender.start();
			receiverForWifiLink.start();

			executor.scheduleWithFixedDelay(sender, 0, timeBetweenCommandMs, TimeUnit.MILLISECONDS);
			executor.scheduleWithFixedDelay(receiverForWifiLink, 0, DELAY_BETWEEN_RECEIVES_MS, TimeUnit.MILLISECONDS);		
		}	
		
		/**
		 * Stop the LightwaveRFReseiver Will set running to false, add a stop
		 * message to the queue so that it stops when empty, close and set the
		 * socket to null
		 */
		public synchronized void stop() {
			logger.info("Stopping LightwaveRFSender");
			receiverForWifiLink.removeListener(sender);
			sender.stop();
			receiverForWifiLink.stop();
			executor.shutdownNow();
			wifilinkSocket.close();
			logger.info("LightwaveRFSender Stopped");
		}
		
		/**
		 * Add LightwaveRFCommand command to queue to send.
		 */
		public void sendLightwaveCommand(LightwaveRFCommand command) {
			sender.sendLightwaveCommand(command);
		}
		
		/**
		 * Add listener to be notified of messages received on the socket
		 * 
		 * @param listener
		 */
		public void addListener(LightwaveRFMessageListener listener) {
			receiverForWifiLink.addListener(listener);
		}

		/**
		 * Remove listener to stop being notified of messages being received on the
		 * socket.
		 * 
		 * @param listener
		 */
		public void removeListener(LightwaveRFMessageListener listener) {
			receiverForWifiLink.removeListener(listener);
		}
	}