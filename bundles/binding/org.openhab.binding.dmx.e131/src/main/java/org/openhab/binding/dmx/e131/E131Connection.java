/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.dmx.e131;

import java.util.ArrayList;
import java.util.List;

import org.openhab.binding.dmx.DmxConnection;
import org.openhab.binding.dmx.e131.internal.E131;
import org.openhab.binding.dmx.e131.internal.E131Node;
import org.openhab.binding.dmx.e131.internal.E131Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DMX Connection Implementation using E1.31 as the DMX target.
 *
 * @author Jan N. Klug
 * @since 1.9.0
 */
public class E131Connection implements DmxConnection {

    private static final Logger logger = LoggerFactory.getLogger(E131Connection.class);

    /** sequence ID, used ordering the E1.31 packets */
    private int sequenceID;

    /** the E131 instance */
    private E131 e131 = new E131();

    /** flag to indicate if the connection was opened */
    private Boolean isConnectionClosed = true;

    /** prepared packet */
    private E131Packet e131Packet;

    /** list of our receivers, filled in open() */
    private List<E131Node> receiverNodes = new ArrayList<E131Node>();

    /**
     * Gets called when the DMX connection is opened
     *
     * The connection String holds the information from openhab.cfg setting for example:
     * dmx:connection,dmx:connection=192.168.2.151:1555,192.168.2.201 will send the
     * DMX data to the both E1.31 receivers listed {@inheritDoc}
     *
     * @see org.openhab.binding.dmx.DmxConnection#open(java.lang.String)
     */
    @Override
    public void open(String connectionString) throws Exception {

        // parse receivers
        logger.debug("DMX E1.31 configstring: " + connectionString);
        for (String receiverString : connectionString.split(",")) {
            E131Node node;
            if (receiverString.contains(":")) {
                String[] parts = receiverString.split(":");
                node = new E131Node(parts[0], Integer.parseInt(parts[1]));
            } else {
                node = new E131Node(receiverString);
            }
            logger.debug("adding E1.31 receiver " + node.toString());
            receiverNodes.add(node);
        }

        // start the sender
        e131.start();
        isConnectionClosed = false;
        logger.debug("E1.31 sender started");
    }

    @Override
    public void close() {
        // remove receivers
        receiverNodes.clear();

        // stop the sender
        e131.stop();
        isConnectionClosed = true;
        logger.debug("E1.31 sender stopped");
    }

    @Override
    public boolean isClosed() {
        return isConnectionClosed;
    }

    /**
     * This function gets called each time DMX data to be submitted it iterates
     * through the list of receivers, and sends out the data to them. when no
     * receiver was specified, the data is broadcasted
     *
     * {@inheritDoc}
     *
     * @see org.openhab.binding.dmx.DmxConnection#sendDmx(byte[])
     */
    @Override
    public void sendDmx(byte[] buffer) throws Exception {

        if (!isConnectionClosed) {
            if (e131Packet == null) {
                e131Packet = new E131Packet(e131.getUUID());
                e131Packet.setUniverse(1);
            }

            e131Packet.setSequence(sequenceID % 256);
            e131Packet.setPayload(buffer, buffer.length);

            if (!receiverNodes.isEmpty()) {

                for (E131Node receiver : receiverNodes) {
                    logger.trace("Sending " + buffer.length + " Bytes to " + receiver.toString());
                    e131.sendPacket(e131Packet, receiver);
                }

            } else {
                logger.trace("Sending " + buffer.length + " Bytes to " + e131Packet.getBroadcastNode().toString());
                e131.sendPacket(e131Packet);
            }

            sequenceID++;
        }
    }
}
