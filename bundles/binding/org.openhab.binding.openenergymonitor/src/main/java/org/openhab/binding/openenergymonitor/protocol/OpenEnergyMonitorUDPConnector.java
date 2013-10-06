/**
 * openHAB, the open Home Automation Bus.
 * Copyright (C) 2010-2012, openHAB.org <admin@openhab.org>
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses>.
 *
 * Additional permission under GNU GPL version 3 section 7
 *
 * If you modify this Program, or any covered work, by linking or
 * combining it with Eclipse (or a modified version of that library),
 * containing parts covered by the terms of the Eclipse Public License
 * (EPL), the licensors of this Program grant you additional permission
 * to convey the resulting work.
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

	int port = 9997;
	DatagramSocket socket = null;

	public OpenEnergyMonitorUDPConnector(int port) {

		logger.debug("Open Energy Monitor UDP message listener started");
		this.port = port;
	}

	@Override
	public void connect() throws OpenEnergyMonitorException {

		if (socket == null) {
			try {
				socket = new DatagramSocket(port);
			} catch (SocketException e) {
				throw new OpenEnergyMonitorException(e);
			}
		}
	}

	@Override
	public void disconnect() throws OpenEnergyMonitorException {

		if (socket == null) {
			socket.close();
			socket = null;
		}
	}

	@Override
	public byte[] receiveDatagram() throws OpenEnergyMonitorException {

		final int PACKETSIZE = 255;

		try {

			if (socket == null) {
				socket = new DatagramSocket(port);
			}

			// Create a packet
			DatagramPacket packet = new DatagramPacket(new byte[PACKETSIZE],
					PACKETSIZE);

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
		}

	}
}
