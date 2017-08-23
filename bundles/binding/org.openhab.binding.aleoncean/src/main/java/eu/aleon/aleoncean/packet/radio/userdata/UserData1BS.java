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

import eu.aleon.aleoncean.packet.radio.RadioPacket1BS;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserData1BS extends UserData {

    public static final int DATA_LENGTH = 1;

    public UserData1BS() {
        super(DATA_LENGTH);
    }

    public UserData1BS(final byte[] data) {
        super(data);
        assert data.length == DATA_LENGTH;
    }

    public boolean isTeachIn() {
        return isTeachIn(getUserData());
    }

    public static boolean isTeachIn(final byte[] userData) {
        return getDataBit(userData, 0, 3) == 0;
    }

    @Override
    public RadioPacket1BS generateRadioPacket() {
        final RadioPacket1BS packet = new RadioPacket1BS();
        packet.setUserDataRaw(getUserData());
        return packet;
    }

}
