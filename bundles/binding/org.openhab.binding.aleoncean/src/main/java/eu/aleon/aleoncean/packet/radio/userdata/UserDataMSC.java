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

import eu.aleon.aleoncean.packet.radio.RadioPacketMSC;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataMSC extends UserData {

    /*
     * The EEP 2.6 specification chapter "3.12) MSC telegram - Manufacturer Specific Command"
     * states, that the first 1.5 bytes of the user data code the manufacturer ID, followed by
     * 1 .. 12.5 variable data.
     * So, we could transmit only complete bytes, so we use a minimum of 2 bytes user data.
     */
    public static final int DATA_LENGTH_MIN = 2;

    public static final int DATA_LENGTH_MAX = 14;

    public UserDataMSC(final int size) {
        super(size);
        assert size >= DATA_LENGTH_MIN && size <= DATA_LENGTH_MAX;
    }

    public UserDataMSC(final byte[] data) {
        super(data);
        assert data.length >= DATA_LENGTH_MIN && data.length <= DATA_LENGTH_MAX;
    }

    @Override
    public RadioPacketMSC generateRadioPacket() {
        final RadioPacketMSC packet = new RadioPacketMSC();
        packet.setUserDataRaw(getUserData());
        return packet;
    }

}
