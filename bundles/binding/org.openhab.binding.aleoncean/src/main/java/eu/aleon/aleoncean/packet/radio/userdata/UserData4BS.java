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
package eu.aleon.aleoncean.packet.radio.userdata;

import eu.aleon.aleoncean.packet.radio.RadioPacket4BS;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserData4BS extends UserData {

    public static final int DATA_LENGTH = 4;

    private static final int TEACH_IN_DB = 0;
    private static final int TEACH_IN_BIT = 3;

    public static boolean isTeachIn(final byte[] userData) {
        return getDataBit(userData, TEACH_IN_DB, TEACH_IN_BIT) == 0;
    }

    public UserData4BS() {
        super(DATA_LENGTH);
    }

    public UserData4BS(final byte[] data) {
        super(data);
        assert data.length == DATA_LENGTH;
    }

    public boolean isTeachIn() {
        return isTeachIn(getUserData());
    }

    public void setTeachIn(final boolean teachIn) {
        setDataBit(TEACH_IN_DB, TEACH_IN_BIT, teachIn ? 0 : 1);
    }

    @Override
    public RadioPacket4BS generateRadioPacket() {
        final RadioPacket4BS packet = new RadioPacket4BS();
        packet.setUserDataRaw(getUserData());
        return packet;
    }

    @Override
    public String toString() {
        return String.format("UserData4BS{%s, teachIn=%b}",
                             super.toString(),
                             isTeachIn());
    }

}
