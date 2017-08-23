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
package eu.aleon.aleoncean.packet.radio.userdata.eepd201;

import eu.aleon.aleoncean.util.Bits;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPD201CMD06 extends UserDataEEPD201 {

    public static final byte CMD = UserDataEEPD201.CMD_ACTUATOR_MEASUREMENT_QUERY;

    private static final int DATA_LENGTH = 2;

    // Query
    private static final int OFFSET_QU = 10;
    private static final int LENGTH_QU = 1;

    // I/O channel
    private static final int OFFSET_IO = 11;
    private static final int LENGTH_IO = 5;

    public UserDataEEPD201CMD06() {
        super(CMD, DATA_LENGTH);
    }

    public UserDataEEPD201CMD06(final byte[] data) {
        super(CMD, data);
        assert data.length == DATA_LENGTH;
    }

    public Query getQuery() {
        return Query.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_QU, LENGTH_QU));
    }

    public void setQuery(final Query value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_QU, LENGTH_QU);
    }

    public IOChannel getIOChannel() {
        return IOChannel.fromByte((byte) Bits.getBitsFromBytes(getUserData(), OFFSET_IO, LENGTH_IO));
    }

    public void setIOChannel(final IOChannel value) {
        Bits.setBitsOfBytes(value.toByte(), getUserData(), OFFSET_IO, LENGTH_IO);
    }

}
