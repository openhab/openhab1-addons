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

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.State;

/**
 * Typical D98 Service Typical HEALTY
 *
 * @author Tonino Fazio
 * @since 1.7.0
 */
public class SoulissTServiceNODE_HEALTY extends SoulissGenericTypical {

    public SoulissTServiceNODE_HEALTY(String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType) {
        super();
        this.setSlot(iSlot);
        this.setSoulissNodeID(iIDNodo);
        this.setType(Constants.Souliss_TService_NODE_HEALTY);
        this.setNote(sOHType);
    }

    /**
     * Returns the souliss' typical state as numerical value
     */
    @Override
    public State getOHState() {
        String sOHState = StateTraslator.statesSoulissToOH(this.getNote(), this.getType(), (short) this.getState());
        if (sOHState == null) {
            return DecimalType.valueOf(Float.toString(this.getState()));
        } else {
            return DecimalType.valueOf(sOHState);
        }
    }

}
