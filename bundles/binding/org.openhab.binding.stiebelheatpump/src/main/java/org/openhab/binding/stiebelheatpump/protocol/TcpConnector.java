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
package org.openhab.binding.stiebelheatpump.protocol;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TcpConnector extends SerialConnector {

    private String host;
    private int port;
    private Socket socket;

    public TcpConnector(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void connectPort() throws Exception {
        socket = new Socket(host, port);
        in = socket.getInputStream();
        out = new DataOutputStream(socket.getOutputStream());
    }

    @Override
    protected void disconnectPort() {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}
