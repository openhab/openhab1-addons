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
package eu.aleon.aleoncean.packet.radio;

import eu.aleon.aleoncean.packet.RadioChoice;
import eu.aleon.aleoncean.packet.RadioPacketFixedLength;
import eu.aleon.aleoncean.packet.radio.userdata.UserData4BS;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RadioPacket4BS extends RadioPacketFixedLength {

    public RadioPacket4BS() {
        super(UserData4BS.DATA_LENGTH, RadioChoice.RORG_4BS);
    }

    public boolean isTeachIn() {
        return UserData4BS.isTeachIn(getUserDataRaw());
    }

    @Override
    public String toString() {
        final String str = String.format(
                "RadioPacket4BS{%s}",
                super.toString()
        );
        return str;
    }

}
