/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
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
package org.openhab.binding.velux.bridge.slip.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.openhab.binding.velux.VeluxBindingConstants;
import org.openhab.binding.velux.bridge.VeluxBridge;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Transport layer supported by the Velux bridge.
 * <P>
 * SLIP-based 2nd Level I/O interface towards the <B>Velux</B> bridge.
 * <P>
 * It provides methods for pre- and post-communication
 * as well as a common method for the real communication.
 * <P>
 * In addition to the generic {@link VeluxBridge} methods, i.e.
 * <UL>
 * <LI>{@link SSLconnection#SSLconnection} for establishing the connection,</LI>
 * <LI>{@link SSLconnection#send} for sending a message to the bridge,</LI>
 * <LI>{@link SSLconnection#receive} for receiving a message from the bridge,</LI>
 * <LI>{@link SSLconnection#close} for tearing down the connection.</LI>
 * </UL>
 *
 * @author Guenther Schreiner - Initial contribution.
 * @since 1.13.0
 */
public class SSLconnection {

    private final Logger logger = LoggerFactory.getLogger(SSLconnection.class);

    private static final int CONNECTION_BUFFER_SIZE = 4096;

    private SSLSocket socket = null;
    private DataOutputStream dOut;
    private DataInputStream dIn;

    /**
     * Fake trust manager to suppress any certificate errors,
     * used within {@link #SSLconnection} for seamless operation
     * even on self-signed certificates like provided by <B>Velux</B>.
     */
    private final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
        }
    } };

    /**
     * Constructor to setup and establish a connection.
     *
     * @param host as String describing the Service Access Point location i.e. hostname.
     * @param port as String describing the Service Access Point location i.e. TCP port.
     * @throws java.net.ConnectException in case of unrecoverable communication failures.
     * @throws java.io.IOException in case of continuous communication I/O failures.
     * @throws java.net.UnknownHostException in case of continuous communication I/O failures.
     */
    public SSLconnection(String host, int port) throws ConnectException, IOException, UnknownHostException {
        logger.debug("SSLconnection({},{}) called.", host, port);
        logger.info("Starting {} bridge connection.", VeluxBindingConstants.BINDING_ID);
        SSLContext ctx = null;
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(null, trustAllCerts, null);
        } catch (Exception e) {
            throw new IOException("create of an empty trust store failed.");
        }
        logger.trace("SSLconnection(): creating socket...");
        socket = (SSLSocket) ctx.getSocketFactory().createSocket(host, port);
        logger.trace("SSLconnection(): starting SSL handshake...");
        socket.startHandshake();
        dOut = new DataOutputStream(socket.getOutputStream());
        dIn = new DataInputStream(socket.getInputStream());
        logger.trace("SSLconnection() finished.");
    }

    /**
     * Method to pass a message towards the bridge.
     *
     * @param packet as Array of bytes to be transmitted towards the bridge via the established connection.
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    public void send(byte[] packet) throws IOException {
        logger.trace("send() called, writing {} bytes.", packet.length);
        dOut.write(packet, 0, packet.length);
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (byte b : packet) {
                sb.append(String.format("%02X ", b));
            }
            logger.trace("send() finished after having send {} bytes: {}", packet.length, sb.toString());
        }
    }

    /**
     * Method to get a message from the bridge.
     *
     * @return <b>packet</b> as Array of bytes as received from the bridge via the established connection.
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    public byte[] receive() throws IOException {
        logger.trace("receive() called.");
        byte[] message = new byte[CONNECTION_BUFFER_SIZE];
        int messageLength = dIn.read(message, 0, message.length);
        byte[] packet = new byte[messageLength];
        System.arraycopy(message, 0, packet, 0, messageLength);
        if (logger.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            for (byte b : packet) {
                sb.append(String.format("%02X ", b));
            }
            logger.trace("receive() finished after having read {} bytes: {}", messageLength, sb.toString());
        }
        return packet;
    }

    /**
     * Destructor to tear down a connection.
     *
     * @throws java.io.IOException in case of a communication I/O failure.
     */
    public void close() throws IOException {
        logger.debug("close() called.");
        logger.info("Shutting down Velux bridge connection.");
        dIn.close();
        dOut.close();
        socket.close();
        logger.trace("close() finished.");
    }

}
