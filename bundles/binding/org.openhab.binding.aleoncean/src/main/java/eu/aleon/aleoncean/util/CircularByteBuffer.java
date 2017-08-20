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

public class CircularByteBuffer {

    /**
     * Space to save the stored data.
     */
    private final byte[] buffer;

    /**
     * Store the maximum size of data that could be stored.
     */
    private final int size;

    /**
     * Store the length of valid data.
     */
    private int length = 0;

    /**
     * Store the next read position.
     */
    private int readPos = 0;

    /**
     * Store the next write position.
     */
    private int writePos = 0;

    /**
     * Store the marked position.
     */
    private int markPos = 0;

    /**
     * Constructs a new circular byte buffer.
     *
     * @param size The maximum number of data, that should be stored in the buffer.
     */
    public CircularByteBuffer(final int size) {
        this.size = size;
        buffer = new byte[size];
    }

    private int incrPos(final int pos) {
        return pos < size - 1 ? pos + 1 : 0;
    }

    private int decrPos(final int pos) {
        return pos > 0 ? pos - 1 : size - 1;
    }

    public int size() {
        return size;
    }

    public int length() {
        return length;
    }

    public boolean isEmpty() {
        return length == 0;
    }

    public void clear() {
        readPos = 0;
        writePos = 0;
        length = 0;
    }

    public byte get() {
        final byte b = buffer[readPos];
        readPos = incrPos(readPos);
        --length;
        return b;
    }

    public void put(final byte b) {
        buffer[writePos] = b;
        writePos = incrPos(writePos);
        ++length;
    }

    public void setMarkToLastReadPos() {
        markPos = decrPos(readPos);
    }

    public boolean setReadPosToNextValue(final byte b) {
        while (length > 0) {
            if (buffer[readPos] == b) {
                return true;
            } else {
                readPos = incrPos(readPos);
                --length;
            }
        }

        return false;
    }

    public byte[] getAllFromMarkToLastRead() {
        int sz = readPos - markPos;
        if (sz <= 0) {
            sz += size;
        }

        int pos = markPos;
        final byte[] d = new byte[sz];
        for (int i = 0; i < sz; ++i) {
            d[i] = buffer[pos];
            pos = incrPos(pos);
        }

        return d;
    }
}
