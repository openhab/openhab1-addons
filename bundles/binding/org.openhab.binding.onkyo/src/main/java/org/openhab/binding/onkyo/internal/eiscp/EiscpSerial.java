/**
 * Copyright (c) 2010-2016, openHAB.org and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onkyo.internal.eiscp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.openhab.binding.onkyo.internal.OnkyoEventListener;
import org.openhab.binding.onkyo.internal.OnkyoStatusUpdateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

/**
 * Onkyo Connector for serial port communication.
 *
 * @author Sriram Balakrishnan
 * @since 1.9.0
 */
public class EiscpSerial implements EiscpInterface, SerialPortEventListener {

    private static final Logger logger = LoggerFactory.getLogger(EiscpSerial.class);

    String serialPortName = null;
    InputStream in = null;
    OutputStream out = null;
    SerialPort serialPort = null;
    EiscpEventNotifier notifier = null;

    private int retryCount = 1;
    private int timeout = 10000; // Onkyo Doc says 50ms timeout

    public EiscpSerial(String serialPort) {
        serialPortName = serialPort;
        notifier = new EiscpEventNotifier(serialPort);
    }

    @Override
    public synchronized void addEventListener(OnkyoEventListener listener) {
        notifier.addEventListener(listener);
    }

    @Override
    public synchronized void removeEventListener(OnkyoEventListener listener) {
        notifier.removeEventListener(listener);
    }

    @Override
    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    @Override
    public boolean connectSocket() {
        return connect(serialPortName);
    }

    /**
     * {@inheritDoc}
     */
    public boolean connect(String serialPortName) {

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
            // Start event listener, which will just sleep and slow down event
            // loop
            serialPort.addEventListener(this);
            serialPort.notifyOnDataAvailable(true);
            return true;
        } catch (Exception e) {
            logger.error("serial port connect error", e);
            e.printStackTrace();
            return false;
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean closeSocket() {
        try {
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
            return true;
        } catch (Exception e) {
            logger.error("Unable to close ", e);
            return false;
        }
    }

    private StringBuilder getEiscpMessage(String eiscpCmd) {
        logger.debug("Requested Command is '{}' ", eiscpCmd);
        StringBuilder sb = new StringBuilder();
        sb.append("!");
        sb.append("1");
        sb.append(eiscpCmd);
        sb.append((char) 0x0D);
        logger.debug("Serial command is '{}'", sb);
        return sb;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendCommand(String eiscpCmd) {
        String data = getEiscpMessage(eiscpCmd).toString();
        if (in == null || out == null) {
            connectSocket();
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

            String response = sendMessage(data, timeout);
            if (response != null) {
                notifier.notifyMessage(new OnkyoStatusUpdateEvent(this), response.getBytes(), 8);
            }

        } catch (IOException e) {

            logger.debug("IO error occurred...reconnect and resend ones");
            closeSocket();
            connectSocket();

            try {
                String response = sendMessage(data, timeout);
                if (response != null) {
                    notifier.notifyMessage(new OnkyoStatusUpdateEvent(this), response.getBytes(), 8);
                }

            } catch (IOException e1) {
                logger.error("Send failed..{}", e1);
            } catch (EiscpException ee) {
                logger.error("Unable to notify {}", ee);
            }

        } catch (Exception e) {
            logger.error("Send failed..{}", e);
        }
    }

    @Override
    public void serialEvent(SerialPortEvent portEvent) {
        try {
            logger.debug("Received serial port event '{}':'{}'", portEvent.toString(), portEvent.getEventType());
            logger.trace("RXTX library CPU load workaround, sleep forever");
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            logger.warn("Unable to notify", e);
        }
    }

    private String sendMessage(String data, int timeout) throws IOException {
        logger.debug("Sent request...'{}'", data);
        out.write(data.getBytes());
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

                if (resp.length() > 7) {
                    return resp;
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    logger.warn("failed reading response");
                }
            }

            elapsedTime = Math.abs((new Date()).getTime() - startTime);
        }
        return resp;

    }
}
