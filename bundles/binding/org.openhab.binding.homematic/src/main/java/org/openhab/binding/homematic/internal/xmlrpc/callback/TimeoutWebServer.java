/**
 * Copyright (c) 2010-2014, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.homematic.internal.xmlrpc.callback;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

import org.apache.xmlrpc.webserver.WebServer;

/**
 * A webserver that has an increased socket timeout. Introduced for slow
 * machines as the raspie in connection with the ccu2.
 * 
 * @author Thomas Letsch (contact@thomas-letsch.de)
 * @since 1.4.0
 */
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
        serverSocket.setSoTimeout(10000);
        return serverSocket;
    }

    @Override
    public synchronized void shutdown() {
        super.shutdown();
        while (serverSocket != null && !serverSocket.isClosed()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                log(e);
            }
        }
    }
}
