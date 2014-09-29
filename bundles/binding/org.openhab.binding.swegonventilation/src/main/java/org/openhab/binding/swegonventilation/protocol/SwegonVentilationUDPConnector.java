/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.swegonventilation.protocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Arrays;

import org.openhab.binding.swegonventilation.internal.SwegonVentilationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connector for UDP communication.
 * 
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class SwegonVentilationUDPConnector extends SwegonVentilationConnector {

	private static final Logger logger = LoggerFactory
			.getLogger(SwegonVentilationUDPConnector.class);

	int port = 9998;
	DatagramSocket socket = null;

	static final int MAX_PACKET_SIZE = 255;

	public SwegonVentilationUDPConnector(int port) {

		if (port > 0) {
			this.port = port;
		}
	}

	@Override
	public void connect() throws SwegonVentilationException {

		if (socket == null) {
			try {
				socket = new DatagramSocket(port);
				logger.debug("Swegon ventilation UDP message listener started");
			} catch (SocketException e) {
				throw new SwegonVentilationException(e);
			}
		}
	}

	@Override
	public void disconnect() throws SwegonVentilationException {

		if (socket != null) {
			socket.close();
			socket = null;
		}
	}

	@Override
	public byte[] receiveDatagram() throws SwegonVentilationException {

		try {

			if (socket == null) {
				socket = new DatagramSocket(port);
			}

			// Create a packet
			DatagramPacket packet = new DatagramPacket(new byte[MAX_PACKET_SIZE],
					MAX_PACKET_SIZE);

			// Receive a packet (blocking)
			socket.receive(packet);

			return Arrays.copyOfRange(packet.getData(), 0,
					packet.getLength() - 1);

		} catch (SocketException e) {

			throw new SwegonVentilationException(e);

		} catch (IOException e) {

			throw new SwegonVentilationException(e);
		}

	}
}
