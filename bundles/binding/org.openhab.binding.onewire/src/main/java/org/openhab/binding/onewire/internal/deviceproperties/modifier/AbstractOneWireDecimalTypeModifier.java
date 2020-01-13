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

import org.openhab.core.library.types.DecimalType;
import org.openhab.core.types.Type;

/**
 * Abstract class which defines a TypeModifier for Openhab DecimalType
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
abstract public class AbstractOneWireDecimalTypeModifier implements OneWireTypeModifier {

    /*
     * (non-Javadoc)
     * 
     * @see org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#modify4Read(org.
     * openhab.core.types.Type)
     */
    @Override
    public Type modify4Read(Type pvType) {
        if (pvType == null) {
            return null;
        }

        if (pvType instanceof DecimalType) {
            return modifyDecimalType4Read((DecimalType) pvType);
        } else {
            throw new ClassCastException(
                    "unexpected class, expected: " + DecimalType.class + " type is:" + pvType.getClass());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.openhab.binding.onewire.internal.deviceproperties.modifier.InterfaceOneWireTypeModifier#modify4Write(org.
     * openhab.core.types.Type)
     */
    @Override
    public Type modify4Write(Type pvType) {
        if (pvType == null) {
            return null;
        }

        if (pvType instanceof DecimalType) {
            return modifyDecimalType4Write((DecimalType) pvType);
        } else {
            throw new ClassCastException(
                    "unexpected class, expected: " + DecimalType.class + " type is:" + pvType.getClass());
        }
    }

    /**
     * @param pvDecimalTypeValue
     * @return modified Type
     */
    abstract public DecimalType modifyDecimalType4Read(DecimalType pvDecimalTypeValue);

    /**
     * @param pvDecimalTypeValue
     * @return modified Type
     */
    abstract public DecimalType modifyDecimalType4Write(DecimalType pvDecimalTypeValue);

}
