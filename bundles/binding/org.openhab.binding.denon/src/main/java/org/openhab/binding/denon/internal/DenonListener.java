/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.denon.internal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage telnet connection to the Denon Receiver
 *
 * @author Jeroen Idserda
 * @since 1.7.0
 */
public class DenonListener extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(DenonListener.class);

    private static final Integer RECONNECT_DELAY = 60000; // 1 minute

    private static final Integer TIMEOUT = 60000; // 1 minute

    private DenonConnectionProperties connection;

    private DenonUpdateReceivedCallback callback;

    private boolean running = true;

    private boolean connected = false;

    private Socket socket;

    private OutputStreamWriter out;

    private BufferedReader in;

    public DenonListener(DenonConnectionProperties connection, DenonUpdateReceivedCallback callback) {
        logger.debug("Denon listener created");
        this.connection = connection;
        this.callback = callback;
    }

    @Override
    public void run() {
        while (running) {
            if (!connected) {
                connectTelnetSocket();
            }

            do {
                try {
                    String line = in.readLine();
                    if (line != null) {
                        line = line.trim();
                        if (!StringUtils.isBlank(line)) {
                            logger.trace("Received from {}: {}", connection.getHost(), line);
                            callback.updateReceived(line);
                        }
                    } else {
                        throw new IOException("Telnet connection disconnected");
                    }
                } catch (SocketTimeoutException e) {
                    logger.trace("Socket timeout");

                    // Disconnects are not always detected unless you write to the socket.
                    try {
                        out.write('\r');
                        out.flush();
                    } catch (IOException e2) {
                        logger.debug("Error writing to socket");
                        connected = false;
                    }

                } catch (IOException e) {
                    callback.listenerDisconnected();
                    logger.debug("Error in telnet connection", e);
                    connected = false;
                }
            } while (running && connected);
        }
    }

    public void sendCommand(String command) {
        if (out != null) {
            logger.debug("Sending command {}", command);
            try {
                out.write(command + '\r');
                out.flush();
            } catch (IOException e) {
                logger.debug("Error sending command", e);
            }
        } else {
            logger.debug("Cannot send command, no telnet connection");
        }
    }

    public void shutdown() {
        this.running = false;
        disconnect();
    }

    private void connectTelnetSocket() {
        disconnect();
        int delay = 0;

        while (socket == null || !socket.isConnected()) {
            try {
                Thread.sleep(delay);
                logger.debug("Connecting to {}", connection.getHost());

                // Use raw socket instead of TelnetClient here because TelnetClient sends an extra newline char
                // after each write which causes the connection to become unresponsive.
                socket = new Socket();
                socket.connect(new InetSocketAddress(connection.getHost(), connection.getTelnetPort()), TIMEOUT);
                socket.setKeepAlive(true);
                socket.setSoTimeout(TIMEOUT);

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

                connected = true;
                callback.listenerConnected();
            } catch (IOException e) {
                logger.debug("Cannot connect to {}", connection.getHost(), e);
            } catch (InterruptedException e) {
                logger.debug("Interrupted while connecting to {}", connection.getHost(), e);
            }
            delay = RECONNECT_DELAY;
        }

        logger.debug("Denon telnet client connected to {}", connection.getHost());
    }

    private void disconnect() {
        if (socket != null) {
            logger.debug("Disconnecting socket");
            try {
                socket.close();
            } catch (IOException e) {
                logger.debug("Error while disconnecting telnet client", e);
            } finally {
                socket = null;
            }
        }
    }

}
