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
package eu.aleon.aleoncean.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class ThreadUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadUtil.class);

    public static void sleep(final long millis) {
        long start;
        long end;
        long remaining = millis;
        do {
            start = System.nanoTime();
            try {
                Thread.sleep(remaining);
                remaining = 0;
            } catch (final InterruptedException ex) {
                end = System.nanoTime();
                remaining -= (end - start) / 1000000;
            }
        } while (remaining > 0);
    }

    public static void join(final Thread thread) {
        boolean done;

        LOGGER.debug("Wait for thread: {}", thread.getName());
        done = false;
        do {
            try {
                thread.join();
                done = true;
            } catch (final InterruptedException ex) {
                LOGGER.debug("Wait for thread interrupted: {}", thread.getName());
            }
        } while (!done);
        LOGGER.debug("Wait for thread done: {}", thread.getName());
    }

    private ThreadUtil() {
    }
}
