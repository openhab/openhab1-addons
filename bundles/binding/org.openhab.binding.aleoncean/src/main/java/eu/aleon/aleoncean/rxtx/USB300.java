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
import eu.aleon.aleoncean.packet.ESP3PacketFactory;
import eu.aleon.aleoncean.packet.ESP3Timeout;
import eu.aleon.aleoncean.packet.ResponsePacket;
import eu.aleon.aleoncean.util.ThreadUtil;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class USB300 implements ESP3Connector {

    private static final Logger LOGGER = LoggerFactory.getLogger(USB300.class);

    private static final int TIMEOUT_MS_WAIT_FOR_OPEN = 3000;
    private static final int BAUD_RATE = 57600;
    private static final int DATA_BITS = SerialPort.DATABITS_8;
    private static final int STOP_BITS = SerialPort.STOPBITS_1;
    private static final int PARITY = SerialPort.PARITY_NONE;

    SerialPort serialPort;
    OutputStream serialPortOut;

    USB300Reader reader;
    BlockingQueue<byte[]> inputQueue;
    BlockingQueue<byte[]> inputQueueResponse;

    Thread writerThread;
    USB300Writer writer;
    BlockingQueue<byte[]> outputQueue;

    @Override
    public boolean connect(final String device) {
        assert serialPort == null;

        final CommPortIdentifier portIdentifier;
        final CommPort commPort;

        try {
            portIdentifier = CommPortIdentifier.getPortIdentifier(device);
        } catch (final NoSuchPortException ex) {
            LOGGER.warn(String.format("Port (%s) not found.", device), ex);
            return false;
        }

        try {
            commPort = portIdentifier.open(this.getClass().getName(), TIMEOUT_MS_WAIT_FOR_OPEN);
        } catch (final PortInUseException ex) {
            LOGGER.warn(String.format("Port (%s) is in use.", device), ex);
            return false;
        }

        if (!(commPort instanceof SerialPort)) {
            LOGGER.warn(String.format("Port (%s) is not a serial port.", device));
            commPort.close();
            return false;
        }

        serialPort = (SerialPort) commPort;

        try {
            serialPort.setSerialPortParams(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
        } catch (final UnsupportedCommOperationException ex) {
            LOGGER.warn(String.format("Port (%s) did not allow parameter settings.", device), ex);
            disconnect();
            return false;
        }

        try {
            inputQueue = new LinkedBlockingQueue<>();
            inputQueueResponse = new LinkedBlockingQueue<>();
            // reader = new USB300ReaderThread(serialPort, inputQueue, inputQueueResponse);
            reader = new USB300ReaderListener(serialPort, inputQueue, inputQueueResponse);
            reader.start();
        } catch (final IOException ex) {
            LOGGER.warn(String.format("Port (%s) raised input stream error.", device), ex);
            disconnect();
            return false;
        }

        try {
            serialPortOut = serialPort.getOutputStream();
            outputQueue = new LinkedBlockingQueue<>();
            writer = new USB300Writer(serialPortOut, outputQueue);
            writerThread = new Thread(null, writer, "USB300 writer thread");
            writerThread.start();
        } catch (final IOException ex) {
            LOGGER.warn(String.format("Port (%s) raised output stream error.", device), ex);
            disconnect();
            return false;
        }

        return true;
    }

    @Override
    public void disconnect() {
        if (reader != null) {
            reader.stop();
            reader = null;
        }

        if (writer != null) {
            writer.stop();
            writer = null;
        }

        if (serialPortOut != null) {
            try {
                serialPortOut.close();
            } catch (final IOException ex) {
                LOGGER.warn("I/O exc. on close (out)", ex);
            }
            serialPortOut = null;
        }

        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }

        if (inputQueue != null) {
            inputQueue = null;
        }

        if (inputQueueResponse != null) {
            inputQueueResponse = null;
        }

        if (outputQueue != null) {
            outputQueue = null;
        }

        if (writerThread != null) {
            ThreadUtil.join(writerThread);
            writerThread = null;
        }
    }

    @Override
    public ResponsePacket write(final ESP3Packet packet) {
        if (outputQueue == null) {
            LOGGER.warn("Cancel write: It seems I am not connected.");
            return null;
        }

        final byte[] raw = packet.generateRaw();

        outputQueue.add(raw);

        try {
            final byte[] responseRaw = inputQueueResponse.poll(ESP3Timeout.RESPONSE, TimeUnit.MILLISECONDS);
            if (responseRaw == null) {
                return null;
            }

            return (ResponsePacket) ESP3PacketFactory.fromRaw(responseRaw);

        } catch (final InterruptedException ex) {
            LOGGER.warn("Interrupted...", ex);
            return null;
        }
    }

    @Override
    public ESP3Packet read(final long timeout, final TimeUnit unit) throws ReaderShutdownException {
        if (inputQueue == null) {
            LOGGER.warn("Cancel read: It seems I am not connected.");
            throw new ReaderShutdownException("Input queue is empty, so it seems I am not connected.");
        }

        try {
            final byte[] raw = inputQueue.poll(timeout, unit);

            if (raw == null) {
                return null;
            }

            if (raw.length == 0) {
                throw new ReaderShutdownException("Got a message with length 0, this is a shutdown indication.");
            }

            return ESP3PacketFactory.fromRaw(raw);
        } catch (final InterruptedException ex) {
            LOGGER.warn("Interrupted...", ex);
            return null;
        }
    }

}
