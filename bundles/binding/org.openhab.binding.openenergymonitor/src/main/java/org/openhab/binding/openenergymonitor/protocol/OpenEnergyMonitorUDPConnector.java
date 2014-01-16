/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.util.Arrays;

import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for UDP communication.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorUDPConnector extends OpenEnergyMonitorConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(OpenEnergyMonitorUDPConnector.class);

	static final int MAX_PACKET_SIZE = 255;

	int port = 9997;
	DatagramSocket socket = null;

	public OpenEnergyMonitorUDPConnector(int port) {

		if (port > 0) {
			this.port = port;
		}
	}

	@Override
	public void connect() throws OpenEnergyMonitorException {

		if (socket == null) {
			try {
				socket = new DatagramSocket(port);
				logger.debug("Open Energy Monitor UDP message listener started");

			} catch (SocketException e) {
				throw new OpenEnergyMonitorException(e);
			}
		}
	}

	@Override
	public void disconnect() throws OpenEnergyMonitorException {

		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	@Override
	public byte[] receiveDatagram() throws OpenEnergyMonitorException {

		try {

			if (socket == null) {
				socket = new DatagramSocket(port);
			}

			// Create a packet
			DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE],
					MAX_PACKET_SIZE);

			// Receive a packet (blocking)
			socket.receive(packet);

			String[] bytes = new String(Arrays.copyOfRange(packet.getData(), 0,
					packet.getLength() - 1)).split(" ");

			ByteBuffer bytebuf = ByteBuffer.allocate(bytes.length);

			for (int i = 0; i < bytes.length; i++) {
				if (bytes[i].isEmpty() == false) {
					byte b = (byte) Integer.parseInt(bytes[i]);
					bytebuf.put(b);
				}
			}

			return bytebuf.array();

		} catch (SocketException e) {

			throw new OpenEnergyMonitorException(e);

		} catch (IOException e) {

			throw new OpenEnergyMonitorException(e);
			
		} catch (NumberFormatException e) {
			
			throw new OpenEnergyMonitorException(e);
		}

	}
}
