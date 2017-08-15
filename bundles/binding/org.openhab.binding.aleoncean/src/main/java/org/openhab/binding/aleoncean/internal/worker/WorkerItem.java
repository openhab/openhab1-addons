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
package org.openhab.binding.aleoncean.internal.worker;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Markus Rathgeb <maggu2810@gmail.com>
 */
public class WorkerItem {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkerItem.class);

    private final BlockingQueue<WorkerReply> replyQueue = new LinkedBlockingQueue<>(1);

    public WorkerReply waitForReply(final long timeout, final TimeUnit unit) {
        WorkerReply reply;

        try {
            reply = replyQueue.poll(timeout, unit);
        } catch (final InterruptedException ex) {
            LOGGER.warn("Exception while reply poll.", ex);
            reply = null;
        }

        return reply != null ? reply : new WorkerReply(WorkerReplyCode.TIMEOUT);
    }

    public void reply(final WorkerReply reply) {
        replyQueue.add(reply);
    }
}
