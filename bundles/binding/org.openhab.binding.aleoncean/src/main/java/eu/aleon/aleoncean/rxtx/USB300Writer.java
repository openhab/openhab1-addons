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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class USB300Writer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(USB300Writer.class);

    private volatile boolean running = true;

    private final DataOutputStream out;
    private final BlockingQueue<byte[]> queue;

    public USB300Writer(final OutputStream out, final BlockingQueue<byte[]> queue) {
        this.out = new DataOutputStream(out);
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            out.flush();
        } catch (final IOException ex) {
            LOGGER.warn("Flush failed.", ex);
        }

        while (running) {
            final byte[] data;
            try {
                data = queue.take();

                /*
                 * running could be set to false if an interruption occurs.
                 * It could also be set to false through the stop function.
                 * To realize a 'stop' a dummy object is written to the queue, so the take above returns.
                 *
                 * If we are woken up (or an exception was raised) and we should stop, break.
                 */
                if (running) {
                    out.write(data);
                    out.flush();
                }
            } catch (final InterruptedException ex) {
                LOGGER.warn("Take data from queue failed.", ex);
                running = false;
            } catch (final IOException ex) {
                LOGGER.warn("Write or flush failed.", ex);
            }
        }
    }

    public void stop() {
        running = false;
        // Wakeup running thread.
        queue.add(new byte[0]);
    }
}
