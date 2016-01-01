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
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.openhab.binding.lightwaverf.internal.message.LightwaveRfStringMessageListener;
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
public class LightwaveRFReceiverThread implements Runnable {
	private static final Logger logger = LoggerFactory.getLogger(LightwaveRFReceiverThread.class);
	private final CopyOnWriteArrayList<LightwaveRfStringMessageListener> listeners = new CopyOnWriteArrayList<LightwaveRfStringMessageListener>();
	private final DatagramSocket receiveSocket;
	private final Set<InetAddress> localIps;

	private boolean running = true;
	
	public LightwaveRFReceiverThread(DatagramSocket socket) throws SocketException, UnknownHostException {
		this.receiveSocket = socket;
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
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
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
	 * Stop the LightwaveRFSender Will close the socket wait for the thread to
	 * exit and null the socket
	 */
	public synchronized void stopRunning() {
		running = false;
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
			if(message != null){
				notifyMessage(message);
			}
		} catch (IOException e) {
			if (!(running == false && receiveSocket.isClosed())) {
				// If running isn't false and the socket isn't closed log the
				// error
				logger.error("Error receiving message", e);
			}
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
		logger.debug("Message received from: " + receivePacket.getAddress() + " message:" + receivedMessage);
		if (localIps.contains(receivePacket.getAddress())) {
			logger.debug("Own Message received and will be discarded: {}", receivedMessage);
			return null;
		}
		return receivedMessage;
	}

	/**
	 * Add listener to be notified of messages received on the socket
	 * 
	 * @param listener
	 */
	public void addListener(LightwaveRfStringMessageListener listener) {
		listeners.add(listener);
	}

	/**
	 * Remove listener to stop being notified of messages being received on the
	 * socket.
	 * 
	 * @param listener
	 */
	public void removeListener(LightwaveRfStringMessageListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify all listeners of a message
	 * 
	 * @param message
	 */
	private void notifyMessage(String message){
		for(LightwaveRfStringMessageListener messageListener : listeners){
			try{
				messageListener.messageReceived(message);
			}
			catch(Exception e){
				logger.error("Unexpected error when notifying listeners of this message[" + message + "] message hasn't been processed", e);
			}
		}
	}
}
