/*
 * Copyright (c) 2014 aleon GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Note for all commercial users of this library:
 * Please contact the EnOcean Alliance (http://www.enocean-alliance.org/)
 * about a possible requirement to become member of the alliance to use the
 * EnOcean protocol implementations.
 *
 * Contributors:
 *    Markus Rathgeb - initial API and implementation and/or initial documentation
 */
package eu.aleon.aleoncean.rxtx;

import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class USB300ReaderListener extends USB300Reader implements SerialPortEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(USB300ReaderListener.class);

    public USB300ReaderListener(final SerialPort serialPort,
                                final BlockingQueue<byte[]> queue,
                                final BlockingQueue<byte[]> queueResponse) throws IOException {
        super(serialPort, queue, queueResponse);
    }

    @Override
    public boolean start() {
        try {
            serialPort.addEventListener(this);
        } catch (final TooManyListenersException ex) {
            LOGGER.warn("Cannot add event listener.", ex);
            return false;
        }

        serialPort.notifyOnDataAvailable(true);

        running = true;

        return true;
    }

    @Override
    protected void stopHandler() {
        serialPort.removeEventListener();
    }

    @Override
    public void serialEvent(final SerialPortEvent ev) {
        switch (ev.getEventType()) {
            case SerialPortEvent.BI:
            case SerialPortEvent.OE:
            case SerialPortEvent.FE:
            case SerialPortEvent.PE:
            case SerialPortEvent.CD:
            case SerialPortEvent.CTS:
            case SerialPortEvent.DSR:
            case SerialPortEvent.RI:
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
                break;

            case SerialPortEvent.DATA_AVAILABLE:
                try {
                    while (in.available() > 0) {
                        if (!doRead()) {
                            break;
                        }
                    }
                } catch (final IOException ex) {
                    LOGGER.warn("I/O exception on read.", ex);
                }
                break;

            default:
                LOGGER.warn("Unknown case.");
                break;
        }
    }

}
