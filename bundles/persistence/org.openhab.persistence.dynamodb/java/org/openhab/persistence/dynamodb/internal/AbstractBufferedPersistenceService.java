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
package org.openhab.persistence.dynamodb.internal;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import org.openhab.core.items.Item;
import org.openhab.core.persistence.PersistenceService;
import org.openhab.core.types.State;
import org.openhab.core.types.UnDefType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractBufferedPersistenceService<T> implements PersistenceService {

    private static final long BUFFER_OFFER_TIMEOUT_MILLIS = 500;

    private final Logger logger = LoggerFactory.getLogger(AbstractBufferedPersistenceService.class);
    protected BlockingQueue<T> buffer;

    private boolean writeImmediately;

    protected void resetWithBufferSize(int bufferSize) {
        int capacity = Math.max(1, bufferSize);
        buffer = new ArrayBlockingQueue<T>(capacity, true);
        writeImmediately = bufferSize == 0;
    }

    protected abstract T persistenceItemFromState(String name, State state, Date time);

    protected abstract boolean isReadyToStore();

    protected abstract void flushBufferedData();

    @Override
    public void store(Item item) {
        store(item, null);
    }

    @Override
    public void store(Item item, String alias) {
        long storeStart = System.currentTimeMillis();
        String uuid = UUID.randomUUID().toString();
        if (item.getState() instanceof UnDefType) {
            logger.debug("Undefined item state received. Not storing item {}.", item.getName());
            return;
        }
        if (!isReadyToStore()) {
            return;
        }
        if (buffer == null) {
            throw new IllegalStateException("Buffer not initialized with resetWithBufferSize. Bug?");
        }
        Date time = new Date(storeStart);
        String realName = item.getName();
        String name = (alias != null) ? alias : realName;
        State state = item.getState();
        T persistenceItem = persistenceItemFromState(name, state, time);
        logger.trace("store() called with item {}, which was converted to {} [{}]", item, persistenceItem, uuid);
        if (writeImmediately) {
            logger.debug("Writing immediately item {} [{}]", realName, uuid);
            // We want to write everything immediately
            // Synchronous behaviour to ensure buffer does not get full.
            synchronized (this) {
                boolean buffered = addToBuffer(persistenceItem);
                assert buffered;
                flushBufferedData();
            }
        } else {
            long bufferStart = System.currentTimeMillis();
            boolean buffered = addToBuffer(persistenceItem);
            if (buffered) {
                logger.debug("Buffered item {} in {} ms. Total time for store(): {} [{}]", realName,
                        System.currentTimeMillis() - bufferStart, System.currentTimeMillis() - storeStart, uuid);
            } else {
                logger.debug(
                        "Buffer is full. Writing buffered data immediately and trying again. Consider increasing bufferSize");
                // Buffer is full, commit it immediately
                flushBufferedData();
                boolean buffered2 = addToBuffer(persistenceItem);
                if (buffered2) {
                    logger.debug("Buffered item in {} ms (2nd try, flushed buffer in-between) [{}]",
                            System.currentTimeMillis() - bufferStart, uuid);
                } else {
                    // The unlikely case happened -- buffer got full again immediately
                    logger.warn("Buffering failed for the second time -- Too small bufferSize? Discarding data [{}]",
                            uuid);
                }
            }
        }
    }

    protected boolean addToBuffer(T persistenceItem) {
        try {
            return buffer.offer(persistenceItem, BUFFER_OFFER_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            logger.warn("Interrupted when trying to buffer data! Dropping data");
            return false;
        }
    }
}
