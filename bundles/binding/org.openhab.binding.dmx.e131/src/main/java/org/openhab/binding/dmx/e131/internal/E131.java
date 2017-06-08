/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.e131.internal;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * E1.31 sender class
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */

public class E131 extends E131Node {

    private static final Logger logger = LoggerFactory.getLogger(E131.class);

    protected boolean isConnected;
    protected DatagramSocket socket;

    protected final UUID senderUUID;

    /**
     * default constructor
     */
    public E131() {
        this.port = E131Node.DEFAULT_PORT;
        this.isConnected = false;
        this.address = null;
        this.senderUUID = UUID.randomUUID();
    }

    /**
     * opens the socket for sending
     *
     * @throws SocketException
     */
    public void start() throws SocketException {
        if (address == null) {
            this.socket = new DatagramSocket(port);
        } else {
            this.socket = new DatagramSocket(port, address);
        }
        this.isConnected = true;
        logger.debug("sender started");
    }

    /**
     * closes the socket
     */
    public void stop() {
        if (socket != null) {
            socket.close();
        }
        this.isConnected = false;
        logger.debug("sender stopped");
    }

    /**
     * checks connection state
     *
     * @return
     *         true if socket is opened
     */
    public boolean isConnected() {
        return this.isConnected;
    }

    /**
     * sends a packet to the default address (multicast)
     *
     * @param packet
     *            initialized E1.31 packet
     * @throws IOException
     */
    public void sendPacket(E131Packet packet) throws IOException {
        // default is sending to broadcast address
        sendPacket(packet, packet.getBroadcastNode());
    }

    /**
     * sends a packet to the specified node
     *
     * @param packet
     *            initialized E1.31 packet
     * @param receiver
     *            E1.31 receiver node
     * @throws IOException
     */
    public void sendPacket(E131Packet packet, E131Node receiver) throws IOException {
        logger.trace("sending packet with length {} to {}", packet.getPacketLength(), receiver.toString());
        DatagramPacket sendPacket = new DatagramPacket(packet.getRawPacket(), packet.getPacketLength(),
                receiver.getAddress(), receiver.getPort());
        socket.send(sendPacket);

    }

    /**
     * gets this senders UUID
     *
     * @return
     *         unique identifier for E1.31 packets
     */
    public UUID getUUID() {
        return this.senderUUID;
    }

}
