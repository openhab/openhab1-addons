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
package org.openhab.io.transport.cul.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

import org.openhab.io.transport.cul.CULCommunicationException;
import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.CULListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract base class for all CULHandler which brings some convenience
 * regarding registering listeners and detecting forbidden messages.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public abstract class AbstractCULHandler<T extends CULConfig> implements CULHandlerInternal<T> {

    private final static Logger log = LoggerFactory.getLogger(AbstractCULHandler.class);

    /**
     * Thread which sends all queued commands to the CUL.
     * The Thread waits on a CUL response before sending a new
     * command to prevent race conditions.
     *
     * @author Till Klocke
     * @since 1.4.0
     *
     */
    private class SendThread extends Thread implements CULListener {

        private final Logger logger = LoggerFactory.getLogger(SendThread.class);

        /**
         * List of commands the CULfw does not response to and we shall not wait for
         */
        private final static String async_cmds = "F";

        /**
         * Timeout in milliseconds the thread will wait for an response by the CUL
         */
        private final static int waitForResponse_ms = 2000;

        @Override
        public void run() {
            String command = null;

            while (!isInterrupted()) {
                try {
                    command = sendQueue.take();
                } catch (InterruptedException e) {
                    logger.warn("Failed to wait for queue: " + e.toString());
                }
                if (command == null) {
                    continue;
                }
                if (!command.endsWith("\r\n")) {
                    command = command + "\r\n";
                }
                try {
                    logger.trace("Writing message: {}", command);

                    writeMessage(command);
                    if (async_cmds.contains(command.subSequence(0, 1))) {
                        continue;
                    }
                    long start_ms = System.nanoTime();
                    waitOnCulResponse();
                    logger.trace("Response took {} ms", (System.nanoTime() - start_ms) / 1000000);
                } catch (CULCommunicationException e) {
                    logger.warn("Error while writing command to CUL", e);
                }
            }
            logger.warn("Sending thread interrupted");
        }

        private synchronized void waitOnCulResponse() {
            try {
                wait(waitForResponse_ms);
            } catch (InterruptedException e) {
                logger.debug("Error while sleeping in SendThread", e);
            }
        }

        @Override
        public synchronized void dataReceived(String data) {
            logger.trace("CUL response received: {}", data);
            notify();
        }

        @Override
        public synchronized void error(Exception e) {
            logger.debug("CUL error received: {}", e);
            notify();
        }
    }

    /**
     * Wrapper class wraps a CULListener and a received Strings and gets
     * executed by a executor in its own thread.
     *
     * @author Till Klocke
     * @since 1.4.0
     *
     */
    private static class NotifyDataReceivedRunner implements Runnable {

        private String message;
        private CULListener listener;

        public NotifyDataReceivedRunner(CULListener listener, String message) {
            this.message = message;
            this.listener = listener;
        }

        @Override
        public void run() {
            listener.dataReceived(message);
        }

    }

    /**
     * Executor to handle received messages. Every listern should be called in
     * its own thread.
     */
    protected Executor receiveExecutor = Executors.newCachedThreadPool();
    protected SendThread sendThread = new SendThread();

    protected T config;

    protected List<CULListener> listeners = new ArrayList<CULListener>();

    protected BlockingQueue<String> sendQueue = new LinkedTransferQueue<String>();
    protected int credit10ms = 0;

    protected AbstractCULHandler(T config) {
        this.config = config;
    }

    @Override
    public void registerListener(CULListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    @Override
    public void unregisterListener(CULListener listener) {
        if (listener != null) {
            listeners.remove(listener);
        }
    }

    @Override
    public boolean hasListeners() {
        return (listeners.size() == 1 && listeners.get(0) != sendThread || listeners.size() > 1);
    }

    @Override
    public void open() throws CULDeviceException {
        openHardware();

        registerListener(sendThread);
        sendThread.start();
    }

    @Override
    public void close() {
        sendThread.interrupt();
        unregisterListener(sendThread);

        closeHardware();
    }

    /**
     * initialize the CUL hardware and open the connection
     *
     * @throws CULDeviceException
     */
    protected abstract void openHardware() throws CULDeviceException;

    /**
     * Close the connection to the hardware and clean up all resources.
     */
    protected abstract void closeHardware();

    protected abstract void write(String command);

    @Override
    public void send(String command) {
        if (isMessageAllowed(command)) {
            if (sendQueue.offer(command)) {
                requestCreditReport();
            } else {
                log.warn("Send buffer overrun. Doing reset");
                sendQueue.clear();
            }
        }
    }

    @Override
    public void sendWithoutCheck(String message) throws CULCommunicationException {
        sendQueue.offer(message);
    }

    /**
     * Checks if the message would alter the RF mode of this device.
     *
     * @param message
     *            The message to check
     * @return true if the message doesn't alter the RF mode, false if it does.
     */
    protected boolean isMessageAllowed(String message) {
        if (message.startsWith("X") || message.startsWith("x")) {
            return false;
        }
        if (message.startsWith("Ar")) {
            return false;
        }
        return true;
    }

    /**
     * Notifies each CULListener about the received data in its own thread.
     *
     * @param data
     */
    protected void notifyDataReceived(String data) {
        for (final CULListener listener : listeners) {
            receiveExecutor.execute(new NotifyDataReceivedRunner(listener, data));
        }
    }

    protected void notifyError(Exception e) {
        for (CULListener listener : listeners) {
            listener.error(e);
        }
    }

    /**
     * read and process next line from underlying transport.
     *
     * @throws CULCommunicationException
     *             if
     */
    protected void processNextLine(String data) {
        log.debug("Received raw message from CUL: {}", data);
        if ("EOB".equals(data)) {
            log.warn("(EOB) End of Buffer. Last message lost. Try sending less messages per time slot to the CUL");
            return;
        } else if ("LOVF".equals(data)) {
            log.warn(
                    "(LOVF) Limit Overflow: Last message lost. You are using more than 1% transmitting time. Reduce the number of rf messages");
            return;
        } else if (data.matches("^\\d+\\s+\\d+")) {
            processCreditReport(data);
        }
        notifyDataReceived(data);
    }

    /**
     * process data received from credit report
     *
     * @param data
     */
    private void processCreditReport(String data) {
        // Credit report received
        String[] report = data.split(" ");
        credit10ms = Integer.parseInt(report[report.length - 1]);
        log.debug("credit10ms = {}", credit10ms);
    }

    /**
     * get the remaining send time on channel as seen at the last send/receive
     * event.
     *
     * @return remaining send time in 10ms units
     */
    @Override
    public int getCredit10ms() {
        return credit10ms;
    }

    /**
     * write out request for a credit report directly to CUL
     */
    private void requestCreditReport() {
        /* this requests a report which provides credit10ms */
        log.trace("Requesting credit report");
        try {
            sendWithoutCheck("X\r\n");
        } catch (CULCommunicationException e) {
            log.warn("Error requesting credit report from CUL", e);
        }
    }

    /**
     * Write a message to the CUL.
     *
     * @param message
     * @throws CULCommunicationException
     */
    private void writeMessage(String message) throws CULCommunicationException {
        log.trace("Writing message: {}", message);
        write(message);
    }

    @Override
    public T getConfig() {
        return config;
    }
}
