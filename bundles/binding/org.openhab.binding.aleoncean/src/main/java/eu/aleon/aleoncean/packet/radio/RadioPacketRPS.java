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
import eu.aleon.aleoncean.packet.radio.userdata.UserDataRPS;
import eu.aleon.aleoncean.util.Bits;

/**
 *
 * @author Markus Rathgeb {@literal <maggu2810@gmail.com>}
 */
public class RadioPacketRPS extends RadioPacketFixedLength {

    private static final int STATUS_POS_NUSTATE = 4;

    private static final int STATUS_POS_T21STATE = 5;

    public enum NUState {

        UNASSIGNEDMESSAGE(false), NORMALMESSAGE(true);

        static NUState fromBoolean(final boolean value) {
            return value ? NORMALMESSAGE : UNASSIGNEDMESSAGE;
        }

        private final boolean value;

        NUState(final boolean value) {
            this.value = value;
        }

        public boolean toBoolean() {
            return value;
        }

        @Override
        public String toString() {
            return value ? "Normal" : "Unassigned";
        }
    }

    public enum T21State {

        PTM_TYPE1(false), PTM_TYPE2(true);

        private final boolean value;

        static T21State fromBoolean(final boolean value) {
            return value ? PTM_TYPE2 : PTM_TYPE1;
        }

        T21State(final boolean value) {
            this.value = value;
        }

        public boolean toBoolean() {
            return value;
        }

        @Override
        public String toString() {
            return value ? "PTM Type 2" : "PTM Type 1";
        }
    }

    public RadioPacketRPS() {
        super(UserDataRPS.DATA_LENGTH, RadioChoice.RORG_RPS);
    }

    /**
     * Check if the PTM module is of type 1 or 2.
     *
     * @return Return the type of the PTM module.
     */
    public T21State getT21State() {
        return T21State.fromBoolean(Bits.getBool(getStatus(), STATUS_POS_T21STATE));
    }

    public void setT21State(final T21State t21) {
        setStatus(Bits.setBit(getStatus(), STATUS_POS_T21STATE, t21.toBoolean()));
    }

    /**
     * Check if the message is of type N or U.
     *
     * @return Return the message type.
     */
    public NUState getNUState() {
        return NUState.fromBoolean(Bits.getBool(getStatus(), STATUS_POS_NUSTATE));
    }

    public void setNUState(final NUState nu) {
        setStatus(Bits.setBit(getStatus(), STATUS_POS_NUSTATE, nu.toBoolean()));
    }

    @Override
    public String toString() {
        return String.format("RadioPacketRPS{%s, nu=%s, t21=%s, userData[0]=0x%02X}",
                             super.toString(),
                             getNUState().toString(),
                             getT21State().toString(),
                             getUserDataRaw()[0]);
    }

}
