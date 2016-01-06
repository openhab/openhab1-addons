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
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
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
public class LightwaveRFReceiver implements Runnable {
	private static final Logger logger = LoggerFactory
			.getLogger(LightwaveRFReceiver.class);
	private static final int DELAY_BETWEEN_RECEIVES_MS = 10;

	private final CopyOnWriteArrayList<LightwaveRFMessageListener> listeners = new CopyOnWriteArrayList<LightwaveRFMessageListener>();
	private final ScheduledExecutorService executor = Executors
			.newScheduledThreadPool(1);

	private final LightwaverfConvertor messageConvertor;
	private final DatagramSocket receiveSocket;
	private final Set<InetAddress> localIps;

	private boolean running = false;

	public LightwaveRFReceiver(LightwaverfConvertor messageConvertor, int port)
			throws SocketException {
		this.messageConvertor = messageConvertor;
		this.receiveSocket = new DatagramSocket(port);
		localIps = getIpAddresses();
	}

	/**
	 * Gets a list of INetAddresses for this server. Allowing us to filter out
	 * broadcast packets we receive that we originally sent
	 * 
	 * @return
	 */
	private Set<InetAddress> getIpAddresses() {
		Set<InetAddress> ips = new LinkedHashSet<InetAddress>();
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface
					.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();
					ips.add(addr);
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}
		return ips;
	}

	/**
	 * Start the LightwaveRFReceiver Will set running true, initialise the
	 * socket and start the thread.
	 */
	public synchronized void start() {
		logger.info("Starting LightwaveRFReceiver");
		running = true;
		executor.scheduleWithFixedDelay(this, 0, DELAY_BETWEEN_RECEIVES_MS,
				TimeUnit.MILLISECONDS);
	}

	/**
	 * Stop the LightwaveRFSender Will close the socket wait for the thread to
	 * exit and null the socket
	 */
	public synchronized void stop() {
		logger.info("Stopping LightwaveRFReceiver");
		running = false;
		executor.shutdownNow();
		receiveSocket.close();
		logger.info("LightwaveRFReceiver Stopped");
	}

	/**
	 * Run method, this will listen to the socket and receive messages. The
	 * blocking is stopped when the socket is closed.
	 */
	@Override
	public void run() {
		String message = null;
		try {
			message = receiveUDP();
			LightwaveRFCommand command = messageConvertor
					.convertFromLightwaveRfMessage(message);
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
			default:
				break;
			}
		} catch (IOException e) {
			if (!(running == false && receiveSocket.isClosed())) {
				// If running isn't false and the socket isn't closed log the
				// error
				logger.error("Error receiving message", e);
			}
		} catch (LightwaveRfMessageException e) {
			logger.error("Error converting message: " + message);
		}
	}

	/**
	 * Receive the next UDP packet on the socket
	 * 
	 * @return
	 * @throws IOException
	 */
	private String receiveUDP() throws IOException {
		String receivedMessage = "";
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData,
				receiveData.length);
		receiveSocket.receive(receivePacket);
		receivedMessage = new String(receivePacket.getData(), 0,
				receivePacket.getLength());
		if (localIps.contains(receivePacket.getAddress())) {
			logger.debug("Own Message received and will be discarded: {}",
					receivedMessage);
			return null;
		}
		logger.debug("Message received: " + receivedMessage);
		return receivedMessage;
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

	/**
	 * Notify all listeners of a message
	 * 
	 * @param message
	 */

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
}
