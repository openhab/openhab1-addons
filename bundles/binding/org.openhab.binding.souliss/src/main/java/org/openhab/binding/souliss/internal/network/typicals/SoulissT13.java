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

/**
 * Typical T13 Digital Input Value
 *
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissT13 extends SoulissGenericTypical {

    public SoulissT13(String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType) {
        super();
        this.setSlot(iSlot);
        this.setSoulissNodeID(iIDNodo);
        this.setType(Constants.Souliss_T13);
        this.setNote(sOHType);
    }

    @Override
    public State getOHState() {
        String sOHState = StateTraslator.statesSoulissToOH(this.getNote(), this.getType(), (short) this.getState());
        if (sOHState != null) {
            if (this.getNote().equals("ContactItem")) {
                return OpenClosedType.valueOf(sOHState);
            } else {
                return OnOffType.valueOf(sOHState);
            }
        }
        return null;
    }

}
