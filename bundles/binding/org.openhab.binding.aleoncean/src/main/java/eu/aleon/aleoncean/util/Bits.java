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

public class Bits {

    public static boolean isBitSet(final byte b, final int pos) {
        return (((b & 0xFF) >>> pos) & 1) == 1;
    }

    public static int getBit(final byte b, final int pos) {
        return ((b & 0xFF) >>> pos) & 1;
    }

    public static boolean getBool(final byte b, final int pos) {
        return (((b & 0xFF) >>> pos) & 1) == 1;
    }

    public static boolean isBitSet(final short s, final int pos) {
        return (((s & 0xFFFF) >>> pos) & 1) == 1;
    }

    public static byte setBit(final byte b, final int pos) {
        return (byte) (b | (1 << pos));
    }

    public static byte clrBit(final byte b, final int pos) {
        return (byte) (b & ~(1 << pos));
    }

    public static byte setBit(final byte b, final int pos, final boolean bit) {
        if (bit) {
            return setBit(b, pos);
        } else {
            return clrBit(b, pos);
        }
    }

    public static byte setBit(final byte b, final int pos, final int bit) {
        return setBit(b, pos, bit != 0);
    }

    /**
     * Generate a byte with given bit range set and rest bits unset.
     *
     * @param startBit The bit position the one(s) should begin. The value must
     *                 be between 0 and 7, and be at least as large as @endBit.
     * @param endBit   The bit position the one(s) should begin. The value must be
     *                 between 0 and 7, and must not be greater then @startBit.
     * @return Return a byte with only given bit range set.
     */
    public static byte getSetBits(final int startBit, final int endBit) {
        assert startBit <= 7;
        assert endBit <= 7;
        assert startBit >= endBit;

        final byte mask1 = (byte) ((1 << (startBit + 1)) - 1);
        final byte mask2 = (byte) ((1 << (endBit)) - 1);
        final byte mask = (byte) (mask1 ^ mask2);

        return mask;
    }

    /**
     * Get a range of bits from a byte.
     *
     * @param in       The input byte the bit range should be taken from.
     * @param startBit The bit position the one(s) should begin. The value must
     *                 be between 0 and 7, and be at least as large as @endBit.
     * @param endBit   The bit position the one(s) should begin. The value must be
     *                 between 0 and 7, and must not be greater then @startBit.
     * @param shift    Flag if the output should be shifted, so the extracted range
     *                 started on bit 0.
     * @return Return a range of bits extracted from a input byte.
     */
    public static byte getBitsFromByte(final byte in, final int startBit, final int endBit, final boolean shift) {
        assert startBit <= 7;
        assert endBit <= 7;
        assert startBit >= endBit;

        byte out = (byte) (in & getSetBits(startBit, endBit));
        if (shift) {
            /* We need to do an unsigned byte shift. */
            out = (byte) ((out & 0xFF) >>> endBit);
        }

        return out;
    }

    /**
     * Get a range of bits from a byte array.
     *
     * @param data      The input byte array the bit range should be taken from.
     * @param startByte The byte position the extraction should be begin.
     * @param startBit  The bit position in the start byte the extraction should be begin.
     * @param endByte   The byte position the extraction should be end.
     * @param endBit    The bit position of in the end byte the extraction should be end.
     * @return Return a value that contains the bits in the given range.
     */
    public static long getBitsFromBytes(final byte[] data, final int startByte, final int startBit, final int endByte, final int endBit) {
        long extr = 0;

        if (startByte == endByte) {
            extr = 0xFF & getBitsFromByte(data[startByte], startBit, endBit, true);
        } else {
            for (int i = startByte; i <= endByte; ++i) {
                extr <<= 8;

                if (i == startByte) {
                    /*
                     * Use all bits lower and equal then startBit.
                     */
                    extr |= 0xFF & getBitsFromByte(data[i], startBit, 0, true);
                } else if (i == endByte) {
                    /*
                     * Use all bits higher and equal then endBit.
                     */
                    extr |= 0xFF & getBitsFromByte(data[i], 7, endBit, false);
                    extr >>>= endBit;
                } else {
                    extr |= 0xFF & data[i];
                }
            }
        }

        return extr;
    }

    /**
     * Get a range of Bits from a byte array.
     *
     * @param data   The input byte array the bit range should be taken from.
     * @param offset The start position given in bit (byte 0 bit 7 = 0, byte 0 bit 0 = 7, byte 1 bit 7 = 8, ...).
     * @param size   The number of bits that should be taken.
     * @return Return a value that contains the bits in the given range.
     */
    public static long getBitsFromBytes(final byte[] data, final int offset, final int size) {
        final int startByte = offset / 8;
        final int startBit = 7 - (offset % 8);
        final int absoluteEndBit = offset + size - 1; // do not forget the "- 1", because the end is inclusive.
        final int endByte = absoluteEndBit / 8;
        final int endBit = 7 - (absoluteEndBit % 8);

        return getBitsFromBytes(data, startByte, startBit, endByte, endBit);
    }

    private static long setBitsOfBytesUtil(final long value, final byte[] data, final int pos, final int startBit, final int endBit) {
        final int numOfBits = startBit + 1 - endBit;
        data[pos] &= ~getSetBits(startBit, endBit);
        final long mask = 0xFF & getSetBits(numOfBits - 1, 0);
        data[pos] |= (value & mask) << endBit;
        return value >>> numOfBits;
    }

    /**
     * Set a value in to a range of bits in a byte array.
     * The value must not be larger then bit range could encode.
     *
     * @param value     The value that should be stored in the byte array.
     * @param data      The byte array that should be modified.
     * @param startByte The byte position the insertion should be begin.
     * @param startBit  The bit position in the start byte the insertion should be begin.
     * @param endByte   The byte position the insertion should be end.
     * @param endBit    The bit position of in the end byte the insertion should be end.
     * @throws IllegalArgumentException The value does not fit in the given bit range.
     */
    public static void setBitsOfBytes(final long value, final byte[] data, final int startByte, final int startBit, final int endByte, final int endBit) {
        long val = value;

        if (startByte == endByte) {
            val = setBitsOfBytesUtil(val, data, startByte, startBit, endBit);
        } else {
            for (int i = endByte; i >= startByte; --i) {
                int localStartBit;
                int localEndBit;

                if (i == startByte) {
                    localStartBit = startBit;
                    localEndBit = 0;
                } else if (i == endByte) {
                    localStartBit = 7;
                    localEndBit = endBit;
                } else {
                    localStartBit = 7;
                    localEndBit = 0;
                }

                val = setBitsOfBytesUtil(val, data, i, localStartBit, localEndBit);
            }
        }

        if (val != 0) {
            throw new IllegalArgumentException(String.format("The value (%d) does not fit in the bit range (remaining: %d)", value, val));
        }
    }

    public static void setBitsOfBytes(final long value, final byte[] data, final int offset, final int size) {
        final int startByte = offset / 8;
        final int startBit = 7 - (offset % 8);
        final int absoluteEndBit = offset + size - 1; // do not forget the "- 1", because the end is inclusive.
        final int endByte = absoluteEndBit / 8;
        final int endBit = 7 - (absoluteEndBit % 8);

        setBitsOfBytes(value, data, startByte, startBit, endByte, endBit);
    }

    private Bits() {
    }

}
