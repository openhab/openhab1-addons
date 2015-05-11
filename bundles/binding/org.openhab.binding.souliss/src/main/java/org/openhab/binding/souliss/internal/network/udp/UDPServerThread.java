/**
 * Copyright (c) 2010-2015, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.souliss.internal.network.udp;

import java.io.*;
import java.net.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.openhab.binding.souliss.internal.network.typicals.SoulissNetworkParameter;
import org.openhab.binding.souliss.internal.network.typicals.SoulissTypicals;

/**
 * This class provide receive packet from network
 * 
 * @author Alessandro Del Pex
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class UDPServerThread extends Thread {

	// protected DatagramSocket socket = null;
	protected BufferedReader in = null;
	protected boolean bExit = false;
	UDPSoulissDecoder decoder = null;
	private static Logger logger = LoggerFactory
			.getLogger(UDPServerThread.class);

	public UDPServerThread(SoulissTypicals typicals) throws IOException {
		super();
		if (SoulissNetworkParameter.serverPort != null) {
			SoulissNetworkParameter.datagramsocket = new DatagramSocket(
					SoulissNetworkParameter.serverPort);
		} else {
			SoulissNetworkParameter.datagramsocket = new DatagramSocket();
		}

		decoder = new UDPSoulissDecoder(typicals);
		logger.info("Start UDPServerThread - Server in ascolto sulla porta "
				+ SoulissNetworkParameter.datagramsocket.getLocalPort());
	}

	public void run() {

		while (true) {
			try {
				byte[] buf = new byte[256];

				// receive request
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				SoulissNetworkParameter.datagramsocket.receive(packet);
				buf = packet.getData();
			
				// **************** DECODER ********************
				logger.debug("Packet received");
				logger.debug(MaCacoToString(buf));
				decoder.decodeVNetDatagram(packet);

			} catch (IOException e) {
				e.printStackTrace();
				logger.error(e.getMessage());
			}
		}
	}

	public DatagramSocket getSocket() {
		return SoulissNetworkParameter.datagramsocket;
	}

	public void closeSocket() {
		SoulissNetworkParameter.datagramsocket.close();
		bExit = true;
	}

	private String MaCacoToString(byte[] frame) {
		StringBuilder sb = new StringBuilder();
		sb.append("HEX: [");
		for (byte b : frame) {
			sb.append(String.format("%02X ", b));
		}
		sb.append("]");
		return sb.toString();
	}
}