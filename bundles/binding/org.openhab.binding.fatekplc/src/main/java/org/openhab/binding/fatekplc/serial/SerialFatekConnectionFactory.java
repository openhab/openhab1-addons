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
package org.openhab.binding.fatekplc.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import org.simplify4u.jfatek.io.FatekConfig;
import org.simplify4u.jfatek.io.FatekConnection;
import org.simplify4u.jfatek.io.FatekConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

/**
 * Serial connection for Fatek PLC binding.
 *
 * @author Slawomir Jaranowski
 * @since 1.9.0
 */
public class SerialFatekConnectionFactory implements FatekConnectionFactory {

    private static final Logger logger = LoggerFactory.getLogger(SerialFatekConnectionFactory.class);

    private static final String SCHEMA_NAME = "serial";

    class Connection extends FatekConnection {

        private SerialPort serialPort;

        Connection(FatekConfig fatekConfig) throws IOException, PortInUseException, UnsupportedCommOperationException {
            super(fatekConfig);

            final CommPortIdentifier commPortIdentifier = findComPort();

            serialPort = commPortIdentifier.open("openHAB-FatekPLC", 2000);
            serialPort.setSerialPortParams(getBaud(), getDataBits(), getStopBits(), getParity());
            serialPort.enableReceiveTimeout(getTimeout());

            logger.info("New connection to: {} opened", serialPort.getName());
        }

        private int getBaud() {
            return getParamAsInt("baudRate").orElse(9600);
        }

        private int getDataBits() {
            return getParamAsInt("dataBits").orElse(SerialPort.DATABITS_7);
        }

        private int getStopBits() throws IOException {
            final Optional<String> stopBits = getParam("stopBits");
            switch (stopBits.orElse("1")) {
                case "1":
                    return SerialPort.STOPBITS_1;
                case "2":
                    return SerialPort.STOPBITS_2;
                case "1.5":
                    return SerialPort.STOPBITS_1_5;
                default:
                    throw new IOException("Unknown stopBits=" + stopBits);
            }
        }

        private int getParity() throws IOException {
            final Optional<String> parity = getParam("parity");
            switch (parity.map(String::toUpperCase).orElse("EVEN")) {
                case "NONE":
                    return SerialPort.PARITY_NONE;
                case "ODD":
                    return SerialPort.PARITY_ODD;
                case "EVEN":
                    return SerialPort.PARITY_EVEN;
                case "MARK":
                    return SerialPort.PARITY_MARK;
                case "SPACE":
                    return SerialPort.PARITY_SPACE;
                default:
                    throw new IOException("Unknown parity=" + parity);
            }
        }

        @Override
        protected InputStream getInputStream() throws IOException {
            return serialPort.getInputStream();
        }

        @Override
        protected OutputStream getOutputStream() throws IOException {
            return serialPort.getOutputStream();
        }

        @Override
        protected void closeConnection() throws IOException {
            serialPort.close();
            logger.info("Connection to: {} closed", serialPort.getName());
            serialPort = null;
        }

        @Override
        public boolean isConnected() {
            return serialPort != null;
        }

        private CommPortIdentifier findComPort() throws IOException {

            // list all available serial ports
            final List<String> availableNames = new ArrayList<>();
            final Enumeration portIdentifiers = CommPortIdentifier.getPortIdentifiers();
            while (portIdentifiers.hasMoreElements()) {
                final CommPortIdentifier id = (CommPortIdentifier) portIdentifiers.nextElement();
                if (id.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    availableNames.add(id.getName());
                }
            }

            final String portNameCandidate = getFullName();
            // find first matching port name, on some system usb serial port can have suffix depends on which usb port we use
            final Optional<String> portName = availableNames.stream().filter(name -> name.startsWith(portNameCandidate)).findFirst();

            try {
                return CommPortIdentifier.getPortIdentifier(portName.orElseThrow(NoSuchPortException::new));
            } catch (NoSuchPortException e) {
                throw new IOException("Serial port '" + portNameCandidate + "' could not be found. Available ports are:\n" + availableNames);
            }
        }
    }

    @Override
    public FatekConnection getConnection(FatekConfig fatekConfig) throws IOException {
        try {
            return new Connection(fatekConfig);
        } catch (PortInUseException | UnsupportedCommOperationException e) {
            throw new IOException(e);
        }
    }
    @Override
    public String getSchema() {
        return SCHEMA_NAME;
    }

}
