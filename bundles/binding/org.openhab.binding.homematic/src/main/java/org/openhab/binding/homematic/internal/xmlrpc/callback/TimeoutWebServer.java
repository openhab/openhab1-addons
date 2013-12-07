package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.apache.xmlrpc.webserver.WebServer;

public class TimeoutWebServer extends WebServer {

    public TimeoutWebServer(int pPort) {
        super(pPort);
    }

    public TimeoutWebServer(int pPort, InetAddress pAddr) {
        super(pPort, pAddr);
    }

    @Override
    protected ServerSocket createServerSocket(int pPort, int backlog, InetAddress addr) throws IOException {
        ServerSocket serverSocket = new ServerSocket(pPort, backlog, addr);
        serverSocket.setSoTimeout(30000);
        return serverSocket;
    }

}
