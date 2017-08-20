/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.rxtx;

import eu.aleon.aleoncean.packet.ESP3Packet;
import eu.aleon.aleoncean.packet.ESP3PacketBuilder;
import eu.aleon.aleoncean.packet.PacketType;
import gnu.io.SerialPort;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public abstract class USB300Reader implements PropertyChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(USB300Reader.class);

    protected final SerialPort serialPort;

    protected final InputStream in;

    private final BlockingQueue<byte[]> queue;
    private final BlockingQueue<byte[]> queueResponse;

    private final ESP3PacketBuilder packetBuilder;

    protected volatile boolean running = false;

    public USB300Reader(final SerialPort serialPort,
                        final BlockingQueue<byte[]> queue,
                        final BlockingQueue<byte[]> queueResponse) throws IOException {
        this.serialPort = serialPort;
        this.in = serialPort.getInputStream();
        this.queue = queue;
        this.queueResponse = queueResponse;

        packetBuilder = new ESP3PacketBuilder();
        packetBuilder.addPropertyChangeListener(this);
    }

    public abstract boolean start();

    protected abstract void stopHandler();

    public final void stop() {
        running = false;

        stopHandler();

        queue.add(new byte[0]);
    }

    protected boolean doRead() {
        final int input;

        try {
            /*
             * Read the next byte from the input stream.
             * The read method blocks until input data is available (a value between 0 and 255 is returned),
             * the end of the stream is detected (-1 is returned),
             * or an exception is thrown.
             */
            input = in.read();

            /*
             * If we are woken up and we should stop, break.
             */
            if (!running) {
                return false;
            }

            /*
             * Check for end of file.
             */
            if (input == -1) {
                LOGGER.debug("EOF was read");
                return false;
            }

            /*
             * Handle exceptions.
             */
        } catch (final IOException ex) {
            LOGGER.warn("Got an IOException while reading.", ex);
            return false;
        }

        /*
         * Add the valid input data to the buffer.
         */
        packetBuilder.add((byte) input);

        return true;
    }

    @Override
    public void propertyChange(final PropertyChangeEvent evt) {
        final byte[] raw = (byte[]) evt.getNewValue();
        if (ESP3Packet.getPacketType(raw) == PacketType.RESPONSE) {
            queueResponse.add(raw);
        } else {
            queue.add(raw);
        }
    }

}
