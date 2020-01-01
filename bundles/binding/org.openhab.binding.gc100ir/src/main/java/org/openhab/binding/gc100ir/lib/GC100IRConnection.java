/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.gc100ir.lib;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Manages the connection with the TCP/IP socket for the specified device
 * attached to the GC-100.
 *
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
class GC100IRConnection {
    private InetAddress inetAddress;
    private String ipAddressString;
    private int port;
    private String deviceType;
    private Socket socket;
    private int module;
    private int connector;
    private boolean isConnected;

    /**
     * Constructor. Initializes the inetAddress, port, module, connector and
     * deviceType to local variables.
     *
     * @param inetAddress
     *            an Object of InetAddress
     * @param ipAddressString
     *            an String of InetAddress
     * @param port
     *            an integer value of port
     * @param module
     *            an integer value of module
     * @param connector
     *            an integer value of connector
     * @param deviceType
     *            a String value of deviceType
     */
    GC100IRConnection(InetAddress ipAddress, String ipAddressString, int port, int module, int connector,
            String deviceType) {
        this.inetAddress = ipAddress;
        this.ipAddressString = ipAddressString;
        this.port = port;
        this.module = module;
        this.connector = connector;
        this.deviceType = deviceType;
    }

    /**
     * Constructor. Initializes the inetAddress, port, module, connector and
     * deviceType to local variables.
     *
     * @param ipAddress
     *            an Object of InetAddress
     * @param ipAddressString
     *            String representation of the ip address
     * @param port
     *            an integer value of port
     * @param module
     *            an integer value of module
     * @param connector
     *            an integer value of connector
     * @param deviceType
     *            a String value of deviceType
     * @param socket
     *            an Object of Socket
     */
    GC100IRConnection(InetAddress ipAddress, String ipAddressString, int port, int module, int connector,
            String deviceType, Socket socket) {
        this.inetAddress = ipAddress;
        this.ipAddressString = ipAddressString;
        this.port = port;
        this.module = module;
        this.connector = connector;
        this.deviceType = deviceType;
        this.socket = socket;
        this.isConnected = true;
    }

    /**
     * Checks if GC100IRConnection is connected.
     *
     * @return true if connected false otherwise.
     */
    boolean isConnected() {
        return isConnected;
    }

    /**
     * Returns IP address in string format.
     *
     * @return an IP address string
     */
    String getIPAddressString() {
        return ipAddressString;
    }

    /**
     * Connect to the TCP/IP socket.
     *
     * @return a boolean whether connection is successful or not.
     * @throws IOException
     */
    boolean connect() throws IOException {
        if (isConnected) {
            return true;
        }

        socket = new Socket(inetAddress, port);

        if (socket.isConnected()) {
            isConnected = true;
            return true;
        }

        return false;
    }

    /**
     * Get IPAddress
     *
     * @return an Object of InetAddress
     */
    InetAddress getInetAddress() {
        return inetAddress;
    }

    /**
     * Get Port no.
     *
     * @return an integer value for portNo.
     */
    int getPort() {
        return port;
    }

    /**
     * Get Socket.
     *
     * @return an Object of Socket
     */
    Socket getSocket() {
        return socket;
    }

    /**
     * Get Module.
     *
     * @return an integer value of module
     */
    int getModule() {
        return module;
    }

    /**
     * Get connector.
     *
     * @return an integer value of connector
     */
    int getConnector() {
        return connector;
    }

    /**
     * Get the device type
     *
     * @return a String value of device type
     */
    String getDeviceType() {
        return deviceType;
    }
}
