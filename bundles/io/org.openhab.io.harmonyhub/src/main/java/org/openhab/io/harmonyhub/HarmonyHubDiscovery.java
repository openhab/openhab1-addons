/**
 * Copyright (c) 2014-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.harmonyhub;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link HarmonyHubDiscovery} class discovers Logitech Harmony Hubs on the
 * network by broadcasting a discovery string to port 5224 on the
 * broadcast address. Hubs respond by making a TCP connection back to
 * the IP address that our packet was sent from and on the port we
 * advertised as part of the original discovery request.
 *
 * @author Dan Cunningham - Initial contribution
 *
 */
public class HarmonyHubDiscovery {

    private Logger logger = LoggerFactory.getLogger(HarmonyHubDiscovery.class);

    // notice the port appended to the end of the string
    private static final String DISCO_STRING = "_logitech-reverse-bonjour._tcp.local.\n%d";

    private static final int DISCO_PORT = 5224;

    static protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private ScheduledFuture<?> broadcastFuture;
    private ScheduledFuture<?> timeoutFuture;
    private ServerSocket serverSocket;
    private HarmonyServer server;
    private int timeout;
    private boolean running;
    private String optionalHost;

    private List<HarmonyHubDiscoveryListener> listeners = new CopyOnWriteArrayList<HarmonyHubDiscoveryListener>();

    /**
     *
     * @param timeout
     *            how long we discover for
     */
    public HarmonyHubDiscovery(int timeout, String optionalHost) {
        this.timeout = timeout;
        this.optionalHost = optionalHost;
        running = false;
        listeners = new LinkedList<HarmonyHubDiscoveryListener>();
    }

    /**
     * Adds a HarmonyHubDiscoveryListener
     *
     * @param listener
     */
    public void addListener(HarmonyHubDiscoveryListener listener) {
        listeners.add(listener);
    }

    /**
     * Removes a HarmonyHubDiscoveryListener
     *
     * @param listener
     */
    public void removeListener(HarmonyHubDiscoveryListener listener) {
        listeners.remove(listener);
    }

    /**
     * Starts discovery for Harmony Hubs
     */
    public synchronized void startDiscovery() {
        if (running) {
            return;
        }

        try {
            serverSocket = new ServerSocket(0);
            logger.debug("Creating Harmony server on port {}", serverSocket.getLocalPort());
            server = new HarmonyServer(serverSocket);
            server.start();

            broadcastFuture = scheduler.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    sendDiscoveryMessage(String.format(DISCO_STRING, serverSocket.getLocalPort()));
                }
            }, 0, 2, TimeUnit.SECONDS);

            timeoutFuture = scheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    stopDiscovery();
                }
            }, timeout, TimeUnit.SECONDS);

            running = true;
        } catch (IOException e) {
            logger.error("Could not start Harmony discovery server ", e);
        }
    }

    /**
     * Stops discovery of Harmony Hubs
     */
    public synchronized void stopDiscovery() {
        if (broadcastFuture != null) {
            broadcastFuture.cancel(true);
        }
        if (timeoutFuture != null) {
            broadcastFuture.cancel(true);
        }
        if (server != null) {
            server.setRunning(false);
        }
        try {
            serverSocket.close();
        } catch (Exception e) {
            logger.error("Could not stop harmony discovery socket", e);
        }
        for (HarmonyHubDiscoveryListener listener : listeners) {
            listener.hubDiscoveryFinished();
        }
        running = false;

    }

    /**
     * Send broadcast message over all active interfaces
     *
     * @param discoverString
     *            String to be used for the discovery
     */
    private void sendDiscoveryMessage(String discoverString) {
        DatagramSocket bcSend = null;
        try {
            bcSend = new DatagramSocket();
            bcSend.setBroadcast(true);

            byte[] sendData = discoverString.getBytes();

            // Broadcast the message over all the network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }
                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress[] broadcast = new InetAddress[3];
                    broadcast[0] = InetAddress.getByName("224.0.0.1");
                    broadcast[1] = InetAddress.getByName("255.255.255.255");
                    broadcast[2] = interfaceAddress.getBroadcast();
                    broadcast[3] = InetAddress.getByName(optionalHost);
                    for (InetAddress bc : broadcast) {
                        // Send the broadcast package!
                        if (bc != null && !bc.isLoopbackAddress()) {
                            try {
                                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, bc,
                                        DISCO_PORT);
                                bcSend.send(sendPacket);
                            } catch (IOException e) {
                                logger.error("IO error during HarmonyHub discovery: {}", e.getMessage());
                            } catch (Exception e) {
                                logger.error(e.getMessage(), e);
                            }
                            logger.trace("Request packet sent to: {} Interface: {}", bc.getHostAddress(),
                                    networkInterface.getDisplayName());
                        }
                    }
                }
            }

        } catch (IOException e) {
            logger.debug("IO error during HarmonyHub discovery: {}", e.getMessage());
        } finally {
            try {
                if (bcSend != null) {
                    bcSend.close();
                }
            } catch (Exception e) {
                // Ignore
            }
        }

    }

    /**
     * Server which accepts TCP connections from Harmony Hubs during the discovery process
     *
     * @author Dan Cunningham - Initial contribution
     *
     */
    private class HarmonyServer extends Thread {
        private ServerSocket serverSocket;
        private boolean running;
        private List<String> responses = new ArrayList<String>();

        public HarmonyServer(ServerSocket serverSocket) {
            this.serverSocket = serverSocket;
            running = true;
        }

        @Override
        public void run() {
            while (running) {
                Socket socket = null;
                try {
                    socket = serverSocket.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String input;
                    while ((input = in.readLine()) != null) {
                        if (!running) {
                            break;
                        }
                        logger.trace("READ {}", input);
                        String propsString = input.replaceAll(";", "\n");
                        propsString = propsString.replaceAll(":", "=");
                        Properties props = new Properties();
                        props.load(new StringReader(propsString));
                        if (!responses.contains(props.getProperty("friendlyName"))) {
                            responses.add(props.getProperty("friendlyName"));
                            HarmonyHubDiscoveryResult result = new HarmonyHubDiscoveryResult(props.getProperty("ip"),
                                    props.getProperty("accountId"), props.getProperty("uuid"),
                                    props.getProperty("host_name").replaceAll("[^A-Za-z0-9\\-_]", ""),
                                    props.getProperty("friendlyName"));
                            for (HarmonyHubDiscoveryListener listener : listeners) {
                                listener.hubDiscovered(result);
                            }
                        }
                    }
                } catch (IOException e) {
                    if (running) {
                        logger.debug("Error connecting with found hub", e);
                    }
                } finally {
                    try {
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException e) {
                        logger.warn("could not close socket", e);
                    }
                }
            }
        }

        public void setRunning(boolean running) {
            this.running = running;
        }
    }
}
