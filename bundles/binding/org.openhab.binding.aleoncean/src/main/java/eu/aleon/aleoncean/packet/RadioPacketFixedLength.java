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

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RadioPacketFixedLength extends RadioPacket {

    private final int userDataLength;

    private final byte[] userData;

    public RadioPacketFixedLength(final int userDataLength, final byte choice) {
        super(choice);
        this.userDataLength = userDataLength;
        this.userData = new byte[userDataLength];
    }

    @Override
    public final void setUserDataRaw(final byte[] userData) {
        assert userData.length == userDataLength;
        System.arraycopy(userData, 0, this.userData, 0, this.userDataLength);
    }

    @Override
    public final byte[] getUserDataRaw() {
        return userData;
    }
}
