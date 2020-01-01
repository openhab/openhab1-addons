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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.powermax.internal.message.PowerMaxBaseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract class for the communication with the Visonic alarm panel that
 * handles stuff common to all communication types
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public abstract class PowerMaxConnector implements PowerMaxConnectorInterface {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxConnector.class);

    private InputStream input = null;
    private OutputStream output = null;
    private boolean connected = false;
    private Thread readerThread = null;
    private long waitingForResponse = 0;
    private List<PowerMaxEventListener> listeners = new ArrayList<PowerMaxEventListener>();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void open();

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void close();

    /**
     * Cleanup everything; to be called when closing the communication
     */
    protected void cleanup() {
        logger.debug("cleanup(): cleaning up Connection");

        if (readerThread != null) {
            logger.debug("Interrupt reader thread");
            readerThread.interrupt();
        }

        if (input != null) {
            IOUtils.closeQuietly(input);
        }

        if (output != null) {
            IOUtils.closeQuietly(output);
        }

        readerThread = null;
        input = null;
        output = null;

        logger.debug("cleanup(): Connection Cleanup");
    }

    /**
     * Handles an incoming message
     *
     * @param incomingMessage
     *            the received message as a table of bytes
     */
    public void handleIncomingMessage(byte[] incomingMessage) {
        PowerMaxEvent event = new PowerMaxEvent(this, PowerMaxBaseMessage.getMessageObject(incomingMessage));

        // send message to event listeners
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).powerMaxEventReceived(event);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendMessage(byte[] data) {
        try {
            output.write(data);
            output.flush();
        } catch (IOException ioException) {
            logger.debug("sendMessage(): Writing error: {}", ioException.getMessage());
            setConnected(false);
        } catch (Exception exception) {
            logger.debug("sendMessage(): Writing error: {}", exception.getMessage());
            setConnected(false);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addEventListener(PowerMaxEventListener listener) {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeEventListener(PowerMaxEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * @return the input stream
     */
    public InputStream getInput() {
        return input;
    }

    /**
     * Set the input stream
     *
     * @param input
     *            the input stream
     */
    public void setInput(InputStream input) {
        this.input = input;
    }

    /**
     * @return the output stream
     */
    public OutputStream getOutput() {
        return output;
    }

    /**
     * Set the output stream
     *
     * @param output
     *            the output stream
     */
    public void setOutput(OutputStream output) {
        this.output = output;
    }

    /**
     * @return true if connected or false if not
     */
    @Override
    public boolean isConnected() {
        return connected;
    }

    /**
     * Set the connection state
     *
     * @param connected
     *            true if connected or false if not
     */
    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    /**
     * @return the thread that handles the message reading
     */
    public Thread getReaderThread() {
        return readerThread;
    }

    /**
     * Set the thread that handles the message reading
     *
     * @param readerThread
     *            the thread
     */
    public void setReaderThread(Thread readerThread) {
        this.readerThread = readerThread;
    }

    /**
     * @return the start time of the time frame to receive a response
     */
    public synchronized long getWaitingForResponse() {
        return waitingForResponse;
    }

    /**
     * Set the start time of the time frame to receive a response
     *
     * @param timeLastReceive
     *            the time in milliseconds
     */
    public synchronized void setWaitingForResponse(long waitingForResponse) {
        this.waitingForResponse = waitingForResponse;
    }

}
