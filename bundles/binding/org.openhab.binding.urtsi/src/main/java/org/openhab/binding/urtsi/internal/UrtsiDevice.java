/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.urtsi.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

/**
 * Implementation of the device. This class is responsible for communicating to the hardware which is connected via a
 * serial port.
 * It completely encapsulates the serial communication and just provides a writeString method which returns true, if the
 * message has been transmitted successfully.
 *
 * @author Oliver Libutzki
 * @author John Cocula -- translated to Java
 * @since 1.3.0
 *
 */
class UrtsiDevice {

    private static final Logger logger = LoggerFactory.getLogger(UrtsiDevice.class);

    private static final int baud = 9600;
    private static final int databits = SerialPort.DATABITS_8;
    private static final int stopbit = SerialPort.STOPBITS_1;
    private static final int parity = SerialPort.PARITY_NONE;

    // Serial communication fields
    private String port;

    private int interval = 100;

    long lastCommandTime = 0;

    CommPortIdentifier portId;
    SerialPort serialPort;
    OutputStream outputStream;
    InputStream inputStream;

    public UrtsiDevice() {
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPort() {
        return this.port;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getInterval() {
        return this.interval;
    }

    /**
     * Initialize this device and open the serial port.
     *
     * @throws InitializationException
     *             if port cannot be opened
     */
    void initialize() throws InitializationException {
        try {
            // parse ports and if the default port is found, initialized the reader
            portId = CommPortIdentifier.getPortIdentifier(port);
            // initialize serial port
            serialPort = portId.open("openHAB", 2000);
            // set port parameters
            serialPort.setSerialPortParams(baud, databits, stopbit, parity);
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
        } catch (UnsupportedCommOperationException e) {
            throw new InitializationException(e);
        } catch (IOException e) {
            throw new InitializationException(e);
        } catch (PortInUseException e) {
            throw new InitializationException(e);
        } catch (NoSuchPortException e) {
            // enumerate the port identifiers in the exception to be helpful
            final StringBuilder sb = new StringBuilder();
            @SuppressWarnings("unchecked")
            Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                final CommPortIdentifier id = portList.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    sb.append(id.getName() + "\n");
                }
            }
            throw new InitializationException(
                    "Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
        }
    }

    /**
     * Sends a string to the serial port of this device.
     * The writing of the msg is executed synchronized, so it's guaranteed that the device doesn't get
     * multiple messages concurrently.
     *
     * @param msg
     *            the string to send
     * @return true, if the message has been transmitted successfully, otherwise false.
     */
    synchronized boolean writeString(final String msg) {
        logger.debug("Writing '{}' to serial port {}", msg, port);

        final long earliestNextExecution = lastCommandTime + interval;
        while (earliestNextExecution > System.currentTimeMillis()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                return false;
            }
        }
        try {
            final List<Boolean> listenerResult = new ArrayList<Boolean>();
            serialPort.addEventListener(new SerialPortEventListener() {
                @Override
                public void serialEvent(SerialPortEvent event) {
                    if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
                        // we get here if data has been received
                        final StringBuilder sb = new StringBuilder();
                        final byte[] readBuffer = new byte[20];
                        try {
                            do {
                                // read data from serial device
                                while (inputStream.available() > 0) {
                                    final int bytes = inputStream.read(readBuffer);
                                    sb.append(new String(readBuffer, 0, bytes));
                                }
                                try {
                                    // add wait states around reading the stream, so that interrupted transmissions are
                                    // merged
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    // ignore interruption
                                }
                            } while (inputStream.available() > 0);
                            final String result = sb.toString();
                            if (result.equals(msg)) {
                                listenerResult.add(true);
                            }
                        } catch (IOException e) {
                            logger.debug("Error receiving data on serial port {}: {}", port, e.getMessage());
                        }
                    }
                }
            });
            serialPort.notifyOnDataAvailable(true);
            outputStream.write(msg.getBytes());
            outputStream.flush();
            lastCommandTime = System.currentTimeMillis();
            final long timeout = lastCommandTime + 1000;
            while (listenerResult.isEmpty() && System.currentTimeMillis() < timeout) {
                // Waiting for response
                Thread.sleep(100);
            }
            return !listenerResult.isEmpty();
        } catch (Exception e) {
            logger.error("Error writing '{}' to serial port {}: {}", msg, port, e.getMessage());
        } finally {
            serialPort.removeEventListener();
        }
        return false;
    }

    /**
     * Close this serial device
     */
    void close() {
        serialPort.removeEventListener();
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(inputStream);
        serialPort.close();
    }
}
