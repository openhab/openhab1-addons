/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.openenergymonitor.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.TooManyListenersException;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.openenergymonitor.internal.OpenEnergyMonitorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * Connector for serial port communication.
 *
 * @author Pauli Anttila
 * @since 1.4.0
 */
public class OpenEnergyMonitorSerialConnector extends OpenEnergyMonitorConnector implements SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(OpenEnergyMonitorSerialConnector.class);

    static final int BAUDRATE = 9600;

    String portName = null;
    SerialPort serialPort = null;
    InputStream in = null;

    public OpenEnergyMonitorSerialConnector(String portName) {

        this.portName = portName;
    }

    @Override
    public void connect() throws OpenEnergyMonitorException {

        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(BAUDRATE, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            serialPort.enableReceiveThreshold(1);
            serialPort.enableReceiveTimeout(100); // In ms. Small values mean faster shutdown but more cpu usage.

            // RXTX serial port library causes high CPU load
            // Start event listener, which will just sleep and slow down event loop
            try {
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {
                logger.warn("Can't add serial port event listener. ", e);
            }

            in = serialPort.getInputStream();

            logger.debug("Open Energy Monitor Serial Port message listener started");

        } catch (Exception e) {
            throw new OpenEnergyMonitorException(e);
        }
    }

    @Override
    public void disconnect() throws OpenEnergyMonitorException {
        logger.debug("Disconnecting");

        if (in != null) {
            logger.debug("Close serial in stream");
            IOUtils.closeQuietly(in);
        }

        in = null;

        if (serialPort != null) {
            logger.debug("Close serial port");
            serialPort.close();
            serialPort = null;
        }

        logger.debug("Closed");
    }

    @Override
    public byte[] receiveDatagram() throws OpenEnergyMonitorException {

        if (in == null) {
            connect();
        }

        byte[] buffer = new byte[100];
        StringBuilder sb = new StringBuilder();

        int len = -1;
        try {
            while ((len = this.in.read(buffer)) > 0) {
                for (int i = 0; i < len; i++) {
                    logger.trace("Received char: {}", buffer[i]);
                    if (buffer[i] != 0x0A) {
                        if (buffer[i] != 0x0D) {
                            sb.append((char) buffer[i]);
                        }
                    } else {
                        return parseLine(sb.toString());
                    }
                }
            }
        } catch (IOException e) {
            throw new OpenEnergyMonitorException("Error occurred while receiving data. ", e);
        }

        return null;
    }

    private byte[] parseLine(String line) throws OpenEnergyMonitorException {
        logger.trace("Received line: {}", line);
        String[] bytes = line.toString().split(" ");
        ByteBuffer bytebuf = ByteBuffer.allocate(bytes.length);

        try {
            for (int j = 0; j < bytes.length; j++) {
                if (!bytes[j].isEmpty()) {
                    byte b = (byte) Integer.parseInt(bytes[j]);
                    bytebuf.put(b);
                }
            }
        } catch (NumberFormatException e) {
            // Rubbish received from the serial port, ignore the whole frame
            throw new OpenEnergyMonitorException("Error occurred while receiving data. ", e);
        }

        return bytebuf.array();
    }

    @Override
    public void serialEvent(SerialPortEvent arg0) {
        try {
            logger.trace("RXTX library CPU load workaround, sleep forever");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
        }
    }
}
