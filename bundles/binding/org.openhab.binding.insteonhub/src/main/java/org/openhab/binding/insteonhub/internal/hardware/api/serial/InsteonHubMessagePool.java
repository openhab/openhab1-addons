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
package org.openhab.binding.insteonhub.internal.hardware.api.serial;

/**
 * Pool for INSTEON Hub messages (buffers)
 *
 * @author Eric Thill
 * @since 1.4.0
 */
public class InsteonHubMessagePool {

    private final byte[][] pool;
    private final int messageLength;
    private int size;

    public InsteonHubMessagePool(int capacity, int messageLength) {
        this.messageLength = messageLength;
        pool = new byte[capacity][];
        for (int i = 0; i < capacity; i++) {
            pool[i] = new byte[messageLength];
        }
        size = capacity;
    }

    public int getCapacity() {
        return pool.length;
    }

    public synchronized byte[] checkout() {
        if (size == 0) {
            // no messages in pool, create a new one
            return new byte[messageLength];
        } else {
            return pool[--size];
        }
    }

    public synchronized void checkin(byte[] msg) {
        if (size < getCapacity()) {
            pool[size++] = msg;
        } else {
            // pool at capacity. let it be GC'ed
        }
    }
}
