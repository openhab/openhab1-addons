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

import java.io.EOFException;
import java.nio.BufferOverflowException;

/**
 * A buffer that operates bytes and support put and fetch operations.
 */
public abstract class ByteBuffer {

    /**
     * Clears the buffer.
     */
    public abstract void clear();

    /**
     * Get the maximum / whole size of the buffer.
     *
     * @return Return the number of bytes that the buffer is able to hold.
     */
    public abstract int size();

    /**
     * Get the number of used bytes.
     *
     * @return Return the number of used bytes.
     */
    public abstract int length();

    /**
     * Check if the content of the buffer is empty.
     *
     * @return Return true if the buffer contains no data.
     */
    public boolean isEmpty() {
        return length() == 0;
    }

    /**
     * Check if the buffer is full.
     *
     * @return Return true if the buffer is full.
     */
    public boolean isFull() {
        return length() == size();
    }

    /**
     * Fetch the next available byte.
     *
     * @return Return the next byte from the buffer.
     * @throws EOFException if the buffer is empty.
     */
    public abstract byte get() throws EOFException;

    /**
     * Fetch content from the buffer.
     *
     * @param buffer The buffer that should be filled with the buffer content.
     * @return Return the length of data put into the supplied buffer.
     */
    public int get(final byte[] buffer) {
        return get(buffer, 0, buffer.length);
    }

    /**
     * Fetch content the buffer.
     *
     * @param buffer The buffer that should be filled.
     * @param offset The position in the buffer the data should be filled to.
     * @param length The number of bytes that should be inserted into the buffer.
     * @return Return the length of data put into the supplied buffer.
     */
    public int get(final byte[] buffer, final int offset, final int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }

        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }

        if (offset + length > buffer.length) {
            throw new IllegalArgumentException("offset + length > buf.length");
        }

        if (length == 0) {
            return 0;
        }

        int count = 0;
        int off = offset;
        int len = length;
        try {
            while (len > 0) {
                buffer[off] = get();
                ++count;
                ++off;
                --len;
            }
        } catch (final EOFException ex) {
            // Nothing to do, the end of the buffer is reached.
        }
        return count;
    }

    /**
     * Fetch a four byte value from the buffer.
     *
     * @return Return a 32-bit integer (little-endian) that is read from the buffer.
     * @throws EOFException if the buffer does not contain at least four bytes.
     */
    public int getInt() throws EOFException {
        final int b0 = get() & 0xFF;
        final int b1 = get() & 0xFF;
        final int b2 = get() & 0xFF;
        final int b3 = get() & 0xFF;
        return (b3 << 24) | (b2 << 16) | (b1 << 8) | b0;
    }

    /**
     * Put a byte into the buffer.
     *
     * @param b The byte that should be inserted into the buffer.
     * @throws BufferOverflowException if the buffer is already full.
     */
    public abstract void put(byte b) throws BufferOverflowException;

    /**
     * Put a number of bytes into the buffer.
     *
     * @param buffer The data that should be inserted into the buffer.
     * @return Return the number of bytes copied into the buffer.
     */
    public int put(final byte[] buffer) {
        return put(buffer, 0, buffer.length);
    }

    /**
     * Put a number of bytes into the buffer
     *
     * @param buffer The array containing data that should be inserted into the buffer.
     * @param offset The position in the buffer, the insertion should be started.
     * @param length The number of bytes, that should be inserted into the buffer.
     * @return Return the number of bytes copied into the buffer.
     */
    public int put(final byte[] buffer, final int offset, final int length) {
        if (offset < 0) {
            throw new IllegalArgumentException("offset < 0");
        }

        if (length < 0) {
            throw new IllegalArgumentException("length < 0");
        }

        if (offset + length > buffer.length) {
            throw new IllegalArgumentException("offset + length > buf.length");
        }

        if (length == 0) {
            return 0;
        }

        int count = 0;
        int off = offset;
        int len = length;
        try {
            while (len > 0) {
                put(buffer[off]);
                ++count;
                ++off;
                --len;
            }
        } catch (final BufferOverflowException e) {
            // Nothing to do, the end of the buffer is reached.
        }
        return count;
    }

    /**
     * Put a four byte value into the buffer.
     *
     * @param x A 32-bit integer that is put (little-endian) to the buffer.
     * @throws BufferOverflowException if the buffer could not be filled with all four bytes.
     */
    public void putInt(final int x) throws BufferOverflowException {
        put((byte) x);
        put((byte) (x >>> 8));
        put((byte) (x >>> 16));
        put((byte) (x >>> 24));
    }
}
