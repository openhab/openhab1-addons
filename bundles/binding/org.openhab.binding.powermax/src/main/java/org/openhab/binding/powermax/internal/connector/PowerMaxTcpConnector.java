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
package org.openhab.binding.powermax.internal.connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class for the communication with the Visonic alarm panel through a TCP connection
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxTcpConnector extends PowerMaxConnector {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxTcpConnector.class);

    private final String ipAddress;
    private final int tcpPort;
    private final int connectTimeout;
    private Socket tcpSocket;

    /**
     * Constructor.
     *
     * @param ip
     *            IP address
     * @param port
     *            TCP port number
     * @param timeout
     *            timeout for socket communications
     */
    public PowerMaxTcpConnector(String ip, int port, int timeout) {
        ipAddress = ip;
        tcpPort = port;
        connectTimeout = timeout;
        tcpSocket = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open() {
        logger.debug("open(): Opening TCP Connection");

        try {
            tcpSocket = new Socket();
            SocketAddress TPIsocketAddress = new InetSocketAddress(ipAddress, tcpPort);
            tcpSocket.connect(TPIsocketAddress, connectTimeout);

            setInput(tcpSocket.getInputStream());
            setOutput(tcpSocket.getOutputStream());

            setReaderThread(new PowerMaxReaderThread(getInput(), this));
            getReaderThread().start();

            setConnected(true);
        } catch (UnknownHostException exception) {
            logger.debug("open(): Unknown Host Exception: {}", exception.getMessage());
            setConnected(false);
        } catch (SocketException socketException) {
            logger.debug("open(): Socket Exception: {}", socketException.getMessage());
            setConnected(false);
        } catch (IOException ioException) {
            logger.debug("open(): IO Exception: {}", ioException.getMessage());
            setConnected(false);
        } catch (Exception exception) {
            logger.debug("open(): Exception: {}", exception.getMessage());
            setConnected(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        logger.debug("close(): Closing TCP Connection");

        super.cleanup();

        try {
            if (tcpSocket != null) {
                tcpSocket.close();
            }
        } catch (IOException ioException) {
            logger.debug("close(): Closing connection error: {}", ioException.getMessage());
        }

        tcpSocket = null;

        setConnected(false);
    }

}
