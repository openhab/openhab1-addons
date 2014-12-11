/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.ekey.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.fhooe.mc.schlgtwt.parser.HomePacket;
import at.fhooe.mc.schlgtwt.parser.MultiPacket;
import at.fhooe.mc.schlgtwt.parser.RarePacket;
import at.fhooe.mc.schlgtwt.parser.UniformPacket;

/**
 * This Class provides the DatagramSocket that listens for eKey packets on the network
 * This will run in a thread and can be interrupted by calling <code>stopListener()<code>
 * Before starting the thread initialization is required (mode, ip, port and deliminator)
 * @author Paul Schlagitweit
 * @since 1.5.0
 */
public class EKeyPacketReceiver implements Runnable {

	private static Logger log = LoggerFactory
			.getLogger(EKeyPacketReceiver.class);

	private final int buffersize = 128;
	private IEKeyListener listener;
	private boolean running;
	private DatagramSocket socket = null;
	private int mode;
	private InetAddress destIp = null;
	private UniformPacket ekeypacket;
	private String deliminator = "_"; // default value

	public EKeyPacketReceiver(IEKeyListener listener) {
			
			this.listener = listener;
	}

	/**
	 * Pass information about the connection to the receiver
	 * @param mode use constants defined in the <code>UniformPacket</code> class
	 * @param senderAddress
	 * @param port
	 * @param deliminator used by HOME and MULTI and defined in the UDP-converter config
	 */
	public void initializeReceiver(int mode, String senderAddress, int port,
			String deliminator) {

		this.mode = mode;

		if (senderAddress != "")
			try {
				destIp = InetAddress.getByName(senderAddress);
			} catch (UnknownHostException e1) {
				e1.printStackTrace();
			}
		else
			destIp = null; // no address specified

		if (deliminator != null)
			this.deliminator = deliminator;

		if (socket != null) // disconnect previous socket
			socket.disconnect();

		try { // create socket
			socket = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Stop the thread
	 */
	public void stopListener() {
		running = false;
		if(socket != null)
		socket.disconnect();
		socket = null;
	}

	@Override
	public void run() {
		running = true; // start loop
		if(socket == null)
			throw new IllegalStateException("Cannot access socket. You must call" +
					" call initializeListener(..) first!");

		byte[] lastpacket = null;
		DatagramPacket packet = new DatagramPacket(new byte[buffersize],
				buffersize);

		while (running) {

			ekeypacket = null;
			packet.setData(new byte[buffersize]);

			try { // wait for the packet
				socket.receive(packet);
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			// ignore packets from destinations other than the specified address
			// if destIp is not set ignore address check - this is not recommended but valid
			if (destIp == null || packet.getAddress().equals(destIp)) {

				lastpacket = packet.getData();

				try { // catch a possible parsing error

					switch (mode) {
					case UniformPacket.tHOME:
						ekeypacket = new HomePacket(deliminator, lastpacket);
						break;
					case UniformPacket.tMULTI:
						ekeypacket = new MultiPacket(deliminator, lastpacket);
						break;
					default: // default configuration is the rare packet
						ekeypacket = new RarePacket(lastpacket);
						break;

					}
				} catch (IllegalArgumentException e) {
					log.error("Error parsing packet", e);
				}
			}

			if (ekeypacket != null)
				listener.publishUpdate(ekeypacket);
			else
				log.debug("Received a packet that does not match the mode\n" +
						"you specified in the 'openhab.cfg'!");

		}
		
		log.debug("eKey Listener stopped!");

	}

}
