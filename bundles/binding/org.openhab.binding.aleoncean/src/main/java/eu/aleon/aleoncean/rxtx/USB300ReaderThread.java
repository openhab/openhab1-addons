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

import eu.aleon.aleoncean.util.ThreadUtil;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class USB300ReaderThread extends USB300Reader implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(USB300ReaderThread.class);

    private final Thread workerThread;

    public USB300ReaderThread(final SerialPort serialPort,
                              final BlockingQueue<byte[]> queue,
                              final BlockingQueue<byte[]> queueResponse) throws IOException {
        super(serialPort, queue, queueResponse);

        workerThread = new Thread(null, this, "USB300 reader thread");
    }

    @Override
    public void run() {

        while (running) {
            if (!doRead()) {
                break;
            }
        }
    }

    @Override
    public boolean start() {
        assert workerThread != null;

        try {
            /*
             * Let's set a receive timeout.
             * This is done, so a read on the input stream will not block until something is received.
             * So we could ensure, that the read thread will check the running flag again.
             */
            serialPort.enableReceiveTimeout(1000);
        } catch (final UnsupportedCommOperationException ex) {
            LOGGER.warn("Cannot set receive timeout.", ex);
            return false;
        }

        running = true;
        workerThread.start();

        return true;
    }

    @Override
    protected void stopHandler() {
        try {
            // If we close the input stream, EOF will be read from the reader thread.
            in.close();
        } catch (final IOException ex) {
            LOGGER.warn("Closing input stream failed.", ex);
        }

        ThreadUtil.join(workerThread);
    }

}
