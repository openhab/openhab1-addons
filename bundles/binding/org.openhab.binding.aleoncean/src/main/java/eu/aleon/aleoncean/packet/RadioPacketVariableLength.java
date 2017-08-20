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
public class RadioPacketVariableLength extends RadioPacket {

    private final int userDataLengthMin;

    private final int userDataLengthMax;

    public RadioPacketVariableLength(final int userDataLengthMin, final int userDataLengthMax, final byte choice) {
        super(choice);
        this.userDataLengthMin = userDataLengthMin;
        this.userDataLengthMax = userDataLengthMax;
    }

    @Override
    public final void setUserDataRaw(final byte[] userData) {
        assert userData.length >= userDataLengthMin;
        assert userData.length <= userDataLengthMax;
        super.setUserDataRaw(userData);
    }

    @Override
    public final byte[] getUserDataRaw() {
        return super.getUserDataRaw();
    }

}
