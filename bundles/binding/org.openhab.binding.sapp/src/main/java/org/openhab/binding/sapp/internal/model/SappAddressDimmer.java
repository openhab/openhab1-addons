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
 * Dimmer Address model
 *
 * @author Paolo Denti
 * @since 1.8.0
 *
 */
public class SappAddressDimmer extends SappAddressDecimal {

    private int increment;

    /**
     * Constructor
     */
    public SappAddressDimmer(String pnmasId, SappAddressType addressType, int address, String subAddress, int minScale,
            int maxScale, int increment) {
        super(pnmasId, addressType, address, subAddress, minScale, maxScale);

        this.increment = increment;
    }

    /**
     * increment getter
     */
    public int getIncrement() {
        return increment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("[ %s:%s:%d:%s:%d:%d:%d ]", getPnmasId(), getAddressType(), getAddress(), getSubAddress(),
                getMinScale(), getMaxScale(), increment);
    }
}
