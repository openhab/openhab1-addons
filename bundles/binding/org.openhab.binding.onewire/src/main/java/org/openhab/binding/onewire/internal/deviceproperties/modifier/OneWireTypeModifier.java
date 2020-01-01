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

import org.openhab.core.types.Type;

/**
 * Interface to implement TypeModifier
 *
 * @author Dennis Riegelbauer
 * @since 1.7.0
 *
 */
public interface OneWireTypeModifier {

    /**
     * @return name of the modifier
     */
    public String getModifierName();

    /**
     * @param pvType
     * @return modify read Value
     */
    public Type modify4Read(Type pvType);

    /**
     * @param pvType
     * @return modify write Value
     */
    public Type modify4Write(Type pvType);

}
