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

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class UserDataEEPF61001Factory {

    private UserDataEEPF61001Factory() {
    }

    public static UserDataRPS getPacketData(final RadioPacketRPS packet) {
        if (packet.getT21State() == RadioPacketRPS.T21State.PTM_TYPE2 && packet.getNUState() == RadioPacketRPS.NUState.UNASSIGNEDMESSAGE) {
            return new UserDataEEPF61001T2U(packet.getUserDataRaw());
        } else {
            return null;
        }

    }
}
