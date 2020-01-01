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
package org.openhab.binding.sapp.internal.model;

/**
 * OnOff Control Address model
 *
 * @author Paolo Denti
 * @since 1.8.0
 *
 */
public class SappAddressOnOffControl extends SappAddress {

    private int onValue;
    private int offValue;

    /**
     * Constructor
     */
    public SappAddressOnOffControl(String pnmasId, SappAddressType addressType, int address, String subAddress,
            int onValue, int offValue) {
        super(pnmasId, addressType, address, subAddress);
        this.onValue = onValue;
        this.offValue = offValue;
    }

    /**
     * onValue getter
     */
    public int getOnValue() {
        return onValue;
    }

    /**
     * offValue getter
     */
    public int getOffValue() {
        return offValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("[ %s:%s:%d:%s:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(),
                onValue, offValue);
    }
}
