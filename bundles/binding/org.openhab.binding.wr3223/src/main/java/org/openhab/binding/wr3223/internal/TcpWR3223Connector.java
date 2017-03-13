/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Connector implementation for a TCP connection to WR3223.
 * 
 * @author Michael Fraefel
 *
 */
public class TcpWR3223Connector extends AbstractWR3223Connector {

    private Socket socket;

    /**
     * Connect to WR3223 over IP
     * 
     * @param host
     * @param port
     * @throws UnknownHostException
     * @throws IOException
     */
    public void connect(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        socket.setSoTimeout(5000);
        connect(new DataInputStream(socket.getInputStream()), new DataOutputStream(socket.getOutputStream()));
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (socket != null && socket.isConnected()) {
            socket.close();
        }
    }

}
