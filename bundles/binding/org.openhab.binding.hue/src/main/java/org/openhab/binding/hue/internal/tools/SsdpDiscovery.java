/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hue.internal.tools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that is able to discover services in the network by a SSDP
 * broadcast.
 * 
 * @author Roman Hartmann
 * @since 1.2.0
 */
public class SsdpDiscovery {

	static final Logger logger = LoggerFactory.getLogger(SsdpDiscovery.class);

	private final static String DISCOVER_MESSAGE = "M-SEARCH * HTTP/1.1\r\n"
			+ "HOST: 239.255.255.250:1900\r\n" + "MAN: \"ssdp:discover\"\r\n"
			+ "MX: 120\r\n" + "ST: ssdp:all\r\n";

	/**
	 * Determines the IP address of a service provided in the network. The
	 * service is detected by one or more keywords that have to appear in the
	 * answer message of the service to the SSDP discover broadcast. The
	 * keywords are not case sensitive. The timeout of the discovery is 120
	 * seconds.
	 * 
	 * @param keywords
	 *            The keywords that have to be found case insensitive in the
	 *            response of the service to be searched.
	 * @return The IP if a service with the keywords has been found, null
	 *         otherwise.
	 * @throws IOException
	 */
	public String findIpForResponseKeywords(String... keywords)
			throws IOException {
		try {
			logger.debug("Sending SSDP discover.");
			MulticastSocket socket = sendDiscoveryBroacast();
			logger.debug("Waiting for response.");
			return scanResposesForKeywords(socket, keywords);
		} catch (SocketTimeoutException e) {
			logger.debug("Timeout of request...");
		}
		return null;
	}

	/**
	 * Broadcasts a SSDP discovery message into the network to find provided
	 * services.
	 * 
	 * @return The Socket the answers will arrive at.
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws SocketException
	 * @throws UnsupportedEncodingException
	 */
	private MulticastSocket sendDiscoveryBroacast()
			throws UnknownHostException, IOException, SocketException,
			UnsupportedEncodingException {
		InetAddress multicastAddress = InetAddress.getByName("239.255.255.250");
		final int port = 1900;
		MulticastSocket socket = new MulticastSocket(port);
		socket.setReuseAddress(true);
		socket.setSoTimeout(130000);
		socket.joinGroup(multicastAddress);
		byte[] requestMessage = DISCOVER_MESSAGE.getBytes("UTF-8");
		DatagramPacket datagramPacket = new DatagramPacket(requestMessage,
				requestMessage.length, multicastAddress, port);
		socket.send(datagramPacket);
		return socket;
	}

	/**
	 * Scans all messages that arrive on the socket and scans them for the
	 * search keywords. The search is not case sensitive.
	 * 
	 * @param socket
	 *            The socket where the answers arrive.
	 * @param keywords
	 *            The keywords to be searched for.
	 * @return
	 * @throws IOException
	 */
	private String scanResposesForKeywords(MulticastSocket socket,
			String... keywords) throws IOException {
		// In the worst case a SocketTimeoutException raises
		socket.setSoTimeout(2000);
		do {
			logger.debug("Got an answer message.");
			byte[] rxbuf = new byte[8192];
			DatagramPacket packet = new DatagramPacket(rxbuf, rxbuf.length);
			socket.receive(packet);
			String foundIp = analyzePacket(packet, keywords);
			if (foundIp != null) {
				return foundIp;
			}
		} while (true);
	}

	/**
	 * Checks whether a packet does contain all given keywords case insensitive.
	 * If all keywords are contained the IP address of the packet sender will be
	 * returned.
	 * 
	 * @param packet
	 *            The data packet to be analyzed.
	 * @param keywords
	 *            The keywords to be searched for.
	 * @return The IP of the sender if all keywords have been found, null
	 *         otherwise.
	 * 
	 * @throws IOException
	 */
	private String analyzePacket(DatagramPacket packet, String... keywords)
			throws IOException {

		logger.debug("Analyzing answer message.");

		InetAddress addr = packet.getAddress();
		ByteArrayInputStream in = new ByteArrayInputStream(packet.getData(), 0,
				packet.getLength());
		String response = IOUtils.toString(in);

		boolean foundAllKeywords = true;

		for (String keyword : keywords) {
			foundAllKeywords &= response.toUpperCase().contains(
					keyword.toUpperCase());
		}

		if (foundAllKeywords) {
			logger.debug("Found matching answer.");
			return addr.getHostAddress();
		}

		logger.debug("Answer did not match.");
		return null;
	}

}
