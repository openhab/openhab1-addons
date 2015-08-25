/**
 * Copyright (c) 2010-2015, openHAB.org and others.
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
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper class that is able to discover Samsung Airconditioners in the network by a SSDP
 * broadcast.
 * 
 * @author Stein Tore Tøsse
 * @since 1.6.0
 */
public class SsdpDiscovery {

	static final int PORT = 1900;
    static final String NEWLINE = "\r\n";

    private final static String DISCOVER_MESSAGE = "NOTIFY * HTTP/1.1" + NEWLINE
            + "HOST: 239.255.255.250:" + PORT + NEWLINE
            + "CACHE-CONTROL: max-age=20" + NEWLINE
            + "SERVER: AIR CONDITIONER" + NEWLINE
            + "SPEC_VER: MSpec-1.00" + NEWLINE 
            + "SERVICE_NAME: ControlServer-MLib" + NEWLINE
            + "MESSAGE_TYPE: CONTROLLER_START" + NEWLINE;

    public static void main(String args[]) {
        Map<String, Map<String, String>> resp = discover();
        System.out.println("\nGot response from possible air conditioner(s):");
        for(Map<String, String> ac : resp.values()) {
            if (ac.get("IP") != null)
                System.out.println(ac);
        }
    }
    
    /**
     * Discovers Samsung Air Conditioners in the network, and returns a Map
     * with all the details about them. We will use the IP-address and the MAC-address later
     * 
     * @return A Map of all values from the air conditioner
     * @throws Exception
     */
    public static Map<String, Map<String, String>> discover() {
        Map<String,Map<String,String>> response = new HashMap<String, Map<String, String>>();
    	System.out.println("Sending multibroadcast to all possible network interfaces...");
        try {
            List<InetAddress> ia = getBroadCastAddress();
            for(InetAddress i : ia) {
    	       try {
                  System.out.println("Broadcasting to " + i);
    		      sendNotify(DISCOVER_MESSAGE, i);
                  Map<String, Map<String,String>> resp = retrieveResponse();
		          response.putAll(resp);
    	       } catch(IOException ioe) {
                 System.out.println("Could not broadcast to " + i + ", moving on to next broadcast address");
               } catch (Exception e) {
                    //No problem, let's try next InetAddress
    	       }
               
            }
        } catch (Exception e) {
            //System.out.println("Could not get broadcast addresses..returning " + e.printStackTrace());
        } 
        return response;
    }

    private static Map<String, String> parseResponse(String response) {
    	Map<String, String> device = new HashMap<String, String>();
    	
        if (response == null) return device;
    	for (String element : response.split(NEWLINE))  {
        	if (element.contains(": "))
        		device.put(element.split(": ")[0], element.split(": ")[1]);
        }
        if (device.size() > 0) {
            String location = device.get("LOCATION");
            if (location != null && location.length() > 1 && location.contains("//")) {
                device.put("IP", device.get("LOCATION").split("//")[1].toString());
            } else {
                //If no LOCATION, then no Air Conditioner
                device.clear();
            }
        }
        return device;
    }

    static Map<String, Map<String, String>> retrieveResponse() throws Exception {
        String response = null;
        Map<String, Map<String, String>> result = new HashMap<String, Map<String, String>>();
        MulticastSocket recSocket = setUpSocket();

        int i = 0;
        System.out.print("Retrieving response");
        while (i < 10) {
            byte[] buf = new byte[2048];
            DatagramPacket input = new DatagramPacket(buf, buf.length);
            try {
                System.out.print(".");
                recSocket.receive(input);
                response = new String(input.getData());
                Map<String, String> parsedResponse = parseResponse(response);
                result.put(parsedResponse.get("IP"), parsedResponse);
            } catch (SocketTimeoutException e) {
            	if (i >= 10) break;
                i++;
            }
        }
        System.out.print("\r\n");
        System.out.println("Response retrieved: " + result);
        return result;
    }

    private static MulticastSocket setUpSocket() throws IOException {
        MulticastSocket recSocket = new MulticastSocket(null);
        recSocket.bind(new InetSocketAddress(InetAddress.getByName("0.0.0.0"), PORT));
        recSocket.setTimeToLive(10);
        recSocket.setSoTimeout(1000);
        recSocket.setBroadcast(true);
        return recSocket;
    }

    private static void sendNotify(String notifyMessage, InetAddress ia) throws Exception {
        MulticastSocket socket = new MulticastSocket(null);
        try {
        	socket.bind(new InetSocketAddress(PORT));
            socket.setTimeToLive(4);
            byte[] data = notifyMessage.toString().getBytes();
            socket.send(new DatagramPacket(data, data.length, new InetSocketAddress(ia, PORT)));   
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
         finally {
            socket.disconnect();
            socket.close();
        }
    }

    private static List<InetAddress> getBroadCastAddress() throws Exception {
        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        List<InetAddress> addresses = new ArrayList<InetAddress>();
        addresses.add(InetAddress.getByName("255.255.255.255"));
        while (interfaces.hasMoreElements()) {
            NetworkInterface networkInterface = interfaces.nextElement();
            if (networkInterface.isLoopback() || !networkInterface.supportsMulticast())
                continue;    // Don't want to broadcast to the loopback interface
            for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                InetAddress broadcast = interfaceAddress.getBroadcast();
                if (broadcast != null) {
                    addresses.add(broadcast);
                }
            }
        }
        return addresses;
    }
}
