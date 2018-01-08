/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.epsonprojector.connector;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.epsonprojector.internal.EpsonProjectorException;
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
 * @since 1.3.0
 */
public class EpsonProjectorSerialConnector implements EpsonProjectorConnector, SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EpsonProjectorSerialConnector.class);

    String serialPortName = null;
    InputStream in = null;
    OutputStream out = null;
    SerialPort serialPort = null;

    public EpsonProjectorSerialConnector(String serialPort) {
        serialPortName = serialPort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void connect() throws EpsonProjectorException {

        try {
            logger.debug("Open connection to serial port '{}'", serialPortName);
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);

            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.enableReceiveThreshold(1);
            serialPort.disableReceiveTimeout();

            in = serialPort.getInputStream();
            out = serialPort.getOutputStream();

            out.flush();
            if (in.markSupported()) {
                in.reset();
            }

            // RXTX serial port library causes high CPU load
            // Start event listener, which will just sleep and slow down event loop
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
        } catch (Exception e) {
            throw new EpsonProjectorException(e);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void disconnect() throws EpsonProjectorException {
        if (out != null) {
            logger.debug("Close serial out stream");
            IOUtils.closeQuietly(out);
        }
        if (in != null) {
            logger.debug("Close serial in stream");
            IOUtils.closeQuietly(in);
        }
        if (serialPort != null) {
            logger.debug("Close serial port");
            serialPort.close();
        }

        serialPort.removeEventListener();

        serialPort = null;
        out = null;
        in = null;

        logger.debug("Closed");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String sendMessage(String data, int timeout) throws EpsonProjectorException {
        if (in == null || out == null) {
            connect();
        }

        try {
            // flush input stream
            if (in.markSupported()) {
                in.reset();
            } else {

                while (in.available() > 0) {

                    int availableBytes = in.available();

                    if (availableBytes > 0) {

                        byte[] tmpData = new byte[availableBytes];
                        in.read(tmpData, 0, availableBytes);
                    }
                }
            }

            return sendMmsg(data, timeout);

        } catch (IOException e) {

            logger.debug("IO error occurred...reconnect and resend ones");
            disconnect();
            connect();

            try {
                return sendMmsg(data, timeout);
            } catch (IOException e1) {
                throw new EpsonProjectorException(e);
            }

        } catch (Exception e) {
            throw new EpsonProjectorException(e);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent arg0) {
        try {
            logger.trace("RXTX library CPU load workaround, sleep forever");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
        }
    }

    private String sendMmsg(String data, int timeout) throws IOException, EpsonProjectorException {
        out.write(data.getBytes());
        out.write("\r\n".getBytes());
        out.flush();

        String resp = "";

        long startTime = System.currentTimeMillis();
        long elapsedTime = 0;

        while (elapsedTime < timeout) {
            int availableBytes = in.available();
            if (availableBytes > 0) {
                byte[] tmpData = new byte[availableBytes];
                int readBytes = in.read(tmpData, 0, availableBytes);
                resp = resp.concat(new String(tmpData, 0, readBytes));

                if (resp.contains(":")) {
                    return resp;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new EpsonProjectorException(e);
                }
            }

            elapsedTime = Math.abs((new Date()).getTime() - startTime);
        }

        return null;
    }
}
