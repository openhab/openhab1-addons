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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.openhab.binding.gc100ir.internal.response.GC100IRCommand;
import org.openhab.binding.gc100ir.internal.response.GC100IRResponseCommandCodeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles the discovery and discarding of the GC-100 devices. It implements
 * IGC100IRControlPoint. This class follows the Singleton pattern, hence the
 * contructor is private. The singleton instance can be instantiated by using the
 * getInstance() method
 *
 * @author Parikshit Thakur & Team
 * @since 1.9.0
 */
public class GC100IRControlPoint implements IGC100IRControlPoint {
    /**
     * Port number to connect to device.
     */
    public static final int CONNECT_PORT = 4998;
    private static final String DEVICE_TYPE_IR = "IR";
    public static final String GC_100_QUERY_MESSAGE_GET_DEVICES = "getdevices\r";
    private static final String END_LIST_DEVICES = "endlistdevices";

    private List<GC100IRConnection> socketList;
    private Map<GC100IRDevice, Long> deviceLastDisTimeMap;
    private Map<GC100IRDevice, Map<String, String>> deviceGCPropsMap;

    private static GC100IRControlPoint gc100ControlPoint;

    private static Logger logger = LoggerFactory.getLogger(GC100IRControlPoint.class);

    /**
     * Returns an Instance of GC100IRControlPoint. Singleton Pattern.
     *
     * @return an Object of GC100IRControlPoint
     */
    public static GC100IRControlPoint getInstance() {
        if (gc100ControlPoint == null) {
            gc100ControlPoint = new GC100IRControlPoint();
        }

        return gc100ControlPoint;
    }

    private GC100IRControlPoint() {
        deviceGCPropsMap = new LinkedHashMap<GC100IRDevice, Map<String, String>>();
        deviceLastDisTimeMap = new HashMap<GC100IRDevice, Long>();
        socketList = new ArrayList<GC100IRConnection>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean connectTarget(String gc100host, int module, int connector) {
        if (gc100host == null || module <= 0 || connector <= 0) {
            logger.warn("hostname, module or connector is invalid");
            return false;
        }

        Set<GC100IRDevice> deviceSet = deviceLastDisTimeMap.keySet();

        if ((deviceSet == null) || (deviceSet.size() == 0)) {
            logger.warn("deviceSet is null or its size is 0.");
            return false;
        }

        GC100IRDevice gcDevice = null;

        for (GC100IRDevice gcDev : deviceSet) {
            String ipOfDevice = gcDev.getIPAddressString();

            if (ipOfDevice.equals(gc100host)) {
                if ((module != gcDev.getModule()) || (connector != gcDev.getConnector())) {
                    continue;
                }

                gcDevice = gcDev;
                break;
            }
        }

        if (gcDevice == null) {
            logger.warn("gcDevice is null.");
            return false;
        }

        GC100IRConnection tcpIpSoc = gcDevice.getTCPIPSocket();

        if (tcpIpSoc != null) {
            if (!tcpIpSoc.isConnected()) {
                try {
                    tcpIpSoc.connect();
                    return true;
                } catch (IOException e) {
                    logger.error("IOException : ", e);
                }
            }
        }

        InetAddress ipAddress;

        try {
            ipAddress = gcDevice.getInetAddress();
        } catch (UnknownHostException e) {
            logger.warn("UnknownHostException : ", e);
            return false;
        }

        String deviceType = gcDevice.getConnectorType();

        Socket socket = null;

        try {
            synchronized (socketList) {
                for (GC100IRConnection tcpIpSocket : socketList) {
                    if (tcpIpSocket == null) {
                        continue;
                    }

                    InetAddress tcpInetAddress = tcpIpSocket.getInetAddress();
                    int tcpPort = tcpIpSocket.getPort();

                    if (tcpInetAddress.equals(ipAddress) && (tcpPort == CONNECT_PORT)) {
                        String tcpIPString = tcpIpSocket.getInetAddress().toString();

                        if (tcpIPString.startsWith("/")) {
                            tcpIPString = tcpIPString.substring(1);
                        }

                        if (gc100host != null && gc100host.equals(tcpIPString) && (module == tcpIpSocket.getModule())
                                && (connector == tcpIpSocket.getConnector())) {

                            if (!tcpIpSocket.isConnected()) {
                                tcpIpSocket.connect();
                            }

                            return true;
                        }

                        if (socket == null) {
                            if (!tcpIpSocket.isConnected()) {
                                tcpIpSocket.connect();
                            }

                            socket = tcpIpSocket.getSocket();
                        }
                    }
                }
            }

            String gchost = null;
            if (ipAddress != null && ipAddress.toString().startsWith("/")) {
                gchost = ipAddress.toString().substring(1);
            } else if (ipAddress != null) {
                gchost = ipAddress.toString();
            }

            if (socket != null) {
                GC100IRConnection tcpIpSocket = new GC100IRConnection(ipAddress, gchost, CONNECT_PORT, module,
                        connector, deviceType, socket);

                synchronized (FLOW_CONTROL_VALUE_FLOW_HARDWARE) {
                    socketList.add(tcpIpSocket);
                }

                gcDevice.setTCPIPSocket(tcpIpSocket);
                return true;
            } else {
                GC100IRConnection tcpIpSocket = new GC100IRConnection(ipAddress, gchost, CONNECT_PORT, module,
                        connector, deviceType);

                if (tcpIpSocket.connect()) {
                    synchronized (FLOW_CONTROL_VALUE_FLOW_HARDWARE) {
                        socketList.add(tcpIpSocket);
                    }

                    gcDevice.setTCPIPSocket(tcpIpSocket);
                    return true;
                }
            }

        } catch (UnknownHostException e) {
            logger.error("UnknownHostException :", e);
        } catch (IOException e) {
            logger.error("IOException :", e);
        }

        return false;
    }

    /**
     * Used to check TCP/IP socket is connected or not.
     *
     * @param ipAddress
     * @return true if socket is connected with ipAddress
     */
    public boolean isGC100ItemConnected(String ipAddress) {
        synchronized (socketList) {
            for (GC100IRConnection gc100IRConnection : socketList) {
                if (gc100IRConnection == null) {
                    continue;
                }

                String tcpIpString = gc100IRConnection.getInetAddress().toString();
                if (tcpIpString.startsWith("/")) {
                    tcpIpString = tcpIpString.substring(1);
                }
                if (ipAddress.equals(tcpIpString)) {
                    return gc100IRConnection.isConnected();
                }
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnectTarget(String ipAddress, int module, int connector) {
        Set<GC100IRDevice> deviceSet = null;

        synchronized (deviceLastDisTimeMap) {
            deviceSet = deviceLastDisTimeMap.keySet();
        }

        if ((deviceSet == null) || (deviceSet.size() == 0)) {
            return;
        }

        for (GC100IRDevice gcDevice : deviceSet) {
            String ipAddressOfDevice = gcDevice.getIPAddressString();
            if (ipAddressOfDevice.equals(ipAddress)) {
                deviceLastDisTimeMap.remove(gcDevice);
            }
        }
    }

    /**
     * Returns GC100IRDevice instance.
     *
     * @param ipAddress
     * @param module
     * @param connector
     * @return GC100Device object
     */
    private GC100IRDevice getGCDevice(String ipAddress, int module, int connector) {

        if ((ipAddress == null) || (module == 0) || (connector == 0)) {
            return null;
        }

        synchronized (deviceLastDisTimeMap) {
            for (GC100IRDevice device : deviceLastDisTimeMap.keySet()) {
                if (device == null) {
                    continue;
                }

                if (ipAddress.equals(device.getIPAddressString()) && module == device.getModule()
                        && connector == device.getConnector()) {
                    return device;
                }
            }
        }

        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized GC100IRCommand doAction(String ipAddressString, int module, int connector, String code) {

        if (code == null) {
            return null;
        }

        GC100IRDevice device = getGCDevice(ipAddressString, module, connector);

        if (device == null) {
            logger.warn("Unable to get GC 100 Device for IP Address '{}', module '{}' and connector '{}'.",
                    ipAddressString, module, connector);
            return null;
        }

        GC100IRConnection tcpIPSocket = device.getTCPIPSocket();

        if (tcpIPSocket == null) {
            if (connectTarget(ipAddressString, module, connector)) {
                device = getGCDevice(ipAddressString, module, connector);

                if (device == null) {
                    logger.warn("Unable to get GC 100 Device for IP Address '{}', module '{}' and connector '{}'.",
                            ipAddressString, module, connector);
                    return null;
                }

                tcpIPSocket = device.getTCPIPSocket();

                if (tcpIPSocket == null) {
                    logger.warn("Unable to get TCP IP Socket for IP Address '{}', module '{}' and connector '{}'.",
                            ipAddressString, module, connector);
                    return null;
                }
            }

        }

        if (tcpIPSocket == null) {
            logger.warn("Unable to get TCP IP Socket for IP Address '{}', module '{}' and connector '{}'.",
                    ipAddressString, module, connector);
            return null;
        }

        String deviceType = tcpIPSocket.getDeviceType();

        if (deviceType == null) {
            logger.warn("Unable to get Device Type for IP Address '{}', module '{}' and connector '{}'.",
                    ipAddressString, module, connector);
            return null;
        }

        try {
            if (deviceType.equals(DEVICE_TYPE_IR)) {
                Socket socket = tcpIPSocket.getSocket();

                if (socket == null) {
                    logger.warn("Unable to get Socket for IP Address '{}', module '{}' and connector '{}'.",
                            ipAddressString, module, connector);
                    return null;
                }

                if (!tcpIPSocket.isConnected()) {
                    tcpIPSocket.connect();
                }

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (in.ready()) {
                    in.read();
                }

                out.write(code.getBytes());

                StringBuilder response = new StringBuilder();

                int count = 0;
                while (!in.ready()) {
                    count++;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        logger.warn("InterruptedException", e);
                    }

                    if (count > 20) {
                        return null;
                    }
                } // Wait till data available to input stream

                while (true) {
                    response.append((char) in.read());

                    if (!in.ready()) {
                        break;
                    }
                }

                return responseOfDevice(response.toString());
            }
        } catch (IOException e) {
            logger.warn("IOException", e);
        }

        return null;

    }

    /**
     * Returns response of device for the specified 'response'.
     *
     * @param response
     *            a String value of response
     * @return a GC100IRCommand command response
     */
    private GC100IRCommand responseOfDevice(String response) {
        return GC100IRResponseCommandCodeFactory.INSTANCE.produce(response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<GC100IRDevice> getAvailableDevices() {
        if (deviceGCPropsMap == null) {
            return null;
        }

        synchronized (deviceGCPropsMap) {
            return deviceGCPropsMap.keySet();
        }

    }

    /**
     * Gets the list of devices.
     *
     * @param getDevices
     *            a String value of getDevices
     * @param sourceAddress
     *            a String value of sourceAddress
     * @return a String value for no. of devices that can be attached to the
     *         GC-100
     * @throws UnknownHostException
     * @throws IOException
     */
    public String queryGC100(String getDevices, String sourceAddress) throws IOException {
        List<String> deviceList = new ArrayList<String>();

        Socket socket = new Socket(sourceAddress, GC100IRControlPoint.CONNECT_PORT);
        SocketAddress socketAddress = socket.getRemoteSocketAddress();

        if (socket.isConnected()) {
            logger.info("Connected Successfully to {}", socketAddress);
        }

        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        byte[] commandInBytes = getDevices.getBytes();

        outToServer.write(commandInBytes);

        int c;
        StringBuilder response = new StringBuilder();

        while ((c = inFromServer.read()) != '\0') {
            response.append((char) c);

            if (c == 13) {
                deviceList.add(response.toString());
                response = new StringBuilder();
            }

            if (response.toString().equals(END_LIST_DEVICES)) {
                break;
            }
        }

        StringBuilder temp = new StringBuilder();
        for (String deviceStr : deviceList) {
            temp.append(deviceStr);
        }

        try {
            socket.close();
        } catch (SocketException e) {
            logger.warn("Socket Exception occurred", e);
        }

        return temp.toString();
    }

    /**
     * Parses the list of devices available to the GC-100 device.
     *
     * @param ipAddress
     *            a String value of ipAddress
     * @param getDevicesResponse
     *            a String value of get Devices Response
     */
    public void parseDevices(String ipAddress, String getDevicesResponse) {
        if ((getDevicesResponse == null)) {
            return;
        }

        StringTokenizer st = new StringTokenizer(getDevicesResponse, "\r ,");
        String configURL = ipAddress;

        if (configURL == null) {
            return;
        }

        Date date = new Date();
        Map<String, String> gcProps = new HashMap<String, String>();

        while (st.hasMoreTokens()) {
            if (st.nextToken().equals("device")) {
                int m = Integer.parseInt(st.nextToken());
                gcProps.put("module", String.valueOf(m));

                int c = Integer.parseInt(st.nextToken());
                gcProps.put("connector", String.valueOf(c));

                String deviceType = st.nextToken();
                gcProps.put("deviceType", deviceType);

                for (int i = 1; i <= c; i++) {
                    GC100IRDevice gcDevice = new GC100IRDevice(gc100ControlPoint, configURL, m, i, deviceType);

                    try {
                        gcProps.put("inetAddress", gcDevice.getInetAddress().toString());
                    } catch (UnknownHostException e) {
                        logger.error("UnknownHostException :", e);
                    }

                    deviceGCPropsMap.put(gcDevice, gcProps);

                    synchronized (deviceLastDisTimeMap) {
                        deviceLastDisTimeMap.put(gcDevice, date.getTime());
                    }
                }
            }
        }
    }
}
