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
import java.io.InterruptedIOException;
import java.util.Arrays;

import javax.xml.bind.DatatypeConverter;

import org.openhab.binding.powermax.internal.message.PowerMaxCommDriver;
import org.openhab.binding.powermax.internal.message.PowerMaxReceiveType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A class that reads messages from the Visonic alarm panel in a dedicated thread
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxReaderThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxReaderThread.class);

    private InputStream in;
    private PowerMaxConnector connector;
    private boolean interrupted;

    /**
     * Constructor
     *
     * @param in
     *            the input stream
     * @param connector
     *            the object that should handle the received message
     */
    public PowerMaxReaderThread(InputStream in, PowerMaxConnector connector) {
        this.in = in;
        this.connector = connector;
        interrupted = false;
    }

    @Override
    public void interrupt() {
        interrupted = true;
        super.interrupt();
        try {
            in.close();
        } catch (IOException e) {
        } // quietly close
    }

    @Override
    public void run() {
        logger.debug("Data listener started");

        final int dataBufferMaxLen = 0xC0;

        byte[] dataBuffer = new byte[dataBufferMaxLen];
        int index = 0;
        int msgLen = 0;
        boolean variableLen = false;

        try {

            byte[] tmpData = new byte[20];
            int len = -1;

            while (((len = in.read(tmpData)) > 0) && !interrupted) {

                for (int i = 0; i < len; i++) {

                    if (index >= dataBufferMaxLen) {
                        // too many bytes received, try to find new start
                        if (logger.isDebugEnabled()) {
                            byte[] logData = Arrays.copyOf(dataBuffer, index);
                            logger.debug("Truncating message {}", DatatypeConverter.printHexBinary(logData));
                        }
                        index = 0;
                    }

                    if (index == 0 && tmpData[i] == 0x0D) {
                        // Preamble

                        dataBuffer[index++] = tmpData[i];

                    } else if (index > 0) {

                        dataBuffer[index++] = tmpData[i];

                        if (index == 2) {

                            try {
                                PowerMaxReceiveType msgType = PowerMaxReceiveType.fromCode(tmpData[i]);
                                msgLen = msgType.getLength();
                                variableLen = ((tmpData[i] & 0x000000FF) > 0x10) && (msgLen == 0);
                            } catch (IllegalArgumentException arg0) {
                                msgLen = 0;
                                variableLen = false;
                            }

                        } else if (index == 5 && variableLen) {

                            msgLen = (tmpData[i] & 0x000000FF) + 7;

                        } else if ((msgLen == 0 && tmpData[i] == 0x0A) || (index == msgLen)) {
                            // Postamble

                            if (tmpData[i] != 0x0A && dataBuffer[index - 1] == 0x43) {
                                // adjust message length for 0x43
                                msgLen++;
                            } else if (checkCRC(dataBuffer, index)) {
                                // whole message received with a right CRC

                                byte[] msg = new byte[index];
                                msg = Arrays.copyOf(dataBuffer, index);

                                connector.setWaitingForResponse(System.currentTimeMillis());
                                connector.handleIncomingMessage(msg);

                                // find new preamble
                                index = 0;
                            } else if (msgLen == 0) {
                                // CRC check failed for a message with an unknown length
                                logger.debug("Message length is now {} but message is apparently not complete",
                                        index + 1);
                            } else {
                                // CRC check failed for a message with a known length

                                connector.setWaitingForResponse(System.currentTimeMillis());

                                // find new preamble
                                index = 0;
                            }
                        }
                    }
                }
            }
        } catch (InterruptedIOException e) {
            Thread.currentThread().interrupt();
            logger.debug("Interrupted via InterruptedIOException");
        } catch (IOException e) {
            logger.debug("Reading failed: {}", e.getMessage());
        }

        logger.debug("Data listener stopped");
    }

    /**
     * Check if the CRC inside a received message is valid or not
     *
     * @param data
     *            the buffer containing the message
     * @param len
     *            the size of the message in the buffer
     *
     * @return true if the CRC is valid or false if not
     */
    private boolean checkCRC(byte[] data, int len) {
        byte checksum = PowerMaxCommDriver.computeCRC(data, len);
        byte expected = data[len - 2];
        if (checksum != expected) {
            byte[] logData = Arrays.copyOf(data, len);
            logger.warn("PowerMax alarm binding: message CRC check failed (expected {}, got {}, message {})",
                    String.format("%02X", expected), String.format("%02X", checksum),
                    DatatypeConverter.printHexBinary(logData));
        }
        return (checksum == expected);
    }

}
