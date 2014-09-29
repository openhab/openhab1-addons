/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.anel.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

/**
 * Connector for UDP communication.
 * 
 * @since 1.6.0
 * @author paphko
 */
class AnelUDPConnector {

	/** Buffer for incoming UDP packages. */
	private static final int MAX_PACKET_SIZE = 512;

	/** Socket for receiving UDP packages. */
	private DatagramSocket socket = null;

	/** The device IP this connector is listening to / sends to. */
	final private String ip;

	/** The port this connector is listening to. */
	final private int receivePort;

	/** The port this connector is sending to. */
	final private int sendPort;

	/**
	 * Create a new connector to an Anel device via the given ip address and UDP
	 * ports.
	 * 
	 * @param ipAddress
	 *            The IP address / network name of the device.
	 * @param udpReceivePort
	 *            The UDP port to listen for packages.
	 * @param udpSendPort
	 *            The UDP port to send packages.
	 */
	AnelUDPConnector(String ipAddress, int udpReceivePort, int udpSendPort) {
		if (udpReceivePort <= 0)
			throw new IllegalArgumentException("Invalid udpReceivePort: " + udpReceivePort);
		if (udpSendPort <= 0)
			throw new IllegalArgumentException("Invalid udpSendPort: " + udpSendPort);
		if (ipAddress == null || ipAddress.isEmpty())
			throw new IllegalArgumentException("Missing ipAddress.");
		this.ip = ipAddress;
		this.receivePort = udpReceivePort;
		this.sendPort = udpSendPort;
	}

	/**
	 * Initialize socket connection to the UDP receive port.
	 * 
	 * @throws SocketException
	 */
	void connect() throws SocketException {
		if (socket == null) {
			socket = new DatagramSocket(receivePort);
		}
	}

	/**
	 * Close the socket connection.
	 */
	void disconnect() {
		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	/**
	 * This is a blocking call for receiving data from the specific UDP port.
	 * 
	 * @return The received data.
	 * @throws Exception
	 *             If an exception occurred.
	 */
	byte[] receiveDatagram() throws Exception {
		try {
			if (socket == null) {
				socket = new DatagramSocket(receivePort);
			}

			// Create a packet
			final DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE], MAX_PACKET_SIZE);

			// Receive a packet (blocking)
			socket.receive(packet);

			return Arrays.copyOfRange(packet.getData(), 0, packet.getLength() - 1);

		} catch (SocketException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}

	/**
	 * Send data to the specified address via the specified UDP port.
	 * 
	 * @param data
	 *            The data to send.
	 * @throws Exception
	 *             If an exception occurred.
	 */
	void sendDatagram(byte[] data) throws Exception {
		if (data == null || data.length == 0)
			throw new IllegalArgumentException("data must not be null or empty");
		try {
			final InetAddress ipAddress = InetAddress.getByName(ip);
			final DatagramPacket packet = new DatagramPacket(data, data.length, ipAddress, sendPort);

			final DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
			socket.close();
		} catch (SocketException e) {
			throw new Exception(e);
		} catch (UnknownHostException e) {
			throw new Exception("Could not resolve host: " + ip, e);
		} catch (IOException e) {
			throw new Exception(e);
		}
	}
}
