/*
 * SSDPClient
 * Connect SDK
 * 
 * Copyright (c) 2014 LG Electronics.
 * Created by Hyun Kook Khang on 6 Jan 2015
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.connectsdk.discovery.provider.ssdp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketAddress;
import java.net.SocketException;

public class SSDPClient {
    /* New line definition */
    public static final String NEWLINE = "\r\n";

    public static final String MULTICAST_ADDRESS = "239.255.255.250";
    public static final int PORT = 1900;

    /* Definitions of start line */
    public static final String NOTIFY = "NOTIFY * HTTP/1.1";
    public static final String MSEARCH = "M-SEARCH * HTTP/1.1";
    public static final String OK = "HTTP/1.1 200 OK";

    /* Definitions of search targets */
//    public static final String DEVICE_MEDIA_SERVER_1 = "urn:schemas-upnp-org:device:MediaServer:1"; 

//    public static final String SERVICE_CONTENT_DIRECTORY_1 = "urn:schemas-upnp-org:service:ContentDirectory:1";
//    public static final String SERVICE_CONNECTION_MANAGER_1 = "urn:schemas-upnp-org:service:ConnectionManager:1";
//    public static final String SERVICE_AV_TRANSPORT_1 = "urn:schemas-upnp-org:service:AVTransport:1";
//    
//    public static final String ST_ContentDirectory = ST + ":" + UPNP.SERVICE_CONTENT_DIRECTORY_1;

    /* Definitions of notification sub type */
    public static final String ALIVE = "ssdp:alive";
    public static final String BYEBYE = "ssdp:byebye";
    public static final String UPDATE = "ssdp:update";

    DatagramSocket datagramSocket;
    MulticastSocket multicastSocket;

    SocketAddress multicastGroup;
    NetworkInterface networkInterface;
    InetAddress localInAddress;

    int timeout = 0;
    static int MX = 5;

    public SSDPClient(InetAddress source) throws IOException {
        this(source, new MulticastSocket(PORT), new DatagramSocket(null));
    }

    public SSDPClient(InetAddress source, MulticastSocket mcSocket, DatagramSocket dgSocket) throws IOException {
        localInAddress = source;
        multicastSocket = mcSocket;
        datagramSocket = dgSocket;

        multicastGroup = new InetSocketAddress(MULTICAST_ADDRESS, PORT);
        networkInterface = NetworkInterface.getByInetAddress(localInAddress);
        multicastSocket.joinGroup(multicastGroup, networkInterface);

        datagramSocket.setReuseAddress(true);
        datagramSocket.bind(new InetSocketAddress(localInAddress, 0));
    }

    /** Used to send SSDP packet */
    public void send(String data) throws IOException {
        DatagramPacket dp = new DatagramPacket(data.getBytes(), data.length(), multicastGroup);

        datagramSocket.send(dp);
    }


    /** Used to receive SSDP Response packet */
    public DatagramPacket responseReceive() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        datagramSocket.receive(dp);

        return dp;
    }

    /** Used to receive SSDP Multicast packet */
    public DatagramPacket multicastReceive() throws IOException {
        byte[] buf = new byte[1024];
        DatagramPacket dp = new DatagramPacket(buf, buf.length);

        multicastSocket.receive(dp);

        return dp;
    }

//    /** Starts the socket */
//    public void start() {
//    
//    }

    public boolean isConnected() {
        return datagramSocket != null && multicastSocket != null && datagramSocket.isConnected() && multicastSocket.isConnected();
    }

    /** Close the socket */
    public void close() {
        if (multicastSocket != null) {
            try {
                multicastSocket.leaveGroup(multicastGroup, networkInterface);
            } catch (IOException e) {
                e.printStackTrace();
            }
            multicastSocket.close();
        }

        if (datagramSocket != null) {
            datagramSocket.disconnect();
            datagramSocket.close();
        }
    }

    public void setTimeout(int timeout) throws SocketException {
        this.timeout = timeout;
        datagramSocket.setSoTimeout(this.timeout);
    }

    public static String getSSDPSearchMessage(String ST) {
        StringBuilder sb = new StringBuilder();

        sb.append(MSEARCH + NEWLINE);
        sb.append("HOST: " + MULTICAST_ADDRESS + ":" + PORT + NEWLINE);
        sb.append("MAN: \"ssdp:discover\"" + NEWLINE);
        sb.append("ST: ").append(ST).append(NEWLINE);
        sb.append("MX: ").append(MX).append(NEWLINE);
        if (ST.contains("udap")) {
            sb.append("USER-AGENT: UDAP/2.0" + NEWLINE);
        }
        sb.append(NEWLINE);

        return sb.toString();
    }
}
