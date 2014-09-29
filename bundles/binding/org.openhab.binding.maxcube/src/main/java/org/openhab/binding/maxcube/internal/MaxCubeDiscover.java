/**
 * Copyright (c) 2010-2013, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.maxcube.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Automatic UDP discovery of a MAX!Cube Lan Gateway on the local network. 
* 
* @author Marcel Verpaalen, based on UDP client code of Michiel De Mey 
* @since 1.4.0
*/
public final class MaxCubeDiscover {

	/**
	* Automatic UDP discovery of a MAX!Cube
	* @return if the cube is found, returns the IP address as a string. Otherwise returns null
	*/
	public final static String discoverIp () {
	
		String maxCubeIP = null;
		String maxCubeName = null;
		String rfAddress = null;
		
		Logger logger = LoggerFactory.getLogger(MaxCubeDiscover.class);
		
		//Find the MaxCube using UDP broadcast
		try {
			DatagramSocket bcSend = new DatagramSocket();
			bcSend.setBroadcast(true);

			byte[] sendData = "eQ3Max*\0**********I".getBytes();

			// Broadcast the message over all the network interfaces
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

				if (networkInterface.isLoopback() || !networkInterface.isUp()) {
					continue;
				}

				for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
					InetAddress broadcast = interfaceAddress.getBroadcast();
					if (broadcast == null) {
						continue;
					}

					// Send the broadcast package!
					try {
						DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 23272);
						bcSend.send(sendPacket);
					} catch (Exception e) {
						logger.debug(e.getMessage());
						logger.debug(Utils.getStackTrace(e));
				}

				logger.trace( "Request packet sent to: {} Interface: {}", broadcast.getHostAddress(),  networkInterface.getDisplayName());
			}
		}

		logger.trace( "Done looping over all network interfaces. Now waiting for a reply!");
		bcSend.close();

		DatagramSocket bcReceipt = new DatagramSocket(23272);
		bcReceipt.setReuseAddress(true);

		//Wait for a response
		byte[] recvBuf = new byte[15000];
		DatagramPacket receivePacket = new DatagramPacket(recvBuf, recvBuf.length);
		bcReceipt.receive(receivePacket);

		//We have a response
		logger.trace( "Broadcast response from server: {}", receivePacket.getAddress());

		//Check if the message is correct
		String message = new String(receivePacket.getData()).trim();

		if (message.startsWith("eQ3Max")) {
			
			maxCubeIP=receivePacket.getAddress().getHostAddress();
			maxCubeName=message.substring(0, 8);
			rfAddress=message.substring(8, 18);
			logger.debug("Found at: {}", maxCubeIP);
			logger.debug("Name    : {}", maxCubeName);
			logger.debug("Serial  : {}", rfAddress);
			logger.trace("Message : {}", message);	
		} else {
			logger.info("No Max!Cube gateway found on network");
		}

		//Close the port!
		bcReceipt.close();

		} catch (IOException ex) {
			logger.debug(ex.toString());
		}
		return maxCubeIP;
	}
}
