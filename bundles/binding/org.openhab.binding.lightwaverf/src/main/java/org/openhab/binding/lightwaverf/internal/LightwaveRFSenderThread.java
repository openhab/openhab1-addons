/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.lightwaverf.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfMessageId;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfRegistrationMessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for transmitting LightwaveRF commands on to the
 * network to the LightwaveRF link which will then act upon these commands.
 * 
 * This class will also listen for the responses from the wifi link and 
 * notify listeners of these responses.
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
public class LightwaveRFSenderThread implements Runnable, LightwaveRFMessageListener {
	private static final Logger logger = LoggerFactory.getLogger(LightwaveRFSenderThread.class);
	/** Message added to the send queue to indicate that we need to shutdown */
	private static final LightwaveRFCommand STOP_MESSAGE = LightwaveRFCommand.STOP_MESSAGE;
	private static final Integer ONE = new Integer(1);
	
	private static final int MAX_RETRY_ATTEMPS = 5;

	/**
	 * Map of CountDownLatches, used to notify when we have received an ok for
	 * one of our messages
	 */
	private final ConcurrentMap<LightwaveRfMessageId, CountDownLatch> latchMap = new ConcurrentHashMap<LightwaveRfMessageId, CountDownLatch>();
	/** Queue of messages to send */
	private final BlockingDeque<LightwaveRFCommand> queue = new LinkedBlockingDeque<LightwaveRFCommand>();
	/** Map of Integers so we can count retry attempts. */
	private final ConcurrentMap<LightwaveRfMessageId, Integer> retryCountMap = new ConcurrentHashMap<LightwaveRfMessageId, Integer>();

	/**
	 * Timeout for OK Messages - if we don't receive an ok in this time we will
	 * re-send. Set as short as you can without missing replies
	 */
	private final int timeoutForOkMessagesMs;
	/** LightwaveRF WIFI hub port. */
	private final int lightwaveWifiLinkTransmitPort;
	/** LightwaveRF WIFI hub IP Address or broadcast address to send messages to */
	private final InetAddress ipAddress;
	/** Socket to transmit messages */
	private final DatagramSocket transmitSocket;

	/** Boolean to indicate if we are running */
	private boolean running = true;

	public LightwaveRFSenderThread(DatagramSocket socket, String lightwaveWifiLinkIp,
			int lightwaveWifiLinkTransmitPort, int timeoutForOkMessagesMs) throws UnknownHostException {
		
		this.lightwaveWifiLinkTransmitPort = lightwaveWifiLinkTransmitPort;
		this.timeoutForOkMessagesMs = timeoutForOkMessagesMs;
		this.ipAddress = InetAddress.getByName(lightwaveWifiLinkIp);
		this.transmitSocket = socket;
	}

	/**
	 * Stop the LightwaveRFReseiver Will set running to false, add a stop
	 * message to the queue so that it stops when empty, close and set the
	 * socket to null
	 */
	public synchronized void stopRunning() {
		running = false;
		sendLightwaveCommand(STOP_MESSAGE);
	}

	/**
	 * Run thread, pulling off any items from the UDP commands buffer, then send
	 * across network
	 */
	@Override
	public void run() {
		try {
			LightwaveRFCommand commandToSend = queue.take();

			if (!commandToSend.equals(STOP_MESSAGE)) {
				CountDownLatch latch = new CountDownLatch(1);
				latchMap.putIfAbsent(commandToSend.getMessageId(), latch);
				retryCountMap.putIfAbsent(commandToSend.getMessageId(), ONE);
				
				netsendUDP(commandToSend);
				boolean unlatched = latch.await(timeoutForOkMessagesMs,
						TimeUnit.MILLISECONDS);
//				logger.info("Unlatched?" + unlatched);
				latchMap.remove(commandToSend.getMessageId());
				if (!unlatched) {
					Integer sendCount = retryCountMap.get(commandToSend.getMessageId());
					if (sendCount.intValue() >= MAX_RETRY_ATTEMPS) {
						logger.error("Unable to send message {} after {} attemps giving up",
								commandToSend.getLightwaveRfCommandString(),
								MAX_RETRY_ATTEMPS);
						return;
					}
					if(!running){
						logger.error("Not retrying message {} as we are stopping", commandToSend.getLightwaveRfCommandString());
						return; 
						
					}
					Integer newRetryCount = Integer.valueOf(sendCount.intValue() + 1);
					logger.info("Ok message not received for {}, retrying again. Retry count {}",
							commandToSend.getLightwaveRfCommandString(),
							newRetryCount);
					retryCountMap.put(commandToSend.getMessageId(), newRetryCount);
					queue.addFirst(commandToSend);
				}
				else{
					logger.info("Ok message received for {}", commandToSend.getLightwaveRfCommandString());
				}
				
			} else {
				logger.info("Stop message received");
			}
		} catch (InterruptedException e) {
			logger.error("Error waiting on queue", e);
		}
	}

	/**
	 * Add LightwaveRFCommand command to queue to send.
	 */
	public void sendLightwaveCommand(LightwaveRFCommand command) {
		try {
			if (running) {
				queue.put(command);
			} else {
				logger.info("Message not added to queue as we are shutting down Message[{}]");
			}
		} catch (InterruptedException e) {
			logger.error("Error adding command[{}] to queue Throwable {}",
					command, e);
		}
	}

	@Override
	public void okMessageReceived(LightwaveRfCommandOk command) {
		CountDownLatch latch = latchMap.get(command.getMessageId());
		if (latch != null) {
			latch.countDown();
		}
	}

	@Override
	public void roomDeviceMessageReceived(LightwaveRfRoomDeviceMessage message) {
		/*
		 * Do Nothing
		 */
	}

	@Override
	public void roomMessageReceived(LightwaveRfRoomMessage message) {
		/* Do Nothing */
	}

	@Override
	public void serialMessageReceived(LightwaveRfSerialMessage message) {
		/* Do Nothing */
	}

	@Override
	public void versionMessageReceived(LightwaveRfVersionMessage message) {
		// If we receive a vesion message we assume this is in response to our
		// registration message and attempt to unlatch the sending thread.
		CountDownLatch latch = latchMap
				.get(new LightwaveRfRegistrationMessageId());
		if (latch != null) {
			latch.countDown();
		}
	}

	@Override
	public void heatInfoMessageReceived(LightwaveRfHeatInfoRequest command) { 
		/* Do Nothing */
	}

	/**
	 * Send the UDP message
	 */
	private void netsendUDP(LightwaveRFCommand command) {
		try {
			logger.debug("Sending command[{}]",
					command.getLightwaveRfCommandString());
			byte[] sendData = new byte[1024];
			sendData = command.getLightwaveRfCommandString().getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData,
					sendData.length, ipAddress, lightwaveWifiLinkTransmitPort);
			transmitSocket.send(sendPacket);
		} catch (IOException e) {
			logger.error("Error sending command {}. Throwable {}", command, e);
		}
	}
}
