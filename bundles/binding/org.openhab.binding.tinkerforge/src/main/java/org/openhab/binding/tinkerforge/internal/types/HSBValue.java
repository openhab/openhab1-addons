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
package org.openhab.binding.tinkerforge.internal.types;

import org.openhab.core.library.types.HSBType;

/*
 * Just a simple wrapper for {@link HSBType}, to get the {@link TinkerforgeValue}
 * marker interface.
 *
 * @author Theo Weiss
 * @since 1.7.0
 */
public class HSBValue implements TinkerforgeValue {

    private HSBType hsbType;

    public HSBValue(HSBType hsbValue) {
        this.hsbType = hsbValue;
    }

    public HSBType getHsbValue() {
        return hsbType;
    }

    public void setHsbValue(HSBType hsbType) {
        this.hsbType = hsbType;
    }

}
