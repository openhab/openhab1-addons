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
package org.openhab.binding.plcbus.internal.protocol;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.io.NRSerialPort;

/**
 * ByteProvider from SerialPort
 *
 * @author Robin Lenz
 * @since 1.1.0
 */
public class SerialPortByteProvider implements IByteProvider {

    private static Logger logger = LoggerFactory.getLogger(SerialPortGateway.class);
    private NRSerialPort serialPort;

    /**
     * Constructor
     * 
     * @param serialPort
     */
    private SerialPortByteProvider(NRSerialPort serialPort) {
        this.serialPort = serialPort;
    }

    /**
     * Create a new instance
     */
    public static IByteProvider create(NRSerialPort serialPort) {
        return new SerialPortByteProvider(serialPort);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
     * getByte()
     */
    @Override
    public byte getByte() {
        return getBytes(1)[0];
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openhab.binding.vitotronic.internal.protocol.utils.IByteProvider#
     * getBytes(int)
     */
    @Override
    public byte[] getBytes(int count) {
        int counter = 0;

        while (counter < 1000) {
            try {
                InputStream in = serialPort.getInputStream();

                if (serialPort.isConnected() && in.available() >= count) {
                    byte[] result = new byte[count];
                    in.read(result);
                    return result;
                }
            } catch (Exception e) {
                logger.info("error in readthread {}", e.getMessage());
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                logger.error("error while handling thread lifecycle", e);
            }
            counter++;
        }

        return new byte[0];
    }

}
