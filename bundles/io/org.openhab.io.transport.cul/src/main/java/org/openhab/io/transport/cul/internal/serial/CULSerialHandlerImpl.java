/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.io.transport.cul.internal.serial;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.TooManyListenersException;

import org.openhab.io.transport.cul.CULDeviceException;
import org.openhab.io.transport.cul.internal.AbstractCULHandler;
import org.openhab.io.transport.cul.internal.CULConfig;
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
 * Implementation for culfw based devices which communicate via serial port
 * (cullite for example). This is based on rxtx and assumes constant parameters
 * for the serial port.
 *
 * @author Till Klocke
 * @since 1.4.0
 */
public class CULSerialHandlerImpl extends AbstractCULHandler<CULSerialConfig> implements SerialPortEventListener {

    private final static Logger logger = LoggerFactory.getLogger(CULSerialHandlerImpl.class);

    private SerialPort serialPort;

    private BufferedWriter bw;
    private BufferedReader br;

    /**
     * Constructor including property map for specific configuration.
     *
     * @param config
     *            Configuration for this implementation.
     */
    public CULSerialHandlerImpl(CULConfig config) {
        super((CULSerialConfig) config);
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            synchronized (serialPort) {
                try {
                    if (br == null) {
                        logger.warn("BufferedReader for serial connection is null");
                    } else {
                        String line = br.readLine();
                        logger.trace("Serial event: received '{}'", line);
                        processNextLine(line);
                    }
                } catch (IOException e) {
                    logger.warn("Can't read from serial device {}", config.getDeviceName(), e);
                    tryReopenHardware();
                }
            }
        }
    }

    @Override
    protected void openHardware() throws CULDeviceException {
        String deviceName = config.getDeviceAddress();
        logger.debug("Opening serial CUL connection for {}", deviceName);
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(deviceName);
            if (portIdentifier.isCurrentlyOwned()) {
                throw new CULDeviceException(
                        "The port " + deviceName + " is currenty used by " + portIdentifier.getCurrentOwner());
            }
            CommPort port = portIdentifier.open(this.getClass().getName(), 2000);
            if (!(port instanceof SerialPort)) {
                throw new CULDeviceException("The device " + deviceName + " is not a serial port");
            }
            serialPort = (SerialPort) port;

            serialPort.setSerialPortParams(config.getBaudRate(), SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    config.getParityMode());
            InputStream is = serialPort.getInputStream();
            OutputStream os = serialPort.getOutputStream();
            synchronized (serialPort) {
                br = new BufferedReader(new InputStreamReader(is));
                bw = new BufferedWriter(new OutputStreamWriter(os));
            }

            serialPort.notifyOnDataAvailable(true);
            logger.debug("Adding serial port event listener");
            serialPort.addEventListener(this);
        } catch (NoSuchPortException e) {
            throw new CULDeviceException(e);
        } catch (PortInUseException e) {
            throw new CULDeviceException(e);
        } catch (UnsupportedCommOperationException e) {
            throw new CULDeviceException(e);
        } catch (IOException e) {
            throw new CULDeviceException(e);
        } catch (TooManyListenersException e) {
            throw new CULDeviceException(e);
        }

    }

    @Override
    protected void closeHardware() {
        logger.debug("Closing serial device {}", config.getDeviceAddress());
        if (serialPort != null) {
            serialPort.removeEventListener();
        }
        try {
            if (br != null) {
                br.close();
            }
            if (bw != null) {
                bw.close();
            }
        } catch (IOException e) {
            logger.warn("Can't close the input and output streams properly", e);
        } finally {
            if (serialPort != null) {
                serialPort.close();
            }
        }
    }

    private void tryReopenHardware() {
        closeHardware();
        try {
            openHardware();
        } catch (CULDeviceException e) {
            logger.warn("Failed to reopen serial connection after connection error", e);
        }
    }

    @Override
    protected void write(String command) {
        try {
            synchronized (serialPort) {
                if (bw == null) {
                    logger.warn("BufferedWriter for serial connection is null");
                } else {
                    logger.trace("Write serial: {}", command);
                    bw.write(command);
                    bw.flush();
                }
            }
        } catch (IOException e) {
            logger.warn("Can't write to serial device {}", config.getDeviceName(), e);
            tryReopenHardware();
        }
    }
}
