/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.km200.internal.discovery;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashSet;

import org.openhab.binding.km200.internal.KM200Comm;
import org.openhab.binding.km200.internal.KM200Device;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.net.InetAddresses;

/**
 * The KM200DeviceDiscovery representing a device discovery service with its all capabilities
 *
 * @author Markus Eckhardt
 *
 * @since 1.9.0
 */

public class KM200DeviceDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(KM200DeviceDiscovery.class);

    private HashSet<InetAddress> listOfBroadcasts = new HashSet<InetAddress>();
    private HashSet<InetAddress> listOfKNDevices = new HashSet<InetAddress>();

    public KM200DeviceDiscovery() {
        determineLocalBrAddresses();
        discoveryKNDevices();
    }

    /**
     * This function discovers devices in network
     *
     */
    public HashSet<InetAddress> getKNDevices() {
        return listOfKNDevices;
    }

    /**
     * This function checks whether a network device is reachable
     *
     */
    private static boolean isReachable(InetAddress addr, int openPort, int timeOutMillis) {
        logger.debug("Testing: {} {} {}", addr, openPort, timeOutMillis);
        try {
            Socket soc = new Socket();
            soc.connect(new InetSocketAddress(addr.getHostAddress(), openPort), timeOutMillis);
            soc.close();
            return true;
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * This function checks whether a network interface is a private one
     *
     */
    private static boolean isPrivateV4Address(InetAddress ip) {
        int address = InetAddresses.coerceToInteger(ip);
        return (((address >>> 24) & 0xFF) == 10)
                || ((((address >>> 24) & 0xFF) == 172) && ((address >>> 16) & 0xFF) >= 16
                        && ((address >>> 16) & 0xFF) <= 31)
                || ((((address >>> 24) & 0xFF) == 192) && (((address >>> 16) & 0xFF) == 168));
    }

    /**
     * This function discovers the broadcast address of all private network interfaces
     *
     */
    private void determineLocalBrAddresses() {
        InetAddress broadcast = null;
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback()) {
                    continue; // Don't want to broadcast to the loopback interface
                }
                if (!networkInterface.isUp()) {
                    continue; // Don't want to broadcast a offline interface
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    if (!isPrivateV4Address(interfaceAddress.getAddress())) {
                        continue;
                    }
                    if (interfaceAddress.getNetworkPrefixLength() < 24) {
                        continue;
                    }
                    broadcast = interfaceAddress.getBroadcast();
                    if (broadcast == null) {
                        continue;
                    }
                    listOfBroadcasts.add(broadcast);
                }
            }
        } catch (SocketException e) {
            logger.error("Device autodetection fails, error: {}", e.getMessage());
        }
    }

    /**
     * This function discovers devices in network
     *
     */
    private void discoveryKNDevices() {
        KM200Device device = new KM200Device();
        KM200Comm comm = null;
        try {
            for (InetAddress broadcast : listOfBroadcasts) {
                byte[] ip = broadcast.getAddress();

                logger.debug("Send request packet to: {}", broadcast);
                for (int i = 1; i <= 254; i++) {
                    ip[3] = (byte) i;
                    InetAddress address = InetAddress.getByAddress(ip);
                    if (isReachable(address, 80, 100)) {
                        String output = address.toString().substring(1);
                        logger.debug("Found device in this network: {}", output);
                        device.setIP4Address(address.getHostAddress());
                        comm = new KM200Comm(device);
                        byte[] recData = comm.getDataFromService("/gateway/DateTime");
                        if (recData == null) {
                            logger.debug("Communication is not possible!");
                            continue;
                        }
                        if (recData.length == 0) {
                            logger.debug("No reply from KM200!");
                            continue;
                        }
                        logger.debug("Device is a KMXXX geteway: {}", address.getHostAddress());
                        listOfKNDevices.add(address);
                    }
                }
            }
        } catch (UnknownHostException e) {
            logger.error("Error in discovering of devices: {}", e.getMessage());
        }
    }
}
