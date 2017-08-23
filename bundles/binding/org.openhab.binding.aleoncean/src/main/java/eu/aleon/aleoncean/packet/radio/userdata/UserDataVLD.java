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

import eu.aleon.aleoncean.packet.radio.RadioPacketVLD;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataVLD extends UserData {

    public static final int DATA_LENGTH_MIN = 1;

    public static final int DATA_LENGTH_MAX = 14;

    public UserDataVLD(final int size) {
        super(size);
        assert size >= DATA_LENGTH_MIN && size <= DATA_LENGTH_MAX;
    }

    public UserDataVLD(final byte[] data) {
        super(data);
        assert data.length >= DATA_LENGTH_MIN && data.length <= DATA_LENGTH_MAX;
    }

    @Override
    public RadioPacketVLD generateRadioPacket() {
        final RadioPacketVLD packet = new RadioPacketVLD();
        packet.setUserDataRaw(getUserData());
        return packet;
    }

}
