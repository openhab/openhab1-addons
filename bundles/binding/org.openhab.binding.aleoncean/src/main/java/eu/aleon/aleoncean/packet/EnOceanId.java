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
package eu.aleon.aleoncean.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public final class EnOceanId implements Comparable<EnOceanId> {

    public static final int LENGTH = 4;

    private static final long BASEID_MIN = 0xFF800000L;
    //private static final long BASEID_MAX = 0xFFFFFF80L;
    private static final long BASEID_MAX = 0xFFFFFFFEL;

    private static final long BROADCAST_ID = 0xFFFFFFFFL;

    public static final EnOceanId getBroadcast() {
        return new EnOceanId(BROADCAST_ID);
    }

    private final byte[] id = new byte[LENGTH];

    public EnOceanId() {
    }

    public EnOceanId(final EnOceanId id) {
        this(id.getBytes());
    }

    public EnOceanId(final byte a, final byte b, final byte c, final byte d) {
        fill(new byte[]{a, b, c, d});
    }

    public EnOceanId(final byte[] d, final int startPos) {
        fill(d, startPos);
    }

    public EnOceanId(final byte[] id) {
        fill(id);
    }

    public EnOceanId(final ByteBuffer id) {
        fill(id);
    }

    public EnOceanId(final long id) {
        fill(id);
    }

    public EnOceanId(final String id) throws IllegalArgumentException, NumberFormatException {
        fill(id);
    }

    public void fill(final EnOceanId id) {
        fill(id.getBytes());
    }

    public void fill(final byte[] id) {
        fill(id, 0);
    }

    public void fill(final byte[] d, final int startPos) {
        System.arraycopy(d, startPos, id, 0, id.length);
    }

    public void fill(final ByteBuffer bb) {
        bb.get(id);
    }

    public void fill(final long id) {
        this.id[0] = (byte) ((id & 0xFF000000L) >>> 24);
        this.id[1] = (byte) ((id & 0x00FF0000L) >>> 16);
        this.id[2] = (byte) ((id & 0x0000FF00L) >>> 8);
        this.id[3] = (byte) (id & 0x000000FFL);
    }

    public void fill(final String id) throws IllegalArgumentException, NumberFormatException {
        final String[] numbers = id.trim().split("[:]");
        if (numbers.length != this.id.length) {
            throw new IllegalArgumentException("Invalid EnOceanId string.");
        }

        for (int i = 0; i < this.id.length; ++i) {
            final int parsedInteger = Integer.parseInt(numbers[i], 16);
            if (parsedInteger < 0 || parsedInteger > 255) {
                throw new NumberFormatException(String.format("Value out of range (0x%s => %d).", numbers[i], parsedInteger));
            }
            this.id[i] = (byte) parsedInteger;
        }
    }

    public byte[] getBytes() {
        return id;
    }

    public long getLong() {
        final long l0 = id[0] & 0xFF;
        final long l1 = id[1] & 0xFF;
        final long l2 = id[2] & 0xFF;
        final long l3 = id[3] & 0xFF;

        final long l = l0 << 24 | l1 << 16 | l2 << 8 | l3;

        return l;
    }

    public boolean isBaseId() {
        /*
         * The valid range for base ID is between 0xFF800000 and 0xFFFFFF80
         */

        return isBaseId(this);
    }

    public static boolean isBaseId(final EnOceanId id) {
        final long baseId = id.getLong();
        return baseId >= BASEID_MIN && baseId <= BASEID_MAX;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EnOceanId other = (EnOceanId) obj;
        return Arrays.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(id);
    }

    @Override
    public String toString() {
        return String.format("%02X:%02X:%02X:%02X", id[0], id[1], id[2], id[3]);
    }

    @Override
    public int compareTo(final EnOceanId o) {
        for (int i = 0; i < id.length; ++i) {
            if (id[i] < o.id[i]) {
                return -1;
            } else if (id[i] > o.id[i]) {
                return 1;
            }
        }

        return 0;
    }

}
