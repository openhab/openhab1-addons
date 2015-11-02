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
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.openhab.binding.lightwaverf.internal.command.LightwaveRFCommand;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfCommandOk;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfHeatInfoRequest;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomDeviceMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfRoomMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfSerialMessage;
import org.openhab.binding.lightwaverf.internal.command.LightwaveRfVersionMessage;
import org.openhab.binding.lightwaverf.internal.exception.LightwaveRfMessageException;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRFMessageListener;
import org.openhab.binding.lightwaverf.internal.message.LightwaveRfStringMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents the LightwaveWifi Link and allows us to send and 
 * receive commands from the wifi link 
 * 
 * @author Neil Renaud
 * @since 1.8.0
 */
public class LightwaveRfWifiLink implements LightwaveRfStringMessageListener {
	private static final Logger logger = LoggerFactory.getLogger(LightwaveRfWifiLink.class);
	private static final int DELAY_BETWEEN_RECEIVES_MS = 10;

	private final CopyOnWriteArrayList<LightwaveRFMessageListener> listeners = new CopyOnWriteArrayList<LightwaveRFMessageListener>();

	private final LightwaverfConvertor messageConvertor;

	/* Socket which receives messages sent from another, source like the phone app, to the WIFI link */
	private final DatagramSocket receiveSocket;
	private final DatagramSocket receiveSocket2;
	/* Socket we transmit and receive from the wifi link */
	private final DatagramSocket transmitSocket;

	private final LightwaveRFReceiverThread receiverThread;
	private final LightwaveRFSenderThread senderThread;
	private final LightwaveRFReceiverThread receiverForWifiLinkThread;

	/** Time between commands so we don't flood the LightwaveRF hub */
	private final int timeBetweenCommandMs;
	/** Executor to keep executing this thread with a fixed delay */
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);
	
	public LightwaveRfWifiLink(
			String lightwaveWifiLinkIp, 
			int lightwaveWifiLinkTransmitPort, 
			int lightwaveWifiLinkReceivePort, 
			LightwaverfConvertor messageConvertor, 
			int timeBetweenCommandMs,
			int timeoutForOkMessagesMs) throws UnknownHostException, SocketException {

		this(lightwaveWifiLinkIp, 
				lightwaveWifiLinkTransmitPort,
				new DatagramSocket(lightwaveWifiLinkTransmitPort),
				new DatagramSocket(lightwaveWifiLinkReceivePort),
				new DatagramSocket(),
				messageConvertor,
				timeBetweenCommandMs,
				timeoutForOkMessagesMs);
	}
	
	// Visible for Testing only
	LightwaveRfWifiLink(String lightwaveWifiLinkIp, 
			int lightwaveWifiLinkTransmitPort,
			DatagramSocket receiveSocket,
			DatagramSocket receiveSocket2,
			DatagramSocket transmitSocket,
			LightwaverfConvertor messageConvertor,
			int timeBetweenCommandMs,
			int timeoutForOkMessagesMs)
					throws UnknownHostException, SocketException {
		
		this.messageConvertor = messageConvertor;
		this.timeBetweenCommandMs = timeBetweenCommandMs;
		
		this.receiveSocket = receiveSocket;
		this.receiveSocket2 = receiveSocket2;
		this.transmitSocket = transmitSocket;
		
		this.receiverThread = new LightwaveRFReceiverThread(receiveSocket);		
		this.senderThread = new LightwaveRFSenderThread(transmitSocket, lightwaveWifiLinkIp, lightwaveWifiLinkTransmitPort, timeoutForOkMessagesMs);
		this.receiverForWifiLinkThread = new LightwaveRFReceiverThread(receiveSocket2);		
		
		receiverThread.addListener(this);
		receiverForWifiLinkThread.addListener(this);
		
		addListener(senderThread);
		
	}
	
	/**
	 * Start the LightwaveRFReceiver Will set running true, initialise the
	 * socket and start the thread.
	 */
	public synchronized void start() {
		logger.info("Starting LightwaveRfWifiLink Connection");
		executor.scheduleWithFixedDelay(receiverThread, 0, DELAY_BETWEEN_RECEIVES_MS, TimeUnit.MILLISECONDS);
		executor.scheduleWithFixedDelay(receiverForWifiLinkThread, 0, DELAY_BETWEEN_RECEIVES_MS, TimeUnit.MILLISECONDS);
		executor.scheduleWithFixedDelay(senderThread, 0, timeBetweenCommandMs, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stop the LightwaveRFSender Will close the socket wait for the thread to
	 * exit and null the socket
	 */
	public synchronized void stop() {
		logger.info("Stopping LightwaveRfWifiLink Connection");
		receiverThread.stopRunning();
		receiverForWifiLinkThread.stopRunning();
		senderThread.stopRunning();
		executor.shutdownNow();
		receiveSocket.close();
		receiveSocket2.close();
		
		transmitSocket.close();
		logger.info("LightwaveRfWifiLink Connection Stopped");
	}
	
	/**
	 * Add LightwaveRFCommand command to queue to send.
	 */
	public void sendLightwaveCommand(LightwaveRFCommand command) {
		senderThread.sendLightwaveCommand(command);
	}	
	
	@Override
	public void messageReceived(String message) {
		try{
			LightwaveRFCommand command = messageConvertor.convertFromLightwaveRfMessage(message);
			switch (command.getMessageType()) {
			case OK:
				notifyOkListners((LightwaveRfCommandOk) command);
				break;
			case ROOM_DEVICE:
				notifyRoomDeviceListners((LightwaveRfRoomDeviceMessage) command);
				break;
			case ROOM:
				notifyRoomListners((LightwaveRfRoomMessage) command);
				break;
			case HEAT_REQUEST:
				notifyHeatRequest((LightwaveRfHeatInfoRequest) command);
				break;
			case SERIAL:
				notifySerialListners((LightwaveRfSerialMessage) command);
				break;
			case VERSION:
				notifyVersionListners((LightwaveRfVersionMessage) command);
				break;
			case NOT_PROCESSED:
			default:
				// Do nothing
				break;
			}
		} catch (LightwaveRfMessageException e) {
			logger.error("Error converting message: " + message);
		}
	}
	
	private void notifyRoomDeviceListners(LightwaveRfRoomDeviceMessage message) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.roomDeviceMessageReceived(message);
		}
	}

	private void notifyRoomListners(LightwaveRfRoomMessage message) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.roomMessageReceived(message);
		}
	}

	private void notifySerialListners(LightwaveRfSerialMessage message) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.serialMessageReceived(message);
		}
	}

	private void notifyOkListners(LightwaveRfCommandOk message) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.okMessageReceived(message);
		}
	}

	private void notifyVersionListners(LightwaveRfVersionMessage message) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.versionMessageReceived(message);
		}
	}

	private void notifyHeatRequest(LightwaveRfHeatInfoRequest command) {
		for (LightwaveRFMessageListener listener : listeners) {
			listener.heatInfoMessageReceived(command);
		}
	}	
	
	/**
	 * Add listener to be notified of messages received on the socket
	 * 
	 * @param listener
	 */
	public void addListener(LightwaveRFMessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove listener to stop being notified of messages being received on the
	 * socket.
	 * 
	 * @param listener
	 */
	public void removeListener(LightwaveRFMessageListener listener) {
		listeners.remove(listener);
	}	
}
