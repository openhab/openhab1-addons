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
package org.openhab.binding.insteonplm.internal.driver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements IOStream for the older hubs (pre 2014).
 * Also works for serial ports exposed via tcp, eg. ser2net
 *
 * @author Bernd Pfrommer
 * @since 1.7.0
 *
 */
public class TcpIOStream extends IOStream {
    private static final Logger logger = LoggerFactory.getLogger(TcpIOStream.class);

    private String m_host = null;
    private int m_port = -1;
    private Socket m_socket = null;

    /**
     * Constructor
     *
     * @param host host name of hub device
     * @param port port to connect to
     */
    public TcpIOStream(String host, int port) {
        m_host = host;
        m_port = port;
    }

    @Override
    public boolean open() {
        if (m_host == null || m_port < 0) {
            logger.error("tcp connection to hub not properly configured!");
            return (false);
        }
        try {
            m_socket = new Socket(m_host, m_port);
            m_in = m_socket.getInputStream();
            m_out = m_socket.getOutputStream();
        } catch (UnknownHostException e) {
            logger.error("unknown host name: {}", m_host, e);
            return (false);
        } catch (IOException e) {
            logger.error("cannot open connection to {} port {}: ", m_host, m_port, e);
            return (false);
        }
        return true;
    }

    @Override
    public void close() {
        try {
            if (m_in != null) {
                m_in.close();
            }
            if (m_out != null) {
                m_out.close();
            }
            if (m_socket != null) {
                m_socket.close();
            }
        } catch (IOException e) {
            logger.error("failed to close streams", e);
        }
    }
}
