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
import java.io.UnsupportedEncodingException;
import java.util.TooManyListenersException;

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
 * A class for the communication with the Visonic alarm panel through a serial connection
 *
 * @author Laurent Garnier
 * @since 1.9.0
 */
public class PowerMaxSerialConnector extends PowerMaxConnector {

    private static final Logger logger = LoggerFactory.getLogger(PowerMaxSerialConnector.class);

    private final String serialPortName;
    private final int baudRate;
    private SerialPort serialPort;

    /**
     * Constructor
     *
     * @param serialPortName
     *            the serial port name
     * @param baudRate
     *            the baud rate to be used
     */
    public PowerMaxSerialConnector(String serialPortName, int baudRate) {
        this.serialPortName = serialPortName;
        this.baudRate = baudRate;
        this.serialPort = null;
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public void open() {
        logger.debug("open(): Opening Serial Connection");

        try {

            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(serialPortName);
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);

            serialPort = (SerialPort) commPort;
            serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);
            serialPort.enableReceiveThreshold(1);
            serialPort.disableReceiveTimeout();

            setInput(serialPort.getInputStream());
            setOutput(serialPort.getOutputStream());

            getOutput().flush();
            if (getInput().markSupported()) {
                getInput().reset();
            }

            setReaderThread(new SerialReaderThread(getInput(), this));
            getReaderThread().start();

            setConnected(true);
        } catch (NoSuchPortException noSuchPortException) {
            logger.debug("open(): No Such Port Exception: {}", noSuchPortException.getMessage());
            setConnected(false);
        } catch (PortInUseException portInUseException) {
            logger.debug("open(): Port in Use Exception: {}", portInUseException.getMessage());
            setConnected(false);
        } catch (UnsupportedCommOperationException unsupportedCommOperationException) {
            logger.debug("open(): Unsupported Comm Operation Exception: {}",
                    unsupportedCommOperationException.getMessage());
            setConnected(false);
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            logger.debug("open(): Unsupported Encoding Exception: {}", unsupportedEncodingException.getMessage());
            setConnected(false);
        } catch (IOException ioException) {
            logger.debug("open(): IO Exception: ", ioException.getMessage());
            setConnected(false);
        }
    }

    /**
     * {@inheritDoc}
     **/
    @Override
    public void close() {
        logger.debug("close(): Closing Serial Connection");

        super.cleanup();

        if (serialPort != null) {
            serialPort.close();
        }

        serialPort = null;

        setConnected(false);
    }

    /**
     * A class that handles the reading of messages through the serial connection
     * It is an extend of the common class PowerMaxReaderThread
     */
    public class SerialReaderThread extends PowerMaxReaderThread implements SerialPortEventListener {
        public SerialReaderThread(InputStream in, PowerMaxConnector connector) {
            super(in, connector);
        }

        @Override
        public void run() {
            logger.debug("Data listener started");

            // RXTX serial port library causes high CPU load
            // Start event listener, which will just sleep and slow down event
            // loop
            try {
                serialPort.addEventListener(this);
                serialPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {
                logger.debug("Too Many Listeners Exception: {}", e.getMessage());
            }

            super.run();

            serialPort.removeEventListener();

            logger.debug("Data listener stopped");
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            try {
                logger.trace("RXTX library CPU load workaround, sleep forever");
                sleep(Long.MAX_VALUE);
            } catch (InterruptedException e) {
            }
        }
    }

}
