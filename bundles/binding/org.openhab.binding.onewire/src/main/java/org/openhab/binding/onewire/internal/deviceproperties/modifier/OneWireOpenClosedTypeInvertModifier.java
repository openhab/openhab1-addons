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
package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import org.openhab.core.library.types.OpenClosedType;

/**
 * This InvertModifier inverts the given Value to the opposite for OpenCloseType.
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public class OneWireOpenClosedTypeInvertModifier extends AbstractOneWireOpenClosedTypeModifier {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#getModifierName()
     */
    @Override
    public String getModifierName() {
        return "Invert OpenCloseType modifier for OnOffType";
    }

    @Override
    public OpenClosedType modifyOpenClosedType4Read(OpenClosedType pvOpenClosedTypeValue) {
        if (pvOpenClosedTypeValue == null) {
            return null;
        } else if (pvOpenClosedTypeValue.equals(OpenClosedType.OPEN)) {
            return OpenClosedType.CLOSED;
        } else if (pvOpenClosedTypeValue.equals(OpenClosedType.CLOSED)) {
            return OpenClosedType.OPEN;
        } else {
            throw new IllegalStateException("Unknown OpenClosedType state:" + pvOpenClosedTypeValue.toString());
        }
    }

    @Override
    public OpenClosedType modifyOpenClosedType4Write(OpenClosedType pvOpenClosedTypeValue) {
        return modifyOpenClosedType4Read(pvOpenClosedTypeValue);
    }
}
