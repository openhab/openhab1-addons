/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.connector;

import java.io.IOException;
import java.net.Socket;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * RFXCOM connector for TCP/IP communication.
 *  serial port over TCP
 *
 * @author Pauli Anttila, Evert van Es, Ivan F. Martinez
 * @since 1.2.0
 */
public class RFXComTcpConnector extends RFXComBaseConnector {

    private static final Logger logger = LoggerFactory.getLogger(RFXComTcpConnector.class);

    private Socket socket = null;
    
    public RFXComTcpConnector() {

    }

    @Override
    public void doConnect(String device) throws IOException {
        logger.info("Connecting to " + device);
        final int idx = device.lastIndexOf(":");
        final String host = device.substring(0, idx);
        final int port =  Integer.valueOf(device.substring(idx+1));
        socket = new Socket(host, port);
        realPort = socket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
    }

    @Override
    public void doClose() {
        IOUtils.closeQuietly(socket);
    }

    @Override
    protected void readerStart() {
    }

    @Override
    protected void readerStop() {
    }


}
