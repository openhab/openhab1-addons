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

/**
 * Typical T58 Pressure Sensor Derived from T51 Analog input, half-precision
 * floating point
 *
 * @author Stephen Olesen
 * @since 1.8.0
 */
public class SoulissT58 extends SoulissT51 {

    public SoulissT58(String sSoulissNodeIPAddressOnLAN, int iIDNodo, int iSlot, String sOHType) {
        super(sSoulissNodeIPAddressOnLAN, iIDNodo, iSlot, sOHType);
        this.setType(Constants.Souliss_T58_PressureSensor);
    }
}
