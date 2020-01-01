/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.souliss.internal.network.typicals;

import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.State;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Typical T1A Digital input bit-wise
 *
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT1A extends SoulissGenericTypical {
    // Parameters sSoulissNode, iSlot, Type and State are stored in the class
    private static Logger LOGGER = LoggerFactory.getLogger(SoulissT1A.class);

    // These are the values bit-wise
    int iBit;
    short sRawState;

    public int getBit() {
        return iBit;
    }

    private void setBit(int _iBit) {
        iBit = _iBit;
    }

    public SoulissT1A(String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType, byte iBit) {
        super();
        this.setSlot(iSlot);
        this.setSoulissNodeID(iIDNodo);
        this.setType(Constants.Souliss_T1A);
        this.setNote(sOHType);
        this.setBit(iBit);

    }

    @Override
    public State getOHState() {
        String sOHState = StateTraslator.statesSoulissToOH(this.getNote(), this.getType(), this.getState(iBit));
        if (sOHState != null) {
            if (this.getNote().equals("ContactItem")) {
                return OpenClosedType.valueOf(sOHState);
            } else {
                return OnOffType.valueOf(sOHState);
            }
        }
        return null;
    }

    public short getRawState() {
        return sRawState;
    }

    public short getState(int i) {
        if (getBitState()) {
            return Constants.Souliss_T1n_OnCoil;
        } else {
            return Constants.Souliss_T1n_OffCoil;
        }

    }

    public boolean getBitState() {
        final int MASK_BIT_1 = 0x1;

        if (((getRawState() >>> getBit()) & MASK_BIT_1) == 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param iState
     *            the iState to set
     */
    public void setState(short iState) {
        sRawState = iState;
        LOGGER.debug("Update State. Name: {} Typ: 0x{}, Node: {}, Slot: {}, Bit: {}, RawBin: {}. New Bit State: {}",
                getName(), Integer.toHexString(getType()), getSoulissNodeID(), getSlot(), getBit(),
                Integer.toBinaryString(getRawState()), getBitState());
        setUpdatedTrue();
    }

}
