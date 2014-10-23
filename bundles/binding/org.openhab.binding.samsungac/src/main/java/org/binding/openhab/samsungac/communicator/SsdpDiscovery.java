/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.binding.openhab.samsungac.communicator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class that is able to discover Samsung Airconditioners in the network by a SSDP
 * broadcast.
 * 
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class SsdpDiscovery {

	static final Logger logger = LoggerFactory.getLogger(SsdpDiscovery.class);
	
	static final int PORT = 1900;
    static final String NEWLINE = "\r\n";

    private final static String DISCOVER_MESSAGE = "NOTIFY: NOTIFY * HTTP/1.1" + NEWLINE
            + "HOST: 239.255.255.250:" + PORT + NEWLINE
            + "CACHE-CONTROL: 'max-age=20" + NEWLINE
            + "SERVER: AIR CONDITIONER" + NEWLINE
            + "SPEC_VER: MSpec-1.00" + NEWLINE 
            + "SERVICE_NAME: ControlServer-MLib" + NEWLINE
            + "MESSAGE_TYPE: CONTROLLER_START" + NEWLINE;

    /**
     * Discovers one Samsung Air Conditioners in the network, and returns a Map
     * with all the details about it. We will use the IP-address and the MAC-address later
     * 
     * @return A Map of all values from the air conditioner
     * @throws Exception
     */
    public static Map<String, String> discover() {
    	Map<String, String> response = new HashMap<String, String>();
    	try {
    		sendNotify(DISCOVER_MESSAGE);
    		response.putAll(parseResponse(retrieveResponse()));
    	} catch (Exception e) {
    		logger.warn("Failed while trying to discover Samsung Air Conditioner", e);
    	}
        logger.debug("Got the following response from Samsung Air Conditioner: " + response);
        return response;
    }

    private static Map<String, String> parseResponse(String response) {
    	logger.debug("Response was:" + response);
        Map<String, String> device = new HashMap<String, String>();
        for (String element : response.split(NEWLINE))  {
        	if (element.contains(": "))
        		device.put(element.split(": ")[0], element.split(": ")[1]);
        }
        device.put("IP", device.get("LOCATION").split("//")[1].toString());
        return device;
    }

    static String retrieveResponse() throws Exception {
        String response = null;
        MulticastSocket recSocket = setUpSocket();

        int i = 0;
        while (response == null) {
            byte[] buf = new byte[2048];
            DatagramPacket input = new DatagramPacket(buf, buf.length);
            try {
                recSocket.receive(input);
                response = new String(input.getData());
            } catch (SocketTimeoutException e) {
                // TODO fix handling of time out
                if (i >= 2) break;
                i++;
            }
        }
        if (response == null) throw new Exception("No air conditioner found");
        return response;
    }

    private static MulticastSocket setUpSocket() throws IOException {
        MulticastSocket recSocket = new MulticastSocket(null);
        recSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), PORT));
        recSocket.setTimeToLive(10);
        recSocket.setSoTimeout(1000);
        recSocket.setBroadcast(true);
        return recSocket;
    }

    private static void sendNotify(String notifyMessage) throws IOException {
        MulticastSocket socket = new MulticastSocket(null);
        try {
        	socket.bind(new InetSocketAddress(InetAddress.getLocalHost().getCanonicalHostName(), PORT));
            byte[] data = notifyMessage.toString().getBytes();
            socket.send(new DatagramPacket(data, data.length, new InetSocketAddress(getBroadCastAddress(), PORT)));
        } catch (IOException e) {
            throw e;
        } finally {
            socket.disconnect();
            socket.close();
        }
    }

    private static InetAddress getBroadCastAddress() throws SocketException {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback())
                continue;    // Don't want to broadcast to the loopback interface
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) return broadcast;
            }
        }
        return null;
    }
	
}
