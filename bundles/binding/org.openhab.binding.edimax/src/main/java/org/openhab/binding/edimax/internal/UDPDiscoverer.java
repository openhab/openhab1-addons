/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.edimax.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringUtils;

/**
 * Discovers EDIMAX devices by using the build-in UDP discovery. One sends a UDP
 * packet to port 20560 and Edimax device responds.
 *
 * @author Heinz
 * @since 1.9.0
 *
 */
public class UDPDiscoverer implements Discoverer {

    /**
     * Discovery Package.
     */
    byte[] DISCOVERY_BYTES = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, 0x45, 0x44,
            0x49, 0x4d, 0x41, 0x58, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, (byte) 0xa1, (byte) 0xff, 0x5e };

    private EdimaxDevice[] discover() throws SocketException, UnknownHostException, IOException {
        List<EdimaxDevice> discoveredDevices = new ArrayList<EdimaxDevice>();
        DatagramSocket serverSocket = null;
        try {
            serverSocket = new DatagramSocket(12346); // choose random port,
                                                      // because with empty
                                                      // port sometimes error
                                                      // occures.

            // send UDP broadcast
            InetAddress ipAddress = InetAddress.getByName("255.255.255.255");
            byte[] sendData = DISCOVERY_BYTES;
            String tempS = new String(sendData);
            System.out.println(tempS);
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, 20560);
            serverSocket.send(sendPacket);

            // receive
            serverSocket.setSoTimeout(1000 * 5);
            byte[] receiveData = new byte[1024];

            try {
                while (true) {
                    DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

                    serverSocket.receive(receivePacket);
                    String sentence = new String(receivePacket.getData());

                    if (!StringUtils.isEmpty(sentence) && sentence.contains("EDIMAX")) {
                        byte[] mac = new byte[6];
                        System.arraycopy(receivePacket.getData(), 0, mac, 0, 6);

                        String encodedMAC = Hex.encodeHexString(mac).toUpperCase();
                        InetAddress discoveredIp = receivePacket.getAddress();

                        EdimaxDevice dev = new EdimaxDevice();
                        dev.setIp(discoveredIp.getHostAddress());
                        dev.setMac(encodedMAC);

                        discoveredDevices.add(dev);
                    }

                }
            } catch (SocketTimeoutException e) {
                // intended to happen
            }
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
        return discoveredDevices.toArray(new EdimaxDevice[discoveredDevices.size()]);
    }

    @Override
    public EdimaxDevice[] discoverDevices() throws DiscoveryException {
        try {
            return discover();
        } catch (IOException e) {
            throw new DiscoveryException(e);
        }
    }

    public static void main(String[] args) throws DiscoveryException {
        UDPDiscoverer udpDiscoverer = new UDPDiscoverer();
        for (EdimaxDevice dev : udpDiscoverer.discoverDevices()) {
            System.out.println("IP: " + dev.getIp() + ", mac:" + dev.getMac());
        }
    }

}
