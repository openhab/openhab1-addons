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
public class UserDataEEPF60201Factory {

    private UserDataEEPF60201Factory() {
    }

    public static UserDataRPS getPacketData(final RadioPacketRPS packet) {
        if (packet.getT21State() == T21State.PTM_TYPE2 && packet.getNUState() == NUState.NORMALMESSAGE) {
            return new UserDataEEPF60201T2N(packet.getUserDataRaw());
        } else if (packet.getT21State() == T21State.PTM_TYPE2 && packet.getNUState() == NUState.UNASSIGNEDMESSAGE) {
            return new UserDataEEPF60201T2U(packet.getUserDataRaw());
        } else {
            return null;
        }
    }

}
