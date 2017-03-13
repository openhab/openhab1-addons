/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.wr3223.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Connector implementation for a serial port connection to WR3223.
 *
 * @author Michael Fraefel
 *
 */
public class SerialWR3223Connector extends AbstractWR3223Connector {

    private static final Logger logger = LoggerFactory.getLogger(SerialWR3223Connector.class);

    private SerialPort serialPort;

    /**
     * Connect to WR2332 over serial port.
     *
     * @param port
     * @param baud
     * @throws IOException
     */
    public void connect(String port, int baud) throws IOException {
        // parse ports and if the default port is found, initialized the reader
        CommPortIdentifier portId = null;
        Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
            if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                if (id.getName().equals(port)) {
                    portId = id;
                }
            }
        }
        if (portId != null) {
            // initialize serial port
            try {

                serialPort = portId.open("wr3223", 2000);
            } catch (PortInUseException e) {
                throw new IOException("Serial port '" + port + "' is already in use.", e);
            }
            try {
                // set port parameters
                serialPort.setSerialPortParams(baud, SerialPort.DATABITS_7, SerialPort.STOPBITS_1,
                        SerialPort.PARITY_EVEN);
            } catch (UnsupportedCommOperationException e) {
                throw new IOException("Serial port '" + port
                        + "' doesn't support the configuration 7 data bit, 1 stop bit and parity even.", e);
            }
            if (!serialPort.isReceiveTimeoutEnabled()) {
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Add a receive timeout of 2000ms.");
                    }
                    serialPort.enableReceiveTimeout(2000);
                } catch (UnsupportedCommOperationException e) {
                    logger.warn("Error by adding receive timeout.", e);
                }
            }
            DataInputStream inputStream = new DataInputStream(serialPort.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(serialPort.getOutputStream());
            connect(inputStream, outputStream);

        } else {
            StringBuilder sb = new StringBuilder();
            portList = CommPortIdentifier.getPortIdentifiers();
            while (portList.hasMoreElements()) {
                CommPortIdentifier id = (CommPortIdentifier) portList.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    sb.append(id.getName() + "\n");
                }
            }
            throw new IOException(
                    "Serial port '" + port + "' could not be found. Available ports are:\n" + sb.toString());
        }
    }

    @Override
    public void close() throws IOException {
        super.close();
        if (serialPort != null) {
            serialPort.close();
            serialPort = null;
        }
    }

}
