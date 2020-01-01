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
package org.openhab.binding.upb.internal;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;

import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class monitors the input stream of a UPB modem. This is done
 * asynchronously. When messages are received, they are broadcast to all
 * subscribed {@link Listener listeners}.
 *
 * @author cvanorman
 * @since 1.9.0
 */
public class UPBReader implements Runnable {

    /**
     * Listener class for handling received messages. A listener can be added by
     * calling {@link UPBReader#addListener(Listener)}.
     *
     * @author cvanorman
     *
     */
    public interface Listener {

        /**
         * Called whenever a message has been received from the UPB modem.
         *
         * @param message
         *            the message that was received.
         */
        void messageReceived(UPBMessage message);
    }

    private static final Logger logger = LoggerFactory.getLogger(UPBReader.class);

    private Collection<Listener> listeners = new LinkedHashSet<>();
    private byte[] buffer = new byte[512];
    private int bufferLength = 0;
    private InputStream inputStream;
    private Thread thread;

    /**
     * Instantiates a new {@link UPBReader}.
     *
     * @param inputStream
     *            the inputStream from the UPB modem.
     */
    public UPBReader(InputStream inputStream) {
        this.inputStream = inputStream;

        thread = new Thread(this);
        thread.start();
    }

    /**
     * Subscribes the listener to any future message events.
     *
     * @param listener
     *            the listener to add.
     */
    public synchronized void addListener(Listener listener) {
        listeners.add(listener);
    }

    /**
     * Removes the listener from further messages.
     *
     * @param listener
     *            the listener to remove.
     */
    public synchronized void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    /**
     * Adds data to the buffer.
     *
     * @param data
     *            the data to add.
     * @param length
     *            the length of data to add.
     */
    private void addData(byte[] data, int length) {

        if (bufferLength + length > buffer.length) {
            // buffer overflow discard entire buffer
            bufferLength = 0;
        }

        System.arraycopy(data, 0, buffer, bufferLength, length);

        bufferLength += length;

        interpretBuffer();
    }

    /**
     * Shuts the reader down.
     */
    public void shutdown() {
        if (thread != null) {
            thread.interrupt();
        }

        try {
            inputStream.close();
        } catch (IOException e) {
        }
    }

    private int findMessageLength(byte[] buffer, int bufferLength) {
        int messageLength = ArrayUtils.INDEX_NOT_FOUND;

        for (int i = 0; i < bufferLength; i++) {
            if (buffer[i] == 13) {
                messageLength = i;
                break;
            }
        }

        return messageLength;
    }

    /**
     * Attempts to interpret any messages that may be contained in the buffer.
     */
    private void interpretBuffer() {
        int messageLength = findMessageLength(buffer, bufferLength);

        while (messageLength != ArrayUtils.INDEX_NOT_FOUND) {
            String message = new String(Arrays.copyOfRange(buffer, 0, messageLength));
            logger.debug("UPB Message: {}", message);

            int remainingBuffer = bufferLength - messageLength - 1;

            if (remainingBuffer > 0) {
                System.arraycopy(buffer, messageLength + 1, buffer, 0, remainingBuffer);
            }
            bufferLength = remainingBuffer;

            notifyListeners(UPBMessage.fromString(message));

            messageLength = findMessageLength(buffer, bufferLength);
        }
    }

    private synchronized void notifyListeners(UPBMessage message) {
        for (Listener l : new ArrayList<>(listeners)) {
            l.messageReceived(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        byte[] buffer = new byte[256];
        try {
            for (int len = -1; (len = inputStream.read(buffer)) >= 0;) {
                if (len > 0) {
                    logger.debug("Received: {}", ArrayUtils.subarray(buffer, 0, len));
                }
                addData(buffer, len);
                if (Thread.interrupted()) {
                    break;
                }
            }
        } catch (Exception e) {
            logger.debug("Failed to read input stream.", e);
        }
        logger.debug("UPBReader stopped.");
    }
}
