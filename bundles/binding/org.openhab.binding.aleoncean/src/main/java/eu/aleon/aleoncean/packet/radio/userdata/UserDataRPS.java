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

import eu.aleon.aleoncean.packet.radio.RadioPacketRPS;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.NUState;
import eu.aleon.aleoncean.packet.radio.RadioPacketRPS.T21State;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataRPS extends UserData {

    public static final int DATA_LENGTH = 1;

    private T21State t21;
    private NUState nu;

    public UserDataRPS() {
        this(T21State.PTM_TYPE2, NUState.NORMALMESSAGE);
    }

    public UserDataRPS(final T21State t21, final NUState nu) {
        super(DATA_LENGTH);

        this.t21 = t21;
        this.nu = nu;
    }

    public UserDataRPS(final byte[] data, final T21State t21, final NUState nu) {
        super(data);
        assert data.length == DATA_LENGTH;

        this.t21 = t21;
        this.nu = nu;
    }

    public T21State getT21State() {
        return t21;
    }

    public void setT21State(final T21State t21) {
        this.t21 = t21;
    }

    public NUState getNUState() {
        return nu;
    }

    public void setNUState(final NUState nu) {
        this.nu = nu;
    }

    @Override
    public RadioPacketRPS generateRadioPacket() {
        final RadioPacketRPS packet = new RadioPacketRPS();
        packet.setUserDataRaw(getUserData());
        packet.setNUState(getNUState());
        packet.setT21State(getT21State());
        return packet;
    }

}
