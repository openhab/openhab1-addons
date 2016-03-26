/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.rfxcom.internal.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.TooManyListenersException;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * RFXCOM connector for serial port communication.
 *
 * @author Pauli Anttila, Evert van Es, Jürgen Richtsfeld, Ivan F. Martinez
 * @since 1.9.0
 */
public abstract class RFXComBaseConnector implements RFXComConnectorInterface {

    private static final Logger logger = LoggerFactory.getLogger(RFXComBaseConnector.class);

    private static final List<RFXComEventListener> _listeners = new CopyOnWriteArrayList<RFXComEventListener>();

    InputStream in = null;
    OutputStream out = null;
    Thread readerThread = null;
    Object realPort = null;
    private boolean ignoreReceiveBuffer = false;

    RFXComBaseConnector() {
    }
    
    protected abstract void doConnect(String device) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException;
    protected abstract void doClose();

    @Override
    public void connect(String device)
            throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException {
        doConnect(device);

        out.flush();
        if (in.markSupported()) {
            in.reset();
        }

        readerThread = new SerialReader(in);
        readerThread.start();
    }

    @Override
    public void disconnect() {
        logger.debug("Disconnecting");

        if (readerThread != null) {
            logger.debug("Interrupt serial listener");
            readerThread.interrupt();
        }

        if (out != null) {
            logger.debug("Close serial out stream");
            IOUtils.closeQuietly(out);
        }
        if (in != null) {
            logger.debug("Close serial in stream");
            IOUtils.closeQuietly(in);
        }

        if (realPort != null) {
            logger.debug("Close serial connection/port");
            doClose();
        }

        readerThread = null;
        realPort = null;
        out = null;
        in = null;

        logger.debug("Closed");
    }

    @Override
    public void sendMessage(byte[] data) throws IOException {
        out.write(data);
        out.flush();
    }

    @Override
    public void addEventListener(RFXComEventListener rfxComEventListener) {
        _listeners.add(rfxComEventListener);
    }

    @Override
    public void removeEventListener(RFXComEventListener listener) {
        _listeners.remove(listener);
    }
    
    protected abstract void readerStart();
    protected abstract void readerStop();
    
    public class SerialReader extends Thread {
        boolean interrupted = false;
        InputStream in;

        public SerialReader(InputStream in) {
            this.in = in;
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
            final int dataBufferMaxLen = Byte.MAX_VALUE;

            byte[] dataBuffer = new byte[dataBufferMaxLen];

            int msgLen = 0;
            int index = 0;
            boolean start_found = false;

            logger.debug("Data listener started");

            readerStart();
            try {

                byte[] tmpData = new byte[20];
                int len = -1;

                while ((len = in.read(tmpData)) > 0 && !interrupted) {

                    byte[] logData = Arrays.copyOf(tmpData, len);
                    logger.trace("Received data (len={}): {}", len, DatatypeConverter.printHexBinary(logData));

                    if (ignoreReceiveBuffer) {
                        // any data already in receive buffer will be ignored
                        ignoreReceiveBuffer = false;
                        start_found = false;
                        if (index > 0) {
                            logger.trace("Ignoring data in receive Buffer : " + index + " bytes");
                        }
                    }
                    for (int i = 0; i < len; i++) {

                        if (index > dataBufferMaxLen) {
                            // too many bytes received, try to find new start
                            start_found = false;
                        }

                        if (start_found == false && tmpData[i] > 0) {

                            start_found = true;
                            index = 0;
                            dataBuffer[index++] = tmpData[i];
                            msgLen = tmpData[i] + 1;

                        } else if (start_found) {

                            dataBuffer[index++] = tmpData[i];

                            if (index == msgLen) {

                                // whole message received, send an event

                                byte[] msg = new byte[msgLen];

                                for (int j = 0; j < msgLen; j++) {
                                    msg[j] = dataBuffer[j];
                                }

                                RFXComMessageReceivedEvent event = new RFXComMessageReceivedEvent(this);

                                try {
                                    Iterator<RFXComEventListener> iterator = _listeners.iterator();

                                    while (iterator.hasNext()) {
                                        iterator.next().packetReceived(event, msg);
                                    }

                                } catch (Exception e) {
                                    logger.error("Event listener invoking error", e);
                                }

                                // find new start
                                start_found = false;
                            }
                        }
                    }
                }
            } catch (InterruptedIOException e) {
                Thread.currentThread().interrupt();
                logger.error("Interrupted via InterruptedIOException");
            } catch (IOException e) {
                logger.error("Reading from serial port failed", e);
            }

            readerStop();
            logger.debug("Data listener stopped");
        }

    }

    public boolean isConnected() {
        return out != null;
    }
    
    @Override
    public void clearReceiveBuffer() {
        ignoreReceiveBuffer = true;
    }
}
