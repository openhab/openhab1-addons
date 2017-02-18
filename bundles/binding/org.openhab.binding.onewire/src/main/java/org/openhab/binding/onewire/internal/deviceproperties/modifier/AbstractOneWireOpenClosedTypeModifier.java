/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.onewire.internal.deviceproperties.modifier;

import org.openhab.core.library.types.OpenClosedType;
import org.openhab.core.types.Type;

/**
 * Abstract class which defines a TypeModifier for Openhab OpenClosedType
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
abstract public class AbstractOneWireOpenClosedTypeModifier implements OneWireTypeModifier {

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

        if (pvType instanceof OpenClosedType) {
            return modifyOpenClosedType4Read((OpenClosedType) pvType);
        } else {
            throw new ClassCastException(
                    "unexpected class, expected: " + OpenClosedType.class + " type is:" + pvType.getClass());
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

        if (pvType instanceof OpenClosedType) {
            return modifyOpenClosedType4Write((OpenClosedType) pvType);
        } else {
            throw new ClassCastException(
                    "unexpected class, expected: " + OpenClosedType.class + " type is:" + pvType.getClass());
        }
    }

    /**
     * @param openClosedValue
     * @return modified Type
     */
    abstract public OpenClosedType modifyOpenClosedType4Read(OpenClosedType openClosedValue);

    /**
     * @param openClosedValue
     * @return modified Type
     */
    abstract public OpenClosedType modifyOpenClosedType4Write(OpenClosedType openClosedValue);

}
